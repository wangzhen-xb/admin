import type { ModalProps } from 'element-plus'

export interface BasicModalProps extends /* @vue-ignore */ Partial<ModalProps> {
  visible: boolean
  title?: string
  width?: string | number
  height?: string | number
  closable?: boolean
  closeOnClickModal?: boolean
  closeOnPressEscape?: boolean
  destroyOnClose?: boolean
  footer?: boolean
}

export interface BasicModalEmits {
  (e: 'update:visible', val: boolean): void
  (e: 'close'): void
  (e: 'confirm'): void
}

export interface BasicModalInstance {
  open: () => void
  close: () => void
  refresh: () => void
}
