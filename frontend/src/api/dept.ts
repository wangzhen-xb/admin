import { get, post, put, del } from '../utils/request'
import type { ApiResponse } from '../types/api'

export interface DeptInfo {
  deptId: number
  deptName: string
  parentId: number
  parentName: string
  leader: string
  phone: string
  sortNum: number
  status: string
  createTime: string
}

export interface DeptCreateRequest {
  deptName: string
  parentId?: number
  leader?: string
  phone?: string
  sortNum?: number
  status?: string
}

export interface DeptUpdateRequest {
  deptId: number
  deptName?: string
  parentId?: number
  leader?: string
  phone?: string
  sortNum?: number
  status?: string
}

export function getDepts(): Promise<ApiResponse<DeptInfo[]>> {
  return get('/system/depts')
}

export function getDept(id: number): Promise<ApiResponse<DeptInfo>> {
  return get(`/system/depts/${id}`)
}

export function createDept(data: DeptCreateRequest): Promise<ApiResponse<DeptInfo>> {
  return post('/system/depts', data)
}

export function updateDept(data: DeptUpdateRequest): Promise<ApiResponse<DeptInfo>> {
  return put(`/system/depts/${data.deptId}`, data)
}

export function deleteDept(id: number): Promise<ApiResponse<void>> {
  return del(`/system/depts/${id}`)
}

export function getDeptsTree(): Promise<ApiResponse<DeptInfo[]>> {
  return get('/system/depts/tree')
}

export function useDeptApi() {
  return {
    getDepts,
    getDept,
    createDept,
    updateDept,
    deleteDept,
    getDeptsTree
  }
}
