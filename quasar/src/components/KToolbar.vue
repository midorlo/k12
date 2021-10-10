<template>
  <q-card flat square class="full-width">

    <q-card-actions style="padding: 0 0 0 10px;">

      <q-input v-show="canSearch"
               v-model="searchString"
               :model-value="searchString"
               square
               dense
               borderless
               size="s"
               color="secondary"
               icon="add"
               type="search"
               class="text-weight-light col-6"
               label="Searching Something?"
               label-color="light-grey"
      />

      <q-btn-group outline class="absolute-right text-brown-4">
        <q-btn size="s" v-show="filter != null" icon="filter_alt" hint="Filter"></q-btn>
        <q-btn size="s" v-show="createTo != null" :to="createTo" icon="create" hint="Create"></q-btn>
        <q-btn size="s" v-show="editTo != null" :to="editTo" icon="edit_note" hint="Edit"></q-btn>
        <q-btn size="s" v-show="refreshTo != null" icon="refresh" hint="Update"></q-btn>
        <q-btn size="s" v-show="deleteTo != null" icon="delete" hint="Delete"/>
      </q-btn-group>

    </q-card-actions>
  </q-card>
</template>

<script>
// noinspection JSUnusedGlobalSymbols
export default {
  props: {
    createTo: {type: String, default: null},
    editTo: {type: String, default: null},
    refreshTo: {type: String, default: null},
    deleteTo: {type: String, default: null},
    filter: {type: String, default: null},

    canSearch: {type: Boolean, default: false},
  },
  data() {
    return {
      searchString: '',
    }
  }
}
</script>
