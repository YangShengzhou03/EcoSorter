<template>
  <div class="bin-layout">
    <div class="bin-screen">
      <div class="bin-header">
        <div class="bin-status">
          <div class="status-indicator" :class="binStatusClass"></div>
          <span class="status-text">{{ binStatusText }}</span>
        </div>
        <div class="bin-datetime">{{ currentTime }}</div>
      </div>
      
      <div class="bin-banners" v-if="banners.length > 0">
        <el-carousel :interval="5000" arrow="hover" height="120px" class="banner-carousel">
          <el-carousel-item v-for="(banner, index) in banners" :key="index">
            <div class="banner-item" :style="{ background: banner.background }">
              <div class="banner-content">
                <h3>{{ banner.title }}</h3>
                <p>{{ banner.description }}</p>
              </div>
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>
      
      <div class="bin-content">
        <router-view />
      </div>
      
      <div class="bin-footer">
        <div class="bin-info">
          <span>设备ID: {{ binInfo.binId }}</span>
          <span>位置: {{ binInfo.location }}</span>
        </div>
        <div class="fill-level">
          <div class="fill-bar">
            <div class="fill-progress" :class="getFillLevelClass()" :style="{ width: getFillPercentage() + '%' }"></div>
          </div>
          <span class="fill-text">{{ binInfo.fillLevel }} / {{ binInfo.maxCapacity }}</span>
          <el-tag v-if="isNearFull" type="warning" size="small" style="margin-left: 10px">接近满载</el-tag>
          <el-tag v-if="isFull" type="danger" size="small" style="margin-left: 10px">已满</el-tag>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import { binApi } from '@/api/bin'
import { bannerApi } from '@/api/banner'

export default {
  name: 'BinLayout',
  setup() {
    const currentTime = ref('')
    const banners = ref([])
    const binInfo = ref({
      binId: localStorage.getItem('binDeviceId') || 'BIN-001',
      location: 'A区-1号楼',
      fillLevel: 65,
      maxCapacity: 100,
      threshold: 80,
      status: 'online'
    })
    
    let timeInterval = null
    let statusInterval = null
    
    const binStatusClass = computed(() => ({
      'status-online': binInfo.value.status === 'online',
      'status-offline': binInfo.value.status === 'offline',
      'status-error': binInfo.value.status === 'error',
      'status-full': binInfo.value.status === 'full'
    }))
    
    const binStatusText = computed(() => ({
      'online': '在线',
      'offline': '离线',
      'error': '故障',
      'full': '已满'
    }[binInfo.value.status] || '未知'))
    
    const getFillPercentage = () => {
      if (!binInfo.value.maxCapacity || binInfo.value.maxCapacity === 0) return 0
      return Math.round((binInfo.value.fillLevel / binInfo.value.maxCapacity) * 100)
    }
    
    const getFillLevelClass = () => {
      const percentage = getFillPercentage()
      if (percentage >= 90) return 'fill-full'
      if (percentage >= binInfo.value.threshold) return 'fill-warning'
      return 'fill-normal'
    }
    
    const isNearFull = computed(() => {
      const percentage = getFillPercentage()
      return percentage >= binInfo.value.threshold && percentage < 90
    })
    
    const isFull = computed(() => {
      const percentage = getFillPercentage()
      return percentage >= 90
    })
    
    const updateTime = () => {
      currentTime.value = new Date().toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      })
    }
    
    const loadBanners = async () => {
      try {
        const response = await bannerApi.getList('bin')
        banners.value = response || []
      } catch (error) {
        console.error('加载轮播图失败:', error)
        banners.value = []
      }
    }
    
    const fetchBinStatus = async () => {
      try {
        const response = await binApi.getBinStatus(binInfo.value.binId)
        binInfo.value.binId = response.binId
        binInfo.value.location = response.location
        binInfo.value.fillLevel = response.fillLevel || 0
        binInfo.value.maxCapacity = response.maxCapacity || 100
        binInfo.value.threshold = response.threshold || 80
        binInfo.value.status = response.status || 'offline'
      } catch (error) {
        console.error('获取垃圾桶状态失败:', error)
      }
    }
    
    onMounted(() => {
      updateTime()
      loadBanners()
      timeInterval = setInterval(updateTime, 1000)
      fetchBinStatus()
      statusInterval = setInterval(fetchBinStatus, 30000)
    })
    
    onUnmounted(() => {
      if (timeInterval) clearInterval(timeInterval)
      if (statusInterval) clearInterval(statusInterval)
    })
    
    return {
      currentTime,
      binInfo,
      binStatusClass,
      binStatusText,
      getFillPercentage,
      getFillLevelClass,
      isNearFull,
      isFull
    }
  }
}
</script>

<style scoped>
.bin-layout {
  width: 100vw;
  height: 100vh;
  background: #f5f5f5;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.bin-screen {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.bin-header {
  height: 50px;
  background: rgba(255, 255, 255, 0.9);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 40px;
  border-bottom:1px solid rgba(0, 0, 0, 0.1);
}

.bin-status {
  display: flex;
  align-items: center;
  gap: 12px;
}

.status-indicator {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: #67c23a;
}

.status-indicator.status-online { background: #67c23a; }
.status-indicator.status-offline { background: #909399; }
.status-indicator.status-error { background: #f56c6c; }
.status-indicator.status-full { background: #e6a23c; }

.status-text {
  color: #303133;
  font-size: 16px;
  font-weight: 500;
}

.bin-datetime {
  color: #303133;
  font-size: 18px;
  font-weight: 400;
  font-family: 'Segoe UI', 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

.bin-content {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0;
  background: transparent;
  overflow: hidden;
}

.bin-footer {
  height: 60px;
  background: rgba(255, 255, 255, 0.9);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 40px;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
}

.bin-info {
  display: flex;
  gap: 30px;
}

.bin-info span {
  color: #303133;
  font-size: 14px;
  font-weight: 400;
  opacity: 0.9;
}

.fill-level {
  display: flex;
  align-items: center;
  gap: 12px;
}

.fill-bar {
  width: 180px;
  height: 8px;
  background: rgba(0, 0, 0, 0.1);
  border-radius: 4px;
  overflow: hidden;
}

.fill-progress {
  height: 100%;
  background: #67c23a;
}

.fill-progress.fill-normal { background: #67c23a; }
.fill-progress.fill-warning { background: #e6a23c; }
.fill-progress.fill-full { background: #f56c6c; }

.fill-text {
  color: #303133;
  font-size: 16px;
  font-weight: 600;
  min-width: 50px;
}
</style>
