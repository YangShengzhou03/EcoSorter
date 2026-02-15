<template>
  <div class="resident-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>我的申诉</span>
          <el-button type="primary" @click="openSubmitDialog">提交申诉</el-button>
        </div>
      </template>

      <el-table :data="complaints" v-loading="loading" border style="width: 100%" empty-text="暂无申诉">
        <el-table-column prop="id" label="申诉ID" width="120" />
        <el-table-column label="分类记录" width="180">
          <template #default="{ row }">
            {{ row.classificationId }} - {{ row.categoryName }}
          </template>
        </el-table-column>
        <el-table-column prop="typeText" label="申诉类型" width="120" />
        <el-table-column prop="description" label="申诉说明" show-overflow-tooltip />
        <el-table-column prop="createdAt" label="提交时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ row.statusText }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="viewDetail(row)">查看</el-button>
            <el-button link type="danger" v-if="row.status === 'pending'" @click="handleDelete(row.id)">撤销</el-button>
          </template>
        </el-table-column>
      </el-table>

    </el-card>

    <el-dialog v-model="detailDialogVisible" title="申诉详情" width="600px">
      <div v-if="currentComplaint" class="detail-content">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="申诉ID">{{ currentComplaint.id }}</el-descriptions-item>
          <el-descriptions-item label="分类记录">
            {{ currentComplaint.classificationId }} - {{ currentComplaint.categoryName }}
          </el-descriptions-item>
          <el-descriptions-item label="申诉类型">{{ currentComplaint.typeText }}</el-descriptions-item>
          <el-descriptions-item label="申诉说明">{{ currentComplaint.description }}</el-descriptions-item>
          <el-descriptions-item label="提交时间">{{ formatDate(currentComplaint.createdAt) }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(currentComplaint.status)">{{ currentComplaint.statusText }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item v-if="currentComplaint.adminResponse" label="管理员回复">
            {{ currentComplaint.adminResponse }}
          </el-descriptions-item>
          <el-descriptions-item v-if="currentComplaint.processedAt" label="处理时间">
            {{ formatTime(currentComplaint.processedAt) }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="submitDialogVisible" title="提交申诉" width="600px" @close="resetForm">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="关联记录" prop="classificationId">
          <el-select v-model="form.classificationId" placeholder="请选择" v-loading="loadingRecords" clearable style="width: 100%">
            <el-option 
              v-for="record in records" 
              :key="record.id" 
              :label="`${record.id} - ${record.categoryName}`" 
              :value="record.id" 
            />
          </el-select>
        </el-form-item>

        <el-form-item label="申诉类型" prop="type">
          <el-radio-group v-model="form.type">
            <el-radio value="misclassification">分类错误</el-radio>
            <el-radio value="weight">重量争议</el-radio>
            <el-radio value="points">积分争议</el-radio>
            <el-radio value="other">其他</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="申诉说明" prop="description">
          <el-input 
            v-model="form.description" 
            type="textarea" 
            :rows="4" 
            placeholder="请详细描述您的问题，我们会尽快处理" 
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="submitDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submit" :loading="submitting">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { complaintApi } from '@/api/complaint'
import { classificationApi } from '@/api/classification'
import { formatTime, getStatusType } from '@/utils/helpers'

defineOptions({
  name: 'ResidentUserComplaint'
})

const formatDate = (dateString) => {
  return formatTime(dateString)
}

const loading = ref(false)
const loadingRecords = ref(false)
const submitting = ref(false)
const records = ref([])
const complaints = ref([])
const detailDialogVisible = ref(false)
const submitDialogVisible = ref(false)
const currentComplaint = ref(null)

const form = reactive({
  classificationId: '',
  type: 'misclassification',
  description: ''
})

const rules = {
  classificationId: [{ required: true, message: '请选择关联记录', trigger: 'change' }],
  type: [{ required: true, message: '请选择申诉类型', trigger: 'change' }],
  description: [{ required: true, message: '请输入申诉说明', trigger: 'blur' }]
}

const formRef = ref(null)

const loadRecords = async () => {
  loadingRecords.value = true
  try {
    const response = await classificationApi.getClassificationHistory({ page: 1, pageSize: 100 })
    records.value = response.records || []
  } catch (error) {
    ElMessage.error('加载记录失败')
  } finally {
    loadingRecords.value = false
  }
}

const loadComplaints = async () => {
  loading.value = true
  try {
    const response = await complaintApi.getMyComplaints()
    complaints.value = response || []
  } catch (error) {
    ElMessage.error('加载申诉列表失败')
  } finally {
    loading.value = false
  }
}

const openSubmitDialog = () => {
  submitDialogVisible.value = true
}

const submit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    submitting.value = true
    try {
      await complaintApi.submitComplaint({
        classificationId: form.classificationId,
        type: form.type,
        description: form.description
      })
      ElMessage.success('申诉提交成功，我们会尽快处理')
      resetForm()
      submitDialogVisible.value = false
      loadComplaints()
    } catch (error) {
      ElMessage.error(error.response?.data?.message || '提交失败')
    } finally {
      submitting.value = false
    }
  })
}

const viewDetail = (complaint) => {
  currentComplaint.value = complaint
  detailDialogVisible.value = true
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要撤销此申诉吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await complaintApi.deleteComplaint(id)
    ElMessage.success('撤销成功')
    loadComplaints()
  } catch (error) {
    ElMessage.error('撤销失败')
  }
}

const resetForm = () => {
  form.classificationId = ''
  form.type = 'misclassification'
  form.description = ''
  formRef.value?.resetFields()
}

onMounted(async () => {
  await Promise.all([
    loadRecords(),
    loadComplaints()
  ])
})
</script>

<style scoped>
.resident-page {
  padding: 0;
}

.complaint-tabs {
  margin-top: 0;
}

.complaint-form {
  width: 100%;
}

.form-select {
  width: 100%;
}

.type-radio-group {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

:deep(.el-form-item) {
  margin-bottom: 24px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #303133;
  padding-bottom: 8px;
}

.form-actions {
  margin-top: 32px;
  margin-bottom: 0;
}

.detail-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.detail-row {
  display: flex;
  gap: 12px;
  align-items: center;
}

.detail-row .label {
  font-size: 14px;
  color: #909399;
  min-width: 80px;
}

.detail-row .value {
  font-size: 14px;
  color: #303133;
  flex: 1;
}

.detail-section {
  background: #f5f7fa;
  padding: 16px;
  border-radius: 4px;
  margin-top: 8px;
}

.section-title {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
}

.section-content {
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
  margin-bottom: 8px;
}

.section-time {
  font-size: 12px;
  color: #909399;
  text-align: right;
}
</style>
