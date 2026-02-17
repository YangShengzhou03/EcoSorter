<template>
  <div class="work-page" @click="handlePageClick">
    <el-carousel height="100%" :interval="8000" arrow="hover" indicator-position="outside">
      <el-carousel-item v-for="(banner, index) in banners" :key="index">
        <div class="banner-content" :style="{ background: banner.background }">
          <h2 class="banner-title">{{ banner.title }}</h2>
          <p class="banner-desc">{{ banner.description }}</p>
        </div>
      </el-carousel-item>
    </el-carousel>
    
    <div class="admin-entry" @click.stop="showLoginOverlay">
      <el-icon><Setting /></el-icon>
    </div>
    
    <div class="login-overlay" :class="{ visible: showLogin }" @click="hideLoginOverlay">
      <div class="login-container" @click.stop>
        <div class="login-header">
          <p class="login-subtitle">请输入管理员密码</p>
        </div>
        
        <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef" class="login-form">
          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入管理员密码"
              size="large"
              show-password
              @keyup.enter="handleLogin"
            >
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          
          <el-form-item>
            <el-button
              type="primary"
              size="large"
              :loading="loginLoading"
              @click="handleLogin"
              style="width: 100%"
            >
              登录
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
    
    <div class="device-info-overlay" :class="{ visible: showDeviceInfo }" @click="hideDeviceInfo">
      <h1 class="brand-title">易控智能垃圾桶</h1>
      <p class="brand-subtitle">{{ currentTime }}</p>
      
      <el-descriptions :column="1" border size="large" class="device-info">
        <el-descriptions-item label="设备名称">{{ deviceInfo.deviceName }}</el-descriptions-item>
        <el-descriptions-item label="设备位置">{{ deviceInfo.location }}</el-descriptions-item>
        <el-descriptions-item label="垃圾桶类型">
          <el-tag :type="binTypeTag" size="large">{{ binTypeLabel }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="设备状态">
          <el-tag type="success" size="large">在线</el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { Setting, Lock } from '@element-plus/icons-vue'
import { trashcanApi } from '@/api/trashcan'
import { ElMessage } from 'element-plus'

defineOptions({
  name: 'WorkStatus'
})

const router = useRouter()
const currentTime = ref('')
let timeInterval = null

const showLogin = ref(false)
const showDeviceInfo = ref(false)

const loginLoading = ref(false)

const loginFormRef = ref(null)

const loginForm = ref({
  password: ''
})

const loginRules = {
  password: [
    { required: true, message: '请输入管理员密码', trigger: 'blur' }
  ]
}

const deviceInfo = ref({
  deviceName: '',
  location: '',
  binType: ''
})

const banners = ref([])

const binTypeMap = {
  recyclable: { label: '可回收物', tag: 'success' },
  hazardous: { label: '有害垃圾', tag: 'danger' },
  kitchen: { label: '厨余垃圾', tag: 'warning' },
  other: { label: '其他垃圾', tag: 'info' }
}

const binTypeLabel = computed(() => binTypeMap[deviceInfo.value.binType]?.label || deviceInfo.value.binType)
const binTypeTag = computed(() => binTypeMap[deviceInfo.value.binType]?.tag || 'info')

const updateTime = () => {
  const now = new Date()
  currentTime.value = now.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

const showLoginOverlay = () => {
  showLogin.value = true
}

const hideLoginOverlay = () => {
  showLogin.value = false
}

const handlePageClick = () => {
  router.push('/login')
}

const hideDeviceInfo = () => {
  showDeviceInfo.value = false
}

const handleLogin = async () => {
  try {
    await loginFormRef.value.validate()
    loginLoading.value = true

    const response = await trashcanApi.adminLogin(loginForm.value.password)
    
    if (response && response.success) {
      localStorage.setItem('trashcanAdminLoggedIn', 'true')
      ElMessage.success('登录成功')
      hideLoginOverlay()
      router.push('/admin')
    } else {
      throw new Error('登录失败')
    }
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '密码错误，请重试')
  } finally {
    loginLoading.value = false
  }
}

const loadBanners = async () => {
  try {
    const response = await trashcanApi.getBanners()
    if (response && response.length > 0) {
      banners.value = response
    }
  } catch (error) {
  }
}

const loadDeviceInfo = async () => {
  try {
    const response = await trashcanApi.getTrashcanInfo()
    if (response) {
      deviceInfo.value = {
        deviceName: response.deviceName || '',
        location: response.location || '',
        binType: response.binType || ''
      }
    }
  } catch (error) {
  }
}

onMounted(() => {
  updateTime()
  timeInterval = setInterval(updateTime, 1000)
  
  const savedInfo = localStorage.getItem('deviceInfo')
  if (savedInfo) {
    deviceInfo.value = JSON.parse(savedInfo)
  }
  
  loadBanners()
  loadDeviceInfo()
})

onUnmounted(() => {
  if (timeInterval) clearInterval(timeInterval)
})
</script>

<style scoped>
.work-page {
  width: 100vw;
  height: 100vh;
  position: relative;
  overflow: hidden;
}

.work-page :deep(.el-carousel) {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
}

.work-page :deep(.el-carousel__container) {
  width: 100%;
  height: 100%;
}

.work-page :deep(.el-carousel__item) {
  width: 100%;
  height: 100%;
}

.banner-content {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: white;
  text-align: center;
  padding: 40px;
}

.banner-title {
  font-size: 48px;
  font-weight: 600;
  margin: 0 0 20px;
}

.banner-desc {
  font-size: 24px;
  margin: 0;
  opacity: 0.9;
}

.admin-entry {
  position: absolute;
  top: 20px;
  right: 20px;
  width: 36px;
  height: 36px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s;
  z-index: 100;
}

.admin-entry:hover {
  background: rgba(255, 255, 255, 0.4);
  transform: scale(1.1);
}

.admin-entry .el-icon {
  font-size: 18px;
  color: white;
}

.login-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.85);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  visibility: hidden;
  transition: all 0.3s ease;
  z-index: 1000;
}

