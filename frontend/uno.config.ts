import { defineConfig, presetUno, presetAttributify, presetIcons } from 'unocss'

export default defineConfig({
  presets: [
    presetUno(),
    presetAttributify(),
    presetIcons()
  ],
  shortcuts: {
    'flex-center': 'flex items-center justify-center',
    'flex-between': 'flex items-center justify-between',
    'card-shadow': 'shadow-lg rounded-xl',
    'btn-primary': 'bg-blue-500 text-white hover:bg-blue-600',
    'btn-secondary': 'bg-gray-100 text-gray-700 hover:bg-gray-200'
  }
})
