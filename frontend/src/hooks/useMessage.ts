import { ElMessage, ElMessageBox, ElNotification } from 'element-plus'

export const useMessage = () => {
  const success = (message: string, title?: string) => {
    ElMessage.success(message)
  }

  const error = (message: string, title?: string) => {
    ElMessage.error(message)
  }

  const warning = (message: string, title?: string) => {
    ElMessage.warning(message)
  }

  const info = (message: string, title?: string) => {
    ElMessage.info(message)
  }

  const confirm = (message: string, title: string = '提示') => {
    return ElMessageBox.confirm(message, title, {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
  }

  const alert = (message: string, title: string = '提示') => {
    return ElMessageBox.alert(message, title)
  }

  const prompt = (message: string, title: string = '提示', options: any = {}) => {
    return ElMessageBox.prompt(message, title, options)
  }

  const notify = (message: string, title: string = '', type: 'success' | 'warning' | 'info' | 'error' = 'info') => {
    ElNotification({
      title,
      message,
      type
    })
  }

  return {
    success,
    error,
    warning,
    info,
    confirm,
    alert,
    prompt,
    notify
  }
}
