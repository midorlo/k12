<template>
   <q-card square flat>
      <q-card-section>
         <q-avatar rounded size="28px">
            <q-img :src="url"/>
         </q-avatar>
         {{ username }}
      </q-card-section>
   </q-card>
</template>

<script>

// noinspection JSUnusedGlobalSymbols
export default {
   props: {
      url: {type: String, required: true},
      username: {type: String, required: true},
   },
}
</script>

<style scoped>

</style>
