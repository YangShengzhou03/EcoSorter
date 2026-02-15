<template>
  <div class="admin-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>垃圾分类管理</span>
          <el-button type="primary" @click="showCreateDialog">添加分类</el-button>
        </div>
      </template>
      
      <el-table :data="categories" style="width: 100%" border v-loading="loading" empty-text="暂无分类">
        <el-table-column prop="name" label="分类名称" />
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column prop="disposalMethod" label="处理方式" />
        <el-table-column prop="color" label="颜色">
          <template #default="{ row }">
            <div class="color-preview" :style="{ background: row.color }"></div>
          </template>
        </el-table-column>
        <el-table-column prop="environmentalImpact" label="环境影响" />
        <el-table-column prop="recyclingRate" label="回收率" />
        <el-table-column prop="hazardous" label="危险品">
          <template #default="{ row }">
            <el-tag :type="row.hazardous ? 'danger' : 'success'">
              {{ row.hazardous ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="active" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.active ? 'success' : 'info'">
              {{ row.active ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button size="small" @click="editCategory(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteCategory(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑分类' : '添加分类'" width="600px">
      <el-form :model="categoryForm" :rules="rules" ref="formRef" label-width="120px">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="categoryForm.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="categoryForm.description" type="textarea" :rows="3" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="处理方式" prop="disposalMethod">
          <el-input v-model="categoryForm.disposalMethod" placeholder="请输入处理方式" />
        </el-form-item>
        <el-form-item label="颜色" prop="color">
          <el-color-picker v-model="categoryForm.color" show-alpha />
        </el-form-item>
        <el-form-item label="环境影响" prop="environmentalImpact">
          <el-input-number v-model="categoryForm.environmentalImpact" :min="1" :max="10" />
        </el-form-item>
        <el-form-item label="回收率(%)" prop="recyclingRate">
          <el-input-number v-model="categoryForm.recyclingRate" :min="0" :max="100" :step="0.1" />
        </el-form-item>
        <el-form-item label="常见物品" prop="commonItems">
          <el-input v-model="categoryForm.commonItemsText" type="textarea" :rows="3" placeholder="请输入常见物品，用逗号分隔" />
        </el-form-item>
        <el-form-item label="处理说明" prop="disposalInstructions">
          <el-input v-model="categoryForm.disposalInstructions" type="textarea" :rows="3" placeholder="请输入处理说明" />
        </el-form-item>
        <el-form-item label="特殊处理" prop="specialHandling">
          <el-switch v-model="categoryForm.specialHandling" />
        </el-form-item>
        <el-form-item label="危险品" prop="hazardous">
          <el-switch v-model="categoryForm.hazardous" />
        </el-form-item>
        <el-form-item label="状态" prop="active">
          <el-switch v-model="categoryForm.active" active-text="启用" inactive-text="禁用" />
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
import { classificationApi } from '@/api/classification'

defineOptions({
  name: 'AdminCategories'
})

const categories = ref([])
const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)
const currentCategoryId = ref(null)

const categoryForm = ref({
  name: '',
  description: '',
  disposalMethod: '',
  color: '#10b981',
  environmentalImpact: 5,
  recyclingRate: 80.0,
  commonItemsText: '',
  disposalInstructions: '',
  specialHandling: false,
  hazardous: false,
  active: true
})

const rules = {
  name: [
    { required: true, message: '请输入分类名称', trigger: 'blur' },
    { min: 2, max: 50, message: '名称长度在2到50个字符', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入描述', trigger: 'blur' },
    { min: 5, max: 500, message: '描述长度在5到500个字符', trigger: 'blur' }
  ],
  disposalMethod: [
    { required: true, message: '请输入处理方式', trigger: 'blur' }
  ],
  color: [
    { required: true, message: '请选择颜色', trigger: 'change' }
  ],
  environmentalImpact: [
    { required: true, message: '请输入环境影响', trigger: 'blur' }
  ],
  recyclingRate: [
    { required: true, message: '请输入回收率', trigger: 'blur' }
  ]
}

const loadCategories = async () => {
  loading.value = true
  try {
    const response = await classificationApi.getCategories()
    categories.value = response || []
  } catch (error) {
    ElMessage.error('加载分类数据失败')
  } finally {
    loading.value = false
  }
}

const showCreateDialog = () => {
  isEdit.value = false
  currentCategoryId.value = null
  categoryForm.value = {
    name: '',
    description: '',
    disposalMethod: '',
    color: '#10b981',
    environmentalImpact: 5,
    recyclingRate: 80.0,
    commonItemsText: '',
    disposalInstructions: '',
    specialHandling: false,
    hazardous: false,
    active: true
  }
  dialogVisible.value = true
}

const editCategory = (row) => {
  isEdit.value = true
  currentCategoryId.value = row.id
  categoryForm.value = {
    name: row.name,
    description: row.description,
    disposalMethod: row.disposalMethod,
    color: row.color,
    environmentalImpact: row.environmentalImpact,
    recyclingRate: row.recyclingRate,
    commonItemsText: row.commonItems ? row.commonItems.join(', ') : '',
    disposalInstructions: row.disposalInstructions,
    specialHandling: row.specialHandling,
    hazardous: row.hazardous,
    active: row.active
  }
  dialogVisible.value = true
}

const submitForm = async () => {
  try {
    await formRef.value.validate()
    submitting.value = true
    
    const formData = {
      ...categoryForm.value,
      commonItems: categoryForm.value.commonItemsText.split(',').map(item => item.trim()).filter(item => item)
    }
    
    if (isEdit.value) {
      await classificationApi.updateCategory(currentCategoryId.value, formData)
      ElMessage.success('更新分类成功')
    } else {
      await classificationApi.createCategory(formData)
      ElMessage.success('添加分类成功')
    }
    
    dialogVisible.value = false
    loadCategories()
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '操作失败')
  } finally {
    submitting.value = false
  }
}

const deleteCategory = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该分类吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await classificationApi.deleteCategory(row.id)
    ElMessage.success('删除成功')
    loadCategories()
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

onMounted(() => {
  loadCategories()
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
