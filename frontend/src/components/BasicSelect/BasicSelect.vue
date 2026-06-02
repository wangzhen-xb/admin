<script setup lang="ts">
import { ElSelect, ElOption } from 'element-plus'
import type { BasicSelectProps, BasicSelectEmits } from './types'

const props = withDefaults(defineProps<BasicSelectProps>(), {
  disabled: false,
  clearable: false,
  multiple: false,
  filterable: false,
  options: () => []
})

const emit = defineEmits<BasicSelectEmits>()

const handleChange = (val: any) => {
  emit('update:modelValue', val)
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
  <ElSelect
    :model-value="modelValue"
    :placeholder="placeholder"
    :disabled="disabled"
    :clearable="clearable"
    :multiple="multiple"
    :filterable="filterable"
    v-bind="$props"
    @change="handleChange"
    @focus="handleFocus"
    @blur="handleBlur"
  >
    <ElOption
      v-for="option in options"
      :key="option.value"
      :label="option.label"
      :value="option.value"
      :disabled="option.disabled"
    />
    <slot />
  </ElSelect>
</template>
