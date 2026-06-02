import type { PaginationProps } from 'element-plus'

export type TableColumnType =
  | 'index'
  | 'selection'
  | 'expand'
  | 'default'

export interface TableColumn<T = any> {
  title: string
  key: string
  dataIndex?: string
  type?: TableColumnType
  width?: string | number
  minWidth?: string | number
  align?: 'left' | 'center' | 'right'
  fixed?: boolean | 'left' | 'right'
  sortable?: boolean | 'custom'
  filters?: { text: string; value: string }[]
  filterMultiple?: boolean
  formatter?: (row: T, column: any) => any
  slot?: string
  children?: TableColumn<T>[]
}

export interface BasicTableProps<T = any> {
  data: T[]
  columns: TableColumn<T>[]
  tableProps?: Record<string, any>
  pagination?: boolean | Partial<PaginationProps>
  loading?: boolean
  bordered?: boolean
  striped?: boolean
  maxHeight?: string | number
}

export interface BasicTableEmits<T = any> {
  (e: 'selection-change', val: T[]): void
  (e: 'sort-change', val: { prop: string; order: string }): void
  (e: 'filter-change', val: Record<string, (string | number)[]>): void
  (e: 'row-click', row: T, column: any, event: Event): void
  (e: 'row-dblclick', row: T, column: any, event: Event): void
  (e: 'cell-click', row: T, column: any, event: Event): void
  (e: 'current-change', currentRow: T, oldCurrentRow: T): void
  (e: 'update:page', page: number): void
  (e: 'update:size', size: number): void
}

export interface BasicTableInstance {
  clearSelection: () => void
  toggleRowSelection: (row: any, selected?: boolean) => void
  toggleAllSelection: () => void
  setCurrentRow: (row: any) => void
  scrollTo: (options: { left?: number; top?: number; behavior?: 'smooth' | 'auto' }) => void
}
