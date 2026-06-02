<script setup lang="ts">
import { computed } from 'vue'
import { ElButton } from 'element-plus'
import type { BasicButtonProps } from './types'

const props = withDefaults(defineProps<BasicButtonProps>(), {
  type: 'default',
  size: 'default',
  loading: false,
  disabled: false,
  ghost: false,
  block: false
})

const buttonProps = computed(() => {
  const { type, size, loading, disabled, ghost, block, ...rest } = props
  return {
    type,
    size,
    loading,
    disabled,
    ghost,
    block,
    ...rest
  }
})
</script>

<template>
  <ElButton v-bind="buttonProps">
    <template #default>
      <slot />
    </template>
  </ElButton>
</template>
