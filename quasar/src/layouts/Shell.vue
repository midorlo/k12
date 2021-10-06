<template>
  <q-layout view="hHr lpR lFr">
    <app-header/>
    <div class='animationScene' v-show="enableBackgroundAnimation"/>
    <q-page-container v-show="showContentPane">
      <router-view/>
    </q-page-container>
    <q-drawer side="right" :model-value="showSettingsBar">
      <app-settings-widget/>
    </q-drawer>

  </q-layout>
</template>

<script>


import {inject} from 'vue'
import AppSettingsWidget from "./components/AppSettingsWidget";
import AppHeader from "./components/AppHeader";

export default {
  setup() {
    const primaryColor = inject('primaryColor')
    const enableBackgroundAnimation = inject('enableBackgroundAnimation')
    const showSettingsBar = inject('showSettingsBar')
    return {
      primaryColor,
      showSettingsBar,
      enableBackgroundAnimation
    }
  },
  computed: {
    theme() {
      return 'background-color: ' + this.primaryColor;
    }
  },
  data() {
    return {
      settings: {
        show: false
      },
      showHeader: true,
      showContentPane: true,
      showSettingsPane: false,
      showAnimation: true,
      currentTab: '',
      principal: '',
      clickable: {type: Boolean, default: true},
      slogan: 'On Time...'
    }
  },
  components: {
    AppHeader,
    AppSettingsWidget,
  },
  mounted() {
    this.startAnimation()
  },
  methods: {
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
    }
  }
}
</script>

<style lang="scss">

@function randomNum($min, $max) {
  $rand: random();
  $randomNum: $min + floor($rand * (($max - $min) + 1));
  @return $randomNum;
}

$base-color: #eeeeee;
$pr: 1%;

.q-page-container {
  background-color: $base-color;
}

.animationScene {
  position: fixed;
  top: 0;
  transform: rotate(80deg);
  left: 0;
  height: 100vh;
  width: 100%;
}

.wave {
  opacity: .4;
  position: absolute;
  left: 10%;
  width: 70%;
  height: 100%;
  margin-left: -100px;
  margin-top: -200px;
  transform-origin: 50% 48%;
  border-radius: 43%;
  animation: drift 7000ms infinite linear;
}

.animationScene:after {
  content: '';
  display: block;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  z-index: 11;
  transform: translate3d(0, 0, 0);
}

@keyframes drift {
  from {
    transform: rotate(0deg);
  }
  from {
    transform: rotate(360deg);
  }
}

$max: 3;
@for $i from 1 through $max {
  $bgColor: lighten($base-color, (randomNum(2,70) * $pr));
  $size: randomNum(100, 300);
  .wave:nth-child(#{$i}) {
    animation: drift randomNum(3500,7000)+ms infinite linear;
    background-color: $bgColor;
    transform-origin: 50% (randomNum(40,60) * $pr);
    border-radius: (randomNum(45,55) * $pr);
  }
}
</style>
