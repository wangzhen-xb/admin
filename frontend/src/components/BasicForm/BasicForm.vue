<script setup lang="ts">
import { ref } from 'vue'
import { ElForm, ElFormItem, ElInput, ElSelect, ElOption, ElRadioGroup, ElRadio, ElCheckboxGroup, ElCheckbox, ElSwitch, ElDatePicker, ElTimePicker } from 'element-plus'
import type { BasicFormProps, BasicFormEmits, BasicFormInstance, FormItem } from './types'

const props = withDefaults(defineProps<BasicFormProps>(), {
  layout: 'horizontal',
  labelWidth: '120px'
})

const emit = defineEmits<BasicFormEmits>()

const formRef = ref<InstanceType<typeof ElForm> | null>(null)

const isItemVisible = (item: FormItem): boolean => {
  if (typeof item.show === 'boolean') {
    return item.show
  }
  if (typeof item.show === 'function') {
    return item.show(props.model)
  }
  return true
}

const handleChange = (field: string, value: any) => {
  emit('change', field, value)
}

const validate = async (): Promise<boolean> => {
  if (!formRef.value) return false
  try {
    await formRef.value.validate()
    return true
  } catch {
    return false
  }
}

const validateField = async (props?: Array<string | { prop: string; callback?: (errorMessage: string) => void }>) => {
  if (!formRef.value) return
  await formRef.value.validateField(props as any)
}

const resetFields = () => {
  formRef.value?.resetFields()
}

const clearValidate = (props?: Array<string | { prop: string; callback?: (errorMessage: string) => void }>) => {
  formRef.value?.clearValidate(props as any)
}

defineExpose<BasicFormInstance>({
  validate,
  validateField,
  resetFields,
  clearValidate
})
</script>

<template>
  <ElForm
    ref="formRef"
    :model="model"
    :label-width="labelWidth"
    v-bind="formProps"
    @submit.prevent="emit('submit', model)"
  >
    <template v-for="item in schema" :key="item.field">
      <ElFormItem
        v-if="isItemVisible(item)"
        :label="item.label"
        :prop="item.field"
        :rules="item.rules"
        v-bind="item.props"
      >
        <ElInput
          v-if="item.type === 'input'"
          v-model="model[item.field]"
          v-bind="item.componentProps"
          @change="(val: any) => handleChange(item.field, val)"
        />
        
        <ElInput
          v-else-if="item.type === 'password'"
          type="password"
          v-model="model[item.field]"
          v-bind="item.componentProps"
          @change="(val: any) => handleChange(item.field, val)"
        />
        
        <ElInput
          v-else-if="item.type === 'textarea'"
          type="textarea"
          v-model="model[item.field]"
          v-bind="item.componentProps"
          @change="(val: any) => handleChange(item.field, val)"
        />
        
        <ElInput
          v-else-if="item.type === 'number'"
          type="number"
          v-model.number="model[item.field]"
          v-bind="item.componentProps"
          @change="(val: any) => handleChange(item.field, val)"
        />
        
        <ElSelect
          v-else-if="item.type === 'select'"
          v-model="model[item.field]"
          v-bind="item.componentProps"
          @change="(val: any) => handleChange(item.field, val)"
        >
          <ElOption
            v-for="option in item.options"
            :key="option.value"
            :label="option.label"
            :value="option.value"
          />
        </ElSelect>
        
        <ElRadioGroup
          v-else-if="item.type === 'radio'"
          v-model="model[item.field]"
          v-bind="item.componentProps"
          @change="(val: any) => handleChange(item.field, val)"
        >
          <ElRadio
            v-for="option in item.options"
            :key="option.value"
            :label="option.label"
            :value="option.value"
          />
        </ElRadioGroup>
        
        <ElCheckboxGroup
          v-else-if="item.type === 'checkbox'"
          v-model="model[item.field]"
          v-bind="item.componentProps"
          @change="(val: any) => handleChange(item.field, val)"
        >
          <ElCheckbox
            v-for="option in item.options"
            :key="option.value"
            :label="option.label"
            :value="option.value"
          />
        </ElCheckboxGroup>
        
        <ElSwitch
          v-else-if="item.type === 'switch'"
          v-model="model[item.field]"
          v-bind="item.componentProps"
          @change="(val: any) => handleChange(item.field, val)"
        />
        
        <ElDatePicker
          v-else-if="item.type === 'date-picker'"
          v-model="model[item.field]"
          v-bind="item.componentProps"
          @change="(val: any) => handleChange(item.field, val)"
        />
        
        <ElTimePicker
          v-else-if="item.type === 'time-picker'"
          v-model="model[item.field]"
          v-bind="item.componentProps"
          @change="(val: any) => handleChange(item.field, val)"
        />
        
        <ElDatePicker
          v-else-if="item.type === 'datetime-picker'"
          type="datetime"
          v-model="model[item.field]"
          v-bind="item.componentProps"
          @change="(val: any) => handleChange(item.field, val)"
        />
        
        <slot v-else-if="item.type === 'slot' && item.slot" :name="item.slot" :field="item.field" :model="model" />
      </ElFormItem>
    </template>
  </ElForm>
</template>
