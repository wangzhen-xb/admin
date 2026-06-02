<script setup lang="ts">
import { ElDrawer, ElButton } from 'element-plus'
import type { BasicDrawerProps, BasicDrawerEmits, BasicDrawerInstance } from './types'

const props = withDefaults(defineProps<BasicDrawerProps>(), {
  title: '',
  width: '30%',
  placement: 'right',
  closable: true,
  closeOnPressEscape: true,
  destroyOnClose: false,
  footer: true
})

const emit = defineEmits<BasicDrawerEmits>()

const handleClose = () => {
  emit('update:visible', false)
  emit('close')
}

const handleConfirm = () => {
  emit('confirm')
}

const open = () => {
  emit('update:visible', true)
}

const close = () => {
  handleClose()
}

const refresh = () => {
  close()
  setTimeout(() => {
    open()
  }, 100)
}

defineExpose<BasicDrawerInstance>({
  open,
  close,
  refresh
})
</script>

<template>
  <ElDrawer
    :visible="visible"
    :title="title"
    :width="width"
    :placement="placement"
    :closable="closable"
    :close-on-press-escape="closeOnPressEscape"
    :destroy-on-close="destroyOnClose"
    v-bind="$props"
    @update:visible="(val: boolean) => emit('update:visible', val)"
    @close="handleClose"
  >
    <template #default>
      <slot />
    </template>
    <template #footer v-if="footer">
      <slot name="footer">
        <ElButton @click="handleClose">取消</ElButton>
        <ElButton type="primary" @click="handleConfirm">确定</ElButton>
      </slot>
    </template>
  </ElDrawer>
</template>
