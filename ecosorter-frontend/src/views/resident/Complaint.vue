<template>
  <div class="page-container">
    <el-tabs v-model="activeTab" class="complaint-tabs">
      <el-tab-pane label="提交申诉" name="submit">
        <div class="submit-section">
          <div class="section-card">
            <el-form :model="form" label-position="top" class="complaint-form" :rules="rules" ref="formRef">
              <el-form-item label="关联记录" prop="classificationId">
                <el-select v-model="form.classificationId" placeholder="请选择" class="form-select" v-loading="loadingRecords">
                  <el-option 
                    v-for="record in records" 
                    :key="record.id" 
                    :label="`${record.id} - ${record.categoryName}`" 
                    :value="record.id" 
                  />
                </el-select>
              </el-form-item>

              <el-form-item label="申诉类型" prop="type">
                <el-radio-group v-model="form.type" class="type-radio-group">
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

              <el-form-item class="form-actions">
                <el-button type="primary" @click="submit" :loading="submitting" class="submit-btn">提交申诉</el-button>
                <el-button @click="reset" class="reset-btn">重置</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </el-tab-pane>

      <el-tab-pane label="我的申诉" name="my-complaints">
        <div class="my-complaints-section">
          <div class="complaint-list" v-loading="loading">
            <div class="complaint-card" v-for="complaint in complaints" :key="complaint.id">
              <div class="complaint-header">
                <div class="complaint-id">申诉ID: {{ complaint.id }}</div>
                <el-tag :type="getStatusType(complaint.status)">
                  {{ complaint.statusText }}
                </el-tag>
              </div>
              
              <div class="complaint-content">
                <div class="complaint-info">
                  <div class="info-row">
                    <span class="label">分类记录:</span>
                    <span class="value">{{ complaint.classificationId }} - {{ complaint.categoryName }}</span>
                  </div>
                  <div class="info-row">
                    <span class="label">申诉类型:</span>
                    <span class="value">{{ complaint.typeText }}</span>
                  </div>
                  <div class="info-row">
                    <span class="label">提交时间:</span>
                    <span class="value">{{ formatDate(complaint.createdAt) }}</span>
                  </div>
                  <div class="info-row description">
                    <span class="label">申诉说明:</span>
                    <span class="value">{{ complaint.description }}</span>
                  </div>
                </div>
                
                <div class="complaint-response" v-if="complaint.adminResponse">
                  <div class="response-title">管理员回复:</div>
                  <div class="response-content">{{ complaint.adminResponse }}</div>
                  <div class="response-time" v-if="complaint.processedAt">
                    处理时间: {{ formatDate(complaint.processedAt) }}
                  </div>
                </div>
              </div>
              
              <div class="complaint-actions" v-if="complaint.status === 'pending'">
                <el-button type="danger" size="small" @click="handleDelete(complaint.id)">撤销申诉</el-button>
              </div>
            </div>
            
            <el-empty v-if="complaints.length === 0 && !loading" description="暂无申诉记录" />
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { complaintApi } from '@/api/complaint'
import { classificationApi } from '@/api/classification'

const activeTab = ref('submit')
const loading = ref(false)
const loadingRecords = ref(false)
const submitting = ref(false)
const records = ref([])
const complaints = ref([])

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
    records.value = response.content || []
  } catch (error) {
    console.error('加载记录失败:', error)
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
    console.error('加载申诉列表失败:', error)
    ElMessage.error('加载申诉列表失败')
  } finally {
    loading.value = false
  }
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
      reset()
      activeTab.value = 'my-complaints'
      loadComplaints()
    } catch (error) {
      console.error('提交失败:', error)
      ElMessage.error(error.response?.data?.message || '提交失败')
    } finally {
      submitting.value = false
    }
  })
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
    if (error !== 'cancel') {
      console.error('撤销失败:', error)
      ElMessage.error('撤销失败')
    }
  }
}

const reset = () => {
  form.classificationId = ''
  form.type = 'misclassification'
  form.description = ''
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
  loadRecords()
  loadComplaints()
})
</script>

<style scoped>
.page-container {
  padding: 0;
}

.complaint-tabs {
  margin-top: 16px;
}

.submit-section,
.my-complaints-section {
  padding: 16px 0;
}

.section-card {
  background: var(--bg-white);
  border-radius: 8px;
  padding: 24px;
  border: 1px solid var(--border-color);
}

.complaint-form {
  max-width: 600px;
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

.submit-btn {
  min-width: 120px;
}

.reset-btn {
  min-width: 80px;
  margin-left: 12px;
}

.complaint-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.complaint-card {
  background: var(--bg-white);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 20px;
  transition: all 0.3s;
}

.complaint-card:hover {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.complaint-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid var(--border-color);
}

.complaint-id {
  font-size: 14px;
  color: var(--text-secondary);
  font-weight: 500;
}

.complaint-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.complaint-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.info-row {
  display: flex;
  gap: 8px;
}

.info-row.description {
  flex-direction: column;
  gap: 4px;
}

.info-row .label {
  font-size: 13px;
  color: var(--text-secondary);
  flex-shrink: 0;
  min-width: 80px;
}

.info-row .value {
  font-size: 14px;
  color: var(--text-primary);
  flex: 1;
}

.complaint-response {
  background: #f5f7fa;
  border-radius: 6px;
  padding: 16px;
  margin-top: 8px;
}

.response-title {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
}

.response-content {
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
  margin-bottom: 8px;
}

.response-time {
  font-size: 12px;
  color: #909399;
  text-align: right;
}

.complaint-actions {
  margin-top: 16px;
  padding-top: 12px;
  border-top: 1px solid var(--border-color);
  display: flex;
  justify-content: flex-end;
}
</style>
