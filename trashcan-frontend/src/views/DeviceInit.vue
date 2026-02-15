<template>
  <div class="init-container">
    <div class="init-screen">
      <div class="logo-section">
        <h1 class="logo-title">易控智能垃圾桶</h1>
        <p class="logo-subtitle">初始化配置</p>
      </div>

      <el-form :model="formData" label-width="100px" class="init-form">
        <el-form-item label="设备名称">
          <el-input
            v-model="formData.deviceName"
            placeholder="请输入设备名称"
            clearable
          />
        </el-form-item>

        <el-form-item label="设备位置">
          <el-input
            v-model="formData.location"
            placeholder="请输入设备位置"
            clearable
          />
        </el-form-item>

        <el-form-item label="垃圾桶类型">
          <el-radio-group v-model="formData.binType" class="bin-type-group">
            <el-radio-button label="recyclable">可回收物</el-radio-button>
            <el-radio-button label="hazardous">有害垃圾</el-radio-button>
            <el-radio-button label="kitchen">厨余垃圾</el-radio-button>
            <el-radio-button label="other">其他垃圾</el-radio-button>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="管理员密码">
          <el-input
            v-model="formData.adminPassword"
            type="password"
            placeholder="请设置管理员密码"
            show-password
          />
        </el-form-item>

        <el-form-item label="确认密码">
          <el-input
            v-model="formData.confirmPassword"
            type="password"
            placeholder="请再次输入密码"
            show-password
          />
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            @click="handleInit"
            :loading="loading"
            class="submit-btn"
          >
            激活设备
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { trashcanApi } from '@/api/trashcan'

defineOptions({
  name: 'DeviceInit'
})

const router = useRouter()

const loading = ref(false)

const formData = ref({
  deviceName: '',
  location: '',
  binType: '',
  adminPassword: '',
  confirmPassword: ''
})

const handleInit = async () => {
  if (!formData.value.deviceName) {
    ElMessage.warning('请输入设备名称')
    return
  }
  if (!formData.value.location) {
    ElMessage.warning('请输入设备位置')
    return
  }
  if (!formData.value.binType) {
    ElMessage.warning('请选择垃圾桶类型')
    return
  }
  if (!formData.value.adminPassword) {
    ElMessage.warning('请设置管理员密码')
    return
  }
  if (formData.value.adminPassword !== formData.value.confirmPassword) {
    ElMessage.error('两次输入的密码不一致')
    return
  }

  loading.value = true
  try {
    const registerData = {
      username: formData.value.deviceName,
      email: `${formData.value.deviceName}@trashcan.com`,
      password: formData.value.adminPassword,
      role: 'TRASHCAN'
    }
    
    const response = await trashcanApi.register(registerData)
    
    if (response.token) {
      localStorage.setItem('token', response.token)
      localStorage.setItem('deviceInitialized', 'true')
      localStorage.setItem('deviceInfo', JSON.stringify({
        deviceName: formData.value.deviceName,
        location: formData.value.location,
        binType: formData.value.binType,
        adminPassword: formData.value.adminPassword
      }))
      
      ElMessage.success('设备激活成功')
      setTimeout(() => {
        router.push('/work')
      }, 1000)
    } else {
      throw new Error('注册失败')
    }
  } catch (error) {
    console.error('设备激活失败:', error)
    ElMessage.error(error.response?.data?.message || '设备激活失败，请稍后重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.init-container {
  min-height: 100vh;
  background: #f5f7fa;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

.init-screen {
  width: 100%;
  max-width: 600px;
  background: white;
  padding: 40px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.logo-section {
  text-align: center;
  margin-bottom: 40px;
}

.logo-title {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin: 0 0 10px 0;
}

.logo-subtitle {
  font-size: 14px;
  color: #909399;
  margin: 0;
}

.init-form {
  margin-top: 30px;
}

.bin-type-group {
  width: 100%;
}

.submit-btn {
  width: 100%;
  height: 44px;
  font-size: 16px;
  font-weight: bold;
}

@media (max-width: 640px) {
  .init-screen {
    padding: 30px 20px;
  }
  
  .logo-title {
    font-size: 24px;
  }
}
</style>
