<script setup lang="ts">
import { computed } from 'vue'
import { ElAvatar } from 'element-plus'
import type { BasicAvatarProps } from './types'

const props = withDefaults(defineProps<BasicAvatarProps>(), {
  size: 'medium'
})

const sizeMap = {
  large: 40,
  medium: 32,
  small: 24
}

const avatarSize = computed(() => {
  if (typeof props.size === 'number') {
    return props.size
  }
  return sizeMap[props.size] || sizeMap.medium
})
</script>

<template>
  <ElAvatar
    :size="avatarSize"
    :src="src"
    :alt="alt"
    v-bind="$props"
  >
    <template #default>
      <slot />
    </template>
    <template #icon>
      <slot name="icon" />
    </template>
  </ElAvatar>
</template>
