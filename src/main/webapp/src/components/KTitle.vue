<template>
  <div id="page-title">
    <slot/>
  </div>
</template>
<script>
import {inject} from "vue";

export default {
  setup() {
    const primaryColor = inject('primaryColor')
    return {primaryColor}
  },
}
</script>

<style>
#page-title {
  display: inline-block;
  width: 100%;
  padding: 1rem;
  font-weight: 10;
  font-size: 2.125rem;
  line-height: 3.5rem;
  letter-spacing: 0.00735em
}
</style>
