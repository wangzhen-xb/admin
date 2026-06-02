import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import enUs from 'element-plus/es/locale/lang/en'
import { createPinia } from 'pinia'
import 'uno.css'
import './styles/index.scss'
import App from './App.vue'
import router from './router'
import i18n from './i18n'

const app = createApp(App)
const pinia = createPinia()

const locale = localStorage.getItem('language') || 'zh-CN'
const elementLocale = locale === 'en-US' ? enUs : zhCn

app.use(ElementPlus, {
  locale: elementLocale
})
app.use(i18n)
app.use(pinia)
app.use(router)
app.mount('#app')
