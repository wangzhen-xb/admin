import type { InputProps } from 'element-plus'

export interface BasicInputProps extends /* @vue-ignore */ Partial<InputProps> {
  modelValue?: any
  placeholder?: string
  disabled?: boolean
  clearable?: boolean
  showPassword?: boolean
  prefixIcon?: any
  suffixIcon?: any
}

export interface BasicInputEmits {
  (e: 'update:modelValue', val: any): void
  (e: 'focus', event: FocusEvent): void
  (e: 'blur', event: FocusEvent): void
  (e: 'change', val: any): void
  (e: 'input', val: any): void
}
