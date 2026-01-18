<template>
  <div class="complaints-page">
    <div class="page-header">
      <h2>申诉管理</h2>
      <el-badge :value="pendingCount" :max="99" class="pending-badge">
        <el-button type="warning" @click="filterByStatus('pending')">
          <el-icon><Warning /></el-icon>
          待处理
        </el-button>
      </el-badge>
    </div>

    <div class="filter-bar">
      <el-select v-model="statusFilter" placeholder="全部状态" clearable @change="loadComplaints">
        <el-option label="全部" value="" />
        <el-option label="待处理" value="pending" />
        <el-option label="处理中" value="processing" />
        <el-option label="已解决" value="resolved" />
        <el-option label="已驳回" value="rejected" />
      </el-select>
    </div>

    <div class="complaint-list">
      <el-table :data="complaintList" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="用户" width="120">
          <template #default="{ row }">
            {{ row.userName || '未知' }}
          </template>
        </el-table-column>
        <el-table-column label="分类记录" width="120">
          <template #default="{ row }">
            <el-button link type="primary" @click="viewClassification(row.classificationId)">
              {{ row.classificationId }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column prop="categoryName" label="分类" width="100" />
        <el-table-column prop="typeText" label="申诉类型" width="100" />
        <el-table-column prop="description" label="申诉说明" min-width="200" show-overflow-tooltip />
        <el-table-column prop="statusText" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ row.statusText }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="提交时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openProcessDialog(row)">处理</el-button>
            <el-button link type="danger" @click="handleDelete(row)" v-if="row.status === 'pending'">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination" v-if="total > 0">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadComplaints"
          @current-change="loadComplaints"
        />
      </div>
    </div>

    <el-dialog
      v-model="dialogVisible"
      title="处理申诉"
      width="700px"
      @close="resetForm"
    >
      <el-descriptions :column="2" border v-if="currentComplaint">
        <el-descriptions-item label="申诉ID">{{ currentComplaint.id }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(currentComplaint.status)">
            {{ currentComplaint.statusText }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="分类记录ID">
          <el-button link type="primary" @click="viewClassification(currentComplaint.classificationId)">
            {{ currentComplaint.classificationId }}
          </el-button>
        </el-descriptions-item>
        <el-descriptions-item label="分类">{{ currentComplaint.categoryName }}</el-descriptions-item>
        <el-descriptions-item label="申诉类型">{{ currentComplaint.typeText }}</el-descriptions-item>
        <el-descriptions-item label="提交时间">{{ formatDate(currentComplaint.createdAt) }}</el-descriptions-item>
        <el-descriptions-item label="申诉说明" :span="2">
          {{ currentComplaint.description }}
        </el-descriptions-item>
      </el-descriptions>

      <el-divider />

      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="处理状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="processing">处理中</el-radio>
            <el-radio label="resolved">已解决</el-radio>
            <el-radio label="rejected">已驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="处理回复" prop="adminResponse">
          <el-input
            v-model="form.adminResponse"
            type="textarea"
            :rows="4"
            placeholder="请输入处理回复"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Warning } from '@element-plus/icons-vue'
import { complaintApi } from '@/api/complaint'

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const complaintList = ref([])
const currentComplaint = ref(null)
const pendingCount = ref(0)

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const statusFilter = ref('')

const form = reactive({
  status: 'processing',
  adminResponse: ''
})

const rules = {
  status: [{ required: true, message: '请选择处理状态', trigger: 'change' }],
  adminResponse: [{ required: true, message: '请输入处理回复', trigger: 'blur' }]
}

const formRef = ref(null)

const loadComplaints = async () => {
  loading.value = true
  try {
    const response = await complaintApi.getAdminList({
      page: currentPage.value,
      pageSize: pageSize.value,
      status: statusFilter.value
    })
    complaintList.value = response.content || []
    total.value = response.totalElements || 0
  } catch (error) {
    console.error('加载申诉列表失败:', error)
    ElMessage.error('加载申诉列表失败')
  } finally {
    loading.value = false
  }
}

const loadPendingCount = async () => {
  try {
    const count = await complaintApi.getPendingCount()
    pendingCount.value = count || 0
  } catch (error) {
    console.error('加载待处理数量失败:', error)
  }
}

const filterByStatus = (status) => {
  statusFilter.value = status
  loadComplaints()
}

const openProcessDialog = (complaint) => {
  currentComplaint.value = complaint
  form.status = complaint.status === 'pending' ? 'processing' : complaint.status
  form.adminResponse = complaint.adminResponse || ''
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    submitting.value = true
    try {
      await complaintApi.processComplaint(currentComplaint.value.id, form)
      ElMessage.success('处理成功')
      dialogVisible.value = false
      loadComplaints()
      loadPendingCount()
    } catch (error) {
      console.error('处理申诉失败:', error)
      ElMessage.error('处理申诉失败')
    } finally {
      submitting.value = false
    }
  })
}

const handleDelete = async (complaint) => {
  try {
    await ElMessageBox.confirm('确定要删除此申诉吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await complaintApi.deleteComplaint(complaint.id)
    ElMessage.success('删除成功')
    loadComplaints()
    loadPendingCount()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除申诉失败:', error)
      ElMessage.error('删除申诉失败')
    }
  }
}

const viewClassification = (classificationId) => {
  ElMessage.info(`查看分类记录: ${classificationId}`)
}

const resetForm = () => {
  form.status = 'processing'
  form.adminResponse = ''
  formRef.value?.resetFields()
}

const getStatusType = (status) => {
  const statusMap = {
    pending: 'warning',
    processing: 'primary',
    resolved: 'success',
    rejected: 'danger'
  }
  return statusMap[status] || 'info'
}

const formatDate = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

onMounted(() => {
  loadComplaints()
  loadPendingCount()
})
</script>

<style scoped>
.complaints-page {
  padding: 0;
}

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}

.page-header h2 {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: var(--text-primary);
}

.pending-badge {
  margin-right: 16px;
}

.filter-bar {
  margin-bottom: 16px;
}

.complaint-list {
  background: var(--bg-white);
  border-radius: 8px;
  padding: 16px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 16px;
}

:deep(.el-descriptions__label) {
  width: 120px;
}
</style>
