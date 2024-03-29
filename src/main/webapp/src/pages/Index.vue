<template>
  <q-page class="q-pa-md flex flex-center">
    <KPlaceholderCard/>
    <KPlaceholderCard/>
    <KPlaceholderCard/>
    <KPlaceholderCard/>
    <q-form
      v-if="!isAuthenticated"
      @submit="onSubmit"
      class="responsive-width q-gutter-md"
      greedy
    >
      <q-input
        autocomplete="on"
        v-model="credentials.username"
        :label="$t('global.form[\'username.label\']')"
        lazy-rules
        :rules="[$rules.required(), $rules.minLength(4)]"
        data-cy="username"
      />
      <q-input
        autocomplete="current-password"
        v-model="credentials.password"
        type="password"
        :label="$t('login.form.password')"
        lazy-rules
        :rules="[$rules.required(), $rules.minLength(4)]"
        data-cy="password"
      />
      <q-checkbox
        v-model="credentials.rememberMe"
        :label="$t('login.form.rememberme')"
        data-cy="rememberme"
      />
      <div class="flex column">
        <q-btn
          type="submit"
          color="primary"
          :label="$t('login.form.button')"
          :loading="loading"
          :disable="loading"
          data-cy="submit"
        />
        <q-btn
          to="/account/reset/init"
          flat
          color="primary"
          :label="$t('login.password.forgot')"
        />
      </div>
      <div class="q-pt-xl column items-center">
        <div class="col">
          {{ $t('global.messages.actuator.register.noaccount') }}
        </div>
        <div class="col">
          <q-btn
            to="/register"
            flat
            color="primary"
            :label="$t('global.menu.account.register')"
          />
        </div>
      </div>
    </q-form>
    <div v-if="isAuthenticated">
      <div class="column">
        <transition
          appear
          enter-active-class="animated rubberBand"
        >
          <img
            alt="Quasar logo"
            src="~assets/jhipster.svg"
            style="width: 200px; height: 100px; animation-duration: 1s"
          />
        </transition>
        <img
          v-if="!$q.dark.isActive"
          alt="Quasar logo"
          src="~assets/quasar-logo-vertical.svg"
          style="width: 200px; height: 200px"
        />
        <img
          v-if="$q.dark.isActive"
          alt="Quasar logo"
          src="~assets/quasar-logo-vertical-dark.svg"
          style="width: 200px; height: 200px"
        />
      </div>

    </div>
  </q-page>
</template>

<script>
import {useQuasar} from 'quasar';
import {computed, defineComponent, reactive, ref} from 'vue';
import {useRoute, useRouter} from 'vue-router';
import {useStore} from 'vuex';
import {authLogin} from '../service/authentication';
import KPlaceholderCard from "components/KPlaceholderCard";

export default defineComponent({
  name: 'PageIndex',
  components: { KPlaceholderCard },
  setup() {
    const $q = useQuasar();
    const store = useStore();
    const route = useRoute();
    const router = useRouter();

    const credentials = reactive({
      username: '',
      password: '',
      rememberMe: false,
    });

    const loading = ref(false);

    const onSubmit = async () => {
      loading.value = true;
      try {
        await authLogin(store, router, route, credentials);
      } catch (e) {
        store.dispatch('auth/logout');
        $q.notify({
          type: 'negative',
          message: 'Operation failed',
        });
      } finally {
        loading.value = false;
      }
    };

    const isAuthenticated = computed(() => store.getters['auth/isAuthenticated']);

    return {
      credentials,
      isAuthenticated,
      onSubmit,
      loading,
    };
  },
});
</script>
