<script setup lang="ts">
import { ref, onMounted, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { useI18n } from 'vue-i18n'
import { useRoute } from 'vue-router'
import { useDictApi, type DictItemInfo, type DictItemCreateRequest, type DictItemUpdateRequest, type DictTypeInfo } from '../../api/dict'
import { Plus, ArrowLeft } from 'lucide-vue-next'
import { useRouter } from 'vue-router'
import { BasicForm, BasicTable } from '../../components'
import type { FormItem, TableColumn, BasicFormInstance } from '@/components/types'

const { t } = useI18n()
const route = useRoute()
const router = useRouter()
const dictApi = useDictApi()

const goBack = () => {
  router.push('/system/dict-types')
}

const dictTypeList = ref<DictTypeInfo[]>([])
const dictItemList = ref<DictItemInfo[]>([])
const selectedDictCode = ref('')
const total = ref(0)
const loading = ref(false)
const showAddDialog = ref(false)
const formRef = ref<BasicFormInstance | null>(null)
const searchForm = ref({ itemValue: '', itemLabel: '' })

const form = ref({
  itemId: undefined as number | undefined,
  dictCode: '' as string,
  itemValue: '' as string,
  itemLabel: '' as string,
  sortNum: 0 as number,
  status: '0' as string,
  remark: '' as string
})

const formSchema = computed<FormItem[]>(() => [
  {
    field: 'dictCode',
    label: t('page.dict.dictType'),
    type: 'select',
    options: dictTypeList.value.map(d => ({ label: d.dictName, value: d.dictCode })),
    rules: [{ required: true, message: t('message.required', { field: t('page.dict.dictType') }), trigger: 'blur' }]
  },
  {
    field: 'itemValue',
    label: t('page.dict.itemValue'),
    type: 'input',
    rules: [{ required: true, message: t('message.required', { field: t('page.dict.itemValue') }), trigger: 'blur' }]
  },
  {
    field: 'itemLabel',
    label: t('page.dict.itemLabel'),
    type: 'input',
    rules: [{ required: true, message: t('message.required', { field: t('page.dict.itemLabel') }), trigger: 'blur' }]
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
])

const columns = computed<TableColumn<DictItemInfo>[]>(() => [
  { title: t('common.number'), key: 'index', type: 'index', width: 60 },
  { title: t('page.dict.itemValue'), key: 'itemValue' },
  { title: t('page.dict.itemLabel'), key: 'itemLabel' },
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
  try {
    const res = await dictApi.getDictTypes()
    dictTypeList.value = res.data || []
    const urlDictCode = (route.query as any)?.dictCode
    if (urlDictCode) {
      selectedDictCode.value = urlDictCode
    } else if (dictTypeList.value.length > 0 && !selectedDictCode.value) {
      selectedDictCode.value = dictTypeList.value[0].dictCode
    }
    fetchDictItems()
  } catch (error) {
    ElMessage.error(t('message.fetchFailed'))
  }
}

const fetchDictItems = async () => {
  if (!selectedDictCode.value) return
  
  loading.value = true
  try {
    const res = await dictApi.getDictItems(selectedDictCode.value)
    dictItemList.value = res.data || []
    total.value = dictItemList.value.length
  } catch (error) {
    ElMessage.error(t('message.fetchFailed'))
  } finally {
    loading.value = false
  }
}

watch(selectedDictCode, () => {
  fetchDictItems()
})

const searchDictItems = () => {
  let filtered = dictItemList.value
  if (searchForm.value.itemValue) {
    filtered = filtered.filter(d => d.itemValue.includes(searchForm.value.itemValue))
  }
  if (searchForm.value.itemLabel) {
    filtered = filtered.filter(d => d.itemLabel.includes(searchForm.value.itemLabel))
  }
}

const resetSearch = () => {
  searchForm.value = { itemValue: '', itemLabel: '' }
  fetchDictItems()
}

const editDictItem = (item: DictItemInfo) => {
  form.value = {
    itemId: item.itemId,
    dictCode: item.dictCode,
    itemValue: item.itemValue,
    itemLabel: item.itemLabel,
    sortNum: item.sortNum,
    status: item.status,
    remark: item.remark
  }
  showAddDialog.value = true
}

const deleteDictItem = async (id: number) => {
  try {
    await dictApi.deleteDictItem(id)
    ElMessage.success(t('message.deleteSuccess'))
    fetchDictItems()
  } catch (error) {
    ElMessage.error(t('message.deleteFailed'))
  }
}

const submitForm = async () => {
  const valid = await formRef.value?.validate()
  if (!valid) return

  try {
    if (form.value.itemId) {
      const updateData: DictItemUpdateRequest = {
        itemId: form.value.itemId,
        dictCode: form.value.dictCode,
        itemValue: form.value.itemValue,
        itemLabel: form.value.itemLabel,
        sortNum: form.value.sortNum,
        status: form.value.status,
        remark: form.value.remark
      }
      await dictApi.updateDictItem(updateData)
    } else {
      const createData: DictItemCreateRequest = {
        dictCode: form.value.dictCode,
        itemValue: form.value.itemValue,
        itemLabel: form.value.itemLabel,
        sortNum: form.value.sortNum,
        status: form.value.status,
        remark: form.value.remark
      }
      await dictApi.createDictItem(createData)
    }
    ElMessage.success(form.value.itemId ? t('message.updateSuccess') : t('message.addSuccess'))
    showAddDialog.value = false
    form.value = {
      itemId: undefined,
      dictCode: selectedDictCode.value,
      itemValue: '',
      itemLabel: '',
      sortNum: 0,
      status: '0',
      remark: ''
    }
    fetchDictItems()
  } catch (error: any) {
    ElMessage.error(error.response?.data?.message || (form.value.itemId ? t('message.updateFailed') : t('message.addFailed')))
  }
}

const openAddDialog = () => {
  form.value = {
    itemId: undefined,
    dictCode: selectedDictCode.value,
    itemValue: '',
    itemLabel: '',
    sortNum: 0,
    status: '0',
    remark: ''
  }
  showAddDialog.value = true
}

onMounted(() => {
  fetchDictTypes()
})
</script>

<template>
  <div class="dict-items-page">
    <div class="page-header">
      <div class="header-info">
        <el-button type="text" class="back-btn" @click="goBack">
          <ArrowLeft class="w-4 h-4 mr-1" />
          <span>{{ t('common.back') }}</span>
        </el-button>
        <h2 class="page-title">{{ t('page.dict.dictItem') }}</h2>
        <p class="page-subtitle">{{ t('page.dict.dictItemDescription') }}</p>
      </div>
      <el-button type="primary" class="add-btn" @click="openAddDialog" :disabled="!selectedDictCode">
        <Plus class="w-4 h-4 mr-1" />
        <span>{{ t('common.add') }}</span>
      </el-button>
    </div>

    <el-card class="main-card">
      <div class="card-header">
        <div class="dict-select">
          <el-select
            v-model="selectedDictCode"
            :placeholder="t('page.dict.selectDictType')"
            class="dict-select-input"
          >
            <el-option
              v-for="dict in dictTypeList"
              :key="dict.dictCode"
              :label="dict.dictName"
              :value="dict.dictCode"
            />
          </el-select>
        </div>
        <div class="search-bar">
          <el-input
            v-model="searchForm.itemValue"
            :placeholder="t('page.dict.itemValue')"
            class="search-input"
            @keyup.enter="searchDictItems"
          ></el-input>
          <el-input
            v-model="searchForm.itemLabel"
            :placeholder="t('page.dict.itemLabel')"
            class="search-input"
            @keyup.enter="searchDictItems"
          ></el-input>
          <el-button type="primary" @click="searchDictItems">{{ t('common.search') }}</el-button>
          <el-button @click="resetSearch">{{ t('common.reset') }}</el-button>
        </div>
      </div>

      <div class="card-body">
        <BasicTable
          :data="dictItemList"
          :columns="columns"
          :loading="loading"
          :empty-text="t('page.dict.noDictItem')"
        >
          <template #status="{ row }">
            <el-tag :type="row.status === '0' ? 'success' : 'warning'">
              {{ row.status === '0' ? t('common.normal') : t('common.disabled') }}
            </el-tag>
          </template>
          <template #action="{ row }">
            <el-button size="small" @click="editDictItem(row as DictItemInfo)">{{ t('common.edit') }}</el-button>
            <el-button size="small" type="danger" @click="deleteDictItem((row as DictItemInfo).itemId)">
              {{ t('common.delete') }}
            </el-button>
          </template>
        </BasicTable>
      </div>
    </el-card>

    <el-dialog
      :title="form.itemId ? t('common.edit') : t('common.add')"
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
.dict-items-page {
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
.dict-select {
  margin-bottom: 16px;
}
.dict-select-input {
  width: 240px;
}
.search-bar {
  display: flex;
  gap: 12px;
  align-items: center;
}
.search-input {
  width: 180px;
}
.back-btn {
  margin-bottom: 8px;
  padding: 0;
  color: #6b7280;
}
.back-btn:hover {
  color: #409eff;
}
</style>