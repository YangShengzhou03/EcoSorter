<template>
  <div class="page-container">
    <div class="section-card">
      <el-form :model="form" label-position="top" class="complaint-form">
        <el-form-item label="关联记录">
          <el-select v-model="form.recordId" placeholder="请选择" class="form-select">
            <el-option label="REC20231201001 - 塑料" value="REC20231201001" />
            <el-option label="REC20231201002 - 纸张" value="REC20231201002" />
            <el-option label="REC20231130001 - 玻璃" value="REC20231130001" />
          </el-select>
        </el-form-item>

        <el-form-item label="申诉类型">
          <el-radio-group v-model="form.type" class="type-radio-group">
            <el-radio value="misclassification">分类错误</el-radio>
            <el-radio value="weight">重量争议</el-radio>
            <el-radio value="points">积分争议</el-radio>
            <el-radio value="other">其他</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="申诉说明">
          <el-input 
            v-model="form.description" 
            type="textarea" 
            :rows="3" 
            placeholder="请描述您的问题" 
            maxlength="200"
            show-word-limit
          />
        </el-form-item>

        <el-form-item class="form-actions">
          <el-button type="primary" @click="submit" :loading="loading" class="submit-btn">提交</el-button>
          <el-button @click="reset" class="reset-btn">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

const loading = ref(false)

const form = reactive({
  recordId: '',
  type: 'misclassification',
  description: ''
})

const submit = () => {
  if (!form.recordId || !form.description) {
    ElMessage.warning('请填写完整信息')
    return
  }
  loading.value = true
  setTimeout(() => {
    ElMessage.success('提交成功')
    reset()
    loading.value = false
  }, 500)
}

const reset = () => {
  form.recordId = ''
  form.type = 'misclassification'
  form.description = ''
}
</script>

<style scoped>
.page-container {
  padding: 16px;
}

.section-card {
  background: #fff;
  border-radius: 8px;
  padding: 16px;
  border: 1px solid #e8eaed;
}

.complaint-form {
  max-width: 500px;
}

.form-select {
  width: 100%;
}

.type-radio-group {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

:deep(.el-form-item) {
  margin-bottom: 20px;
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
  min-width: 100px;
}

.reset-btn {
  min-width: 80px;
  margin-left: 12px;
}
</style>
