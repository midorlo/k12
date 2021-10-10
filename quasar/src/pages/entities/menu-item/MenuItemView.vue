<template>
  <q-page class="q-pa-md flex flex-center">
    <div class="responsive-width q-gutter-md">
      <q-field
        :label="$t('k12App.menuItem.i18n')"
        readonly
        stack-label
      >
        <template v-slot:control>
          {{ menuItem.data.i18n }}
        </template>
      </q-field>
      <q-field
        :label="$t('k12App.menuItem.icon')"
        readonly
        stack-label
      >
        <template v-slot:control>
          {{ menuItem.data.icon }}
        </template>
      </q-field>
      <q-field
        :label="$t('k12App.menuItem.target')"
        readonly
        stack-label
      >
        <template v-slot:control>
          {{ menuItem.data.target }}
        </template>
      </q-field>
      <q-field
        :label="$t('k12App.menuItem.enabled')"
        readonly
        stack-label
      >
        <template v-slot:control>
          <q-icon
            :name="`${menuItem.data.enabled ? 'check_box' : 'check_box_outline_blank'}`"
            size="sm"
          />
        </template>
      </q-field>
      <q-field
        :label="$t('k12App.menuItem.requiredClearance')"
        readonly
        stack-label
      >
        <template v-slot:control>
          {{ menuItem.data.requiredClearance && menuItem.data.requiredClearance.id }}
        </template>
      </q-field>
      <q-field
        :label="$t('k12App.menuItem.parent')"
        readonly
        stack-label
      >
        <template v-slot:control>
          {{ menuItem.data.parent && menuItem.data.parent.id }}
        </template>
      </q-field>
      <div class="flex justify-end">
        <router-link
          :to="`/menu-items/${menuItem.data.id}/edit`"
          v-slot="{ navigate }"
          custom
        >
          <q-btn
            icon="edit"
            @click="navigate"
            color="primary"
          />
        </router-link>
      </div>
    </div>
  </q-page>
</template>

<script>
import { api } from 'boot/axios';
import { defineComponent, reactive } from 'vue';
import { useRoute } from 'vue-router';

export default defineComponent({
  name: 'PageMenuItemView',

  setup() {
    const route = useRoute();

    const menuItem = reactive({
      data: {
        id: null,
        i18n: null,
        icon: null,
        target: null,
        enabled: null,
        requiredClearance: null,
        parent: null,
      },
    });

    (async function fetchMenuItem() {
      if (route.params.id) {
        menuItem.data = (await api.get(`/api/menu-items/${route.params.id}`)).data;
      }
    })();

    return {
      menuItem,
    };
  },
});
</script>
