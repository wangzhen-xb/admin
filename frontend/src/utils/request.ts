import axios, {
  type AxiosInstance,
  type AxiosResponse,
  type InternalAxiosRequestConfig,
  type AxiosError
} from 'axios'
import { ElMessage } from 'element-plus'
import { ApiResponse, ApiCode } from '../types/api'
import { refreshToken } from '../api/auth'
import router from '../router'

const service: AxiosInstance = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json;charset=UTF-8'
  }
})

let isRefreshing = false
let refreshSubscribers: Array<(token: string) => void> = []

const addRefreshSubscriber = (callback: (token: string) => void) => {
  refreshSubscribers.push(callback)
}

const onRefreshToken = (token: string) => {
  refreshSubscribers.forEach(callback => callback(token))
  refreshSubscribers = []
}

service.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = localStorage.getItem('accessToken')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error: AxiosError) => {
    console.error('Request error:', error)
    return Promise.reject(error)
  }
)

service.interceptors.response.use(
  (response: AxiosResponse<ApiResponse>) => {
    const res = response.data

    if (res.code !== ApiCode.SUCCESS) {
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message || '请求失败'))
    }

    return response
  },
  async (error: AxiosError) => {
    console.error('Response error:', error)
    const originalRequest = error.config as InternalAxiosRequestConfig & { _retry?: boolean }

    if (error.response) {
      const status = error.response.status

      switch (status) {
        case ApiCode.UNAUTHORIZED:
          if (!originalRequest._retry) {
            originalRequest._retry = true
            
            if (!isRefreshing) {
              isRefreshing = true
              try {
                const refreshTokenVal = localStorage.getItem('refreshToken')
                const refreshTokenRes = await refreshToken(refreshTokenVal || '')
                const newAccessToken = refreshTokenRes.data.accessToken
                const newRefreshToken = refreshTokenRes.data.refreshToken
                
                localStorage.setItem('accessToken', newAccessToken)
                localStorage.setItem('refreshToken', newRefreshToken)
                
                originalRequest.headers = originalRequest.headers || {}
                originalRequest.headers.Authorization = `Bearer ${newAccessToken}`
                
                onRefreshToken(newAccessToken)
                
                return service(originalRequest)
              } catch (refreshError) {
                ElMessage.warning('登录已过期，请重新登录')
                localStorage.removeItem('accessToken')
                localStorage.removeItem('refreshToken')
                localStorage.removeItem('userInfo')
                router.push('/login')
                return Promise.reject(refreshError)
              } finally {
                isRefreshing = false
              }
            } else {
              return new Promise((resolve) => {
                addRefreshSubscriber((token: string) => {
                  originalRequest.headers = originalRequest.headers || {}
                  originalRequest.headers.Authorization = `Bearer ${token}`
                  resolve(service(originalRequest))
                })
              })
            }
          }
          break
        case ApiCode.FORBIDDEN:
          ElMessage.error('没有权限访问此资源')
          break
        case ApiCode.NOT_FOUND:
          ElMessage.error('请求的资源不存在')
          break
        case ApiCode.SERVER_ERROR:
          ElMessage.error('服务器内部错误')
          break
        default:
          ElMessage.error('请求失败')
      }
    } else if (error.request) {
      ElMessage.error('网络请求超时，请检查网络连接')
    } else {
      ElMessage.error('请求配置错误')
    }

    return Promise.reject(error)
  }
)

export async function get<T = any>(url: string, params?: Record<string, any>): Promise<ApiResponse<T>> {
  const response = await service.get(url, { params })
  return response.data
}

export async function post<T = any>(url: string, data?: Record<string, any>): Promise<ApiResponse<T>> {
  const response = await service.post(url, data)
  return response.data
}

export async function put<T = any>(url: string, data?: Record<string, any>): Promise<ApiResponse<T>> {
  const response = await service.put(url, data)
  return response.data
}

export async function del<T = any>(url: string): Promise<ApiResponse<T>> {
  const response = await service.delete(url)
  return response.data
}

export default service
