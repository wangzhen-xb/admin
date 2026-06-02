import { get, post, put, del } from '../utils/request'
import type { ApiResponse, PageResult } from '../types/api'
import type { UserInfo } from './auth'

export interface UserCreateRequest {
  username: string
  password: string
  email?: string
  phone?: string
  deptId?: number
  status?: number
}

export interface UserUpdateRequest {
  id: number
  username?: string
  password?: string
  email?: string
  phone?: string
  deptId?: number
  status?: number
}

export interface UserQueryParams {
  username?: string
  deptId?: number
  page?: number
  size?: number
}

export function getUsers(params?: UserQueryParams): Promise<ApiResponse<PageResult<UserInfo>>> {
  return get('/users', params)
}

export function getUser(id: number): Promise<ApiResponse<UserInfo>> {
  return get(`/users/${id}`)
}

export function createUser(data: UserCreateRequest): Promise<ApiResponse<UserInfo>> {
  return post('/users', data)
}

export function updateUser(data: UserUpdateRequest): Promise<ApiResponse<UserInfo>> {
  return put(`/users/${data.id}`, data)
}

export function deleteUser(id: number): Promise<ApiResponse<void>> {
  return del(`/users/${id}`)
}

export function useUserApi() {
  return {
    getUsers,
    getUser,
    createUser,
    updateUser,
    deleteUser
  }
}
