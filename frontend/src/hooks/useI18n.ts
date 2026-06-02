import { useI18n as useVueI18n } from 'vue-i18n'

export const useI18n = () => {
  const { t, locale, messages } = useVueI18n()

  const setLocale = (lang: string) => {
    locale.value = lang
  }

  const getLocale = () => {
    return locale.value
  }

  const getAvailableLocales = () => {
    return Object.keys(messages.value)
  }

  return {
    t,
    locale,
    setLocale,
    getLocale,
    getAvailableLocales
  }
}
