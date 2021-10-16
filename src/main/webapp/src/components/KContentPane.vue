<!--suppress JSUnusedGlobalSymbols -->
<template>
  <div :style="theme">
    <title-label>{{ title }}</title-label>
    <q-btn :v-show="closeable"
           @click=changePageTitle
           flat
           color="accent"
           class="absolute-top-right"
           label="X"/>
    <slot/>
  </div>
</template>

<script>
import TitleLabel from "./KTitle";
import {inject} from "vue";

export default {
  name: 'KContentPane',
  setup() {

    const primaryColor = inject('primaryColor')
    return {primaryColor}
  },
  computed: {
    theme() {
      return 'border-top: 5px solid ' + this.primaryColor + ';'
    }
  },
  props: {
    value: Boolean,
    closeable: {type: Boolean, default: false},
    title: {type: String, required: true},
  },
  emits: ['update:modelValue'],
  methods: {
    changePageTitle() {
      this.$emit('update:modelValue', false) // previously was `this.$emit('input', title)`
    }
  },
  components: {TitleLabel},
}
</script>
