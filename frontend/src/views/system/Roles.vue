<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { useI18n } from 'vue-i18n'
import { useRoleApi, type RoleInfo, type RoleCreateRequest, type RoleUpdateRequest } from '../../api/role'
import { Plus } from 'lucide-vue-next'
import { BasicForm, BasicTable, BasicModal } from '../../components'
import type { FormItem, TableColumn, BasicFormInstance } from '@/components/types'

const { t } = useI18n()
const roleApi = useRoleApi()

const roleList = ref<RoleInfo[]>([])
const total = ref(0)
const loading = ref(false)
const showAddDialog = ref(false)
const formRef = ref<BasicFormInstance | null>(null)
const searchForm = ref({ roleName: '' })

const form = ref({
  roleId: undefined as number | undefined,
  roleName: '' as string,
  roleCode: '' as string,
  sortNum: 0 as number,
  status: '0' as string,
  remark: '' as string
})

const formSchema: FormItem[] = [
  {
    field: 'roleName',
    label: t('page.roles.roleName'),
    type: 'input',
    componentProps: { placeholder: t('page.roles.roleName') },
    rules: [{ required: true, message: t('message.required', { field: t('page.roles.roleName') }), trigger: 'blur' }]
  },
  {
    field: 'roleCode',
    label: t('page.roles.roleCode'),
    type: 'input',
    componentProps: { placeholder: t('page.roles.roleCode') },
    rules: [{ required: true, message: t('message.required', { field: t('page.roles.roleCode') }), trigger: 'blur' }]
  },
  {
    field: 'sortNum',
    label: t('common.sort'),
    type: 'number'
  },
  {
    field: 'status',
    label: t('common.status'),
    type: 'radio',
    options: [
      { label: t('common.normal'), value: '0' },
      { label: t('common.disabled'), value: '1' }
    ]
  },
  {
    field: 'remark',
    label: t('common.remark'),
    type: 'textarea'
  }
]

const columns = computed<TableColumn<RoleInfo>[]>(() => [
  { title: t('common.number'), key: 'index', type: 'index', width: 60 },
  { title: t('page.roles.roleName'), key: 'roleName' },
  { title: t('page.roles.roleCode'), key: 'roleCode' },
  { title: t('common.sort'), key: 'sortNum', width: 80 },
  {
    title: t('common.status'),
    key: 'status',
    width: 100,
    slot: 'status'
  },
  { title: t('common.createTime'), key: 'createTime', width: 180 },
  {
    title: t('common.operation'),
    key: 'action',
    width: 140,
    slot: 'action'
  }
])

const fetchRoles = async () => {
  loading.value = true
  try {
    const res = await roleApi.getRoles()
    roleList.value = res.data || []
    total.value = roleList.value.length
  } catch (error) {
    ElMessage.error(t('message.fetchFailed'))
  } finally {
    loading.value = false
  }
}

const searchRoles = () => {
  const filtered = searchForm.value.roleName
    ? roleList.value.filter(r => r.roleName.includes(searchForm.value.roleName))
    : roleList.value
  roleList.value = filtered
}

const resetSearch = () => {
  searchForm.value = { roleName: '' }
  fetchRoles()
}

const editRole = (role: RoleInfo) => {
  form.value = { 
    roleId: role.roleId,
    roleName: role.roleName,
    roleCode: role.roleCode,
    sortNum: role.sortNum,
    status: role.status,
    remark: role.remark
  }
  showAddDialog.value = true
}

const deleteRole = async (id: number) => {
  try {
    await roleApi.deleteRole(id)
    ElMessage.success(t('message.deleteSuccess'))
    fetchRoles()
  } catch (error) {
    ElMessage.error(t('message.deleteFailed'))
  }
}

const submitForm = async () => {
  const valid = await formRef.value?.validate()
  if (!valid) return

  try {
    if (form.value.roleId) {
      const updateData: RoleUpdateRequest = {
        roleId: form.value.roleId,
        roleName: form.value.roleName,
        roleCode: form.value.roleCode,
        sortNum: form.value.sortNum,
        status: form.value.status,
        remark: form.value.remark
      }
      await roleApi.updateRole(updateData)
    } else {
      const createData: RoleCreateRequest = {
        roleName: form.value.roleName,
        roleCode: form.value.roleCode,
        sortNum: form.value.sortNum,
        status: form.value.status,
        remark: form.value.remark
      }
      await roleApi.createRole(createData)
    }
    ElMessage.success(form.value.roleId ? t('message.updateSuccess') : t('message.addSuccess'))
    showAddDialog.value = false
    form.value = { roleId: undefined, roleName: '', roleCode: '', sortNum: 0, status: '0', remark: '' }
    fetchRoles()
  } catch (error) {
    ElMessage.error(form.value.roleId ? t('message.updateFailed') : t('message.addFailed'))
  }
}

onMounted(() => {
  fetchRoles()
})
</script>

<template>
  <div class="roles-page">
    <div class="page-header">
      <div class="header-info">
        <h2 class="page-title">{{ t('layout.roles') }}</h2>
        <p class="page-subtitle">{{ t('page.roles.description') }}</p>
      </div>
      <el-button type="primary" class="add-btn" @click="showAddDialog = true">
        <Plus class="w-4 h-4 mr-1" />
        <span>{{ t('common.add') }}</span>
      </el-button>
    </div>

    <el-card class="main-card">
      <div class="card-header">
        <div class="search-bar">
          <el-input
            v-model="searchForm.roleName"
            :placeholder="t('page.roles.roleName')"
            class="search-input"
            @keyup.enter="searchRoles"
          ></el-input>
          <el-button type="primary" @click="searchRoles">{{ t('common.search') }}</el-button>
          <el-button @click="resetSearch">{{ t('common.reset') }}</el-button>
        </div>
      </div>

      <div class="card-body">
        <BasicTable
          :data="roleList"
          :columns="columns"
          :loading="loading"
        >
          <template #status="{ row }">
            <el-tag :type="row.status === '0' ? 'success' : 'warning'">
              {{ row.status === '0' ? t('common.normal') : t('common.disabled') }}
            </el-tag>
          </template>
          <template #action="{ row }">
            <el-button size="small" @click="editRole(row)">{{ t('common.edit') }}</el-button>
            <el-button size="small" type="danger" @click="deleteRole(row.roleId)">
              {{ t('common.delete') }}
            </el-button>
          </template>
        </BasicTable>
      </div>
    </el-card>

    <BasicModal
      :visible="showAddDialog"
      :title="form.roleId ? t('common.edit') : t('common.add')"
      width="450px"
      @update:visible="(val) => showAddDialog = val"
      @confirm="submitForm"
    >
      <BasicForm
        ref="formRef"
        :model="form"
        :schema="formSchema"
        :label-width="100"
        layout="vertical"
      />
    </BasicModal>
  </div>
</template>

<style scoped>
.roles-page {
  padding: 0;
}
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}
.header-info {
  display: flex;
  flex-direction: column;
}
.page-title {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: #1f2937;
}
.page-subtitle {
  margin: 4px 0 0;
  font-size: 14px;
  color: #6b7280;
}
.add-btn {
  display: flex;
  align-items: center;
  gap: 6px;
}
.btn-icon {
  width: 16px;
  height: 16px;
}
.main-card {
  border-radius: 8px;
}
.card-header {
  margin-bottom: 16px;
}
.search-bar {
  display: flex;
  gap: 12px;
  align-items: center;
}
.search-input {
  width: 240px;
}
</style>
