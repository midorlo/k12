<template>
  <div class="q-pa-md">
    <q-table
      :title="$t('k12App.menu.home.title')"
      :rows="rows"
      :columns="columns"
      row-key="id"
      v-model:pagination="pagination"
      :loading="loading"
      @request="onRequest"
      binary-state-sort
    >
      <template v-slot:top-right>
        <router-link
          to="/menus/new"
          v-slot="{ navigate }"
          custom
        >
          <q-btn
            color="primary"
            :label="$t('k12App.menu.home.createLabel')"
            @click="navigate"
          />
        </router-link>
      </template>
      <template v-slot:body="props">
        <q-tr>
          <q-td>
            {{ props.row.id }}
          </q-td>
          <q-td>
            {{ props.row.i18n }}
          </q-td>
          <q-td>
            {{ props.row.icon }}
          </q-td>
          <q-td>
            <q-icon
              :name="`${props.row.enabled ? 'check_box' : 'check_box_outline_blank'}`"
              size="sm"
            />
          </q-td>
          <q-td>
            {{ props.row.parent && props.row.parent.id }}
          </q-td>
          <q-td>
            {{ props.row.requiredClearance && props.row.requiredClearance.id }}
          </q-td>
          <q-td>
            <router-link
              :to="`/menus/${props.row.id}`"
              v-slot="{ navigate }"
              custom
            >
              <q-btn
                icon="visibility"
                @click="navigate"
              />
            </router-link>
            <router-link
              :to="`/menus/${props.row.id}/edit`"
              v-slot="{ navigate }"
              custom
            >
              <q-btn
                icon="edit"
                @click="navigate"
              />
            </router-link>
            <q-btn
              icon="delete_forever"
              @click="deleteMenu(props.row.id)"
            />
          </q-td>
        </q-tr>
      </template>
    </q-table>
  </div>
</template>

<script>
import { api } from 'boot/axios';
import { useQuasar } from 'quasar';
import { defineComponent, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useStore } from 'vuex';

export default defineComponent({
  name: 'PageMenus',

  setup() {
    const $q = useQuasar();
    const { t } = useI18n();
    const store = useStore();
    const route = useRoute();
    const router = useRouter();

    const rows = ref([]);
    const loading = ref(false);

    const pagination = ref({
      sortBy: route.query.sortBy || 'id',
      descending: route.query.descending === 'true',
      page: Number.parseInt(route.query.page || 1),
      rowsPerPage: Number.parseInt(route.query.rowsPerPage) || 10,
      rowsNumber: 10,
    });

    const columns = [
      { name: 'id', align: 'left', label: t('k12App.menu.id'), field: 'id', sortable: true },
      { name: 'i18n', align: 'left', label: t('k12App.menu.i18n'), field: 'i18n', sortable: true },
      { name: 'icon', align: 'left', label: t('k12App.menu.icon'), field: 'icon', sortable: true },
      { name: 'enabled', align: 'left', label: t('k12App.menu.enabled'), field: 'enabled', sortable: true },
      { name: 'parent.id', align: 'left', label: t('k12App.menu.parent'), field: 'parent', sortable: true },
      { name: 'requiredClearance.id', align: 'left', label: t('k12App.menu.requiredClearance'), field: 'requiredClearance', sortable: true },
    ];

    const onRequest = async props => {
      const { page, rowsPerPage, sortBy, descending } = props.pagination;

      loading.value = true;

      try {
        const response = await api.get('/api/menus', {
          params: {
            page: page - 1,
            size: rowsPerPage === 0 ? pagination.value.rowsNumber : rowsPerPage,
            sort: `${sortBy},${descending ? 'desc' : 'asc'}`,
          },
        });
        pagination.value.rowsNumber = response.headers['x-total-count'];
        rows.value = response.data;
      } finally {
        loading.value = false;
      }

      pagination.value.page = page;
      pagination.value.rowsPerPage = rowsPerPage;
      pagination.value.sortBy = sortBy;
      pagination.value.descending = descending;

      router.replace({ query: { page, sortBy, descending, rowsPerPage } });
    };

    onMounted(() => onRequest({ pagination: pagination.value }));

    return {
      store,
      loading,
      pagination,
      columns,
      rows,
      onRequest,
      deleteMenu: id => {
        $q.dialog({
          message: t('k12App.menu.delete.question', { id: id }),
          cancel: true,
        }).onOk(() => {
          api.delete(`/api/menus/${id}`).then(() => {
            onRequest({ pagination: pagination.value });
          });
        });
      },
    };
  },
});
</script>
