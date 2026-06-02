<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { useI18n } from 'vue-i18n'
import { useRouter } from 'vue-router'
import { useDictApi, type DictTypeInfo, type DictTypeCreateRequest, type DictTypeUpdateRequest } from '../../api/dict'
import { Plus } from 'lucide-vue-next'
import { BasicForm, BasicTable, BasicModal } from '../../components'
import type { FormItem, TableColumn, BasicFormInstance } from '@/components/types'

const { t } = useI18n()
const router = useRouter()
const dictApi = useDictApi()

const goToDictItems = (dictCode: string) => {
  router.push(`/system/dict-items?dictCode=${dictCode}`)
}

const dictList = ref<DictTypeInfo[]>([])
const total = ref(0)
const loading = ref(false)
const showAddDialog = ref(false)
const formRef = ref<BasicFormInstance | null>(null)
const searchForm = ref({ dictName: '', dictCode: '' })

const form = ref({
  dictId: undefined as number | undefined,
  dictName: '' as string,
  dictCode: '' as string,
  sortNum: 0 as number,
  status: '0' as string,
  remark: '' as string
})

const formSchema: FormItem[] = [
  {
    field: 'dictName',
    label: t('page.dict.dictName'),
    type: 'input',
    rules: [{ required: true, message: t('message.required', { field: t('page.dict.dictName') }), trigger: 'blur' }]
  },
  {
    field: 'dictCode',
    label: t('page.dict.dictCode'),
    type: 'input',
    rules: [{ required: true, message: t('message.required', { field: t('page.dict.dictCode') }), trigger: 'blur' }]
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

const columns = computed<TableColumn<DictTypeInfo>[]>(() => [
  { title: t('common.number'), key: 'index', type: 'index', width: 60 },
  { title: t('page.dict.dictName'), key: 'dictName' },
  { 
    title: t('page.dict.dictCode'), 
    key: 'dictCode',
    slot: 'dictCode'
  },
  { title: t('common.sort'), key: 'sortNum', width: 80, align: 'center' },
  {
    title: t('common.status'),
    key: 'status',
    width: 100,
    align: 'center',
    slot: 'status'
  },
  { title: t('common.remark'), key: 'remark', width: 200 },
  {
    title: t('common.operation'),
    key: 'action',
    width: 140,
    align: 'center',
    slot: 'action'
  }
])

const fetchDictTypes = async () => {
  loading.value = true
  try {
    const res = await dictApi.getDictTypes()
    dictList.value = res.data || []
    total.value = dictList.value.length
  } catch (error) {
    ElMessage.error(t('message.fetchFailed'))
  } finally {
    loading.value = false
  }
}

const searchDictTypes = () => {
  let filtered = dictList.value
  if (searchForm.value.dictName) {
    filtered = filtered.filter(d => d.dictName.includes(searchForm.value.dictName))
  }
  if (searchForm.value.dictCode) {
    filtered = filtered.filter(d => d.dictCode.includes(searchForm.value.dictCode))
  }
}

const resetSearch = () => {
  searchForm.value = { dictName: '', dictCode: '' }
  fetchDictTypes()
}

const editDictType = (dict: DictTypeInfo) => {
  form.value = {
    dictId: dict.dictId,
    dictName: dict.dictName,
    dictCode: dict.dictCode,
    sortNum: dict.sortNum,
    status: dict.status,
    remark: dict.remark
  }
  showAddDialog.value = true
}

const deleteDictType = async (id: number) => {
  try {
    await dictApi.deleteDictType(id)
    ElMessage.success(t('message.deleteSuccess'))
    fetchDictTypes()
  } catch (error) {
    ElMessage.error(t('message.deleteFailed'))
  }
}

const submitForm = async () => {
  const valid = await formRef.value?.validate()
  if (!valid) return

  try {
    if (form.value.dictId) {
      const updateData: DictTypeUpdateRequest = {
        dictId: form.value.dictId,
        dictName: form.value.dictName,
        dictCode: form.value.dictCode,
        sortNum: form.value.sortNum,
        status: form.value.status,
        remark: form.value.remark
      }
      await dictApi.updateDictType(updateData)
    } else {
      const createData: DictTypeCreateRequest = {
        dictName: form.value.dictName,
        dictCode: form.value.dictCode,
        sortNum: form.value.sortNum,
        status: form.value.status,
        remark: form.value.remark
      }
      await dictApi.createDictType(createData)
    }
    ElMessage.success(form.value.dictId ? t('message.updateSuccess') : t('message.addSuccess'))
    showAddDialog.value = false
    form.value = {
      dictId: undefined,
      dictName: '',
      dictCode: '',
      sortNum: 0,
      status: '0',
      remark: ''
    }
    fetchDictTypes()
  } catch (error: any) {
    ElMessage.error(error.response?.data?.message || (form.value.dictId ? t('message.updateFailed') : t('message.addFailed')))
  }
}

onMounted(() => {
  fetchDictTypes()
})
</script>

<template>
  <div class="dict-types-page">
    <div class="page-header">
      <div class="header-info">
        <h2 class="page-title">{{ t('layout.dict') }}</h2>
        <p class="page-subtitle">{{ t('page.dict.dictTypeDescription') }}</p>
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
            v-model="searchForm.dictName"
            :placeholder="t('page.dict.dictName')"
            class="search-input"
            @keyup.enter="searchDictTypes"
          ></el-input>
          <el-input
            v-model="searchForm.dictCode"
            :placeholder="t('page.dict.dictCode')"
            class="search-input"
            @keyup.enter="searchDictTypes"
          ></el-input>
          <el-button type="primary" @click="searchDictTypes">{{ t('common.search') }}</el-button>
          <el-button @click="resetSearch">{{ t('common.reset') }}</el-button>
        </div>
      </div>

      <div class="card-body">
        <BasicTable
          :data="dictList"
          :columns="columns"
          :loading="loading"
        >
          <template #dictCode="{ row }">
            <el-button 
              size="small" 
              text
              class="dict-code-link"
              @click="goToDictItems((row as DictTypeInfo).dictCode)"
            >
              {{ (row as DictTypeInfo).dictCode }}
            </el-button>
          </template>
          <template #status="{ row }">
            <el-tag :type="row.status === '0' ? 'success' : 'warning'">
              {{ row.status === '0' ? t('common.normal') : t('common.disabled') }}
            </el-tag>
          </template>
          <template #action="{ row }">
            <el-button size="small" @click="editDictType(row as DictTypeInfo)">{{ t('common.edit') }}</el-button>
            <el-button size="small" type="danger" @click="deleteDictType((row as DictTypeInfo).dictId)">
              {{ t('common.delete') }}
            </el-button>
          </template>
        </BasicTable>
      </div>
    </el-card>

    <BasicModal
      :visible="showAddDialog"
      :title="form.dictId ? t('common.edit') : t('common.add')"
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
.dict-types-page {
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
    width: 200px;
  }
  .dict-code-link {
    color: #409eff;
    padding: 0;
    margin: 0;
    text-decoration: underline;
  }
  .dict-code-link:hover {
    color: #66b1ff;
  }
</style>