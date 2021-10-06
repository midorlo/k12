<template>
  <router-view />
</template>
<script>
import { defineComponent } from 'vue';
import {provide, ref} from 'vue';
export default defineComponent({
  name: 'App',
  setup() {
    const primaryColor = ref('#0091ea')
    const showSettingsBar = ref(false)
    const primaryLanguage = ref('Deutsch')
    const enableBackgroundAnimation = ref(true)
    provide('primaryColor', primaryColor)
    provide('primaryLanguage', primaryLanguage)
    provide('showSettingsBar', showSettingsBar)
    provide('enableBackgroundAnimation', enableBackgroundAnimation)
  }
})
</script>
