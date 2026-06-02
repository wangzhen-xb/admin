import { defineStore } from 'pinia'
import { ref, watch } from 'vue'
import i18n from '../i18n'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import enUs from 'element-plus/es/locale/lang/en'

interface SystemStats {
  users: number
  roles: number
  menus: number
  depts: number
}

export type Language = 'zh-CN' | 'en-US'
export type Theme = 'light' | 'dark'

export const useSystemStore = defineStore('system', () => {
  const stats = ref<SystemStats>({
    users: 0,
    roles: 0,
    menus: 0,
    depts: 0
  })

  const sidebarCollapsed = ref<boolean>(false)

  const language = ref<Language>((localStorage.getItem('language') as Language) || 'zh-CN')

  const theme = ref<Theme>((localStorage.getItem('theme') as Theme) || 'light')

  const setStats = (newStats: Partial<SystemStats>) => {
    stats.value = { ...stats.value, ...newStats }
  }

  const toggleSidebar = () => {
    sidebarCollapsed.value = !sidebarCollapsed.value
  }

  const setSidebarCollapsed = (collapsed: boolean) => {
    sidebarCollapsed.value = collapsed
  }

  const setLanguage = (lang: Language) => {
    language.value = lang
    localStorage.setItem('language', lang)
    i18n.global.locale.value = lang

    const elementLocale = lang === 'en-US' ? enUs : zhCn
    const configProvider = document.querySelector('el-config-provider')
    if (configProvider) {
      ;(configProvider as HTMLElement).setAttribute('locale', JSON.stringify(elementLocale))
    }
  }

  const setTheme = (newTheme: Theme) => {
    theme.value = newTheme
    localStorage.setItem('theme', newTheme)

    if (newTheme === 'dark') {
      document.documentElement.classList.add('dark')
    } else {
      document.documentElement.classList.remove('dark')
    }
  }

  const toggleTheme = () => {
    setTheme(theme.value === 'light' ? 'dark' : 'light')
  }

  watch(language, newLang => {
    localStorage.setItem('language', newLang)
  })

  watch(theme, newTheme => {
    localStorage.setItem('theme', newTheme)
  })

  return {
    stats,
    sidebarCollapsed,
    language,
    theme,
    setStats,
    toggleSidebar,
    setSidebarCollapsed,
    setLanguage,
    setTheme,
    toggleTheme
  }
})
