import { useUserStore } from '../stores/user'

export const useUser = () => {
  const userStore = useUserStore()

  return {
    userStore,
    userInfo: userStore.userInfo,
    token: userStore.token,
    isLogin: () => !!userStore.token,
    logout: () => userStore.logout(),
    setToken: (accessToken: string, refreshToken: string) => userStore.setToken(accessToken, refreshToken),
    setUserInfo: (user: Record<string, any>) => userStore.setUserInfo(user)
  }
}
