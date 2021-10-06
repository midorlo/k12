<template>
  <q-btn-dropdown v-show="principal"
                  :model-value="greeting"
                  :label="greeting"
                  flat
                  class="k9-login-button">

    <q-item clickable @click="goToProfile()">
      <q-item-section avatar>
        <q-icon name="today" class="text-orange" style="font-size: 2em;"/>
      </q-item-section>

      <q-item-section>
        <q-item-label class=" text-h7">Profile</q-item-label>
      </q-item-section>
    </q-item>
    <q-item clickable v-close-popup @click="logout()">
      <q-item-section avatar>
        <q-icon name="logout" class="text-orange" style="font-size: 2em;"/>
      </q-item-section>
      <q-item-section>
        <q-item-label class=" text-h7">Logout</q-item-label>
      </q-item-section>
    </q-item>

  </q-btn-dropdown>
</template>

<script>
export default {
  props: {
    username: {type: String, default: null}
  },
  data: function () {
    return {
      token: '',
      principal: ''
    }
  },
  computed: {
    greeting() {
      return (this.principal) ? 'Hi, ' + this.principal : 'WTF?'
    },
    isLoggedIn() {
      return this.token != null && this.token !== 'null'
    }
  },
  methods: {
    logout() {
      this.principal = null
      this.token = null
      this.api.identity.logout().then(() => {
        window.location = '/'
      })
    },
    goToProfile() {
      this.$router.push('/me')
    },
    toggleLoginDialog() {
      this.$router.push('/login')
    }
  }
}
</script>
<style scoped lang="scss">

.k9-login-button {
  border-radius: 0;
  //noinspection SassScssResolvedByNameOnly
  color: $primary;
}
</style>
