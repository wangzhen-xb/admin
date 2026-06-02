<script setup lang="ts">
import { ElInput } from 'element-plus'
import type { BasicInputProps, BasicInputEmits } from './types'

const props = withDefaults(defineProps<BasicInputProps>(), {
  disabled: false,
  clearable: false,
  showPassword: false
})

const emit = defineEmits<BasicInputEmits>()

const handleInput = (val: any) => {
  emit('update:modelValue', val)
  emit('input', val)
}

const handleChange = (val: any) => {
  emit('change', val)
}

const handleFocus = (event: FocusEvent) => {
  emit('focus', event)
}

const handleBlur = (event: FocusEvent) => {
  emit('blur', event)
}
</script>

<template>
  <ElInput
    :model-value="modelValue"
    :placeholder="placeholder"
    :disabled="disabled"
    :clearable="clearable"
    :show-password="showPassword"
    :prefix-icon="prefixIcon"
    :suffix-icon="suffixIcon"
    v-bind="$props"
    @input="handleInput"
    @change="handleChange"
    @focus="handleFocus"
    @blur="handleBlur"
  />
</template>
