<template>
  <div class="work-container">
    <div class="work-screen">
      <div class="work-header">
        <h2 class="work-title">易控智能垃圾桶</h2>
        <div class="work-time">{{ currentTime }}</div>
      </div>

      <div class="work-content">
        <el-carousel height="400" arrow="always" indicator-position="outside" class="banner-carousel">
          <el-carousel-item v-for="(banner, index) in banners" :key="index">
            <div class="banner-item" :style="{ background: banner.background }">
              <h3 class="banner-title">{{ banner.title }}</h3>
              <p class="banner-desc">{{ banner.description }}</p>
            </div>
          </el-carousel-item>
        </el-carousel>

        <div class="device-info">
          <el-descriptions title="设备信息" :column="2" border>
            <el-descriptions-item label="设备名称">{{ deviceInfo.deviceName }}</el-descriptions-item>
            <el-descriptions-item label="设备位置">{{ deviceInfo.location }}</el-descriptions-item>
            <el-descriptions-item label="垃圾桶类型">{{ deviceInfo.binType }}</el-descriptions-item>
            <el-descriptions-item label="设备状态">
              <el-tag type="success">在线</el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </div>
      </div>

      <div class="work-footer">
        <el-button type="primary" size="large" @click="goToLogin" class="action-btn">
          点击屏幕开始使用
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
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
  binType: '可回收物'
})

const banners = ref([])

const updateTime = () => {
  const now = new Date()
  currentTime.value = now.toLocaleTimeString('zh-CN', {
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
    if (response && response.data) {
      banners.value = response.data
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
        binType: response.statusText || deviceInfo.value.binType
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
.work-container {
  min-height: 100vh;
  background: #f5f7fa;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

.work-screen {
  width: 100%;
  max-width: 800px;
  background: white;
  padding: 30px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.work-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
}

.work-title {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin: 0;
}

.work-time {
  font-size: 18px;
  color: #606266;
  font-family: monospace;
}

.work-content {
  margin-bottom: 30px;
}

.banner-carousel {
  margin-bottom: 30px;
}

.banner-item {
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 40px;
  color: white;
  text-align: center;
}

.banner-title {
  font-size: 28px;
  font-weight: bold;
  margin: 0 0 15px 0;
}

.banner-desc {
  font-size: 16px;
  margin: 0;
  opacity: 0.9;
}

.device-info {
  margin-top: 20px;
}

.work-footer {
  text-align: center;
}

.action-btn {
  width: 100%;
  height: 50px;
  font-size: 18px;
  font-weight: bold;
}

@media (max-width: 768px) {
  .work-screen {
    padding: 20px;
  }
  
  .work-title {
    font-size: 20px;
  }
  
  .work-time {
    font-size: 16px;
  }
}
</style>
