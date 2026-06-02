import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const accessToken = ref<string>(localStorage.getItem('accessToken') || '')
  const refreshToken = ref<string>(localStorage.getItem('refreshToken') || '')
  const userInfo = ref<UserInfo | object>(JSON.parse(localStorage.getItem('userInfo') || '{}'))
  const isLoggedIn = ref<boolean>(!!accessToken.value)

  const setToken = (newAccessToken: string, newRefreshToken?: string) => {
    accessToken.value = newAccessToken
    localStorage.setItem('accessToken', newAccessToken)
    if (newRefreshToken) {
      refreshToken.value = newRefreshToken
      localStorage.setItem('refreshToken', newRefreshToken)
    }
    isLoggedIn.value = true
  }

  const setRefreshToken = (newRefreshToken: string) => {
    refreshToken.value = newRefreshToken
    localStorage.setItem('refreshToken', newRefreshToken)
  }

  const setUserInfo = (info: UserInfo) => {
    userInfo.value = info
    localStorage.setItem('userInfo', JSON.stringify(info))
  }

  const logout = () => {
    accessToken.value = ''
    refreshToken.value = ''
    userInfo.value = {}
    isLoggedIn.value = false
    localStorage.removeItem('accessToken')
    localStorage.removeItem('refreshToken')
    localStorage.removeItem('userInfo')
  }

  const isTokenExpired = (): boolean => {
    const token = accessToken.value
    if (!token) return true
    
    try {
      const payload = token.split('.')[1]
      const decoded = JSON.parse(atob(payload))
      const exp = decoded.exp * 1000
      return Date.now() > exp
    } catch {
      return true
    }
  }

  return {
    accessToken,
    refreshToken,
    userInfo,
    isLoggedIn,
    setToken,
    setRefreshToken,
    setUserInfo,
    logout,
    isTokenExpired
  }
})
