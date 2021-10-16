<template>
  <q-page class="q-pa-md flex flex-center">
    <q-form
      @submit="onSubmit"
      class="responsive-width q-gutter-md"
      greedy
    >
      <q-input
        v-model="role.data.i18n"
        :label="$t('k12App.role.i18n')"
        :rules="[$rules.required(), $rules.minValue(6), $rules.maxValue(50)]"
        @keydown.enter.prevent
      />
      <q-select
        v-model="role.data.clearances"
        :label="$t('k12App.role.clearances')"
        :options="clearances"
        option-value="id"
        option-label="id"
        multiple
      >
        <template v-slot:option="{ itemProps, opt, selected, toggleOption }">
          <q-item v-bind="itemProps">
            <q-item-section>
              <q-item-label v-html="opt.id"></q-item-label>
            </q-item-section>
            <q-item-section side>
              <q-toggle
                :model-value="selected"
                @update:modelValue="toggleOption(opt)"
              />
            </q-item-section>
          </q-item>
        </template>
      </q-select>
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
  name: 'PageRoleEdit',

  setup() {
    const { t } = useI18n();
    const $q = useQuasar();
    const router = useRouter();
    const route = useRoute();

    const role = reactive({
      data: {
        id: null,
        i18n: null,
        clearances: null,
      },
    });

    const loading = ref(false);

    (async function fetchRole() {
      if (route.params.id) {
        role.data = (await api.get(`/api/roles/${route.params.id}`)).data;
      }
    })();

    const clearances = ref([]);
    (async function fetchClearances() {
      clearances.value = (await api.get(`/api/clearances`)).data;
    })();

    const onSubmit = async () => {
      loading.value = true;
      try {
        await api({
          method: role.data.id ? 'put' : 'post',
          url: `/api/roles/${role.data.id ? role.data.id : ''}`,
          data: role.data,
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
      role,
      clearances,
      onSubmit,
      loading,
    };
  },
});
</script>
