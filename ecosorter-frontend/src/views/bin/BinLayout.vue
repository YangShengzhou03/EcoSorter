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
            <div class="fill-progress" :style="{ width: binInfo.fillLevel + '%' }"></div>
          </div>
          <span class="fill-text">{{ binInfo.fillLevel }}%</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted, onUnmounted } from 'vue'

export default {
  name: 'BinLayout',
  setup() {
    const currentTime = ref('')
    const binInfo = ref({
      binId: 'BIN-001',
      location: 'A区-1号楼',
      fillLevel: 65,
      status: 'online'
    })
    
    let timeInterval = null
    
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
    
    const fetchBinStatus = () => {
      const statuses = ['online', 'offline', 'error', 'full']
      binInfo.value.status = statuses[Math.floor(Math.random() * statuses.length)]
      binInfo.value.fillLevel = Math.floor(Math.random() * 100)
    }
    
    onMounted(() => {
      updateTime()
      timeInterval = setInterval(updateTime, 1000)
      fetchBinStatus()
      setInterval(fetchBinStatus, 30000)
    })
    
    onUnmounted(() => {
      if (timeInterval) clearInterval(timeInterval)
    })
    
    return {
      currentTime,
      binInfo,
      binStatusClass,
      binStatusText
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
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
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

.fill-text {
  color: #303133;
  font-size: 16px;
  font-weight: 600;
  min-width: 50px;
}
</style>
