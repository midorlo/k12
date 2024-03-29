<template>
  <q-page class="q-pa-md flex flex-center">
    <q-form
      @submit="onSubmit"
      class="responsive-width q-gutter-md"
      greedy
    >
      <q-input
        v-model="user.data.login"
        :label="$t('userManagement.login')"
        lazy-rules
        :rules="[$rules.required(), $rules.maxLength(50)]"
        @keydown.enter.prevent
        data-cy="login"
      />
      <q-input
        v-model="user.data.firstName"
        :label="$t('userManagement.firstName')"
        lazy-rules
        :rules="[$rules.maxLength(50)]"
        @keydown.enter.prevent
        data-cy="firstName"
      />
      <q-input
        v-model="user.data.lastName"
        :label="$t('userManagement.lastName')"
        lazy-rules
        :rules="[$rules.maxLength(50)]"
        @keydown.enter.prevent
        data-cy="lastName"
      />
      <q-input
        v-model="user.data.email"
        type="email"
        :label="$t('userManagement.email')"
        lazy-rules
        :rules="[$rules.required(), $rules.minLength(5), $rules.maxLength(254)]"
        @keydown.enter.prevent
        data-cy="email"
      />
      <q-checkbox
        v-if="route.params.login"
        v-model="user.data.activated"
        :label="$t('userManagement.activated')"
        data-cy="activated"
      />
      <q-select
        v-model="user.data.langKey"
        :options="langObjects"
        option-value="key"
        option-label="value"
        emit-value
        map-options
        :label="$t('userManagement.langKey')"
        :rules="[$rules.required()]"
        data-cy="langKey"
      />
      <q-select
        v-model="user.data.authorities"
        :options="availableRoles"
        :label="$t('userManagement.profiles')"
        multiple
        data-cy="authorities"
      >
        <template v-slot:option="{ itemProps, opt, selected, toggleOption }">
          <q-item v-bind="itemProps">
            <q-item-section>
              <q-item-label v-html="opt"></q-item-label>
            </q-item-section>
            <q-item-section side>
              <q-toggle
                :model-value="selected"
                @update:modelValue="toggleOption(opt)"
                data-cy="toggle"
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
          data-cy="submit"
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
import { langObjects } from '../constants/i18nConstants';

export default defineComponent({
  name: 'PageUserEdit',

  setup() {
    const { t } = useI18n();
    const $q = useQuasar();
    const router = useRouter();
    const route = useRoute();

    const user = reactive({
      data: {
        id: null,
        login: null,
        firstName: null,
        lastName: null,
        email: null,
        activated: false,
        langKey: null,
        authorities: null,
      },
    });

    const loading = ref(false);

    const fetchUser = async () => {
      if (route.params.login) {
        user.data = (await api.get(`/api/admin/users/${route.params.login}`)).data;
      }
    };

    fetchUser();

    const onSubmit = async () => {
      loading.value = true;
      try {
        await api({
          method: user.data.id ? 'put' : 'post',
          url: '/api/admin/users',
          data: user.data,
        });
        router.back();
      } catch (e) {
        $q.notify({
          type: 'negative',
          message: t(e.response.data.message),
        });
        loading.value = false;
      }
    };

    return {
      user,
      langObjects,
      route,
      availableRoles: ['ROLE_ADMIN', 'ROLE_USER'],
      onSubmit,
      loading,
    };
  },
});
</script>
