<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { ElMessage } from 'element-plus'
import {
  LayoutDashboard,
  Users,
  Settings,
  LogOut,
  ChevronLeft,
  ChevronRight,
  ChevronDown,
  Menu,
  Home,
  Shield,
  FolderOpen,
  Building2,
  Bell,
  User,
  BookOpen,
  Tag
} from 'lucide-vue-next'
import { useUserStore } from '../stores/user'
import { useSystemStore } from '../stores/system'

const router = useRouter()
const route = useRoute()
const { t } = useI18n()
const userStore = useUserStore()
const systemStore = useSystemStore()

const collapsed = ref(false)
const showSettingsDrawer = ref(false)
const activeMenu = computed(() => route.path)

const settings = ref({
  systemName: '管理系统',
  systemDesc: '基于Vue 3 + Spring Boot构建的管理系统',
  language: 'zh-CN',
  enableCaptcha: true,
  enableNotification: true
})

const userSettings = ref({
  theme: systemStore.theme,
  sidebarCollapsed: false
})

const menuItems = [
  { path: '/', icon: Home, label: 'layout.home' },
  { path: '/system/users', icon: Users, label: 'layout.users' },
  { path: '/system/roles', icon: Shield, label: 'layout.roles' },
  { path: '/system/menus', icon: FolderOpen, label: 'layout.menus' },
  { path: '/system/depts', icon: Building2, label: 'layout.depts' },
  { path: '/system/dict-types', icon: BookOpen, label: 'layout.dict' },
  { path: '/system/announcements', icon: Bell, label: 'layout.announcements' }
]

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  userStore.logout()
  ElMessage.success(t('login.success'))
  router.push('/login')
}

const toggleCollapse = () => {
  collapsed.value = !collapsed.value
}

const changeLanguage = (lang: 'zh-CN' | 'en-US') => {
  localStorage.setItem('language', lang)
  systemStore.setLanguage(lang)
  showSettingsDrawer.value = false
}

const changeTheme = () => {
  systemStore.toggleTheme()
}

const toggleSettingsDrawer = () => {
  showSettingsDrawer.value = !showSettingsDrawer.value
}

const closeSettingsDrawer = () => {
  showSettingsDrawer.value = false
}

const handleUserAction = (command: string) => {
  switch (command) {
    case 'profile':
      ElMessage.info('查看个人资料')
      break
    case 'settings':
      showSettingsDrawer.value = true
      break
    case 'logout':
      handleLogout()
      break
  }
}

const saveSettings = () => {
  ElMessage.success(t('message.saveSuccess'))
}

const changeThemeSetting = () => {
  systemStore.setTheme(userSettings.value.theme as 'light' | 'dark')
}

const toggleSidebarSetting = () => {
  collapsed.value = userSettings.value.sidebarCollapsed
}
</script>

