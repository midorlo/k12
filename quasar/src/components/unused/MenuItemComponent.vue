<template>
  <div v-if="url">
    <q-item clickable tag="a" :to="url">
      <q-item-section v-if="icon" avatar>
        <q-icon :name="icon"/>
      </q-item-section>
      <q-item-section>
        <q-item-label>{{ title }}</q-item-label>
        <q-item-label caption> {{ caption }}</q-item-label>
      </q-item-section>
    </q-item>
  </div>
</template>

<script>
export default {
  props: {
    title: {type: String, required: true},
    caption: {type: String, default: ''},
    url: {type: String, default: null},
    route: {type: String, default: null},
    icon: {type: String, default: ''}
  }
}
</script>
