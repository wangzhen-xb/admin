import { computed } from 'vue'
import { useUserStore } from '../stores/user'

export const usePermission = () => {
  const userStore = useUserStore()

  const permissions = computed(() => userStore.userInfo?.permissions || [])
  const roles = computed(() => userStore.userInfo?.roles || [])

  const hasPermission = (permission: string): boolean => {
    return permissions.value.includes(permission)
  }

  const hasRole = (role: string | string[]): boolean => {
    if (Array.isArray(role)) {
      return role.some(r => roles.value.includes(r))
    }
    return roles.value.includes(role)
  }

  const hasAnyPermission = (permissions: string[]): boolean => {
    return permissions.some(p => hasPermission(p))
  }

  const hasAllPermissions = (permissions: string[]): boolean => {
    return permissions.every(p => hasPermission(p))
  }

  return {
    permissions,
    roles,
    hasPermission,
    hasRole,
    hasAnyPermission,
    hasAllPermissions
  }
}
