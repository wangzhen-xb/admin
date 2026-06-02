import type { DrawerProps } from 'element-plus'

export interface BasicDrawerProps extends Partial<DrawerProps> {
  visible: boolean
  title?: string
  width?: string | number
  placement?: 'top' | 'right' | 'bottom' | 'left'
  closable?: boolean
  closeOnPressEscape?: boolean
  destroyOnClose?: boolean
  footer?: boolean
}

export interface BasicDrawerEmits {
  (e: 'update:visible', val: boolean): void
  (e: 'close'): void
  (e: 'confirm'): void
}

export interface BasicDrawerInstance {
  open: () => void
  close: () => void
  refresh: () => void
}
