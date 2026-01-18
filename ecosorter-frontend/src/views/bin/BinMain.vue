<template>
  <div class="bin-main">
    <div class="bin-container">
      <div class="bin-display">
        <div class="camera-section">
          <div class="camera-preview">
            <el-icon class="camera-icon"><Camera /></el-icon>
            <p>摄像头预览</p>
            <el-button type="primary" @click="captureImage" :loading="capturing">
              {{ capturing ? '识别中...' : '拍摄识别' }}
            </el-button>
          </div>
        </div>

        <div class="result-section">
          <div v-if="!result" class="result-placeholder">
            <el-icon class="placeholder-icon"><Box /></el-icon>
            <p>等待拍摄...</p>
          </div>
          <div v-else class="result-content">
            <div class="category-display">
              <div class="category-icon" :style="{ background: result.color }">
                <el-icon><component :is="getCategoryIcon(result.name)" /></el-icon>
              </div>
              <div class="category-info">
                <h2>{{ result.name }}</h2>
                <p class="confidence">置信度: {{ (result.confidence * 100).toFixed(1) }}%</p>
              </div>
            </div>
            <div class="result-details">
              <div class="detail-item">
                <span class="label">分类结果:</span>
                <span class="value">{{ result.name }}</span>
              </div>
              <div class="detail-item">
                <span class="label">AI建议:</span>
                <span class="value">{{ result.aiSuggestion }}</span>
              </div>
              <div class="detail-item">
                <span class="label">处理时间:</span>
                <span class="value">{{ result.processingTime }}ms</span>
              </div>
            </div>
            <div class="result-actions">
              <el-button type="success" @click="confirmClassification">
                <el-icon><Check /></el-icon>
                确认分类
              </el-button>
              <el-button @click="resetResult">
                <el-icon><Refresh /></el-icon>
                重新拍摄
              </el-button>
            </div>
          </div>
        </div>
      </div>

      <div class="bin-status">
        <div class="status-card">
          <h3>垃圾桶状态</h3>
          <div class="status-info">
            <div class="info-row">
              <span class="label">当前容量:</span>
              <el-progress :percentage="fillPercentage" :color="progressColor" />
            </div>
            <div class="info-row">
              <span class="label">设备状态:</span>
              <el-tag :type="statusType">{{ statusText }}</el-tag>
            </div>
            <div class="info-row">
              <span class="label">今日投放:</span>
              <span class="value">{{ todayCount }} 次</span>
            </div>
          </div>
        </div>

        <div class="category-guide">
          <h3>分类指南</h3>
          <div class="guide-list">
            <div v-for="category in categories" :key="category.id" class="guide-item">
              <div class="guide-icon" :style="{ background: category.color }">
                <el-icon><component :is="category.icon" /></el-icon>
              </div>
              <div class="guide-info">
                <h4>{{ category.name }}</h4>
                <p>{{ category.description }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Camera, Box, Check, Refresh, Delete, Warning, Food } from '@element-plus/icons-vue'
import { binApi } from '@/api/bin'
import { classificationApi } from '@/api/classification'

const capturing = ref(false)
const result = ref(null)
const binStatus = ref({
  fillLevel: 65,
  maxCapacity: 100,
  status: 'online'
})
const todayCount = ref(0)

const categories = ref([
  { id: 1, name: '可回收物', color: '#10b981', icon: 'Refresh', description: '废纸、塑料、玻璃、金属等' },
  { id: 2, name: '有害垃圾', color: '#ef4444', icon: 'Warning', description: '电池、灯管、药品等' },
  { id: 3, name: '厨余垃圾', color: '#f59e0b', icon: 'Food', description: '菜叶、剩饭、果皮等' },
  { id: 4, name: '其他垃圾', color: '#6b7280', icon: 'Delete', description: '砖瓦、陶瓷、卫生纸等' }
])

const fillPercentage = computed(() => {
  return Math.round((binStatus.value.fillLevel / binStatus.value.maxCapacity) * 100)
})

const progressColor = computed(() => {
  const percentage = fillPercentage.value
  if (percentage >= 90) return '#f56c6c'
  if (percentage >= 80) return '#e6a23c'
  return '#67c23a'
})

