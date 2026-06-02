<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'

import { login, getCaptcha } from '../api/auth'

import { ElMessage } from 'element-plus'
import { useUserStore } from '../stores/user'
import { User, Lock, RefreshCw, Eye, EyeOff, Shield, QrCode, Smartphone, MessageCircle, GitBranch, Globe } from 'lucide-vue-next'

const router = useRouter()
const { t } = useI18n()
const userStore = useUserStore()

const form = ref({
  username: '',
  password: '',
  captcha: '',
  captchaKey: ''
})

const captchaImage = ref('')
const isLoading = ref(false)
const showPassword = ref(false)
const isCaptchaLoading = ref(false)
const captchaStyle = ref({})
const rememberAccount = ref(false)
const loginType = ref('account')

const refreshCaptcha = async () => {
  isCaptchaLoading.value = true
  try {
    const res = await getCaptcha()
    form.value.captchaKey = res.data.key
    captchaImage.value = res.data.captcha
    generateCaptchaStyle()
  } catch (error) {
    ElMessage.error(t('login.captchaError'))
    captchaImage.value = ''
  } finally {
    isCaptchaLoading.value = false
  }
}

const generateCaptchaStyle = () => {
  const colors = ['#1f2937', '#374151', '#4b5563', '#6b7280', '#059669', '#7c3aed', '#db2777', '#ea580c']
  const bgColors = ['#ffffff', '#f9fafb', '#f3f4f6', '#fef3c7']
  
  captchaStyle.value = {
    color: colors[Math.floor(Math.random() * colors.length)],
    backgroundColor: bgColors[Math.floor(Math.random() * bgColors.length)],
    letterSpacing: `${Math.random() * 4 + 2}px`,
    transform: `rotate(${(Math.random() * 6 - 3)}deg)`
  }
}

const handleLogin = async () => {
  if (!form.value.username) {
    ElMessage.warning(t('message.required', { field: t('login.username') }))
    return
  }
  if (!form.value.password) {
    ElMessage.warning(t('message.required', { field: t('login.password') }))
    return
  }
  if (!form.value.captcha) {
    ElMessage.warning(t('message.required', { field: t('login.captcha') }))
    return
  }

  isLoading.value = true

  try {
    const res = await login({
      username: form.value.username,
      password: form.value.password,
      captcha: form.value.captchaKey + ':' + form.value.captcha,
      captchaKey: ''
    })
    userStore.setToken(res.data.accessToken, res.data.refreshToken)
    userStore.setUserInfo(res.data.user)
    ElMessage.success(t('login.success'))
    router.push('/')
  } catch (error) {
    ElMessage.error(t('login.failed'))
    refreshCaptcha()
  } finally {
    isLoading.value = false
  }
}

const switchLoginType = (type: string) => {
  loginType.value = type
}

onMounted(() => {
  refreshCaptcha()
})
</script>

