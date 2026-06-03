export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}

export interface PageResult<T = any> {
  list: T[]
  total: number
  page: number
  size: number
}

export interface PaginationParams {
  page?: number
  size?: number
}

export enum ApiCode {
  SUCCESS = 200,
  UNAUTHORIZED = 401, // 未授权
  FORBIDDEN = 403, // 没有权限访问此资源
  NOT_FOUND = 404, // 请求的资源不存在
  SERVER_ERROR = 500 // 服务器内部错误
}
