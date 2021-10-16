<template>
  <div class="q-pa-lg flex flex-center">
    <q-pagination
        unelevated
        v-model="current"
        color="primary"
        :max="10"
        :max-pages="6"
        boundary-numbers
        :model-value="current"/>
  </div>
</template>

<script>
// noinspection JSUnusedGlobalSymbols
export default {
  name: "K9Paginator",
  props: {
    currentPage: {type: Number, default: 1},
    totalPages: {type: Number, default: 1}
  },
  data() {
    return {
      current: null
    }
  }
}
</script>
