<script setup lang="ts">
import { ref, computed } from 'vue'
import { ElTable, ElTableColumn, ElPagination } from 'element-plus'
import type { BasicTableProps, BasicTableEmits, BasicTableInstance } from './types'
import type { TableColumnCtx, TableInstance } from 'element-plus'

const props = withDefaults(defineProps<BasicTableProps>(), {
  bordered: true,
  striped: false,
  pagination: false,
  loading: false
})

const emit = defineEmits<BasicTableEmits>()

const tableRef = ref<TableInstance | null>(null)

const currentPage = ref(1)
const pageSize = ref(10)

const paginationProps = computed(() => {
  if (typeof props.pagination === 'boolean') {
    return {}
  }
  return props.pagination || {}
})

const handlePageChange = (val: number) => {
  currentPage.value = val
  emit('update:page', val)
}

const handleSizeChange = (val: number) => {
  pageSize.value = val
  currentPage.value = 1
  emit('update:size', val)
}

const clearSelection = () => {
  tableRef.value?.clearSelection()
}

const toggleRowSelection = (row: any, selected?: boolean) => {
  tableRef.value?.toggleRowSelection(row, selected)
}

const toggleAllSelection = () => {
  tableRef.value?.toggleAllSelection()
}

const setCurrentRow = (row: any) => {
  tableRef.value?.setCurrentRow(row)
}

const scrollTo = (options: { left?: number; top?: number; behavior?: 'smooth' | 'auto' }) => {
  tableRef.value?.scrollTo(options)
}

defineExpose<BasicTableInstance>({
  clearSelection,
  toggleRowSelection,
  toggleAllSelection,
  setCurrentRow,
  scrollTo
})
</script>

<template>
  <div class="basic-table">
    <ElTable
      ref="tableRef"
      :data="data"
      :border="bordered"
      :stripe="striped"
      :loading="loading"
      :max-height="maxHeight"
      v-bind="tableProps"
      @selection-change="(val: any[]) => emit('selection-change', val)"
      @sort-change="(val: any) => emit('sort-change', val)"
      @filter-change="(val: any) => emit('filter-change', val)"
      @row-click="(val: any) => emit('row-click', val.row, val.column, val.event)"
      @row-dblclick="(val: any) => emit('row-dblclick', val.row, val.column, val.event)"
      @cell-click="(val: any) => emit('cell-click', val.row, val.column, val.event)"
      @current-change="(val: any, old: any) => emit('current-change', val, old)"
    >
      <template v-for="column in columns" :key="column.key">
        <ElTableColumn
          v-if="column.children && column.children.length > 0"
          :type="column.type === 'selection' ? 'selection' : column.type === 'index' ? 'index' : undefined"
          :label="column.title"
          :prop="column.dataIndex || column.key"
          :width="column.width"
          :min-width="column.minWidth"
          :align="column.align"
          :fixed="column.fixed"
          :sortable="column.sortable"
          :filters="column.filters"
          :filter-multiple="column.filterMultiple"
        >
          <template v-for="child in column.children" :key="child.key">
            <ElTableColumn
              :type="child.type === 'selection' ? 'selection' : child.type === 'index' ? 'index' : undefined"
              :label="child.title"
              :prop="child.dataIndex || child.key"
              :width="child.width"
              :min-width="child.minWidth"
              :align="child.align"
              :fixed="child.fixed"
              :sortable="child.sortable"
              :filters="child.filters"
              :filter-multiple="child.filterMultiple"
            >
              <template #default="scope" v-if="child.slot">
                <slot :name="child.slot" :row="scope.row" :column="child" />
              </template>
              <template #default="scope" v-else-if="child.formatter">
                {{ child.formatter(scope.row, scope.column as TableColumnCtx<any>) }}
              </template>
            </ElTableColumn>
          </template>
        </ElTableColumn>
        
        <ElTableColumn
          v-else
          :type="column.type === 'selection' ? 'selection' : column.type === 'index' ? 'index' : undefined"
          :label="column.title"
          :prop="column.dataIndex || column.key"
          :width="column.width"
          :min-width="column.minWidth"
          :align="column.align"
          :fixed="column.fixed"
          :sortable="column.sortable"
          :filters="column.filters"
          :filter-multiple="column.filterMultiple"
        >
          <template #default="scope" v-if="column.slot">
            <slot :name="column.slot" :row="scope.row" :column="column" />
          </template>
          <template #default="scope" v-else-if="column.formatter">
            {{ column.formatter(scope.row, scope.column as TableColumnCtx<any>) }}
          </template>
        </ElTableColumn>
      </template>
    </ElTable>
    
    <div v-if="pagination" class="basic-table__pagination">
      <ElPagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="(pagination as any)?.total || 0"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @update:page-size="handleSizeChange"
        @update:current-page="handlePageChange"
        v-bind="paginationProps"
      />
    </div>
  </div>
</template>

<style lang="scss" scoped>
.basic-table {
  &__pagination {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    padding: 12px 0;
  }
}
</style>
