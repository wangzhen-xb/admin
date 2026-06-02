import { get, post } from '../utils/request'
import type { ApiResponse } from '../types/api'

export interface LoginUser {
  username: string
  password: string
  captcha: string
  captchaKey: string
}

export interface LoginResponse {
  accessToken: string
  refreshToken: string
  user: UserInfo
}

export interface UserInfo {
  userId: number
  username: string
  email?: string
  phone?: string
  deptId?: number
  deptName?: string
  status: string
  createTime: string
}

export interface CaptchaResponse {
  key: string
  captcha: string
}

export function login(data: LoginUser): Promise<ApiResponse<LoginResponse>> {
  return post('/auth/login', data)
}

export function refreshToken(refreshToken: string): Promise<ApiResponse<LoginResponse>> {
  return post('/auth/refresh', { refreshToken })
}

export function getCaptcha(): Promise<ApiResponse<CaptchaResponse>> {
  return get('/auth/captcha')
}

export function useAuthApi() {
  return {
    login,
    refreshToken,
    getCaptcha
  }
}
