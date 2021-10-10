<template>
  <q-page class="q-pa-md flex flex-center">
    <div class="responsive-width q-gutter-md">
      <q-field
        :label="$t('k12App.menu.i18n')"
        readonly
        stack-label
      >
        <template v-slot:control>
          {{ menu.data.i18n }}
        </template>
      </q-field>
      <q-field
        :label="$t('k12App.menu.icon')"
        readonly
        stack-label
      >
        <template v-slot:control>
          {{ menu.data.icon }}
        </template>
      </q-field>
      <q-field
        :label="$t('k12App.menu.enabled')"
        readonly
        stack-label
      >
        <template v-slot:control>
          <q-icon
            :name="`${menu.data.enabled ? 'check_box' : 'check_box_outline_blank'}`"
            size="sm"
          />
        </template>
      </q-field>
      <q-field
        :label="$t('k12App.menu.parent')"
        readonly
        stack-label
      >
        <template v-slot:control>
          {{ menu.data.parent && menu.data.parent.id }}
        </template>
      </q-field>
      <q-field
        :label="$t('k12App.menu.requiredClearance')"
        readonly
        stack-label
      >
        <template v-slot:control>
          {{ menu.data.requiredClearance && menu.data.requiredClearance.id }}
        </template>
      </q-field>
      <div class="flex justify-end">
        <router-link
          :to="`/menus/${menu.data.id}/edit`"
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
  name: 'PageMenuView',

  setup() {
    const route = useRoute();

    const menu = reactive({
      data: {
        id: null,
        i18n: null,
        icon: null,
        enabled: null,
        parent: null,
        requiredClearance: null,
      },
    });

    (async function fetchMenu() {
      if (route.params.id) {
        menu.data = (await api.get(`/api/webapp/menus/${route.params.id}`)).data;
      }
    })();

    return {
      menu,
    };
  },
});
</script>