<template>
  <el-container class="h-screen">
    <el-aside
      class="bg-slate-900 text-white z-50 transition-all duration-300"
      :width="collapsed ? '5rem' : '15rem'"
    >
      <div class="h-full flex flex-col">
        <el-header
          class="h-16 flex items-center justify-between px-4 border-b border-slate-700 bg-transparent"
        >
          <div v-if="!collapsed" class="flex items-center gap-2">
            <div
              class="w-8 h-8 bg-gradient-to-br from-blue-500 to-purple-600 rounded-lg flex items-center justify-center"
            >
              <LayoutDashboard class="w-5 h-5" />
            </div>
            <span class="font-bold text-lg">Admin</span>
          </div>
          <div v-else class="w-full flex justify-center">
            <div
              class="w-8 h-8 bg-gradient-to-br from-blue-500 to-purple-600 rounded-lg flex items-center justify-center"
            >
              <LayoutDashboard class="w-5 h-5" />
            </div>
          </div>
        </el-header>

        <nav class="flex-1 py-4 overflow-y-auto scrollbar-thin">
          <ul class="space-y-1 px-3">
            <li v-for="item in menuItems" :key="item.path">
              <router-link
                :to="item.path"
                class="flex items-center gap-3 px-3 py-2.5 rounded-lg transition-all duration-200"
                :class="
                  activeMenu === item.path
                    ? 'bg-blue-500/20 text-blue-400'
                    : 'text-slate-400 hover:bg-white/5 hover:text-white'
                "
              >
                <component :is="item.icon" class="w-5 h-5 flex-shrink-0" />
                <span
                  class="text-sm font-medium overflow-hidden whitespace-nowrap transition-all duration-200"
                  :class="collapsed ? 'w-0 opacity-0' : 'w-auto opacity-100'"
                  style="transition-delay: 50ms"
                  >{{ t(item.label) }}</span
                >
              </router-link>
            </li>
          </ul>
        </nav>

        <div class="p-3 border-t border-slate-700">
          <el-button @click="toggleCollapse" class="w-full" text>
            <ChevronLeft v-if="!collapsed" class="w-5 h-5" />
            <ChevronRight v-else class="w-5 h-5" />
          </el-button>
        </div>
      </div>
    </el-aside>

    <el-main class="h-full" :style="{ backgroundColor: 'var(--el-bg-color-page, #f8fafc)' }">
      <el-header
        class="fixed top-0 right-0 h-16 flex items-center justify-between shadow-sm z-40 transition-all duration-300"
        :class="collapsed ? 'left-5rem' : 'left-15rem'"
        :style="{ backgroundColor: 'var(--el-bg-color, #ffffff)', borderColor: 'var(--el-border-color, #e2e8f0)' }"
      >
        <el-button @click="toggleCollapse" text>
          <Menu class="w-5 h-5" :style="{ color: 'var(--el-text-color-primary, #1e293b)' }" />
        </el-button>

        <div class="flex items-center gap-2">
          <el-tooltip :content="t('layout.settings')" placement="bottom">
            <el-button @click="toggleSettingsDrawer" text>
              <Settings class="w-5 h-5" :style="{ color: 'var(--el-text-color-primary, #1e293b)' }" />
            </el-button>
          </el-tooltip>

          <el-tooltip :content="t('layout.language')" placement="bottom">
            <el-button
              @click="changeLanguage(systemStore.language === 'zh-CN' ? 'en-US' : 'zh-CN')"
              text
            >
              <span class="text-sm font-medium" :style="{ color: 'var(--el-text-color-primary, #1e293b)' }">
                {{ systemStore.language === 'zh-CN' ? '中' : 'EN' }}
              </span>
            </el-button>
          </el-tooltip>

          <el-tooltip :content="t('layout.theme')" placement="bottom">
            <el-button @click="changeTheme" text>
              <span class="text-lg">{{ systemStore.theme === 'dark' ? '☀️' : '🌙' }}</span>
            </el-button>
          </el-tooltip>

          <div class="w-px h-6 mx-1" :style="{ backgroundColor: 'var(--el-border-color, #e2e8f0)' }"></div>

          <el-dropdown trigger="click" @command="handleUserAction">
            <div class="flex items-center gap-2 cursor-pointer rounded-lg px-2 py-1 transition-colors" :style="{ color: 'var(--el-text-color-primary, #1e293b)', backgroundColor: 'var(--el-bg-color, #ffffff)' }">
              <el-avatar :size="32" class="bg-gradient-to-br from-blue-500 to-purple-600 text-white font-medium">
                {{ (userStore.userInfo as any)?.username?.charAt(0) || 'U' }}
              </el-avatar>
              <span class="text-sm font-medium" :style="{ color: 'var(--el-text-color-primary, #1e293b)' }">{{ (userStore.userInfo as any)?.username || 'Guest' }}</span>
              <ChevronDown class="w-4 h-4" :style="{ color: 'var(--el-text-color-secondary, #64748b)' }" />
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <User class="w-4 h-4 mr-2" />
                  {{ t('layout.userProfile') }}
                </el-dropdown-item>
                <el-dropdown-item command="settings">
                  <Settings class="w-4 h-4 mr-2" />
                  {{ t('layout.accountSettings') }}
                </el-dropdown-item>
                <el-dropdown-divider></el-dropdown-divider>
                <el-dropdown-item command="logout" class="text-red-500">
                  <LogOut class="w-4 h-4 mr-2" />
                  {{ t('layout.logout') }}
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <div class="pt-16 overflow-y-auto overflow-x-hidden scrollbar-thin" style="height: calc(100% - 4rem);">
        <router-view />
      </div>
    </el-main>
  </el-container>

  <el-drawer
    :title="t('layout.settings')"
    v-model="showSettingsDrawer"
    :before-close="closeSettingsDrawer"
    width="400px"
  >
    <div class="space-y-6">
      <div>
        <h3 class="text-lg font-semibold mb-3">{{ t('layout.systemSettings') }}</h3>
        <el-form :model="settings" label-width="100px" class="space-y-4">
          <el-form-item :label="t('settings.systemName')">
            <el-input v-model="settings.systemName" :placeholder="t('settings.enterSystemName')" />
          </el-form-item>
          <el-form-item :label="t('settings.systemDesc')">
            <el-textarea v-model="settings.systemDesc" :placeholder="t('settings.enterSystemDesc')" rows="3" />
          </el-form-item>
          <el-form-item :label="t('settings.defaultLanguage')">
            <el-select v-model="settings.language">
              <el-option label="中文" value="zh-CN"></el-option>
              <el-option label="English" value="en-US"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="t('settings.enableCaptcha')">
            <el-switch v-model="settings.enableCaptcha" />
          </el-form-item>
          <el-form-item :label="t('settings.enableNotification')">
            <el-switch v-model="settings.enableNotification" />
          </el-form-item>
          <el-form-item class="flex justify-end">
            <el-button type="primary" @click="saveSettings">{{ t('common.save') }}</el-button>
          </el-form-item>
        </el-form>
      </div>

      <div class="pt-4 border-t">
        <h3 class="text-lg font-semibold mb-3">{{ t('layout.userSettings') }}</h3>
        <el-form :model="userSettings" label-width="100px" class="space-y-4">
          <el-form-item :label="t('settings.theme')">
            <el-select v-model="userSettings.theme" @change="changeThemeSetting">
              <el-option :label="t('settings.light')" value="light"></el-option>
              <el-option :label="t('settings.dark')" value="dark"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="t('settings.sidebarCollapsed')">
            <el-switch v-model="userSettings.sidebarCollapsed" @change="toggleSidebarSetting" />
          </el-form-item>
        </el-form>
      </div>
    </div>
  </el-drawer>
</template>
