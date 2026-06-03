import { get, post, put, del } from '../utils/request'
import type { ApiResponse, PageResult } from '../types/api'

export interface AnnouncementInfo {
  id: number
  title: string
  content: string
  type: string
  status: string
  publishTime: string
  createTime: string
}

export interface AnnouncementCreateRequest {
  title: string
  content: string
  type?: string
  status?: string
}

export interface AnnouncementUpdateRequest {
  id: number
  title?: string
  content?: string
  type?: string
  status?: string
}

export interface AnnouncementQueryParams {
  title?: string
  page?: number
  size?: number
}
export function getAnnouncements(params?: AnnouncementQueryParams): Promise<ApiResponse<PageResult<AnnouncementInfo>>> {
  return get(`/announcements`, params)
}

export function getAnnouncement(id: number): Promise<ApiResponse<AnnouncementInfo>> {
  return get(`/announcements/${id}`)
}

export function createAnnouncement(
  data: AnnouncementCreateRequest
): Promise<ApiResponse<AnnouncementInfo>> {
  return post('/announcements', data)
}

export function updateAnnouncement(
  data: AnnouncementUpdateRequest
): Promise<ApiResponse<AnnouncementInfo>> {
  return put(`/announcements/${data.id}`, data)
}

export function deleteAnnouncement(id: number): Promise<ApiResponse<void>> {
  return del(`/announcements/${id}`)
}

export function getActiveAnnouncements(page: number, size: number): Promise<ApiResponse<PageResult<AnnouncementInfo>>> {
  return get('/announcements/active', { params: { page, size } })
}

export function useAnnouncementApi() {
  return {
    getAnnouncements,
    getAnnouncement,
    createAnnouncement,
    updateAnnouncement,
    deleteAnnouncement,
    getActiveAnnouncements
  }
}
