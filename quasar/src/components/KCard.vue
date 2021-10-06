<template>
  <q-card :flat="flat">
    <q-card-section>
      <div class="section-title fit row wrap justify-start items-start content-start">{{ title }}</div>
    </q-card-section>
    <q-card-actions v-show="horizontal">
      <slot/>
    </q-card-actions>
    <q-card-section v-show="!horizontal">
      <slot/>
    </q-card-section>
  </q-card>
</template>

<script>
export default {
  props: {
    title: {type: String, required: true},
    horizontal: {type: Boolean, default: false},
    flat: {type: Boolean, default: true},
  }
}
</script>
