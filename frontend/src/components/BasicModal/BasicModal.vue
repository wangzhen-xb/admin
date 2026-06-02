<script setup lang="ts">import { ElModal } from 'element-plus';
import type { BasicModalProps, BasicModalEmits, BasicModalInstance } from './types';
const props = withDefaults(defineProps<BasicModalProps>(), {
 title: '',
 width: '500px',
 closable: true,
 closeOnClickModal: false,
 closeOnPressEscape: true,
 destroyOnClose: false,
 footer: true
});
const emit = defineEmits<BasicModalEmits>();
const handleClose = () => {
 emit('update:visible', false);
 emit('close');
};
const handleConfirm = () => {
 emit('confirm');
};
const open = () => {
 emit('update:visible', true);
};
const close = () => {
 handleClose();
};
const refresh = () => {
 close();
 setTimeout(() => {
 open();
 }, 100);
};
defineExpose<BasicModalInstance>({
 open,
 close,
 refresh
});
</script>

<template>
  <ElModal
    :visible="visible"
    :title="title"
    :width="width"
    :closable="closable"
    :close-on-click-modal="closeOnClickModal"
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
  </ElModal>
</template>
