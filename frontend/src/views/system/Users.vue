<script setup lang="ts">
import { ref, onMounted, computed, watch } from 'vue'
import { useI18n } from 'vue-i18n'
import { ElMessage } from 'element-plus'
import { Plus, Edit, Trash2, Eye, Search, RefreshCw } from 'lucide-vue-next'
import { useUserApi } from '../../api/user'
import { useDeptApi } from '../../api/dept'
import type { UserInfo } from '../../api/auth'
import { BasicForm, BasicTable, BasicModal } from '../../components'
import type { FormItem, TableColumn } from '@/components/types'

const { t } = useI18n()
const userApi = useUserApi()
const deptApi = useDeptApi()

const tableData = ref<UserInfo[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref<InstanceType<typeof BasicForm> | null>(null)

const searchForm = ref({
  username: undefined as string | undefined,
  deptId: undefined as number | undefined
})

const form = ref({
  id: undefined as number | undefined,
  username: '' as string | undefined,
  password: '' as string | undefined,
  email: '' as string | undefined,
  phone: '' as string | undefined,
  deptId: undefined as number | undefined,
  status: 1 as number
})

const deptOptions = ref<{ id: number; name: string }[]>([])

const fetchDepts = async () => {
  try {
    const res = await deptApi.getDepts()
    deptOptions.value = (res.data || []).map(d => ({ id: d.deptId, name: d.deptName }))
  } catch (error) {
    ElMessage.error(t('message.fetchFailed'))
  }
}

const formSchema = computed<FormItem[]>(() => [{
    field: 'username',
    label: t('page.users.username'),
    type: 'input',
    componentProps: {
      placeholder: t('page.users.enterUsername')
    },
    rules: [{ required: true, message: t('message.required', { field: t('page.users.username') }), trigger: 'blur' }]
  },
  {
    field: 'password',
    label: t('page.users.password'),
    type: 'password',
    componentProps: {
      placeholder: t('page.users.enterPassword'),
      showPassword: true
    },
    show: () => !form.value.id,
    rules: [{ required: true, message: t('message.required', { field: t('page.users.password') }), trigger: 'blur' }]
  },
  {
    field: 'email',
    label: t('page.users.email'),
    type: 'input',
    componentProps: {
      placeholder: t('page.users.enterEmail')
    }
  },
  {
    field: 'phone',
    label: t('page.users.phone'),
    type: 'input',
    componentProps: {
      placeholder: t('page.users.enterPhone')
    }
  },
  {
    field: 'deptId',
    label: t('page.users.dept'),
    type: 'select',
    options: deptOptions.value.map(d => ({ label: d.name, value: d.id })),
    componentProps: {
      placeholder: t('page.users.selectDept')
    }
  },
  {
    field: 'status',
    label: t('common.status'),
    type: 'select',
    options: [
      { label: t('common.enabled'), value: 1 },
      { label: t('common.disabled'), value: 0 }
    ]
  }
])

const columns = computed<TableColumn[]>(() => [
  { title: t('common.number'), key: 'index', type: 'index', width: 60 },
  { title: t('page.users.userId'), key: 'userId', width: 80 },
  { title: t('page.users.username'), key: 'username' },
  { title: t('page.users.email'), key: 'email' },
  { title: t('page.users.phone'), key: 'phone' },
  {
    title: t('page.users.dept'),
    key: 'deptName',
    formatter: (row) => row.deptName || '-'
  },
  {
    title: t('common.status'),
    key: 'status',
    width: 80,
    slot: 'status'
  },
  { title: t('common.createTime'), key: 'createTime', width: 160 },
  {
    title: t('common.operation'),
    key: 'action',
    width: 180,
    slot: 'action'
  }
])

const loadUsers = async () => {
  loading.value = true
  try {
    const res = await userApi.getUsers({
      page: page.value,
      size: size.value,
      ...searchForm.value
    })
    tableData.value = res.data?.list || []
    total.value = res.data?.total || 0
  } catch (error) {
    ElMessage.error(t('message.fetchFailed'))
    tableData.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  page.value = 1
  loadUsers()
}

const handleReset = () => {
  searchForm.value = {
    username: undefined,
    deptId: undefined
  }
  handleSearch()
}

const openAddDialog = () => {
  dialogTitle.value = t('common.add')
  form.value = {
    id: undefined,
    username: '',
    password: '',
    email: '',
    phone: '',
    deptId: undefined,
    status: 1
  }
  dialogVisible.value = true
}

const openEditDialog = (row: UserInfo) => {
  dialogTitle.value = t('common.edit')
  form.value = {
    id: row.userId,
    username: row.username || '',
    password: '',
    email: row.email || '',
    phone: row.phone || '',
    deptId: row.deptId,
    status: row.status === '0' ? 0 : 1
  }
  dialogVisible.value = true
}

const handleSave = async () => {
  const valid = await formRef.value?.validate()
  if (!valid) return

  try {
    if (form.value.id) {
      await userApi.updateUser({ ...form.value, id: form.value.id! })
      ElMessage.success(t('message.updateSuccess'))
    } else {
      await userApi.createUser({
        username: form.value.username || '',
        password: form.value.password || '',
        email: form.value.email || undefined,
        phone: form.value.phone || undefined,
        deptId: form.value.deptId,
        status: form.value.status
      })
      ElMessage.success(t('message.addSuccess'))
    }
    dialogVisible.value = false
    loadUsers()
  } catch (error) {
    ElMessage.error(form.value.id ? t('message.updateFailed') : t('message.addFailed'))
  }
}

const handleDelete = async (id: number) => {
  try {
    await userApi.deleteUser(id)
    ElMessage.success(t('message.deleteSuccess'))
    loadUsers()
  } catch (error) {
    ElMessage.error(t('message.deleteFailed'))
  }
}

const handlePageChange = (newPage: number) => {
  page.value = newPage
  loadUsers()
}

const handleSizeChange = (newSize: number) => {
  size.value = newSize
  page.value = 1
  loadUsers()
}

onMounted(async () => {
  await fetchDepts()
  loadUsers()
})
</script>

<template>
  <el-card>
    <template #header>
      <div class="flex items-center justify-between w-full">
        <div>
          <h2 class="text-xl font-semibold text-slate-800">{{ t('layout.users') }}</h2>
          <p class="text-sm text-slate-500 mt-1">{{ t('page.users.description') }}</p>
        </div>
        <el-button type="primary" @click="openAddDialog">
          <Plus class="w-4 h-4 mr-1" />
          {{ t('common.add') }}
        </el-button>
      </div>
    </template>

    <div class="flex items-center gap-4 mb-4">
      <el-input
        v-model="searchForm.username"
        :placeholder="t('page.users.enterUsername')"
        :prefix-icon="Search"
        class="w-48"
      />
      <el-select
        v-model.number="searchForm.deptId"
        :placeholder="t('page.users.selectDept')"
        class="w-48"
      >
        <el-option v-for="dept in deptOptions" :key="dept.id" :value="dept.id" :label="dept.name" />
      </el-select>
      <el-button type="primary" @click="handleSearch">
        {{ t('common.search') }}
      </el-button>
      <el-button @click="handleReset">
        <RefreshCw class="w-4 h-4 mr-1" />
        {{ t('common.reset') }}
      </el-button>
    </div>

    <div class="overflow-x-auto">
      <BasicTable
        :data="tableData"
        :columns="columns"
        :loading="loading"
        :pagination="{ total }"
        @selection-change="(val) => console.log(val)"
      >
        <template #status="{ row }">
          <span
            class="px-2 py-1 rounded-full text-xs font-medium"
            :class="row.status === '0' ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'"
          >
            {{ row.status === '0' ? t('common.enabled') : t('common.disabled') }}
          </span>
        </template>
        <template #action="{ row }">
          <div class="flex items-center gap-2">
            <el-tooltip :content="t('common.view')">
              <el-button link icon="View">
                <Eye class="w-4 h-4" />
              </el-button>
            </el-tooltip>
            <el-tooltip :content="t('common.edit')">
              <el-button link icon="Edit" @click="openEditDialog(row)">
                <Edit class="w-4 h-4" />
              </el-button>
            </el-tooltip>
            <el-tooltip :content="t('common.delete')">
              <el-button link icon="Delete" @click="handleDelete(row.userId)">
                <Trash2 class="w-4 h-4" />
              </el-button>
            </el-tooltip>
          </div>
        </template>
      </BasicTable>
    </div>
  </el-card>

  <BasicModal
      :visible="dialogVisible"
      :title="dialogTitle"
      width="500px"
      @update:visible="(val) => dialogVisible = val"
      @confirm="handleSave"
    >
      <BasicForm
        ref="formRef"
        :model="form"
        :schema="formSchema"
        :label-width="100"
        layout="vertical"
        @submit="handleSave"
      />
    </BasicModal>
</template>
