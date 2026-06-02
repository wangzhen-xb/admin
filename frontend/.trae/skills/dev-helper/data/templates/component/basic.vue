<template>
  <div class="{{ componentName }}">
    <slot></slot>
  </div>
</template>

<script lang="ts" setup>
defineProps<{
  // Add props here
}>();

// Add setup code here
</script>

<style lang="less" scoped>
.{{ componentName }} {
  // Add styles here
}
</style>
