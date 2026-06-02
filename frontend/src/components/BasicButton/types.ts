import type { ButtonProps } from 'element-plus'

export type ButtonType = 'primary' | 'success' | 'warning' | 'danger' | 'info' | 'text'

export type ButtonSize = 'large' | 'default' | 'small'

export interface BasicButtonProps extends /* @vue-ignore */ Partial<ButtonProps> {
  type?: ButtonType
  size?: ButtonSize
  loading?: boolean
  disabled?: boolean
  icon?: any
  iconPosition?: 'left' | 'right'
  ghost?: boolean
  block?: boolean
}
