<template>
   <q-card square flat style="align-content: flex-end">
      <q-card-section horizontal style="justify-content: center">
         <q-card-section class="text-weight-light text-grey-8 text-h6 text-capitalize text-subtitle1">
            {{ parsedTime }}
         </q-card-section>
      </q-card-section>
   </q-card>
</template>

<script>
// noinspection JSUnusedGlobalSymbols
export default {
   data() {
      return {
         author: 'Admin'
      }
   },
   props: {
      time: {type: String, required: true} //2021-06-29T18:54:16.702405Z
   },
   computed: {
      parsedTime() {
         let date = new Date(this.time)
         return date.getDay()
            + '.'
            + date.getMonth()
            + '.'
            + date.getFullYear()

      }
   }
}
</script>
