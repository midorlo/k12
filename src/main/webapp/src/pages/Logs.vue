<template>
  <div class="q-pa-md">
    <div class="q-pa-md flex flex-left">
      <q-input
        v-model="filterValue"
        style="width: 300px"
        @keyup="filter(filterValue)"
      >
        <template v-slot:prepend>
          <q-icon name="search" />
        </template>
      </q-input>
    </div>
    <q-virtual-scroll
      type="table"
      style="max-height: 80vh"
      :virtual-scroll-item-size="48"
      :virtual-scroll-sticky-size-start="48"
      :virtual-scroll-sticky-size-end="32"
      :items="loggers"
    >
      <template v-slot:before>
        <thead class="text-left">
          <tr>
            <th scope="col">
              {{ $t('logs.table.name') }}
            </th>
            <th scope="col">
              {{ $t('logs.table.level') }}
            </th>
          </tr>
        </thead>
      </template>
      <template v-slot="{ item: row, index }">
        <tr :key="index">
          <td>
            {{ row.logger }}
          </td>
          <td>
            <q-btn-toggle
              v-model="model[row.logger]"
              toggle-color="primary"
              :options="levels"
              @click="toggle(row, model[row.logger])"
            />
          </td>
        </tr>
      </template>
    </q-virtual-scroll>
  </div>
</template>

<script>
import { api } from 'boot/axios';
import { defineComponent, ref } from 'vue';

export default defineComponent({
  name: 'PageLogs',

  setup() {
    const loggers = ref([]);
    const levels = ref([]);
    const model = ref([]);
    const filterValue = ref('');
    let initialLoggers = [];

    const filter = filterInputValue => {
      loggers.value = Object.values(initialLoggers).filter(log => log.logger.toLowerCase().includes(filterInputValue.toLowerCase()));
    };

    const fetchLoggers = async filterInputValue => {
      initialLoggers = [];
      const response = await api.get('/management/loggers');
      const data = response.data;
      const entries = Object.entries(data.loggers);

      for (const [key, value] of entries) {
        model.value[key] = value.effectiveLevel;
        initialLoggers.push({ logger: key, level: value.effectiveLevel });
      }

      levels.value = data.levels.map(level => {
        return { label: level, value: level };
      });

      filter(filterInputValue);
    };

    fetchLoggers(filterValue.value);

    return {
      loggers,
      levels,
      model,
      filterValue,
      filter,
      toggle: async (row, level) => {
        await api.post(`/management/loggers/${row.logger}`, { configuredLevel: level });
        fetchLoggers(filterValue.value);
      },
    };
  },
});
</script>
