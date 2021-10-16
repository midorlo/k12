<!--suppress CssUnusedSymbol, CssUnusedSymbol -->
<template>
  <q-card class="content-card">
    <q-table
        flat
        bordered
        binary-state-sort
        :row-key="tableKey"
        :columns="defaultTableColumns"
        :data="records"
        v-model="pagination"
        :loading="isLoading"
        :filter="filter"
        @request="onRequest"
    />
  </q-card>
</template>

<script>

import SpringBootApiService from "../../boot/SpringBootApiService";

// noinspection JSUnusedGlobalSymbols
export default {


  props: {
    columnNames: {type: Array, required: false, default: this.defaultTableColumns()},
    api: {type: SpringBootApiService, required: true},
    tableKey: {type: String, required: false, default: 'id'}
  },

  data() {
    return {
      isLoading: false,
      filter: undefined,
      records: [],
      pagination: {sortBy: 'asc', descending: false, page: 1, rowsPerPage: 20, rowsNumber: 10}
    }
  },

  computed: {

    /** Columns generated from #columnNames. */
    defaultTableColumns() {
      // Map given columns to the expected data structure
      // todo Dialoge sollen irgendwann aus Datenbankabfragen entstehen. Danach kann der hier raus.
      let defaultColumns = this.columnNames.map(key => (
          {name: key, align: 'left', label: key, field: key, sortable: true})
      )
      // Apply a custom style on header and  1st column
      defaultColumns[0]['classes'] = 'bg-grey-2 ellipsis'
      defaultColumns[0]['style'] = 'max-width: 100px'
      defaultColumns[0]['headerClasses'] = 'bg-primary text-white'
      return defaultColumns
    },
  },

  /** Triggers #onRequest when the component has mounted. */
  mounted() {
    this.onRequest({pagination: this.pagination, filter: this.filter})
  },

  methods: {

    /**
     * Contacts the Spring Boot API and fetches a Pageable<Bookmark>. <p>
     * Triggers from #QTable emitting #@request.
     *
     * @param props Request details
     */
    onRequest(props) {
      let {sortBy, descending, page, rowsPerPage, rowsNumber} = props.pagination

      this.setIsLoading(true)
      this.setParamArgs(page, rowsPerPage)

      this.doRequest(page, rowsPerPage).then(response => {

        rowsNumber = parseInt(response.headers['x-total-count'])
        this.setTableData(response.data, props.filter)
        this.setPaginationData(sortBy, descending, page, rowsPerPage, rowsNumber)
        this.setIsLoading(false)
      })
    },

    async doRequest(page, rowsPerPage) {
      console.log('doRequest', {page, rowsPerPage})
      return this.api.readPage(page, rowsPerPage)
    },

    setPaginationData(sortBy, descending, page, rowsPerPage, rowsNumber) {
      this.pagination.sortBy = (sortBy != null) ? sortBy : this.pagination.sortBy
      this.pagination.descending = (descending != null) ? descending : this.pagination.descending
      this.pagination.page = page
      this.pagination.rowsPerPage = rowsPerPage
      this.pagination.rowsNumber = rowsNumber
      console.log('setPaginationData', this.pagination)
    },

    setTableData(records, filter) {
      this.records = records
      this.filter = filter
      console.log('setTableData', {records, filter})
    },

    setIsLoading(isLoading) {
      this.isLoading = isLoading
      console.log('setLoading', this.isLoading)
    }
  }
}
</script>

<style scoped lang="sass">

.sticky-header
  height: 600px


  thead tr th
    position: sticky
    z-index: 1

  thead tr:first-child th
    top: 0

  &.q-table--loading thead tr:last-child th
    top: 48px
</style>
