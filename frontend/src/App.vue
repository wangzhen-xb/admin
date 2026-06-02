<template>
  <div id="app">
    <ElConfigProvider :locale="currentLocale">
      <router-view />
    </ElConfigProvider>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, watch } from 'vue'
import { ElConfigProvider } from 'element-plus'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import enUs from 'element-plus/es/locale/lang/en'
import { useSystemStore } from './stores/system'
import { useUserStore } from './stores/user'

const systemStore = useSystemStore()
const userStore = useUserStore()

const currentLocale = computed(() => {
  return systemStore.language === 'en-US' ? enUs : zhCn
})

const setThemeVars = (isDark: boolean) => {
  const root = document.documentElement
  if (isDark) {
    root.style.setProperty('--el-bg-color', '#1a1a2e')
    root.style.setProperty('--el-bg-color-page', '#16213e')
    root.style.setProperty('--el-bg-color-overlay', '#0f0f23')
    root.style.setProperty('--el-bg-color-primary', '#1f3460')
    root.style.setProperty('--el-bg-color-secondary', '#2d2d44')
    root.style.setProperty('--el-text-color-primary', '#e4e7ed')
    root.style.setProperty('--el-text-color-regular', '#c0c4cc')
    root.style.setProperty('--el-text-color-secondary', '#909399')
    root.style.setProperty('--el-text-color-placeholder', '#606266')
    root.style.setProperty('--el-border-color', '#2d2d44')
    root.style.setProperty('--el-border-color-light', '#2d2d44')
    root.style.setProperty('--el-fill-color', '#1f3460')
    root.style.setProperty('--el-fill-color-light', '#2d2d44')
    root.style.setProperty('--el-fill-color-blank', '#16213e')
  } else {
    root.style.removeProperty('--el-bg-color')
    root.style.removeProperty('--el-bg-color-page')
    root.style.removeProperty('--el-bg-color-overlay')
    root.style.removeProperty('--el-bg-color-primary')
    root.style.removeProperty('--el-bg-color-secondary')
    root.style.removeProperty('--el-text-color-primary')
    root.style.removeProperty('--el-text-color-regular')
    root.style.removeProperty('--el-text-color-secondary')
    root.style.removeProperty('--el-text-color-placeholder')
    root.style.removeProperty('--el-border-color')
    root.style.removeProperty('--el-border-color-light')
    root.style.removeProperty('--el-fill-color')
    root.style.removeProperty('--el-fill-color-light')
    root.style.removeProperty('--el-fill-color-blank')
  }
}

watch(() => systemStore.theme, (newTheme) => {
  const isDark = newTheme === 'dark'
  document.documentElement.classList.toggle('dark', isDark)
  setThemeVars(isDark)
})

onMounted(() => {
  const isDark = systemStore.theme === 'dark'
  document.documentElement.classList.toggle('dark', isDark)
  setThemeVars(isDark)
  
  const savedUser = localStorage.getItem('user')
  if (savedUser) {
    try {
      userStore.setUserInfo(JSON.parse(savedUser))
    } catch {
      console.error('Failed to parse user info')
    }
  }
})
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html,
body,
#app {
  height: 100%;
}

.dark .el-aside {
  background-color: #0f0f23 !important;
}

.dark .el-aside .bg-slate-900 {
  background-color: #0f0f23 !important;
}

.dark .el-aside .border-slate-700 {
  border-color: #2d2d44 !important;
}

.dark .el-aside .text-slate-400 {
  color: #c0c4cc !important;
}

.dark .el-aside .hover\:bg-white\/5:hover {
  background-color: #1f3460 !important;
}

.dark .el-aside .hover\:text-white:hover {
  color: #e4e7ed !important;
}

.dark .el-aside .text-white {
  color: #e4e7ed !important;
}

.dark .el-main .bg-slate-50 {
  background-color: #16213e !important;
}
</style>
