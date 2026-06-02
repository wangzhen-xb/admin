export const APP_CONFIG = {
  title: 'Vben Admin',
  description: '开箱即用的大型中后台管理系统',
  version: '1.0.0',
  logo: '/favicon.ico',
  copyright: 'Copyright © 2024 Vben Admin'
}

export const THEME_CONFIG = {
  primaryColor: '#3b82f6',
  successColor: '#22c55e',
  warningColor: '#f59e0b',
  dangerColor: '#ef4444',
  infoColor: '#06b6d4'
}

export const LAYOUT_CONFIG = {
  sidebar: {
    collapsedWidth: 64,
    expandedWidth: 200,
    defaultCollapsed: false
  },
  header: {
    height: 60
  },
  footer: {
    height: 60,
    show: true
  }
}

export const ROUTER_CONFIG = {
  loginPath: '/login',
  homePath: '/',
  notFoundPath: '/404',
  forbiddenPath: '/403'
}

export const API_CONFIG = {
  baseUrl: '/api',
  timeout: 30000,
  retryCount: 3,
  retryDelay: 1000
}

export const STORAGE_CONFIG = {
  tokenKey: 'access_token',
  refreshTokenKey: 'refresh_token',
  userInfoKey: 'user_info',
  themeKey: 'theme',
  localeKey: 'locale'
}
