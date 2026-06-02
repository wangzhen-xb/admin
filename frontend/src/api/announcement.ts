import { get, post, put, del } from '../utils/request'
import type { ApiResponse } from '../types/api'

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

export function getAnnouncements(): Promise<ApiResponse<AnnouncementInfo[]>> {
  return get('/announcements')
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

export function getActiveAnnouncements(): Promise<ApiResponse<AnnouncementInfo[]>> {
  return get('/announcements/active')
}

export function useAnnouncementApi() {
  return {
    getAnnouncements,
    getAnnouncement,
    createAnnouncement,
    updateAnnouncement,
    deleteAnnouncement
  }
}
