<template>
  <q-page class="q-pa-md flex flex-center">
    <q-form
      @submit="onSubmit"
      class="responsive-width q-gutter-md"
      greedy
    >
      <q-input
        v-model="menu.data.i18n"
        :label="$t('k12App.menu.i18n')"
        :rules="[$rules.required(), $rules.minValue(6), $rules.maxValue(50)]"
        @keydown.enter.prevent
      />
      <q-input
        v-model="menu.data.icon"
        :label="$t('k12App.menu.icon')"
        :rules="[$rules.required(), $rules.minValue(6), $rules.maxValue(50)]"
        @keydown.enter.prevent
      />
      <q-checkbox
        v-model="menu.data.enabled"
        :label="$t('k12App.menu.enabled')"
        :rules="[$rules.required()]"
      />
      <q-select
        v-model="menu.data.parent"
        :label="$t('k12App.menu.parent')"
        :options="[null].concat(menus)"
        option-label="id"
      />
      <q-select
        v-model="menu.data.requiredClearance"
        :label="$t('k12App.menu.requiredClearance')"
        :options="[null].concat(clearances)"
        option-label="id"
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
  name: 'PageMenuEdit',

  setup() {
    const { t } = useI18n();
    const $q = useQuasar();
    const router = useRouter();
    const route = useRoute();

    const menu = reactive({
      data: {
        id: null,
        i18n: null,
        icon: null,
        enabled: false,
        parent: null,
        requiredClearance: null,
      },
    });

    const loading = ref(false);

    (async function fetchMenu() {
      if (route.params.id) {
        menu.data = (await api.get(`/api/webapp/menus/${route.params.id}`)).data;
      }
    })();

    const menus = ref([]);
    (async function fetchMenus() {
      menus.value = (await api.get(`/api/webapp/menus`)).data;
    })();

    const clearances = ref([]);
    (async function fetchClearances() {
      clearances.value = (await api.get(`/api/clearances`)).data;
    })();

    const onSubmit = async () => {
      loading.value = true;
      try {
        await api({
          method: menu.data.id ? 'put' : 'post',
          url: `/api/webapp/menus/${menu.data.id ? menu.data.id : ''}`,
          data: menu.data,
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
      menu,
      menus,
      clearances,
      onSubmit,
      loading,
    };
  },
});
</script>
