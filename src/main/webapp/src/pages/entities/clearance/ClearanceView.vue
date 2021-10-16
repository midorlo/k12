<template>
  <q-page class="q-pa-md flex flex-center">
    <div class="responsive-width q-gutter-md">
      <q-field
        :label="$t('k12App.clearance.i18n')"
        readonly
        stack-label
      >
        <template v-slot:control>
          {{ clearance.data.i18n }}
        </template>
      </q-field>
      <div class="flex justify-end">
        <router-link
          :to="`/clearances/${clearance.data.id}/edit`"
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
  name: 'PageClearanceView',

  setup() {
    const route = useRoute();

    const clearance = reactive({
      data: {
        id: null,
        i18n: null,
      },
    });

    (async function fetchClearance() {
      if (route.params.id) {
        clearance.data = (await api.get(`/api/clearances/${route.params.id}`)).data;
      }
    })();

    return {
      clearance,
    };
  },
});
</script>