const statusType = computed(() => {
  const statusMap = {
    'online': 'success',
    'offline': 'info',
    'error': 'danger'
  }
  return statusMap[binStatus.value.status] || 'info'
})

const statusText = computed(() => {
  const textMap = {
    'online': '在线',
    'offline': '离线',
    'error': '故障'
  }
  return textMap[binStatus.value.status] || '未知'
})

const getCategoryIcon = (name) => {
  const iconMap = {
    '可回收物': 'Recycle',
    '有害垃圾': 'Warning',
    '厨余垃圾': 'Food',
    '其他垃圾': 'Delete'
  }
  return iconMap[name] || 'Box'
}

const captureImage = async () => {
  capturing.value = true
  try {
    const formData = new FormData()
    const mockImage = new Blob([''], { type: 'image/jpeg' })
    formData.append('image', mockImage, 'capture.jpg')

    const response = await classificationApi.classifyWasteFromImage(formData)
    result.value = {
      name: response.result || '其他垃圾',
      confidence: response.confidence || 0.8,
      aiSuggestion: response.aiSuggestion || '请按照分类标准投放',
      processingTime: response.processingTime || 1000,
      color: categories.value.find(c => c.name === (response.result || '其他垃圾'))?.color || '#6b7280'
    }
    ElMessage.success('识别成功')
  } catch (error) {
    console.error('识别失败:', error)
    ElMessage.error('识别失败，请重试')
  } finally {
    capturing.value = false
  }
}

const confirmClassification = () => {
  ElMessage.success('分类已确认')
  todayCount.value++
  resetResult()
}

const resetResult = () => {
  result.value = null
}

const loadBinStatus = async () => {
  try {
    const deviceId = localStorage.getItem('binDeviceId') || 'BIN-001'
    const response = await binApi.getBinStatus(deviceId)
    binStatus.value = {
      fillLevel: response.fillLevel || 65,
      maxCapacity: response.maxCapacity || 100,
      status: response.status || 'online'
    }
  } catch (error) {
    console.error('加载垃圾桶状态失败:', error)
  }
}

onMounted(() => {
  loadBinStatus()
  setInterval(loadBinStatus, 30000)
})
</script>

<style scoped>
.bin-main {
  width: 100%;
  height: 100%;
  padding: 20px;
  overflow: auto;
}

.bin-container {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 20px;
  height: 100%;
}

.bin-display {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.camera-section {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.camera-preview {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 300px;
  background: #f5f7fa;
  border-radius: 8px;
  border: 2px dashed #dcdfe6;
}

.camera-icon {
  font-size: 64px;
  color: #909399;
  margin-bottom: 16px;
}

.camera-preview p {
  color: #606266;
  margin-bottom: 20px;
}

.result-section {
  flex: 1;
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.result-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #909399;
}

.placeholder-icon {
  font-size: 64px;
  margin-bottom: 16px;
}

.result-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.category-display {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 8px;
}

.category-icon {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.category-icon .el-icon {
  font-size: 40px;
}

.category-info h2 {
  margin: 0 0 8px 0;
  font-size: 24px;
  color: #303133;
}

.confidence {
  color: #909399;
  margin: 0;
}

.result-details {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.detail-item {
  display: flex;
  justify-content: space-between;
  padding: 12px;
  background: #f5f7fa;
  border-radius: 6px;
}

.detail-item .label {
  color: #606266;
  font-weight: 500;
}

.detail-item .value {
  color: #303133;
  font-weight: 600;
}

.result-actions {
  display: flex;
  gap: 12px;
  margin-top: auto;
}

.bin-status {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.status-card,
.category-guide {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.status-card h3,
.category-guide h3 {
  margin: 0 0 16px 0;
  font-size: 16px;
  color: #303133;
}

.status-info {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.info-row {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.info-row .label {
  color: #606266;
  font-size: 14px;
}

.info-row .value {
  color: #303133;
  font-weight: 600;
  font-size: 16px;
}

.guide-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.guide-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: #f5f7fa;
  border-radius: 6px;
}

.guide-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  flex-shrink: 0;
}

.guide-info h4 {
  margin: 0 0 4px 0;
  font-size: 14px;
  color: #303133;
}

.guide-info p {
  margin: 0;
  font-size: 12px;
  color: #909399;
}
</style>