<template>
  <div class="min-h-screen flex">
    <div class="hidden lg:flex lg:w-1/2 xl:w-[55%] relative overflow-hidden bg-gradient-to-br from-blue-50 via-indigo-50 to-purple-50">
      <div class="absolute inset-0">
        <div class="absolute top-20 left-20 w-64 h-64 bg-blue-200 rounded-full mix-blend-multiply filter blur-3xl opacity-40 animate-pulse"></div>
        <div class="absolute bottom-20 right-20 w-80 h-80 bg-purple-200 rounded-full mix-blend-multiply filter blur-3xl opacity-40 animate-pulse" style="animation-delay: 1s;"></div>
        <div class="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-96 h-96 bg-indigo-200 rounded-full mix-blend-multiply filter blur-3xl opacity-30"></div>
      </div>

      <div class="relative z-10 flex flex-col h-full p-12 lg:p-16">
        <div class="flex items-center gap-3 mb-16">
          <div class="w-10 h-10 bg-gradient-to-br from-blue-500 to-purple-600 rounded-lg flex items-center justify-center">
            <span class="text-white font-bold text-lg">V</span>
          </div>
          <span class="text-xl font-bold text-gray-800">Vben Admin</span>
        </div>

        <div class="flex-1 flex flex-col justify-center">
          <div class="relative mb-8">
            <svg viewBox="0 0 400 300" class="w-full max-w-md mx-auto">
              <defs>
                <linearGradient id="grad1" x1="0%" y1="0%" x2="100%" y2="100%">
                  <stop offset="0%" style="stop-color:#3b82f6;stop-opacity:1" />
                  <stop offset="100%" style="stop-color:#8b5cf6;stop-opacity:1" />
                </linearGradient>
                <linearGradient id="grad2" x1="0%" y1="0%" x2="100%" y2="100%">
                  <stop offset="0%" style="stop-color:#60a5fa;stop-opacity:1" />
                  <stop offset="100%" style="stop-color:#a78bfa;stop-opacity:1" />
                </linearGradient>
              </defs>
              
              <ellipse cx="200" cy="280" rx="120" ry="15" fill="#e0e7ff" opacity="0.5"/>
              
              <rect x="80" y="180" width="240" height="80" rx="8" fill="url(#grad1)" opacity="0.9"/>
              <rect x="90" y="190" width="100" height="60" rx="4" fill="white" opacity="0.9"/>
              <rect x="200" y="190" width="110" height="40" rx="4" fill="white" opacity="0.9"/>
              <rect x="200" y="240" width="60" height="30" rx="4" fill="#f0f9ff"/>
              
              <polygon points="120,180 140,140 160,180" fill="url(#grad2)"/>
              <polygon points="130,175 140,150 150,175" fill="white" opacity="0.8"/>
              
              <rect x="200" y="120" width="60" height="60" rx="6" fill="url(#grad1)" opacity="0.9"/>
              <circle cx="230" cy="150" r="15" fill="white" opacity="0.9"/>
              
              <circle cx="300" cy="150" r="30" fill="url(#grad2)" opacity="0.9"/>
              <circle cx="300" cy="150" r="20" fill="white" opacity="0.9"/>
              <circle cx="300" cy="150" r="8" fill="#3b82f6"/>
              
              <circle cx="100" cy="120" r="25" fill="#fef3c7"/>
              <path d="M92 120 Q100 110 108 120 Q100 130 92 120" fill="#f59e0b"/>
              
              <circle cx="330" cy="200" r="20" fill="#dcfce7"/>
              <path d="M322 200 L326 204 L334 196" stroke="#22c55e" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"/>
              
              <rect x="150" y="80" width="4" height="40" fill="#cbd5e1"/>
              <rect x="160" y="60" width="4" height="60" fill="#cbd5e1"/>
              <rect x="170" y="90" width="4" height="30" fill="#cbd5e1"/>
              <rect x="180" y="70" width="4" height="50" fill="#cbd5e1"/>
            </svg>
          </div>

          <div class="text-center max-w-md mx-auto">
            <h2 class="text-3xl font-bold text-gray-800 mb-4">开箱即用的大型中后台管理系统</h2>
            <p class="text-gray-500">工程化、高性能、开箱即用的前端模板</p>
          </div>
        </div>

        <div class="mt-auto text-center">
          <p class="text-sm text-gray-400">Copyright © 2024 Vben</p>
        </div>
      </div>
    </div>

    <div class="flex-1 flex items-center justify-center p-4 lg:p-8 bg-white">
      <div class="w-full max-w-md">
        <div class="text-center mb-8">
          <h1 class="text-2xl font-bold text-gray-800 mb-2">欢迎回来 <span class="text-2xl">👋</span></h1>
          <p class="text-gray-500">请输入您的账户信息以开始管理您的项目</p>
        </div>

        <div class="bg-white rounded-2xl shadow-lg border border-gray-100 p-8">
          <form @submit.prevent="handleLogin" class="space-y-5">
            <div>
              <label class="block text-sm font-medium text-gray-600 mb-2">快速选择账号</label>
              <el-select 
                v-model="form.username" 
                placeholder="请选择账号" 
                class="w-full"
                :size="'large'"
              >
                <el-option label="admin" value="admin" />
                <el-option label="test" value="test" />
              </el-select>
            </div>

            <el-input
              v-model="form.username"
              :placeholder="t('login.username')"
              :prefix-icon="User"
              class="w-full"
              :size="'large'"
            />

            <el-input
              v-model="form.password"
              :type="showPassword ? 'text' : 'password'"
              :placeholder="t('login.password')"
              :prefix-icon="Lock"
              :suffix-icon="showPassword ? Eye : EyeOff"
              @click-suffix="showPassword = !showPassword"
              class="w-full"
              :size="'large'"
            />

            <div class="flex gap-3 items-center">
              <el-input
                v-model="form.captcha"
                :placeholder="t('login.captcha')"
                :prefix-icon="Shield"
                maxlength="4"
                class="flex-1"
                :size="'large'"
              />
              <div
                class="w-28 h-12 rounded-lg border border-gray-200 flex items-center justify-center text-gray-700 font-mono font-bold text-lg cursor-pointer hover:bg-gray-50 hover:border-gray-300 transition-all select-none"
                :style="captchaStyle"
                @click="refreshCaptcha"
              >
                <RefreshCw v-if="isCaptchaLoading" class="w-5 h-5 text-blue-500 animate-spin" />
                <span v-else>{{ captchaImage || '--' }}</span>
              </div>
            </div>

            <div class="flex items-center justify-between">
              <label class="flex items-center gap-2 cursor-pointer">
                <input type="checkbox" v-model="rememberAccount" class="w-4 h-4 text-blue-500 rounded border-gray-300 focus:ring-blue-500" />
                <span class="text-sm text-gray-600">记住账号</span>
              </label>
              <a href="#" class="text-sm text-blue-500 hover:text-blue-600 transition-colors">忘记密码？</a>
            </div>

            <el-button type="primary" native-type="submit" :loading="isLoading" class="w-full" :size="'large'">
              {{ t('login.loginBtn') }}
            </el-button>
          </form>

          <div class="mt-6">
            <div class="relative">
              <div class="absolute inset-0 flex items-center">
                <div class="w-full border-t border-gray-200"></div>
              </div>
              <div class="relative flex justify-center text-sm">
                <span class="px-4 bg-white text-gray-400">其他登录方式</span>
              </div>
            </div>

            <div class="mt-4 flex gap-3">
              <button 
                @click="switchLoginType('mobile')"
                class="flex-1 flex items-center justify-center gap-2 py-2.5 px-4 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors text-gray-600"
              >
                <Smartphone class="w-4 h-4" />
                <span class="text-sm">手机号登录</span>
              </button>
              <button 
                @click="switchLoginType('qr')"
                class="flex-1 flex items-center justify-center gap-2 py-2.5 px-4 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors text-gray-600"
              >
                <QrCode class="w-4 h-4" />
                <span class="text-sm">扫码登录</span>
              </button>
            </div>

            <div class="mt-4 flex justify-center gap-4">
              <button class="w-10 h-10 flex items-center justify-center rounded-full border border-gray-200 hover:bg-green-50 hover:border-green-200 transition-colors">
                <Globe class="w-5 h-5 text-green-500" />
              </button>
              <button class="w-10 h-10 flex items-center justify-center rounded-full border border-gray-200 hover:bg-gray-50 transition-colors">
                <GitBranch class="w-5 h-5 text-gray-700" />
              </button>
              <button class="w-10 h-10 flex items-center justify-center rounded-full border border-gray-200 hover:bg-blue-50 hover:border-blue-200 transition-colors">
                <MessageCircle class="w-5 h-5 text-blue-500" />
              </button>
            </div>
          </div>

          <div class="mt-6 text-center">
            <span class="text-sm text-gray-500">还没有账号？</span>
            <a href="#" class="text-sm text-blue-500 hover:text-blue-600 ml-1">创建账号</a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

svg {
  animation: float 6s ease-in-out infinite;
}
</style>
