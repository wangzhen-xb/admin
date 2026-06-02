<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { useUserStore } from '../stores/user'
import { ElCard, ElStatistic, ElAvatar, ElRow, ElCol, ElBadge, ElDropdown, ElDropdownMenu, ElDropdownItem, ElProgress } from 'element-plus'
import { Users, Shield, FolderOpen, Building2, Home, BarChart3, Settings, User, Github, Vue, Html5, Angular, React, Js, MessageSquare, Bell, Mail, FileText, HelpCircle, LogOut, ChevronRight, Calendar, TrendingUp, CheckCircle2, AlertCircle } from 'lucide-vue-next'

const { t } = useI18n()
const userStore = useUserStore()

const currentTime = ref(new Date())
const greeting = computed(() => {
  const hour = currentTime.value.getHours()
  if (hour < 6) return '夜深了'
  if (hour < 12) return '早安'
  if (hour < 18) return '下午好'
  return '晚上好'
})

const weatherInfo = ref({
  temp: '26°C',
  condition: '晴'
})

const stats = ref([
  { label: '总用户', value: 1234, icon: Users, color: 'blue', trend: '+12.5%' },
  { label: '角色数', value: 24, icon: Shield, color: 'green', trend: '+5.3%' },
  { label: '菜单数', value: 156, icon: FolderOpen, color: 'purple', trend: '+8.1%' },
  { label: '部门数', value: 12, icon: Building2, color: 'orange', trend: '+3.2%' }
])

const projects = ref([
  { name: 'Github', icon: Github, desc: '不要等待机会，而要创造机会。', date: '2021-04-01', status: '开发组' },
  { name: 'Vue', icon: Vue, desc: '现在的你决定将来的你。', date: '2021-04-01', status: '算法组', color: 'green' },
  { name: 'Html5', icon: Html5, desc: '没有什么比努力更重要。', date: '2021-04-01', status: '运维组', color: 'orange' },
  { name: 'Angular', icon: Angular, desc: '热情和欲望可以突破一切难关。', date: '2021-04-01', status: 'UI组', color: 'red' },
  { name: 'React', icon: React, desc: '健康的身体是实现目标的基石。', date: '2021-04-01', status: '技术组', color: 'cyan' },
  { name: 'Js', icon: Js, desc: '路是走出来的，而不是空想出来的。', date: '2021-04-01', status: '架构组', color: 'yellow' }
])

const quickNavs = ref([
  { name: '首页', icon: Home, path: '/' },
  { name: '仪表盘', icon: BarChart3, path: '/dashboard' },
  { name: '系统管理', icon: Settings, path: '/system/users' },
  { name: '权限管理', icon: Shield, path: '/system/roles' }
])

const recentActivities = ref([
  { user: '威廉', action: '创建了项目', target: 'Vue', time: '刚刚', avatar: 'W' },
  { user: '艾文', action: '关注了', target: '威廉', time: '1个小时前', avatar: 'A' },
  { user: '克里斯', action: '发布了动态', target: '', time: '1天前', avatar: 'K' }
])

const pendingTasks = ref([
  { title: '审查前端代码提交', time: '2024-07-11 09:00', completed: false, progress: 60 },
  { title: '系统性能优化', time: '2024-07-11 10:00', completed: false, progress: 40 },
  { title: '安全检查', time: '2024-07-11 14:00', completed: true, progress: 100 },
  { title: '新功能发布', time: '2024-07-12 09:00', completed: false, progress: 20 }
])

const userInfo = computed(() => userStore.userInfo)

const colorClasses: Record<string, string> = {
  blue: 'bg-blue-500',
  green: 'bg-green-500',
  purple: 'bg-purple-500',
  orange: 'bg-orange-500',
  red: 'bg-red-500',
  cyan: 'bg-cyan-500',
  yellow: 'bg-yellow-500'
}

const iconBgClasses: Record<string, string> = {
  blue: 'bg-blue-100 text-blue-600',
  green: 'bg-green-100 text-green-600',
  purple: 'bg-purple-100 text-purple-600',
  orange: 'bg-orange-100 text-orange-600',
  red: 'bg-red-100 text-red-600',
  cyan: 'bg-cyan-100 text-cyan-600',
  yellow: 'bg-yellow-100 text-yellow-600'
}

onMounted(() => {
  setInterval(() => {
    currentTime.value = new Date()
  }, 1000)
})
</script>

