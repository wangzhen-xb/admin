<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { useI18n } from 'vue-i18n'
import { useDeptApi, type DeptInfo, type DeptCreateRequest, type DeptUpdateRequest } from '../../api/dept'
import { Plus } from 'lucide-vue-next'
import { BasicForm, BasicTable } from '../../components'
import type { FormItem, TableColumn, BasicFormInstance } from '@/components/types'

const { t } = useI18n()
const deptApi = useDeptApi()

const deptList = ref<DeptInfo[]>([])
const total = ref(0)
const loading = ref(false)
const showAddDialog = ref(false)
const formRef = ref<BasicFormInstance | null>(null)
const searchForm = ref({ deptName: '' })

const form = ref({
  deptId: undefined as number | undefined,
  parentId: 0 as number,
  deptName: '' as string,
  leader: '' as string,
  phone: '' as string,
  sortNum: 0 as number,
  status: '0' as string
})

const formSchema: FormItem[] = [
  {
    field: 'parentId',
    label: t('page.depts.parentDept'),
    type: 'select',
    options: [
      { label: t('common.none'), value: 0 },
      ...deptList.value.map(d => ({ label: d.deptName, value: d.deptId }))
    ]
  },
  {
    field: 'deptName',
    label: t('page.depts.deptName'),
    type: 'input',
    rules: [{ required: true, message: t('message.required', { field: t('page.depts.deptName') }), trigger: 'blur' }]
  },
  {
    field: 'leader',
    label: t('page.depts.leader'),
    type: 'input'
  },
  {
    field: 'phone',
    label: t('page.depts.phone'),
    type: 'input'
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
  }
]

const columns = computed<TableColumn<DeptInfo>[]>(() => [
  { title: t('common.number'), key: 'index', type: 'index', width: 60 },
  { title: t('page.depts.deptName'), key: 'deptName' },
  { title: t('page.depts.parentDept'), key: 'parentName' },
  { title: t('page.depts.leader'), key: 'leader' },
  { title: t('page.depts.phone'), key: 'phone' },
  { title: t('common.sort'), key: 'sortNum', width: 80, align: 'center' },
  {
    title: t('common.status'),
    key: 'status',
    width: 100,
    align: 'center',
    slot: 'status'
  },
  {
    title: t('common.operation'),
    key: 'action',
    width: 140,
    align: 'center',
    slot: 'action'
  }
])

const fetchDepts = async () => {
  loading.value = true
  try {
    const res = await deptApi.getDepts()
    deptList.value = res.data || []
    total.value = deptList.value.length
  } catch (error) {
    ElMessage.error(t('message.fetchFailed'))
  } finally {
    loading.value = false
  }
}

const searchDepts = () => {
  const filtered = searchForm.value.deptName
    ? deptList.value.filter(d => d.deptName.includes(searchForm.value.deptName))
    : deptList.value
  deptList.value = filtered
}

const resetSearch = () => {
  searchForm.value = { deptName: '' }
  fetchDepts()
}

const editDept = (dept: DeptInfo) => {
  form.value = { 
    deptId: dept.deptId,
    parentId: dept.parentId || 0,
    deptName: dept.deptName,
    leader: dept.leader,
    phone: dept.phone,
    sortNum: dept.sortNum,
    status: dept.status
  }
  showAddDialog.value = true
}

const deleteDept = async (id: number) => {
  try {
    await deptApi.deleteDept(id)
    ElMessage.success(t('message.deleteSuccess'))
    fetchDepts()
  } catch (error) {
    ElMessage.error(t('message.deleteFailed'))
  }
}

const submitForm = async () => {
  const valid = await formRef.value?.validate()
  if (!valid) return

  try {
    if (form.value.deptId) {
      const updateData: DeptUpdateRequest = {
        deptId: form.value.deptId,
        deptName: form.value.deptName,
        parentId: form.value.parentId,
        leader: form.value.leader,
        phone: form.value.phone,
        sortNum: form.value.sortNum,
        status: form.value.status
      }
      await deptApi.updateDept(updateData)
    } else {
      const createData: DeptCreateRequest = {
        deptName: form.value.deptName,
        parentId: form.value.parentId,
        leader: form.value.leader,
        phone: form.value.phone,
        sortNum: form.value.sortNum,
        status: form.value.status
      }
      await deptApi.createDept(createData)
    }
    ElMessage.success(form.value.deptId ? t('message.updateSuccess') : t('message.addSuccess'))
    showAddDialog.value = false
    form.value = {
      deptId: undefined,
      parentId: 0,
      deptName: '',
      leader: '',
      phone: '',
      sortNum: 0,
      status: '0'
    }
    fetchDepts()
  } catch (error) {
    ElMessage.error(form.value.deptId ? t('message.updateFailed') : t('message.addFailed'))
  }
}

onMounted(() => {
  fetchDepts()
})
</script>

<template>
  <div class="depts-page">
    <div class="page-header">
      <div class="header-info">
        <h2 class="page-title">{{ t('layout.depts') }}</h2>
        <p class="page-subtitle">{{ t('page.depts.description') }}</p>
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
            v-model="searchForm.deptName"
            :placeholder="t('page.depts.deptName')"
            class="search-input"
            @keyup.enter="searchDepts"
          ></el-input>
          <el-button type="primary" @click="searchDepts">{{ t('common.search') }}</el-button>
          <el-button @click="resetSearch">{{ t('common.reset') }}</el-button>
        </div>
      </div>

      <div class="card-body">
        <BasicTable
          :data="deptList"
          :columns="columns"
          :loading="loading"
        >
          <template #status="{ row }">
            <el-tag :type="row.status === '0' ? 'success' : 'warning'">
              {{ row.status === '0' ? t('common.normal') : t('common.disabled') }}
            </el-tag>
          </template>
          <template #action="{ row }">
            <el-button size="small" @click="editDept(row)">{{ t('common.edit') }}</el-button>
            <el-button size="small" type="danger" @click="deleteDept(row.deptId)">
              {{ t('common.delete') }}
            </el-button>
          </template>
        </BasicTable>
      </div>
    </el-card>

    <el-dialog
      :title="form.deptId ? t('common.edit') : t('common.add')"
      v-model="showAddDialog"
      width="450px"
    >
      <BasicForm
        ref="formRef"
        :model="form"
        :schema="formSchema"
        :label-width="100"
        layout="vertical"
      />
      <template #footer>
        <el-button @click="showAddDialog = false">{{ t('common.cancel') }}</el-button>
        <el-button type="primary" @click="submitForm">{{ t('common.confirm') }}</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.depts-page {
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
