import type { FormItemRule, FormProps, FormItemProps } from 'element-plus'

export type FormItemType =
  | 'input'
  | 'textarea'
  | 'select'
  | 'radio'
  | 'checkbox'
  | 'switch'
  | 'date-picker'
  | 'time-picker'
  | 'datetime-picker'
  | 'number'
  | 'password'
  | 'slot'

export interface FormItem {
  field: string
  label: string
  type: FormItemType
  props?: Partial<FormItemProps>
  componentProps?: Record<string, any>
  rules?: FormItemRule | FormItemRule[]
  options?: { label: string; value: any }[]
  slot?: string
  span?: number
  show?: boolean | ((formModel: Record<string, any>) => boolean)
}

export interface BasicFormProps {
  model: Record<string, any>
  schema: FormItem[]
  formProps?: Partial<FormProps>
  labelWidth?: string | number
  layout?: 'horizontal' | 'vertical' | 'inline'
}

export interface BasicFormEmits {
  (e: 'submit', values: Record<string, any>): void
  (e: 'change', field: string, value: any): void
}

export interface BasicFormInstance {
  validate: () => Promise<boolean>
  validateField: (props?: Array<string | { prop: string; callback?: (errorMessage: string) => void }>) => Promise<void>
  resetFields: () => void
  clearValidate: (props?: Array<string | { prop: string; callback?: (errorMessage: string) => void }>) => void
}
