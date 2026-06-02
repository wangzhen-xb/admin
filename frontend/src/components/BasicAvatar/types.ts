import type { AvatarProps } from 'element-plus'

export interface BasicAvatarProps extends Partial<AvatarProps> {
  size?: number | 'large' | 'medium' | 'small'
  icon?: any
  src?: string
  alt?: string
}
