<template>
  <q-page class="q-pa-md flex flex-center">
    <q-form
      @submit="onSubmit"
      class="responsive-width q-gutter-md"
      greedy
    >
      <q-input
        v-model="menuItem.data.i18n"
        :label="$t('k12App.menuItem.i18n')"
        :rules="[$rules.required(), $rules.minValue(6), $rules.maxValue(50)]"
        @keydown.enter.prevent
      />
      <q-input
        v-model="menuItem.data.icon"
        :label="$t('k12App.menuItem.icon')"
        :rules="[$rules.required(), $rules.minValue(6), $rules.maxValue(50)]"
        @keydown.enter.prevent
      />
      <q-input
        v-model="menuItem.data.target"
        :label="$t('k12App.menuItem.target')"
        :rules="[$rules.required(), $rules.minValue(6), $rules.maxValue(50)]"
        @keydown.enter.prevent
      />
      <q-checkbox
        v-model="menuItem.data.enabled"
        :label="$t('k12App.menuItem.enabled')"
        :rules="[$rules.required()]"
      />
      <q-select
        v-model="menuItem.data.requiredClearance"
        :label="$t('k12App.menuItem.requiredClearance')"
        :options="[null].concat(clearances)"
        option-label="id"
      />
      <q-select
        v-model="menuItem.data.parent"
        :label="$t('k12App.menuItem.parent')"
        :options="[null].concat(menus)"
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
  name: 'PageMenuItemEdit',

  setup() {
    const { t } = useI18n();
    const $q = useQuasar();
    const router = useRouter();
    const route = useRoute();

    const menuItem = reactive({
      data: {
        id: null,
        i18n: null,
        icon: null,
        target: null,
        enabled: false,
        requiredClearance: null,
        parent: null,
      },
    });

    const loading = ref(false);

    (async function fetchMenuItem() {
      if (route.params.id) {
        menuItem.data = (await api.get(`/api/menu-items/${route.params.id}`)).data;
      }
    })();

    const clearances = ref([]);
    (async function fetchClearances() {
      clearances.value = (await api.get(`/api/clearances`)).data;
    })();

    const menus = ref([]);
    (async function fetchMenus() {
      menus.value = (await api.get(`/api/menus`)).data;
    })();

    const onSubmit = async () => {
      loading.value = true;
      try {
        await api({
          method: menuItem.data.id ? 'put' : 'post',
          url: `/api/menu-items/${menuItem.data.id ? menuItem.data.id : ''}`,
          data: menuItem.data,
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
      menuItem,
      clearances,
      menus,
      onSubmit,
      loading,
    };
  },
});
</script>
