<template>
  <div class="init-page">
    <div class="left-panel">
      <div class="brand-section">
        <h1 class="brand-title">智能垃圾分类系统</h1>
        <p class="brand-subtitle">Smart Waste Sorting System</p>
      </div>
      
      <div class="features-section">
        <div class="feature-item">
          <el-icon class="feature-icon"><Cpu /></el-icon>
          <span class="feature-title">AI智能识别</span>
          <span class="feature-desc">深度学习算法精准分类</span>
        </div>
        <div class="feature-item">
          <el-icon class="feature-icon"><DataAnalysis /></el-icon>
          <span class="feature-title">数据分析</span>
          <span class="feature-desc">实时统计投放数据</span>
        </div>
        <div class="feature-item">
          <el-icon class="feature-icon"><Cloudy /></el-icon>
          <span class="feature-title">云端管理</span>
          <span class="feature-desc">远程监控设备状态</span>
        </div>
      </div>
    </div>

    <div class="right-panel">
      <h2 class="form-title">垃圾桶设备激活</h2>
      <p class="form-subtitle">请填写设备信息完成激活配置</p>
      
      <el-form ref="formRef" :model="formData" :rules="formRules" label-position="top" class="init-form" @submit.prevent="handleInit">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设备名称" prop="deviceName">
              <el-input
                v-model="formData.deviceName"
                placeholder="如：A区-01号"
                size="large"
                clearable
              >
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备位置" prop="location">
              <el-input
                v-model="formData.location"
                placeholder="如：科技园A栋一楼"
                size="large"
                clearable
              >
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="垃圾桶类型" prop="binType">
          <el-select
            v-model="formData.binType"
            placeholder="请选择垃圾桶类型"
            size="large"
            style="width: 100%"
          >
            <el-option
              v-for="type in binTypes"
              :key="type.value"
              :label="type.label"
              :value="type.value"
            >
              <el-tag :type="type.tagType" size="small" effect="plain">{{ type.label }}</el-tag>
            </el-option>
          </el-select>
        </el-form-item>

        <el-divider></el-divider>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="管理员密码" prop="adminPassword">
              <el-input
                v-model="formData.adminPassword"
                type="password"
                placeholder="6-20位密码"
                size="large"
                show-password
              >
                <template #prefix>
                  <el-icon><Lock /></el-icon>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input
                v-model="formData.confirmPassword"
                type="password"
                placeholder="再次输入密码"
                size="large"
                show-password
              >
                <template #prefix>
                  <el-icon><Lock /></el-icon>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-button
          type="primary"
          @click="handleInit"
          :loading="loading"
          size="large"
          class="submit-btn"
        >
          激活设备
        </el-button>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Cpu, DataAnalysis, Cloudy, Lock } from '@element-plus/icons-vue'
import { trashcanApi } from '@/api/trashcan'

defineOptions({
  name: 'DeviceInit'
})

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)

const binTypes = [
  { label: '可回收物', value: 'recyclable', tagType: 'success' },
  { label: '有害垃圾', value: 'hazardous', tagType: 'danger' },
  { label: '厨余垃圾', value: 'kitchen', tagType: 'warning' },
  { label: '其他垃圾', value: 'other', tagType: 'info' }
]

const formData = reactive({
  deviceName: '',
  location: '',
  binType: '',
  adminPassword: '',
  confirmPassword: ''
})

const validatePass = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== formData.adminPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const formRules = reactive({
  deviceName: [
    { required: true, message: '请输入设备名称', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  location: [
    { required: true, message: '请输入设备位置', trigger: 'blur' }
  ],
  binType: [
    { required: true, message: '请选择垃圾桶类型', trigger: 'change' }
  ],
  adminPassword: [
    { required: true, message: '请设置管理员密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validatePass, trigger: 'blur' }
  ]
})

const handleInit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    const activateData = {
      deviceName: formData.deviceName,
      location: formData.location,
      binType: formData.binType,
      password: formData.adminPassword
    }
    
    const response = await trashcanApi.activateDevice(activateData)
    
    if (response && response.authToken) {
      localStorage.setItem('token', response.authToken)
      localStorage.setItem('deviceInitialized', 'true')
      localStorage.setItem('deviceInfo', JSON.stringify({
        deviceId: response.deviceId,
        deviceName: response.deviceName,
        location: response.location,
        binType: response.binType,
        status: response.status
      }))
      
      ElMessage.success('设备激活成功')
      
      await router.push('/work')
    } else {
      throw new Error('激活失败：响应中缺少 authToken')
    }
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '设备激活失败，请稍后重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.init-page {
  width: 100vw;
  height: 100vh;
  display: flex;
  background: #f5f5f5;
}

.left-panel {
  width: 40%;
  background: #1a1a2e;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 40px;
}

.brand-section {
  text-align: center;
  margin-bottom: 60px;
}

.logo-wrapper {
  width: 72px;
  height: 72px;
  background: #409eff;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
}

.logo-icon {
  color: white;
}

.brand-title {
  font-size: 28px;
  font-weight: 600;
  color: white;
  margin: 0 0 8px;
}

.brand-subtitle {
  font-size: 14px;
  color: #909399;
  margin: 0;
  letter-spacing: 1px;
}

.features-section {
  width: 100%;
  max-width: 280px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px 0;
  border-bottom: 1px solid #2a2a3e;
}

.feature-item:last-child {
  border-bottom: none;
}

.feature-icon {
  font-size: 28px;
  color: #409eff;
  flex-shrink: 0;
}

.feature-title {
  font-size: 15px;
  color: white;
  font-weight: 500;
  display: block;
}

.feature-desc {
  font-size: 12px;
  color: #909399;
  display: block;
}

.right-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 40px;
}

.form-title {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 8px;
}

.form-subtitle {
  font-size: 14px;
  color: #909399;
  margin: 0 0 32px;
}

.init-form {
  width: 100%;
  max-width: 480px;
}

.init-form :deep(.el-form-item__label) {
  font-weight: 500;
  color: #303133;
}

.init-form :deep(.el-form-item) {
  margin-bottom: 22px;
}

.submit-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  margin-top: 16px;
}

@media (max-width: 1024px) {
  .left-panel {
    width: 35%;
    padding: 30px;
  }
  
  .brand-title {
    font-size: 24px;
  }
  
  .right-panel {
    padding: 30px;
  }
}

@media (max-width: 768px) {
  .init-page {
    flex-direction: column;
  }
  
  .left-panel {
    width: 100%;
    padding: 30px 20px;
  }
  
  .brand-section {
    margin-bottom: 24px;
  }
  
  .features-section {
    display: none;
  }
  
  .right-panel {
    flex: 1;
    padding: 20px;
  }
  
  .init-form {
    max-width: 100%;
  }
}
</style>