<template>
  <div class="p-6" :style="{ backgroundColor: 'var(--el-bg-color-page)' }">
    <div class="bg-gradient-to-r from-blue-500 via-purple-500 to-pink-500 rounded-2xl p-6 mb-6 shadow-lg">
      <div class="flex items-start justify-between">
        <div>
          <div class="flex items-center gap-3 mb-2">
            <div class="w-14 h-14 rounded-full bg-white/20 flex items-center justify-center">
              <User class="w-7 h-7 text-white" />
            </div>
            <div>
              <h2 class="text-2xl font-bold text-white">{{ greeting }}，{{ userInfo.name || 'Admin' }}</h2>
              <p class="text-white/80 text-sm">开始您一天的工作吧！</p>
            </div>
          </div>
          <div class="flex items-center gap-4 mt-4">
            <span class="text-white/60 text-sm">{{ currentTime.toLocaleDateString('zh-CN', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' }) }}</span>
            <span class="text-white/40">|</span>
            <span class="text-white/60 text-sm flex items-center gap-1">
              <Calendar class="w-4 h-4" />
              {{ weatherInfo.condition }}，{{ weatherInfo.temp }}
            </span>
          </div>
        </div>
        <div class="text-right">
          <div class="text-6xl">👋</div>
        </div>
      </div>
    </div>

    <el-row :gutter="20" class="mb-6">
      <el-col :span="6" v-for="stat in stats" :key="stat.label">
        <el-card class="hover:shadow-lg transition-shadow duration-300">
          <div class="flex items-center justify-between mb-3">
            <div :class="[colorClasses[stat.color], 'w-10 h-10 rounded-xl flex items-center justify-center']">
              <component :is="stat.icon" class="w-5 h-5 text-white" />
            </div>
            <div class="flex items-center gap-1 text-green-500 text-xs">
              <TrendingUp class="w-3 h-3" />
              {{ stat.trend }}
            </div>
          </div>
          <div class="text-2xl font-bold text-gray-800">{{ stat.value.toLocaleString() }}</div>
          <div class="text-sm text-gray-500 mt-1">{{ stat.label }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="16">
        <el-card class="mb-6" header="项目">
          <el-row :gutter="16">
            <el-col :span="8" v-for="project in projects" :key="project.name">
              <div class="bg-gray-50 rounded-xl p-4 hover:bg-gray-100 transition-colors cursor-pointer group">
                <div class="flex items-center gap-3 mb-3">
                  <div :class="[iconBgClasses[project.color || 'blue'], 'w-10 h-10 rounded-lg flex items-center justify-center group-hover:scale-110 transition-transform']">
                    <component :is="project.icon" class="w-5 h-5" />
                  </div>
                  <div>
                    <div class="font-semibold text-gray-800">{{ project.name }}</div>
                    <div class="text-xs text-gray-400">{{ project.status }}</div>
                  </div>
                </div>
                <p class="text-sm text-gray-500 mb-3">{{ project.desc }}</p>
                <div class="flex items-center justify-between">
                  <span class="text-xs text-gray-400">{{ project.date }}</span>
                  <ChevronRight class="w-4 h-4 text-gray-400 group-hover:text-blue-500 transition-colors" />
                </div>
              </div>
            </el-col>
          </el-row>
        </el-card>

        <el-card header="最新动态">
          <div class="space-y-4">
            <div 
              v-for="(activity, index) in recentActivities" 
              :key="index"
              class="flex items-center gap-3 py-2 border-b border-gray-100 last:border-0"
            >
              <el-avatar class="bg-gradient-to-br from-blue-500 to-purple-500 text-white font-medium">
                {{ activity.avatar }}
              </el-avatar>
              <div class="flex-1">
                <span class="text-sm font-medium text-gray-800">{{ activity.user }}</span>
                <span class="text-sm text-gray-500"> {{ activity.action }} </span>
                <span v-if="activity.target" class="text-sm font-medium text-blue-500">{{ activity.target }}</span>
              </div>
              <span class="text-xs text-gray-400">{{ activity.time }}</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card class="mb-6" header="快捷导航">
          <div class="grid grid-cols-2 gap-3">
            <div 
              v-for="nav in quickNavs" 
              :key="nav.name"
              class="bg-gray-50 rounded-xl p-4 flex flex-col items-center gap-2 hover:bg-blue-50 hover:border-blue-200 border border-transparent cursor-pointer transition-all group"
            >
              <div class="w-12 h-12 rounded-full bg-white shadow-sm flex items-center justify-center group-hover:shadow-md transition-shadow">
                <component :is="nav.icon" class="w-6 h-6 text-gray-600 group-hover:text-blue-500 transition-colors" />
              </div>
              <span class="text-sm font-medium text-gray-700">{{ nav.name }}</span>
            </div>
          </div>
        </el-card>

        <el-card header="待办事项">
          <div class="space-y-4">
            <div 
              v-for="(task, index) in pendingTasks" 
              :key="index"
              class="bg-gray-50 rounded-lg p-3"
            >
              <div class="flex items-start gap-2">
                <component 
                  :is="task.completed ? CheckCircle2 : AlertCircle" 
                  :class="task.completed ? 'w-5 h-5 text-green-500' : 'w-5 h-5 text-gray-400'" 
                />
                <div class="flex-1">
                  <div :class="task.completed ? 'text-sm text-gray-400 line-through' : 'text-sm font-medium text-gray-700'">
                    {{ task.title }}
                  </div>
                  <div class="text-xs text-gray-400 mt-1">{{ task.time }}</div>
                  <ElProgress 
                    v-if="!task.completed"
                    :percentage="task.progress" 
                    :stroke-width="4" 
                    class="mt-2"
                    :show-text="false"
                  />
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
