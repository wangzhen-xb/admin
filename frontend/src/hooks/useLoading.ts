import { ref } from 'vue'

export const useLoading = () => {
  const loading = ref(false)

  const start = () => {
    loading.value = true
  }

  const stop = () => {
    loading.value = false
  }

  const wrap = async <T>(fn: () => Promise<T>): Promise<T> => {
    loading.value = true
    try {
      return await fn()
    } finally {
      loading.value = false
    }
  }

  return {
    loading,
    start,
    stop,
    wrap
  }
}
