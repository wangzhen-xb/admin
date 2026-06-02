import type { SelectProps } from 'element-plus'

export interface SelectOption {
  label: string
  value: any
  disabled?: boolean
}

export interface BasicSelectProps extends /* @vue-ignore */ Partial<SelectProps> {
  modelValue?: any
  placeholder?: string
  disabled?: boolean
  clearable?: boolean
  multiple?: boolean
  filterable?: boolean
  options?: SelectOption[]
}

export interface BasicSelectEmits {
  (e: 'update:modelValue', val: any): void
  (e: 'change', val: any): void
  (e: 'focus', event: FocusEvent): void
  (e: 'blur', event: FocusEvent): void
}
