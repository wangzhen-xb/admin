<template>
  <div class="settings">
      <el-form :model="settings" label-width="120px">
        <el-form-item label="系统名称">
          <el-input v-model="settings.systemName"></el-input>
        </el-form-item>
        <el-form-item label="系统描述">
          <el-textarea v-model="settings.systemDesc"></el-textarea>
        </el-form-item>
        <el-form-item label="默认语言">
          <el-select v-model="settings.language">
            <el-option label="中文" value="zh-CN"></el-option>
            <el-option label="English" value="en-US"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否启用验证码">
          <el-switch v-model="settings.enableCaptcha"></el-switch>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveSettings">保存设置</el-button>
        </el-form-item>
      </el-form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const settings = ref({
  systemName: '管理系统',
  systemDesc: '基于Vue 3 + Spring Boot构建的管理系统',
  language: 'zh-CN',
  enableCaptcha: true
})

const saveSettings = () => {
  ElMessage.success('设置保存成功')
}
</script>

<style scoped>
.settings {
  padding: 0;
}
</style>
