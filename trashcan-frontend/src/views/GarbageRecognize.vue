<template>
  <div class="recognize-page">
    <div class="left-panel">
      <div class="panel-header">
        <el-button @click="goBack" text class="back-btn">
          <el-icon><ArrowLeft /></el-icon>
          返回
        </el-button>
        <h2 class="panel-title">垃圾识别</h2>
      </div>

      <div class="camera-section">
        <div class="camera-container">
          <video ref="videoRef" class="camera-video" autoplay playsinline></video>
          <canvas ref="canvasRef" class="camera-canvas" style="display: none;"></canvas>
          <div v-if="!cameraActive" class="camera-placeholder">
            <el-icon :size="64" class="placeholder-icon"><VideoCamera /></el-icon>
            <p class="placeholder-text">点击下方按钮启动摄像头</p>
          </div>
        </div>
        
        <div class="camera-controls">
          <el-button
            v-if="!cameraActive"
            type="primary"
            size="large"
            @click="startCamera"
          >
            启动摄像头
          </el-button>
          <el-button
            v-if="cameraActive"
            type="success"
            size="large"
            @click="captureImage"
          >
            拍照识别
          </el-button>
          <el-button
            v-if="cameraActive"
            size="large"
            @click="stopCamera"
          >
            关闭
          </el-button>
        </div>
      </div>
    </div>

    <div class="right-panel">
      <div v-if="recognizeResult" class="result-section">
        <div class="result-header">
          <el-tag :type="resultType" size="large" effect="dark">{{ recognizeResult.category }}</el-tag>
          <el-button type="primary" @click="resetRecognition">重新识别</el-button>
        </div>

        <div class="result-image">
          <el-image :src="capturedImage" fit="contain" class="captured-img" />
        </div>

        <el-descriptions :column="1" border class="result-details">
          <el-descriptions-item label="识别物品">
            <span class="result-item">{{ recognizeResult.item }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="置信度">
            <el-progress :percentage="recognizeResult.confidence" :stroke-width="12" :color="progressColor" />
          </el-descriptions-item>
          <el-descriptions-item label="投放建议">
            {{ recognizeResult.advice }}
          </el-descriptions-item>
        </el-descriptions>

        <div class="category-guide">
          <h4 class="guide-title">分类指南</h4>
          <p class="guide-text">{{ categoryGuide }}</p>
        </div>
      </div>

      <div v-else class="empty-section">
        <el-icon :size="80" color="#c0c4cc"><Camera /></el-icon>
        <p class="empty-text">请启动摄像头进行垃圾识别</p>
        <p class="empty-tip">将垃圾物品对准摄像头，点击拍照即可识别</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { VideoCamera, ArrowLeft, Camera } from '@element-plus/icons-vue'
import { trashcanApi } from '@/api/trashcan'

defineOptions({
  name: 'GarbageRecognize'
})

const router = useRouter()

const videoRef = ref(null)
const canvasRef = ref(null)
const cameraActive = ref(false)
const capturedImage = ref('')
const recognizeResult = ref(null)

let stream = null

const categoryGuides = {
  '可回收物': '请保持物品清洁干燥，投放到蓝色垃圾桶。包括：废纸、塑料瓶、金属罐、玻璃瓶等。',
  '有害垃圾': '请轻放并密封包装，投放到红色垃圾桶。包括：废电池、废灯管、过期药品等。',
  '厨余垃圾': '请沥干水分后投放，投放到绿色垃圾桶。包括：剩饭剩菜、果皮、菜叶等。',
  '其他垃圾': '投放到灰色垃圾桶。包括：卫生纸、陶瓷碎片、烟头等。'
}

const categoryGuide = computed(() => {
  if (!recognizeResult.value) return ''
  return categoryGuides[recognizeResult.value.category] || '请正确分类投放垃圾。'
})

const goBack = () => {
  stopCamera()
  router.push('/work')
}

const startCamera = async () => {
  try {
    stream = await navigator.mediaDevices.getUserMedia({
      video: {
        facingMode: 'environment',
        width: { ideal: 1280 },
        height: { ideal: 720 }
      }
    })
    
    if (videoRef.value) {
      videoRef.value.srcObject = stream
      cameraActive.value = true
      ElMessage.success('摄像头已启动')
    }
  } catch (error) {
    ElMessage.error('无法启动摄像头，请检查权限设置')
  }
}

const stopCamera = () => {
  if (stream) {
    stream.getTracks().forEach(track => track.stop())
    stream = null
  }
  if (videoRef.value) {
    videoRef.value.srcObject = null
  }
  cameraActive.value = false
}

const dataURLtoBlob = (dataURL) => {
  const arr = dataURL.split(',')
  const mime = arr[0].match(/:(.*?);/)[1]
  const bstr = atob(arr[1])
  let n = bstr.length
  const u8arr = new Uint8Array(n)
  while (n--) {
    u8arr[n] = bstr.charCodeAt(n)
  }
  return new Blob([u8arr], { type: mime })
}

const captureImage = async () => {
  if (!videoRef.value || !canvasRef.value) return
  
  const video = videoRef.value
  const canvas = canvasRef.value
  
  canvas.width = video.videoWidth
  canvas.height = video.videoHeight
  
  const ctx = canvas.getContext('2d')
  ctx.drawImage(video, 0, 0, canvas.width, canvas.height)
  
  capturedImage.value = canvas.toDataURL('image/jpeg', 0.8)
  
  try {
    ElMessage.info('正在识别...')
    
    const blob = dataURLtoBlob(capturedImage.value)
    const file = new File([blob], 'capture.jpg', { type: 'image/jpeg' })
    
    const response = await trashcanApi.getClassificationWithFile(file)
    
    if (response && response.data) {
      const result = response.data
      
      recognizeResult.value = {
        item: result.item || '未知物品',
        category: result.category || '其他垃圾',
        confidence: result.confidence || 85,
        advice: result.advice || '请正确分类投放'
      }
      
      ElMessage.success('识别完成')
    } else {
      throw new Error('识别失败')
    }
  } catch (error) {
    ElMessage.error('识别失败，请重试')
  }
}

const resultType = computed(() => {
  if (!recognizeResult.value) return ''
  const typeMap = {
    '可回收物': 'success',
    '有害垃圾': 'danger',
    '厨余垃圾': 'warning',
    '其他垃圾': 'info'
  }
  return typeMap[recognizeResult.value.category] || ''
})

const progressColor = computed(() => {
  if (!recognizeResult.value) return '#409eff'
  const colorMap = {
    '可回收物': '#67c23a',
    '有害垃圾': '#f56c6c',
    '厨余垃圾': '#e6a23c',
    '其他垃圾': '#909399'
  }
  return colorMap[recognizeResult.value.category] || '#409eff'
})

const resetRecognition = () => {
  recognizeResult.value = null
  capturedImage.value = ''
}

onUnmounted(() => {
  stopCamera()
})
</script>

<style scoped>
.recognize-page {
  width: 100vw;
  height: 100vh;
  display: flex;
  background: #f5f5f5;
}

.left-panel {
  width: 50%;
  display: flex;
  flex-direction: column;
  padding: 24px;
}

.panel-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
}

