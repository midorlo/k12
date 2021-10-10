<template>
  <q-page class="q-pa-md flex flex-center">
    <div class="responsive-width q-gutter-md">
      <q-field
        :label="$t('k12App.role.i18n')"
        readonly
        stack-label
      >
        <template v-slot:control>
          {{ role.data.i18n }}
        </template>
      </q-field>
      <q-field
        :label="$t('k12App.role.clearances')"
        readonly
        stack-label
      >
        <template v-slot:control>
          <q-list dense>
            <q-item
              :key="clearances.id"
              v-for="clearances in role.data.clearances"
            >
              <q-item-section>
                {{ clearances.id }}
              </q-item-section>
            </q-item>
          </q-list>
        </template>
      </q-field>
      <div class="flex justify-end">
        <router-link
          :to="`/roles/${role.data.id}/edit`"
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
  name: 'PageRoleView',

  setup() {
    const route = useRoute();

    const role = reactive({
      data: {
        id: null,
        i18n: null,
        clearances: null,
      },
    });

    (async function fetchRole() {
      if (route.params.id) {
        role.data = (await api.get(`/api/roles/${route.params.id}`)).data;
      }
    })();

    return {
      role,
    };
  },
});
</script>
