<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { ElCarousel, ElCarouselItem, ElCard, ElButton, ElStatistic, ElDescriptions, ElAvatar, ElRow, ElCol } from 'element-plus'
import { Users, Shield, FolderOpen, Building2, Clock, Activity, Server, Volume2, ChevronLeft, ChevronRight } from 'lucide-vue-next'
import { getStats, getRecentUsers, getSystemInfo, getActivityLog, type DashboardStats, type RecentUser, type SystemInfoItem, type ActivityLogItem } from '../api/dashboard'
import { getActiveAnnouncements, type AnnouncementInfo } from '../api/announcement'

const { t } = useI18n()

const announcements = ref<AnnouncementInfo[]>([])
const carouselRef = ref<InstanceType<typeof ElCarousel> | null>(null)
const currentIndex = ref(0)

const stats = ref<DashboardStats>({
  totalUsers: 0,
  totalRoles: 0,
  totalMenus: 0,
  totalDepts: 0
})

const recentUsers = ref<RecentUser[]>([])
const systemInfo = ref<SystemInfoItem[]>([])
const activityList = ref<ActivityLogItem[]>([])

const currentTime = ref(new Date())

const greeting = computed(() => {
  const hour = currentTime.value.getHours()
  if (hour < 6) return 'dashboard.night'
  if (hour < 12) return 'dashboard.morning'
  if (hour < 18) return 'dashboard.afternoon'
  return 'dashboard.evening'
})

const statItems = computed(() => [
  { label: 'dashboard.totalUsers', value: stats.value.totalUsers, icon: Users, color: 'blue', trend: '+12.5%' },
  { label: 'dashboard.totalRoles', value: stats.value.totalRoles, icon: Shield, color: 'green', trend: '+5.3%' },
  { label: 'dashboard.totalMenus', value: stats.value.totalMenus, icon: FolderOpen, color: 'purple', trend: '+8.1%' },
  { label: 'dashboard.totalDepts', value: stats.value.totalDepts, icon: Building2, color: 'orange', trend: '+3.2%' }
])

const actionButtons = ref([
  { icon: Users, type: 'primary', label: 'dashboard.manageUsers' },
  { icon: Shield, type: 'success', label: 'dashboard.manageRoles' },
  { icon: FolderOpen, type: 'warning', label: 'dashboard.manageMenus' },
  { icon: Building2, type: 'danger', label: 'dashboard.manageDepts' }
])

const prevAnnouncement = () => {
  carouselRef.value?.prev()
}

const nextAnnouncement = () => {
  carouselRef.value?.next()
}

const onCarouselChange = (index: number) => {
  currentIndex.value = index
}

const colorClasses: Record<string, string> = {
  blue: 'bg-blue-500',
  green: 'bg-green-500',
  purple: 'bg-purple-500',
  orange: 'bg-orange-500'
}

const iconBgClasses: Record<string, string> = {
  green: 'bg-green-100 text-green-600',
  blue: 'bg-blue-100 text-blue-600',
  orange: 'bg-orange-100 text-orange-600'
}

const fetchData = async () => {
  try {
    const [statsRes, usersRes, infoRes, activityRes, announcementRes] = await Promise.all([
      getStats(),
      getRecentUsers(),
      getSystemInfo(),
      getActivityLog(),
      getActiveAnnouncements()
    ])

    if (statsRes.code === 200 && statsRes.data) {
      stats.value = statsRes.data
    }

    if (usersRes.code === 200 && usersRes.data) {
      recentUsers.value = usersRes.data
    }

    if (infoRes.code === 200 && infoRes.data) {
      systemInfo.value = infoRes.data
    }

    if (activityRes.code === 200 && activityRes.data) {
      activityList.value = activityRes.data
    }

    if (announcementRes.code === 200 && announcementRes.data) {
      announcements.value = announcementRes.data
    }
  } catch (error) {
    console.error('Failed to fetch dashboard data:', error)
  }
}

onMounted(() => {
  setInterval(() => {
    currentTime.value = new Date()
  }, 1000)
  fetchData()
})
</script>

