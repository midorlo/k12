<template>
  <q-header elevated>
    <q-toolbar class="toolbar-1 shadow-1 bg-grey-1">
      <q-item clickable @click="onClickLogo" flat class="text-orange-6 text-h4">K<sup>9</sup></q-item>
      <q-item>
        <div class="text-blue-grey-10 text-h6 text-weight-light">{{ slogan }}</div>
      </q-item>

      <q-space/>
      <q-btn flat @click="toggleSettings" icon="settings" v-bind:style="{ color: primaryColor}"/>
    </q-toolbar>


    <q-tabs class="text-secondary text-white text-weight-bolder text-h6"
            align="left"
            v-model="currentTab"
            dense
            no-caps
            v-bind:style="theme"
    >
      <!--suppress RequiredAttributes -->
      <q-btn-dropdown v-for="menu in menus"
                      :key="menu.id"
                      :tabindex="menu.id"
                      auto-close
                      stretch
                      flat
                      :label="menu['i18n']">
        <q-item v-for="menuItem in menu.childItems"
                :key="menuItem.id"
                :to="menuItem.target">
          <q-item-section>
            {{ menuItem.i18n }}
          </q-item-section>
        </q-item>
      </q-btn-dropdown>
      <q-space></q-space>
      <q-btn
        flat
        round
        @click="changeTheme()"
        :icon="$q.dark.isActive ? 'dark_mode' : 'light_mode'"
      />
      <q-route-tab to="/login" label="Login" v-show="!principal"/>
    </q-tabs>
  </q-header>
</template>

<script>
import {inject} from "vue";
import {LocalStorage, useQuasar} from "quasar";
import {api} from "boot/axios";

export default {
  name: "AppHeader",
  setup() {
    const $q = useQuasar()

    const primaryColor = inject('primaryColor')
    const showSettingsBar = inject('showSettingsBar')
    const enableBackgroundAnimation = inject('enableBackgroundAnimation')


    const changeTheme = function () {
      $q.dark.toggle();
      LocalStorage.set('dark-mode', $q.dark.mode);
    }

    return {
      primaryColor,
      showSettingsBar,
      enableBackgroundAnimation,
      changeTheme
    }
  },
  computed: {
    theme() {
      return 'background-color: ' + this.primaryColor;
    },
    principal() {
      return LocalStorage.getItem('bearer')
    }
  },
  data() {
    return {
      menus: [],
      slogan: 'On Time...',
      currentTab: 1
    }
  },
  mounted() {
    this.startAnimation()
    this.fetchMenus()
  },
  methods: {
    fetchMenus() {
      api.get('/api/webapp/menus').then(r => this.menus = r.data)
    },
    startAnimation() {
      for (let i = 0; i < 3; i++) {
        let wave = document.createElement('div')
        wave.className = "wave"
        document.getElementsByClassName('animationScene')[0].appendChild(wave);
      }
    },

    toggleSettings() {
      this.showSettingsBar = !this.showSettingsBar;
    },

    onClickLogo() {
      this.$router.push('/')
    },


  }
}
</script>

<style scoped>

</style>
