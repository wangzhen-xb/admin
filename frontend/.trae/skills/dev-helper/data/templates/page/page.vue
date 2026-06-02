<script lang="ts" setup>
import { ref, computed, onMounted } from 'vue';
import { BasicForm, useForm, FormSchema } from '@/components/Form';
import { CollapseContainer } from '@/components/Container';
import { useMessage } from '@/hooks/web/useMessage';

const { createMessage, createConfirm } = useMessage();

// Add setup code here
const loading = ref(false);
const formData = ref({});

// Form schema
const formSchema: FormSchema[] = [
  // Add form fields here
];

// Form
const [registerForm, formMethod] = useForm({
  schemas: formSchema,
  showActionButtonGroup: false,
  baseColProps: { span: 6 },
  rowProps: {
    gutter: 16,
  },
  labelWidth: 100,
  labelAlign: 'left',
});

// Field change handler
const handleFieldChange = (field, value, { formModel, schema }) => {
  // Add field change logic here
};

// Field blur handler
const handleFieldBlur = (field, value, { formModel }) => {
  // Add field blur logic here
};

onMounted(() => {
  // Initialization code here
});
</script>

<template>
  <a-card :bordered="false">
    <CollapseContainer title="{{ PageName }}" :showBgColor="false">
      <BasicForm
        class="p-y-4"
        @register="registerForm"
        @field-value-change="handleFieldChange"
        @field-value-blur="handleFieldBlur"
      />
    </CollapseContainer>
  </a-card>
</template>

<style lang="less" scoped>
.{{ pageName }} {
  // Add styles here
}
</style>
