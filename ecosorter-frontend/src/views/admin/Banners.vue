<template>
  <div class="admin-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>轮播图管理</span>
          <el-button type="primary" @click="showCreateDialog">添加轮播图</el-button>
        </div>
      </template>
      
      <el-table :data="banners" style="width: 100%" border v-loading="loading" empty-text="暂无轮播图">
        <el-table-column prop="id" label="ID"/>
        <el-table-column prop="title" label="标题"/>
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column prop="background" label="背景色">
          <template #default="{ row }">
            <div class="color-preview" :style="{ background: row.background }"></div>
          </template>
        </el-table-column>
        <el-table-column prop="sortOrder" label="排序"/>
        <el-table-column prop="target" label="目标端">
          <template #default="{ row }">
            <el-tag :type="row.target === 'user' ? 'primary' : 'warning'">
              {{ row.target === 'user' ? '用户端' : '垃圾桶端' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button size="small" @click="editBanner(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteBanner(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑轮播图' : '添加轮播图'" width="500px">
      <el-form :model="bannerForm" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="bannerForm.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="bannerForm.description" type="textarea" :rows="3" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="背景色" prop="background">
          <el-color-picker v-model="bannerForm.background" show-alpha style="width: 80px;" />
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="bannerForm.sortOrder" :min="0" />
        </el-form-item>
        <el-form-item label="目标端" prop="target">
          <el-select v-model="bannerForm.target" placeholder="请选择目标端">
            <el-option label="用户端" value="user" />
            <el-option label="垃圾桶端" value="trashcan" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitting">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { bannerApi } from '@/api/banner'

defineOptions({
  name: 'AdminBanners'
})

const banners = ref([])
const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)
const currentBannerId = ref(null)

const bannerForm = ref({
  title: '',
  description: '',
  background: '#2c3e50',
  sortOrder: 0,
  target: 'user'
})

const rules = {
  title: [
    { required: true, message: '请输入标题', trigger: 'blur' },
    { min: 2, max: 50, message: '标题长度在2到50个字符', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入描述', trigger: 'blur' },
    { min: 5, max: 200, message: '描述长度在5到200个字符', trigger: 'blur' }
  ],
  background: [
    { required: true, message: '请选择背景色', trigger: 'change' }
  ],
  sortOrder: [
    { required: true, message: '请输入排序', trigger: 'blur' }
  ],
  target: [
    { required: true, message: '请选择目标端', trigger: 'change' }
  ]
}

const loadBanners = async () => {
  loading.value = true
  try {
    const response = await bannerApi.getList()
    banners.value = response || []
  } catch (error) {
    ElMessage.error('加载轮播图数据失败')
  } finally {
    loading.value = false
  }
}

const showCreateDialog = () => {
  isEdit.value = false
  currentBannerId.value = null
  bannerForm.value = {
    title: '',
    description: '',
    background: '#2c3e50',
    sortOrder: 0,
    target: 'user'
  }
  dialogVisible.value = true
}

const editBanner = (row) => {
  isEdit.value = true
  currentBannerId.value = row.id
  bannerForm.value = {
    title: row.title,
    description: row.description,
    background: row.background,
    sortOrder: row.sortOrder,
    target: row.target
  }
  dialogVisible.value = true
}

const submitForm = async () => {
  try {
    await formRef.value.validate()
    submitting.value = true
    
    if (isEdit.value) {
      await bannerApi.update(currentBannerId.value, bannerForm.value)
      ElMessage.success('更新轮播图成功')
    } else {
      await bannerApi.create(bannerForm.value)
      ElMessage.success('添加轮播图成功')
    }
    
    dialogVisible.value = false
    loadBanners()
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '操作失败')
  } finally {
    submitting.value = false
  }
}

const deleteBanner = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该轮播图吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await bannerApi.delete(row.id)
    ElMessage.success('删除成功')
    loadBanners()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  loadBanners()
})
</script>

<style scoped>
.admin-page {
  padding: 0;
}

.color-preview {
  width: 40px;
  height: 20px;
  border-radius: 0;
  border: 1px solid #ddd;
}
</style>