.login-overlay.visible {
  opacity: 1;
  visibility: visible;
}

.login-container {
  background: white;
  border-radius: 8px;
  padding: 24px;
  width: 100%;
  max-width: 320px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.login-header {
  text-align: center;
  margin-bottom: 20px;
}

.login-subtitle {
  font-size: 13px;
  color: #909399;
  margin: 0;
}

.login-form {
  margin-top: 16px;
}

.device-info-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.85);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px;
  opacity: 0;
  visibility: hidden;
  transition: all 0.3s ease;
  z-index: 1000;
}

.device-info-overlay.visible {
  opacity: 1;
  visibility: visible;
}

.brand-title {
  font-size: 36px;
  font-weight: 600;
  color: white;
  margin: 0 0 12px;
}

.brand-subtitle {
  font-size: 16px;
  color: #909399;
  margin: 0 0 40px;
  font-family: monospace;
}

.device-info {
  max-width: 600px;
  width: 100%;
}

.device-info :deep(.el-descriptions) {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
}

.device-info :deep(.el-descriptions__label) {
  color: #e5e7eb;
  background: rgba(255, 255, 255, 0.05);
  font-size: 16px;
  font-weight: 500;
}

.device-info :deep(.el-descriptions__content) {
  color: white;
  background: rgba(255, 255, 255, 0.05);
  font-size: 18px;
}

.device-info :deep(.el-descriptions__cell) {
  border-color: rgba(255, 255, 255, 0.1);
  padding: 16px;
}

.close-hint {
  position: absolute;
  bottom: 40px;
  color: #9ca3af;
  font-size: 14px;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% {
    opacity: 0.6;
  }
  50% {
    opacity: 1;
  }
}

@media (max-width: 1024px) {
  .banner-title {
    font-size: 36px;
  }
  
  .banner-desc {
    font-size: 20px;
  }
  
  .brand-title {
    font-size: 28px;
  }
  
  .device-info {
    max-width: 500px;
  }
}

@media (max-width: 768px) {
  .banner-title {
    font-size: 28px;
  }
  
  .banner-desc {
    font-size: 16px;
  }
  
  .brand-title {
    font-size: 24px;
  }
  
  .brand-subtitle {
    font-size: 14px;
  }
  
  .device-info-overlay {
    padding: 20px;
  }
  
  .device-info {
    max-width: 100%;
  }
  
  .device-info :deep(.el-descriptions__label) {
    font-size: 14px;
  }
  
  .device-info :deep(.el-descriptions__content) {
    font-size: 16px;
  }
  
  .device-info :deep(.el-descriptions__cell) {
    padding: 12px;
  }
  
  .recognition-container,
  .points-container {
    padding: 24px;
    max-width: 90%;
  }
  
  .camera-preview {
    height: 200px;
  }
}
</style>
