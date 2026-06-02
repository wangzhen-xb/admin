<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { useI18n } from 'vue-i18n'
import { useMenuApi, type MenuInfo, type MenuCreateRequest, type MenuUpdateRequest } from '../../api/menu'
import { Plus } from 'lucide-vue-next'
import { BasicForm, BasicTable } from '../../components'
import type { FormItem, TableColumn, BasicFormInstance } from '@/components/types'

const { t } = useI18n()
const menuApi = useMenuApi()

const menuList = ref<MenuInfo[]>([])
const total = ref(0)
const loading = ref(false)
const showAddDialog = ref(false)
const formRef = ref<BasicFormInstance | null>(null)
const searchForm = ref({ menuName: '' })

const form = ref({
  menuId: undefined as number | undefined,
  parentId: 0 as number,
  menuName: '' as string,
  path: '' as string,
  component: '' as string,
  icon: '' as string,
  menuType: 'C' as string,
  sortNum: 0 as number,
  status: '0' as string,
  remark: '' as string
})

const formSchema: FormItem[] = [
  {
    field: 'parentId',
    label: t('page.menus.parentMenu'),
    type: 'select',
    options: [
      { label: t('common.none'), value: 0 },
      ...menuList.value.map(m => ({ label: m.menuName, value: m.menuId }))
    ]
  },
  {
    field: 'menuName',
    label: t('page.menus.menuName'),
    type: 'input',
    rules: [{ required: true, message: t('message.required', { field: t('page.menus.menuName') }), trigger: 'blur' }]
  },
  {
    field: 'path',
    label: t('page.menus.path'),
    type: 'input'
  },
  {
    field: 'component',
    label: t('page.menus.component'),
    type: 'input'
  },
  {
    field: 'menuType',
    label: t('page.menus.menuType'),
    type: 'select',
    options: [
      { label: t('page.menus.typeMenu'), value: 'M' },
      { label: t('page.menus.typePage'), value: 'C' },
      { label: t('page.menus.typeBtn'), value: 'F' }
    ]
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

const columns = computed<TableColumn<MenuInfo>[]>(() => [
  { title: t('common.number'), key: 'index', type: 'index', width: 60 },
  { title: t('page.menus.menuName'), key: 'menuName' },
  {
    title: t('page.menus.menuType'),
    key: 'menuType',
    width: 120,
    slot: 'menuType'
  },
  { title: t('page.menus.path'), key: 'path' },
  { title: t('page.menus.component'), key: 'component' },
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

const getMenuTypeTagType = (type: string) => {
  switch (type) {
    case 'M':
      return 'primary'
    case 'C':
      return 'success'
    case 'F':
      return 'warning'
    default:
      return 'info'
  }
}

const getMenuTypeName = (type: string) => {
  switch (type) {
    case 'M':
      return t('page.menus.typeMenu')
    case 'C':
      return t('page.menus.typePage')
    case 'F':
      return t('page.menus.typeBtn')
    default:
      return ''
  }
}

const fetchMenus = async () => {
  loading.value = true
  try {
    const res = await menuApi.getMenus()
    menuList.value = res.data || []
    total.value = menuList.value.length
  } catch (error) {
    ElMessage.error(t('message.fetchFailed'))
  } finally {
    loading.value = false
  }
}

const searchMenus = () => {
  const filtered = searchForm.value.menuName
    ? menuList.value.filter(m => m.menuName.includes(searchForm.value.menuName))
    : menuList.value
  menuList.value = filtered
}

const resetSearch = () => {
  searchForm.value = { menuName: '' }
  fetchMenus()
}

const editMenu = (menu: MenuInfo) => {
  form.value = { 
    menuId: menu.menuId,
    parentId: menu.parentId || 0,
    menuName: menu.menuName,
    path: menu.path,
    component: menu.component,
    icon: '',
    menuType: menu.menuType,
    sortNum: menu.sortNum,
    status: menu.status,
    remark: ''
  }
  showAddDialog.value = true
}

const deleteMenu = async (id: number) => {
  try {
    await menuApi.deleteMenu(id)
    ElMessage.success(t('message.deleteSuccess'))
    fetchMenus()
  } catch (error) {
    ElMessage.error(t('message.deleteFailed'))
  }
}

const submitForm = async () => {
  const valid = await formRef.value?.validate()
  if (!valid) return

  try {
    if (form.value.menuId) {
      const updateData: MenuUpdateRequest = {
        menuId: form.value.menuId,
        menuName: form.value.menuName,
        parentId: form.value.parentId,
        path: form.value.path,
        component: form.value.component,
        menuType: form.value.menuType,
        sortNum: form.value.sortNum,
        status: form.value.status
      }
      await menuApi.updateMenu(updateData)
    } else {
      const createData: MenuCreateRequest = {
        menuName: form.value.menuName,
        parentId: form.value.parentId,
        path: form.value.path,
        component: form.value.component,
        menuType: form.value.menuType,
        sortNum: form.value.sortNum,
        status: form.value.status
      }
      await menuApi.createMenu(createData)
    }
    ElMessage.success(form.value.menuId ? t('message.updateSuccess') : t('message.addSuccess'))
    showAddDialog.value = false
    form.value = {
      menuId: undefined,
      parentId: 0,
      menuName: '',
      path: '',
      component: '',
      icon: '',
      menuType: 'C',
      sortNum: 0,
      status: '0',
      remark: ''
    }
    fetchMenus()
  } catch (error) {
    ElMessage.error(form.value.menuId ? t('message.updateFailed') : t('message.addFailed'))
  }
}

onMounted(() => {
  fetchMenus()
})
</script>

<template>
  <div class="menus-page">
    <div class="page-header">
      <div class="header-info">
        <h2 class="page-title">{{ t('layout.menus') }}</h2>
        <p class="page-subtitle">{{ t('page.menus.description') }}</p>
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
            v-model="searchForm.menuName"
            :placeholder="t('page.menus.menuName')"
            class="search-input"
            @keyup.enter="searchMenus"
          ></el-input>
          <el-button type="primary" @click="searchMenus">{{ t('common.search') }}</el-button>
          <el-button @click="resetSearch">{{ t('common.reset') }}</el-button>
        </div>
      </div>

      <div class="card-body">
        <BasicTable
          :data="menuList"
          :columns="columns"
          :loading="loading"
        >
          <template #menuType="{ row }">
            <el-tag :type="getMenuTypeTagType(row.menuType)">
              {{ getMenuTypeName(row.menuType) }}
            </el-tag>
          </template>
          <template #status="{ row }">
            <el-tag :type="row.status === '0' ? 'success' : 'warning'">
              {{ row.status === '0' ? t('common.normal') : t('common.disabled') }}
            </el-tag>
          </template>
          <template #action="{ row }">
            <el-button size="small" @click="editMenu(row)">{{ t('common.edit') }}</el-button>
            <el-button size="small" type="danger" @click="deleteMenu(row.menuId)">
              {{ t('common.delete') }}
            </el-button>
          </template>
        </BasicTable>
      </div>
    </el-card>

    <el-dialog
      :title="form.menuId ? t('common.edit') : t('common.add')"
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
.menus-page {
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
