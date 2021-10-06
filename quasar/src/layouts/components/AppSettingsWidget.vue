<template>
  <app-content-panel title="Settings" :closeable="false" v-model="showSettingsBar">

    <!-- Theme Chooser -->
    <k-content-item title="Primary Color" :horizontal="true">
      <div v-for="theme in themes" :key="theme.slot">
        <q-btn @click="setColor(theme.value)"
               class="col-2"
               id="widget-button"
               :style="'background-color:' + theme.value"
               round/>
      </div>
      <q-btn @click="pickColor()"
             class="col-2"
             id="widget-button"
             style="background-color:#FFFFFF"
             label="?"
             round/>
    </k-content-item>

    <!-- Language Chooser -->
    <k-content-item title="Language">
      <q-select label="Primary Language" :model-value="primaryLanguage" :options="availableLanguages" outlined square/>
    </k-content-item>

    <!-- Animations Chooser -->
    <k-content-item title="Animations">
      <q-checkbox label="Background Animation"
                  v-model="enableBackgroundAnimation"
                  :model-value="enableBackgroundAnimation"/>
    </k-content-item>
  </app-content-panel>

</template>
<script>
import {inject} from "vue";
import AppContentPanel from "components/KContentPane";
import KContentItem from "components/KCard";

export default {
  name: 'AppSettingsWidget',
  components: {KContentItem, AppContentPanel},
  setup() {
    const primaryColor = inject('primaryColor')
    const primaryLanguage = inject('primaryLanguage')
    const showSettingsBar = inject('showSettingsBar')
    const enableBackgroundAnimation = inject('enableBackgroundAnimation')

    return {
      primaryColor,
      primaryLanguage,
      showSettingsBar,
      enableBackgroundAnimation
    }
  },
  data() {
    return {
      showComponent: true,
      availableLanguages: ['Deutsch', 'English'],
      themes: [
        {value: '#f5951f', slot: 'one'},
        {value: '#0091ea', slot: 'two'},
        {value: '#0011ea', slot: 'three'},
        {value: '#F191ea', slot: 'four'}
      ],
    }
  },
  methods: {
    closeSettings() {
      this.showSettingsBar = false
    },
    setColor(c) {
      this.primaryColor = c
    },
    pickColor() {
      console.log('todo popup with color picker')
    }
  },
  computed: {
    theme() {
      return 'background-color:' + this.theme.value + ';'
    }
  }
}
</script>

<style scoped>
#widget-button {
  overflow: auto;
  border-radius: 0;
  min-width: 56px;
  min-height: 56px;
}

</style>
