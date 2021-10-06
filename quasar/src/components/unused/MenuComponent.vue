<template>
  <div>
    <q-list>
      <q-item-label header>{{ title }}</q-item-label>
      <div v-for="item in data" :key="item.title">
        <menu-item-component
            :title="item.title"
            :icon="item.icon"
            :caption="item.caption"
            :url="item.url"
        />
      </div>
    </q-list>
  </div>
</template>

<script>
import MenuItemComponent from "./MenuItemComponent";

// noinspection JSUnusedGlobalSymbols
export default {
  components: {MenuItemComponent},
  props: {
    title: {type: String, required: true},
    src: {type: String, required: true},
  },
  data() {
    return {
      data: {}
    }
  },
  async created() {
    this.$axios.get(this.src).then(response => this.data = response.data)
  },
}
</script>
