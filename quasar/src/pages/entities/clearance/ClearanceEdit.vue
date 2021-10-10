<template>
  <q-page class="q-pa-md flex flex-center">
    <q-form
      @submit="onSubmit"
      class="responsive-width q-gutter-md"
      greedy
    >
      <q-input
        v-model="clearance.data.i18n"
        :label="$t('k12App.clearance.i18n')"
        :rules="[$rules.required(), $rules.minValue(6), $rules.maxValue(50)]"
        @keydown.enter.prevent
      />
      <div class="flex justify-between">
        <q-btn
          type="submit"
          color="primary"
          :label="$t('entity.action.save')"
          :loading="loading"
          :disable="loading"
        />
      </div>
    </q-form>
  </q-page>
</template>

<script>
import { api } from 'boot/axios';
import { useQuasar } from 'quasar';
import { defineComponent, reactive, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

export default defineComponent({
  name: 'PageClearanceEdit',

  setup() {
    const { t } = useI18n();
    const $q = useQuasar();
    const router = useRouter();
    const route = useRoute();

    const clearance = reactive({
      data: {
        id: null,
        i18n: null,
      },
    });

    const loading = ref(false);

    (async function fetchClearance() {
      if (route.params.id) {
        clearance.data = (await api.get(`/api/clearances/${route.params.id}`)).data;
      }
    })();

    const onSubmit = async () => {
      loading.value = true;
      try {
        await api({
          method: clearance.data.id ? 'put' : 'post',
          url: `/api/clearances/${clearance.data.id ? clearance.data.id : ''}`,
          data: clearance.data,
        });
        router.back();
      } catch (e) {
        loading.value = false;
        if (e.response.status !== 400) return;
        $q.notify({
          type: 'negative',
          message: t(e.response.data.message),
        });
      }
    };

    return {
      clearance,
      onSubmit,
      loading,
    };
  },
});
</script>
