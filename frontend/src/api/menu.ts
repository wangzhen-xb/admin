import { get, post, put, del } from '../utils/request'
import type { ApiResponse } from '../types/api'

export interface MenuInfo {
  menuId: number
  menuName: string
  parentId: number
  path: string
  component: string
  menuType: string
  sortNum: number
  status: string
  createTime: string
}

export interface MenuCreateRequest {
  menuName: string
  parentId?: number
  path?: string
  component?: string
  menuType?: string
  sortNum?: number
  status?: string
}

export interface MenuUpdateRequest {
  menuId: number
  menuName?: string
  parentId?: number
  path?: string
  component?: string
  menuType?: string
  sortNum?: number
  status?: string
}

export function getMenus(): Promise<ApiResponse<MenuInfo[]>> {
  return get('/system/menus')
}

export function getMenu(id: number): Promise<ApiResponse<MenuInfo>> {
  return get(`/system/menus/${id}`)
}

export function createMenu(data: MenuCreateRequest): Promise<ApiResponse<MenuInfo>> {
  return post('/system/menus', data)
}

export function updateMenu(data: MenuUpdateRequest): Promise<ApiResponse<MenuInfo>> {
  return put(`/system/menus/${data.menuId}`, data)
}

export function deleteMenu(id: number): Promise<ApiResponse<void>> {
  return del(`/system/menus/${id}`)
}

export function getMenusTree(): Promise<ApiResponse<MenuInfo[]>> {
  return get('/system/menus/tree')
}

export function useMenuApi() {
  return {
    getMenus,
    getMenu,
    createMenu,
    updateMenu,
    deleteMenu,
    getMenusTree
  }
}
