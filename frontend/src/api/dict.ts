import { get, post, put, del } from '../utils/request'
import type { ApiResponse } from '../types/api'

export interface DictTypeInfo {
  dictId: number
  dictName: string
  dictCode: string
  sortNum: number
  status: string
  remark: string
  createTime: string
}

export interface DictItemInfo {
  itemId: number
  dictCode: string
  itemValue: string
  itemLabel: string
  sortNum: number
  status: string
  remark: string
  createTime: string
}

export interface DictTypeCreateRequest {
  dictName: string
  dictCode: string
  sortNum?: number
  status?: string
  remark?: string
}

export interface DictTypeUpdateRequest {
  dictId: number
  dictName?: string
  dictCode?: string
  sortNum?: number
  status?: string
  remark?: string
}

export interface DictItemCreateRequest {
  dictCode: string
  itemValue: string
  itemLabel: string
  sortNum?: number
  status?: string
  remark?: string
}

export interface DictItemUpdateRequest {
  itemId: number
  dictCode?: string
  itemValue?: string
  itemLabel?: string
  sortNum?: number
  status?: string
  remark?: string
}

export function getDictTypes(): Promise<ApiResponse<DictTypeInfo[]>> {
  return get('/system/dict/types')
}

export function getDictType(id: number): Promise<ApiResponse<DictTypeInfo>> {
  return get(`/system/dict/types/${id}`)
}

export function createDictType(data: DictTypeCreateRequest): Promise<ApiResponse<DictTypeInfo>> {
  return post('/system/dict/types', data)
}

export function updateDictType(data: DictTypeUpdateRequest): Promise<ApiResponse<DictTypeInfo>> {
  return put(`/system/dict/types/${data.dictId}`, data)
}

export function deleteDictType(id: number): Promise<ApiResponse<void>> {
  return del(`/system/dict/types/${id}`)
}

export function getDictItems(dictCode: string): Promise<ApiResponse<DictItemInfo[]>> {
  return get(`/system/dict/items/${dictCode}`)
}

export function getEnabledDictItems(dictCode: string): Promise<ApiResponse<DictItemInfo[]>> {
  return get(`/system/dict/items/${dictCode}/enabled`)
}

export function getDictItem(id: number): Promise<ApiResponse<DictItemInfo>> {
  return get(`/system/dict/item/${id}`)
}

export function createDictItem(data: DictItemCreateRequest): Promise<ApiResponse<DictItemInfo>> {
  return post('/system/dict/items', data)
}

export function updateDictItem(data: DictItemUpdateRequest): Promise<ApiResponse<DictItemInfo>> {
  return put(`/system/dict/items/${data.itemId}`, data)
}

export function deleteDictItem(id: number): Promise<ApiResponse<void>> {
  return del(`/system/dict/items/${id}`)
}

export function useDictApi() {
  return {
    getDictTypes,
    getDictType,
    createDictType,
    updateDictType,
    deleteDictType,
    getDictItems,
    getEnabledDictItems,
    getDictItem,
    createDictItem,
    updateDictItem,
    deleteDictItem
  }
}