<template>
  <div class="products-page">
    <div class="page-header">
      <h2>商品管理</h2>
      <el-button type="primary" @click="openCreateDialog">
        <el-icon><Plus /></el-icon>
        添加商品
      </el-button>
    </div>

    <div class="filter-bar">
      <el-select v-model="filterCategory" placeholder="选择分类" @change="loadProducts" clearable style="width: 150px">
        <el-option label="全部" value="" />
        <el-option label="生活用品" value="生活用品" />
        <el-option label="电子产品" value="电子产品" />
        <el-option label="食品饮料" value="食品饮料" />
        <el-option label="学习用品" value="学习用品" />
      </el-select>
      <el-select v-model="filterStatus" placeholder="商品状态" @change="loadProducts" clearable style="width: 150px">
        <el-option label="全部" value="" />
        <el-option label="上架" value="available" />
        <el-option label="下架" value="unavailable" />
      </el-select>
    </div>

    <div class="products-list">
      <el-table :data="products" v-loading="loading">
        <el-table-column prop="name" label="商品名称" min-width="150" />
        <el-table-column prop="category" label="分类" width="100" />
        <el-table-column prop="points" label="积分" width="100" />
        <el-table-column prop="stock" label="库存" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'available' ? 'success' : 'info'">
              {{ row.status === 'available' ? '上架' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEditDialog(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination" v-if="total > 0">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          @size-change="loadProducts"
          @current-change="loadProducts"
        />
      </div>
    </div>

    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑商品' : '添加商品'"
      width="600px"
      @close="resetForm"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入商品名称" />
        </el-form-item>
        
        <el-form-item label="商品分类" prop="category">
          <el-select v-model="form.category" placeholder="请选择商品分类">
            <el-option label="生活用品" value="生活用品" />
            <el-option label="电子产品" value="电子产品" />
            <el-option label="食品饮料" value="食品饮料" />
            <el-option label="学习用品" value="学习用品" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="商品描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="4"
            placeholder="请输入商品描述"
          />
        </el-form-item>
        
        <el-form-item label="商品图片" prop="imageUrl">
          <el-input v-model="form.imageUrl" placeholder="请输入图片URL" />
        </el-form-item>
        
        <el-form-item label="所需积分" prop="points">
          <el-input-number v-model="form.points" :min="0" :max="999999" />
        </el-form-item>
        
        <el-form-item label="库存数量" prop="stock">
          <el-input-number v-model="form.stock" :min="0" :max="999999" />
        </el-form-item>
        
        <el-form-item label="商品状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="available">上架</el-radio>
            <el-radio label="unavailable">下架</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitting">
          {{ isEdit ? '保存' : '添加' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { productApi } from '@/api/product'

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const products = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const filterCategory = ref('')
const filterStatus = ref('')

const form = reactive({
  id: null,
  name: '',
  description: '',
  imageUrl: '',
  points: 0,
  stock: 0,
  status: 'available',
  category: ''
})

const rules = {
  name: [
    { required: true, message: '请输入商品名称', trigger: 'blur' }
  ],
  category: [
    { required: true, message: '请选择商品分类', trigger: 'change' }
  ],
  description: [
    { required: true, message: '请输入商品描述', trigger: 'blur' }
  ],
  points: [
    { required: true, message: '请输入所需积分', trigger: 'blur' }
  ],
  stock: [
    { required: true, message: '请输入库存数量', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择商品状态', trigger: 'change' }
  ]
}

const loadProducts = async () => {
  loading.value = true
  try {
    const response = await productApi.getList({
      page: currentPage.value,
      pageSize: pageSize.value,
      category: filterCategory.value,
      status: filterStatus.value
    })
    products.value = response.content || []
    total.value = response.totalElements || 0
  } catch (error) {
    console.error('加载商品列表失败:', error)
    ElMessage.error('加载商品列表失败')
  } finally {
    loading.value = false
  }
}

const openCreateDialog = () => {
  isEdit.value = false
  dialogVisible.value = true
}

const openEditDialog = (row) => {
  isEdit.value = true
  form.id = row.id
  form.name = row.name
  form.description = row.description
  form.imageUrl = row.imageUrl
  form.points = row.points
  form.stock = row.stock
  form.status = row.status
  form.category = row.category
  dialogVisible.value = true
}

const submitForm = async () => {
  try {
    await formRef.value.validate()
    
    submitting.value = true
    if (isEdit.value) {
      await productApi.update(form.id, form)
      ElMessage.success('更新成功')
    } else {
      await productApi.create(form)
      ElMessage.success('添加成功')
    }
    
    dialogVisible.value = false
    loadProducts()
  } catch (error) {
    if (error !== false) {
      console.error('提交失败:', error)
      ElMessage.error('提交失败')
    }
  } finally {
    submitting.value = false
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该商品吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await productApi.delete(row.id)
      ElMessage.success('删除成功')
      loadProducts()
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

const resetForm = () => {
  formRef.value?.resetFields()
  form.id = null
  form.name = ''
  form.description = ''
  form.imageUrl = ''
  form.points = 0
  form.stock = 0
  form.status = 'available'
  form.category = ''
}

const formatDate = (dateString) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

onMounted(() => {
  loadProducts()
})
</script>

<style scoped>
.products-page {
  padding: 20px;
}

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: var(--text-primary);
}

.filter-bar {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.products-list {
  background: var(--bg-white);
  border-radius: 8px;
  padding: 16px;
  border: 1px solid var(--border-color);
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