.back-btn {
  color: #606266;
}

.panel-title {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}

.camera-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.camera-container {
  flex: 1;
  background: #000;
  position: relative;
  overflow: hidden;
}

.camera-video {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.camera-canvas {
  display: none;
}

.camera-placeholder {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
}

.placeholder-icon {
  color: #c0c4cc;
  margin-bottom: 16px;
}

.placeholder-text {
  font-size: 14px;
  color: #909399;
  margin: 0;
}

.camera-controls {
  display: flex;
  gap: 12px;
  justify-content: center;
}

.camera-controls .el-button {
  min-width: 120px;
}

.right-panel {
  flex: 1;
  background: white;
  display: flex;
  flex-direction: column;
  padding: 24px;
}

.result-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.result-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.result-image {
  height: 200px;
  background: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.captured-img {
  max-width: 100%;
  max-height: 100%;
}

.result-details {
  flex-shrink: 0;
}

.result-item {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.category-guide {
  background: #f5f7fa;
  padding: 16px;
  border-left: 3px solid #409eff;
}

.guide-title {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 8px;
}

.guide-text {
  font-size: 13px;
  color: #606266;
  margin: 0;
  line-height: 1.6;
}

.empty-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 16px;
}

.empty-text {
  font-size: 16px;
  color: #606266;
  margin: 0;
}

.empty-tip {
  font-size: 14px;
  color: #909399;
  margin: 0;
}

@media (max-width: 1024px) {
  .left-panel {
    width: 45%;
    padding: 20px;
  }
  
  .right-panel {
    padding: 20px;
  }
  
  .result-image {
    height: 160px;
  }
}

@media (max-width: 768px) {
  .recognize-page {
    flex-direction: column;
  }
  
  .left-panel {
    width: 100%;
    height: 50vh;
    padding: 16px;
  }
  
  .right-panel {
    flex: 1;
    padding: 16px;
  }
  
  .result-image {
    height: 120px;
  }
}
</style>
