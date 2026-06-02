<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useI18n } from 'vue-i18n'
import { useAnnouncementApi, type AnnouncementInfo, type AnnouncementCreateRequest, type AnnouncementUpdateRequest } from '../../api/announcement'
import { Plus } from 'lucide-vue-next'
import { BasicForm, BasicTable } from '../../components'
import type { FormItem, TableColumn, BasicFormInstance } from '@/components/types'

const { t } = useI18n()
const announcementApi = useAnnouncementApi()

const announcementList = ref<AnnouncementInfo[]>([])
const loading = ref(false)
const showAddDialog = ref(false)
const formRef = ref<BasicFormInstance | null>(null)
const searchForm = ref({ title: '' })

const form = ref({
  id: undefined as number | undefined,
  title: '' as string,
  content: '' as string,
  type: 'notice' as string,
  status: '0' as string
})

const formSchema: FormItem[] = [
  {
    field: 'title',
    label: t('page.announcements.title'),
    type: 'input',
    componentProps: {
      placeholder: t('page.announcements.enterTitle')
    },
    rules: [{ required: true, message: t('message.required', { field: t('page.announcements.title') }), trigger: 'blur' }]
  },
  {
    field: 'content',
    label: t('page.announcements.content'),
    type: 'textarea',
    componentProps: {
      placeholder: t('page.announcements.enterContent'),
      rows: 5
    },
    rules: [{ required: true, message: t('message.required', { field: t('page.announcements.content') }), trigger: 'blur' }]
  },
  {
    field: 'type',
    label: t('page.announcements.type'),
    type: 'select',
    options: [
      { label: t('page.announcements.typeNotice'), value: 'notice' },
      { label: t('page.announcements.typeAlert'), value: 'alert' },
      { label: t('page.announcements.typeInfo'), value: 'info' }
    ]
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

const columns = computed<TableColumn<AnnouncementInfo>[]>(() => [
  { title: t('common.number'), key: 'index', type: 'index', width: 60 },
  { title: t('page.announcements.title'), key: 'title', minWidth: 150 },
  { title: t('page.announcements.content'), key: 'content', minWidth: 200 },
  {
    title: t('page.announcements.type'),
    key: 'type',
    width: 100,
    slot: 'type'
  },
  {
    title: t('common.status'),
    key: 'status',
    width: 100,
    slot: 'status'
  },
  { title: t('page.announcements.publishTime'), key: 'publishTime', width: 160 },
  {
    title: t('common.operation'),
    key: 'action',
    width: 140,
    fixed: 'right',
    slot: 'action'
  }
])

const filteredList = computed(() => {
  if (!searchForm.value.title) return announcementList.value
  return announcementList.value.filter(a => a.title.includes(searchForm.value.title))
})

const getTypeTagType = (type: string) => {
  switch (type) {
    case 'notice':
      return 'primary'
    case 'alert':
      return 'danger'
    case 'info':
      return 'info'
    default:
      return 'info'
  }
}

const getTypeName = (type: string) => {
  switch (type) {
    case 'notice':
      return t('page.announcements.typeNotice')
    case 'alert':
      return t('page.announcements.typeAlert')
    case 'info':
      return t('page.announcements.typeInfo')
    default:
      return ''
  }
}

const fetchAnnouncements = async () => {
  loading.value = true
  try {
    const res = await announcementApi.getAnnouncements()
    announcementList.value = res.data || []
  } catch (error) {
    ElMessage.error(t('message.fetchFailed'))
  } finally {
    loading.value = false
  }
}

const searchAnnouncements = () => {
}

const resetSearch = () => {
  searchForm.value = { title: '' }
}

const editAnnouncement = (item: AnnouncementInfo) => {
  form.value = { 
    id: item.id,
    title: item.title,
    content: item.content,
    type: item.type,
    status: item.status
  }
  showAddDialog.value = true
}

const deleteAnnouncement = async (id: number) => {
  try {
    await announcementApi.deleteAnnouncement(id)
    ElMessage.success(t('message.deleteSuccess'))
    fetchAnnouncements()
  } catch (error) {
    ElMessage.error(t('message.deleteFailed'))
  }
}

const submitForm = async () => {
  const valid = await formRef.value?.validate()
  if (!valid) return

  try {
    if (form.value.id) {
      const updateData: AnnouncementUpdateRequest = {
        id: form.value.id,
        title: form.value.title,
        content: form.value.content,
        type: form.value.type,
        status: form.value.status
      }
      await announcementApi.updateAnnouncement(updateData)
    } else {
      const createData: AnnouncementCreateRequest = {
        title: form.value.title,
        content: form.value.content,
        type: form.value.type,
        status: form.value.status
      }
      await announcementApi.createAnnouncement(createData)
    }
    ElMessage.success(form.value.id ? t('message.updateSuccess') : t('message.addSuccess'))
    showAddDialog.value = false
    form.value = { id: undefined, title: '', content: '', type: 'notice', status: '0' }
    fetchAnnouncements()
  } catch (error) {
    ElMessage.error(form.value.id ? t('message.updateFailed') : t('message.addFailed'))
  }
}

onMounted(() => {
  fetchAnnouncements()
})
</script>

<template>
  <div class="announcements-page">
    <div class="page-header">
      <div class="header-info">
        <h2 class="page-title">{{ t('layout.announcements') }}</h2>
        <p class="page-subtitle">{{ t('page.announcements.description') }}</p>
      </div>
      <el-button type="primary" @click="showAddDialog = true">
        <Plus class="w-4 h-4 mr-1" />
        <span>{{ t('common.add') }}</span>
      </el-button>
    </div>

    <el-card class="main-card">
      <div class="card-header">
        <div class="search-bar">
          <el-input
            v-model="searchForm.title"
            :placeholder="t('page.announcements.title')"
            class="search-input"
            @keyup.enter="searchAnnouncements"
          ></el-input>
          <el-button type="primary" @click="searchAnnouncements">{{
            t('common.search')
          }}</el-button>
          <el-button @click="resetSearch">{{ t('common.reset') }}</el-button>
        </div>
      </div>

      <div class="card-body">
        <BasicTable
          :data="filteredList"
          :columns="columns"
          :loading="loading"
        >
          <template #type="{ row }">
            <el-tag :type="getTypeTagType(row.type)">{{ getTypeName(row.type) }}</el-tag>
          </template>
          <template #status="{ row }">
            <el-tag :type="row.status === '0' ? 'success' : 'warning'">
              {{ row.status === '0' ? t('common.normal') : t('common.disabled') }}
            </el-tag>
          </template>
          <template #action="{ row }">
            <el-button size="small" @click="editAnnouncement(row)">
              {{ t('common.edit') }}
            </el-button>
            <el-button size="small" type="danger" @click="deleteAnnouncement(row.id)">
              {{ t('common.delete') }}
            </el-button>
          </template>
        </BasicTable>
      </div>
    </el-card>

    <el-dialog
      :title="form.id ? t('common.edit') : t('common.add')"
      v-model="showAddDialog"
      width="600px"
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
.announcements-page {
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
