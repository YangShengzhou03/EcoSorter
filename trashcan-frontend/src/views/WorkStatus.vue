<template>
  <div class="work-page">
    <div class="left-panel">
      <div class="brand-section">
        <div class="logo-wrapper">
          <el-icon :size="40" class="logo-icon"><Delete /></el-icon>
        </div>
        <div class="brand-info">
          <h1 class="brand-title">易控智能垃圾桶</h1>
          <p class="brand-subtitle">{{ currentTime }}</p>
        </div>
      </div>
      
      <div class="device-info">
        <el-descriptions :column="1" border size="small">
          <el-descriptions-item label="设备名称">{{ deviceInfo.deviceName }}</el-descriptions-item>
          <el-descriptions-item label="设备位置">{{ deviceInfo.location }}</el-descriptions-item>
          <el-descriptions-item label="垃圾桶类型">
            <el-tag :type="binTypeTag" size="small">{{ binTypeLabel }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="设备状态">
            <el-tag type="success" size="small">在线</el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </div>

    <div class="right-panel">
      <el-carousel height="100%" :interval="8000" arrow="never" indicator-position="none">
        <el-carousel-item v-for="(banner, index) in banners" :key="index">
          <div class="banner-content" :style="{ background: banner.background }">
            <h2 class="banner-title">{{ banner.title }}</h2>
            <p class="banner-desc">{{ banner.description }}</p>
          </div>
        </el-carousel-item>
      </el-carousel>
      
      <div class="start-overlay" @click="goToLogin">
        <div class="start-content">
          <el-icon class="touch-icon"><Pointer /></el-icon>
          <span class="start-text">点击屏幕开始使用</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { trashcanApi } from '@/api/trashcan'

defineOptions({
  name: 'WorkStatus'
})

const router = useRouter()

const currentTime = ref('')
let timeInterval = null

const deviceInfo = ref({
  deviceName: '易控智能垃圾桶-001',
  location: 'A区-1号楼大厅',
  binType: 'recyclable'
})

const banners = ref([
  { title: '智能垃圾分类', description: '让垃圾分类更简单，让环境更美好', background: '#409eff' },
  { title: '保护环境', description: '从我做起，从垃圾分类做起', background: '#67c23a' },
  { title: '绿色生活', description: '共建美好家园，共享绿色未来', background: '#e6a23c' }
])

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

const goToLogin = () => {
  router.push('/login')
}

const loadBanners = async () => {
  try {
    const response = await trashcanApi.getBanners()
    if (response && response.length > 0) {
      banners.value = response
    }
  } catch (error) {
    console.error('加载轮播图失败:', error)
  }
}

const loadDeviceInfo = async () => {
  try {
    const response = await trashcanApi.getTrashcanInfo()
    if (response) {
      deviceInfo.value = {
        deviceName: response.deviceId || deviceInfo.value.deviceName,
        location: response.location || deviceInfo.value.location,
        binType: response.binType || deviceInfo.value.binType
      }
    }
  } catch (error) {
    console.error('加载设备信息失败:', error)
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
  display: flex;
  background: #f5f5f5;
}

.left-panel {
  width: 320px;
  background: #1a1a2e;
  display: flex;
  flex-direction: column;
  padding: 24px;
}

.brand-section {
  display: flex;
  align-items: center;
  gap: 16px;
  padding-bottom: 20px;
  border-bottom: 1px solid #2a2a3e;
  margin-bottom: 20px;
}

.logo-wrapper {
  width: 56px;
  height: 56px;
  background: #409eff;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.logo-icon {
  color: white;
}

.brand-info {
  flex: 1;
}

.brand-title {
  font-size: 18px;
  font-weight: 600;
  color: white;
  margin: 0 0 4px;
}

.brand-subtitle {
  font-size: 13px;
  color: #909399;
  margin: 0;
  font-family: monospace;
}

.device-info {
  flex: 1;
}

.device-info :deep(.el-descriptions) {
  background: transparent;
}

.device-info :deep(.el-descriptions__label) {
  color: #909399;
  background: #1a1a2e;
}

.device-info :deep(.el-descriptions__content) {
  color: white;
  background: #1a1a2e;
}

.device-info :deep(.el-descriptions__cell) {
  border-color: #2a2a3e;
}

.right-panel {
  flex: 1;
  position: relative;
  overflow: hidden;
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
  font-size: 42px;
  font-weight: 600;
  margin: 0 0 16px;
}

.banner-desc {
  font-size: 20px;
  margin: 0;
  opacity: 0.9;
}

.start-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  background: rgba(0, 0, 0, 0.3);
  opacity: 0;
  transition: opacity 0.3s;
}

.start-overlay:hover {
  opacity: 1;
}

.start-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  color: white;
}

.touch-icon {
  font-size: 48px;
}

.start-text {
  font-size: 18px;
  font-weight: 500;
}

@media (max-width: 1024px) {
  .left-panel {
    width: 280px;
    padding: 20px;
  }
  
  .banner-title {
    font-size: 32px;
  }
  
  .banner-desc {
    font-size: 16px;
  }
}

@media (max-width: 768px) {
  .work-page {
    flex-direction: column;
  }
  
  .left-panel {
    width: 100%;
    padding: 16px;
  }
  
  .brand-section {
    padding-bottom: 12px;
    margin-bottom: 12px;
  }
  
  .logo-wrapper {
    width: 44px;
    height: 44px;
  }
  
  .brand-title {
    font-size: 16px;
  }
  
  .device-info {
    display: none;
  }
  
  .right-panel {
    flex: 1;
  }
  
  .banner-title {
    font-size: 24px;
  }
  
  .banner-desc {
    font-size: 14px;
  }
}
</style>