<template>
  <div class="p-6" :style="{ backgroundColor: 'var(--el-bg-color)' }">
    <el-row class="mb-6">
      <el-col :span="12">
        <h1 class="text-2xl font-bold" :style="{ color: 'var(--el-text-color-primary)' }">{{ t('dashboard.title') }}</h1>
        <p class="mt-1" :style="{ color: 'var(--el-text-color-secondary)' }">{{ t(greeting) }}, {{ t('dashboard.welcome') }}</p>
      </el-col>
      <el-col :span="12" class="text-right">
        <div class="text-3xl font-mono" :style="{ color: 'var(--el-text-color-secondary)' }">{{ currentTime.toLocaleTimeString() }}</div>
        <div class="text-sm" :style="{ color: 'var(--el-text-color-placeholder)' }">{{ currentTime.toLocaleDateString() }}</div>
      </el-col>
    </el-row>

    <el-card v-if="announcements.length > 0" class="mb-6" body-style="padding: 0;">
      <div class="bg-gradient-to-r from-blue-600 to-purple-600">
        <el-row align="middle">
          <el-col :span="3">
            <div class="px-5 py-4 flex items-center gap-3">
              <Volume2 class="w-5 h-5 text-white animate-pulse" />
              <span class="text-white font-medium text-sm">{{ t('page.announcements.title') }}</span>
            </div>
          </el-col>
          <el-col :span="18">
            <el-carousel
              ref="carouselRef"
              direction="vertical"
              :interval="5000"
              height="48px"
              indicator-position="none"
              class="!bg-transparent"
              @change="onCarouselChange"
            >
              <el-carousel-item
                v-for="announcement in announcements"
                :key="announcement.id"
                class="!bg-transparent !h-full flex items-center"
              >
                <span class="text-white text-sm font-medium">{{ announcement.title }}：</span>
                <span class="text-white text-sm">{{ announcement.content }}</span>
                <span class="text-white/60 text-xs ml-4">{{ announcement.publishTime }}</span>
              </el-carousel-item>
            </el-carousel>
          </el-col>
          <el-col :span="3" class="flex items-center justify-end gap-2 px-4">
            <el-button @click="prevAnnouncement" text circle>
              <ChevronLeft class="w-4 h-4 text-white" />
            </el-button>
            <span class="text-white text-sm">{{ currentIndex + 1 }} / {{ announcements.length }}</span>
            <el-button @click="nextAnnouncement" text circle>
              <ChevronRight class="w-4 h-4 text-white" />
            </el-button>
          </el-col>
        </el-row>
      </div>
    </el-card>

    <el-row :gutter="20" class="mb-6">
      <el-col :span="6" v-for="stat in statItems" :key="stat.label">
        <el-card>
          <div class="flex items-center justify-between mb-4">
            <div :class="[colorClasses[stat.color], 'p-2.5 rounded-lg']">
              <component :is="stat.icon" class="w-5 h-5 text-white" />
            </div>
            <span v-if="stat.trend" class="text-green-500 text-xs font-medium">{{ stat.trend }}</span>
          </div>
          <el-statistic :value="stat.value" :label="t(stat.label)" />
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mb-6">
      <el-col :span="16">
        <el-card :header="t('dashboard.recentUsers')">
          <div class="space-y-3">
            <div
              v-for="(user, index) in recentUsers"
              :key="user.id || index"
              class="flex items-center justify-between py-2 border-b last:border-0"
              :style="{ borderColor: 'var(--el-border-color-light)' }"
            >
              <div class="flex items-center gap-3">
                <el-avatar class="bg-gradient-to-br from-blue-500 to-purple-500 text-white font-medium">
                  {{ user.name.charAt(0) }}
                </el-avatar>
                <div>
                  <div class="text-sm font-medium" :style="{ color: 'var(--el-text-color-primary)' }">{{ user.name }}</div>
                  <div class="text-xs" :style="{ color: 'var(--el-text-color-secondary)' }">{{ user.role }}</div>
                </div>
              </div>
              <div class="text-xs" :style="{ color: 'var(--el-text-color-placeholder)' }">{{ user.createTime }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card :header="t('dashboard.systemInfo')">
          <el-descriptions :column="1" border>
            <el-descriptions-item
              v-for="(info, index) in systemInfo"
              :key="index"
              :label="info.label"
            >
              {{ info.value }}
            </el-descriptions-item>
          </el-descriptions>
          <div class="mt-4 flex items-center gap-3">
            <Server class="w-5 h-5 text-green-500" />
            <span class="text-sm text-green-600 font-medium">{{ t('dashboard.serverOnline') }}</span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card :header="t('dashboard.recentActivity')">
          <div class="space-y-4">
            <div
              v-for="(activity, index) in activityList"
              :key="index"
              class="flex items-start gap-3"
            >
              <div :class="[iconBgClasses[activity.color], 'w-8 h-8 rounded-full flex items-center justify-center flex-shrink-0']">
                <component :is="activity.icon === 'Activity' ? Activity : activity.icon === 'FolderOpen' ? FolderOpen : Clock" class="w-4 h-4" />
              </div>
              <div>
                <div class="text-sm font-medium" :style="{ color: 'var(--el-text-color-primary)' }">{{ activity.label }}</div>
                <div class="text-xs" :style="{ color: 'var(--el-text-color-placeholder)' }">{{ activity.user }} - {{ activity.time }}</div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card :header="t('dashboard.quickActions')" class="h-full">
          <el-row :gutter="12">
            <el-col :span="12" v-for="(action, index) in actionButtons" :key="index">
              <el-button 
                :type="action.type as any" 
                class="w-full flex flex-col items-center gap-2 py-5 mt-5 rounded-xl transition-all duration-300 hover:scale-105 hover:shadow-lg"
              >
                <component :is="action.icon" class="w-8 h-8" />
                <span class="text-sm font-medium">{{ t(action.label) }}</span>
              </el-button>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
