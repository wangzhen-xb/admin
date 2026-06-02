export interface LoginForm {
  username: string
  password: string
  captcha: string
}

export interface LoginResponse {
  token: string
  user: UserInfo
}

export interface CaptchaResponse {
  key: string
  captcha: string
}

export interface UserInfo {
  userId?: number
  username?: string
  nickname?: string
  email?: string
  phone?: string
  avatar?: string
  status?: string
  createTime?: string
}

export interface User {
  userId: number
  username: string
  nickname?: string
  password?: string
  email?: string
  phone?: string
  sex?: string
  avatar?: string
  status?: string
  deptId?: number
  deptName?: string
  roleIds?: number[]
  roles?: Role[]
  createTime?: string
  updateTime?: string
}

export interface Role {
  roleId: number
  roleName: string
  roleKey: string
  description?: string
  status?: string
  createTime?: string
  updateTime?: string
}

export interface Menu {
  menuId: number
  menuName: string
  parentId?: number
  orderNum?: number
  path?: string
  component?: string
  menuType?: string
  visible?: string
  status?: string
  perms?: string
  icon?: string
  children?: Menu[]
  createTime?: string
  updateTime?: string
}

export interface Dept {
  deptId: number
  parentId?: number
  deptName: string
  orderNum?: number
  leader?: string
  phone?: string
  email?: string
  status?: string
  children?: Dept[]
  createTime?: string
  updateTime?: string
}

export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}

export interface PageResponse<T = any> {
  code: number
  message: string
  data: {
    content: T[]
    totalElements: number
    totalPages: number
    page: number
    size: number
  }
}
