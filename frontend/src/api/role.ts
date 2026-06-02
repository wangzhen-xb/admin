import { get, post, put, del } from '../utils/request'
import type { ApiResponse } from '../types/api'

export interface RoleInfo {
  roleId: number
  roleName: string
  roleCode: string
  sortNum: number
  status: string
  remark: string
  createTime: string
}

export interface RoleCreateRequest {
  roleName: string
  roleCode: string
  sortNum?: number
  status?: string
  remark?: string
}

export interface RoleUpdateRequest {
  roleId: number
  roleName?: string
  roleCode?: string
  sortNum?: number
  status?: string
  remark?: string
}

export function getRoles(): Promise<ApiResponse<RoleInfo[]>> {
  return get('/system/roles')
}

export function getRole(id: number): Promise<ApiResponse<RoleInfo>> {
  return get(`/system/roles/${id}`)
}

export function createRole(data: RoleCreateRequest): Promise<ApiResponse<RoleInfo>> {
  return post('/system/roles', data)
}

export function updateRole(data: RoleUpdateRequest): Promise<ApiResponse<RoleInfo>> {
  return put(`/system/roles/${data.roleId}`, data)
}

export function deleteRole(id: number): Promise<ApiResponse<void>> {
  return del(`/system/roles/${id}`)
}

export function useRoleApi() {
  return {
    getRoles,
    getRole,
    createRole,
    updateRole,
    deleteRole
  }
}
