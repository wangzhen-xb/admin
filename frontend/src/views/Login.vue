<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'

import { login, getCaptcha } from '../api/auth'

import { ElMessage } from 'element-plus'
import { useUserStore } from '../stores/user'
import { User, Lock, RefreshCw, Eye, EyeOff, Shield } from 'lucide-vue-next'

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

onMounted(() => {
  refreshCaptcha()
})
</script>

<template>
  <div
    class="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-200 via-indigo-200 to-purple-200 p-4"
  >
    <div class="absolute inset-0 overflow-hidden">
      <div
        class="absolute -top-40 -right-40 w-80 h-80 bg-blue-300 rounded-full mix-blend-multiply filter blur-3xl opacity-50"
      ></div>
      <div
        class="absolute -bottom-40 -left-40 w-80 h-80 bg-purple-300 rounded-full mix-blend-multiply filter blur-3xl opacity-50"
      ></div>
      <div
        class="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-96 h-96 bg-indigo-300 rounded-full mix-blend-multiply filter blur-3xl opacity-30"
      ></div>
    </div>

    <div class="relative w-full max-w-md">
      <div class="bg-white rounded-2xl p-8 shadow-xl border border-gray-100">
        <div class="text-center mb-8">
          <div
            class="inline-flex items-center justify-center w-16 h-16 rounded-2xl bg-gradient-to-br from-blue-500 to-purple-600 mb-4 shadow-lg"
          >
            <User class="w-8 h-8 text-white" />
          </div>
          <h1 class="text-2xl font-bold text-gray-800 mb-2">{{ t('login.title') }}</h1>
          <p class="text-gray-500">{{ t('login.subtitle') }}</p>
        </div>

        <form @submit.prevent="handleLogin" class="space-y-6">
          <el-input
            v-model="form.username"
            :placeholder="t('login.username')"
            :prefix-icon="User"
            class="w-full"
          />

          <el-input
            v-model="form.password"
            :type="showPassword ? 'text' : 'password'"
            :placeholder="t('login.password')"
            :prefix-icon="Lock"
            :suffix-icon="showPassword ? Eye : EyeOff"
            @click-suffix="showPassword = !showPassword"
            class="w-full"
          />

          <div class="flex gap-3 items-center">
            <el-input
              v-model="form.captcha"
              :placeholder="t('login.captcha')"
              :prefix-icon="Shield"
              maxlength="4"
              class="flex-1"
            />
            <div
              class="w-28 h-10 rounded-lg border border-gray-200 flex items-center justify-center text-gray-700 font-mono font-bold text-lg cursor-pointer hover:bg-gray-50 hover:border-gray-300 transition-all select-none"
              :style="captchaStyle"
              @click="refreshCaptcha"
            >
              <RefreshCw v-if="isCaptchaLoading" class="w-5 h-5 text-blue-500 animate-spin" />
              <span v-else>{{ captchaImage || '--' }}</span>
            </div>
          </div>

          <el-button type="primary" native-type="submit" :loading="isLoading" class="w-full">
            {{ t('login.loginBtn') }}
          </el-button>
        </form>

        <div class="mt-6 p-4 bg-gray-50 rounded-xl">
          <p class="text-center text-sm text-gray-500">{{ t('login.tips') }}</p>
        </div>
      </div>
    </div>
  </div>
</template>
