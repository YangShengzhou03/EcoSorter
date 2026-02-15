<template>
  <div class="admin-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>申诉管理</span>
        </div>
      </template>

      <el-table :data="complaintList" v-loading="loading" border empty-text="暂无申诉">
        <el-table-column prop="id" label="ID" />
        <el-table-column label="用户">
          <template #default="{ row }">
            {{ row.userName || '未知' }}
          </template>
        </el-table-column>
        <el-table-column label="分类记录">
          <template #default="{ row }">
            <el-button link type="primary" @click="viewClassification(row.classificationId)">
              {{ row.classificationId }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column prop="categoryName" label="分类" />
        <el-table-column prop="typeText" label="申诉类型" />
        <el-table-column prop="description" label="申诉说明" show-overflow-tooltip />
        <el-table-column prop="statusText" label="状态">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ row.statusText }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="提交时间">
          <template #default="{ row }">
            {{ formatDate(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button link type="primary" @click="openDetailDialog(row)">查看详情</el-button>
            <el-button link type="success" @click="handleComplaint(row)" v-if="row.status === 'pending'">处理</el-button>
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
          @size-change="loadComplaints"
          @current-change="loadComplaints"
        />
      </div>
    </el-card>

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
import { ElMessage } from 'element-plus'
import { complaintApi } from '@/api/complaint'
import { getStatusType } from '@/utils/helpers'

defineOptions({
  name: 'AdminComplaints'
})

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const complaintList = ref([])
const currentComplaint = ref(null)

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

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
      pageSize: pageSize.value
    })
    complaintList.value = response.records || []
    total.value = response.total || 0
  } catch (error) {
    ElMessage.error('加载申诉列表失败')
  } finally {
    loading.value = false
  }
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
    } catch (error) {
      ElMessage.error('处理申诉失败')
    } finally {
      submitting.value = false
    }
  })
}

const viewClassification = (classificationId) => {
  ElMessage.info(`查看分类记录: ${classificationId}`)
}

const resetForm = () => {
  form.status = 'processing'
  form.adminResponse = ''
  formRef.value?.resetFields()
}

onMounted(() => {
  loadComplaints()
})
</script>

<style scoped>
.admin-page {
  padding: 0;
}

.pending-badge {
  margin-right: 16px;
}

:deep(.el-descriptions__label) {
  width: 120px;
}
</style>
