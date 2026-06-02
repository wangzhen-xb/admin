import { get } from '../utils/request'
import type { ApiResponse } from '../types/api'

export interface DashboardStats {
  totalUsers: number
  totalRoles: number
  totalMenus: number
  totalDepts: number
}

export interface RecentUser {
  id: number
  name: string
  role: string
  createTime: string
}

export interface SystemInfoItem {
  label: string
  value: string
}

export interface ActivityLogItem {
  icon: string
  color: string
  label: string
  user: string
  time: string
}

export function getStats(): Promise<ApiResponse<DashboardStats>> {
  return get('/dashboard/stats')
}

export function getRecentUsers(): Promise<ApiResponse<RecentUser[]>> {
  return get('/dashboard/recent-users')
}

export function getSystemInfo(): Promise<ApiResponse<SystemInfoItem[]>> {
  return get('/dashboard/system-info')
}

export function getActivityLog(): Promise<ApiResponse<ActivityLogItem[]>> {
  return get('/dashboard/activity-log')
}

export function useDashboardApi() {
  return {
    getStats,
    getRecentUsers,
    getSystemInfo,
    getActivityLog
  }
}