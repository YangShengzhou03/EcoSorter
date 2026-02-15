<template>
  <div class="recognize-container">
    <div class="recognize-screen">
      <div class="recognize-header">
        <el-button @click="goBack" class="back-btn">返回</el-button>
        <h2 class="recognize-title">垃圾识别</h2>
        <div class="placeholder"></div>
      </div>

      <div class="recognize-content">
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
              type="primary"
              size="large"
              @click="startCamera"
              v-if="!cameraActive"
              class="control-btn"
            >
              启动摄像头
            </el-button>
            <el-button
              type="success"
              size="large"
              @click="captureImage"
              v-if="cameraActive"
              class="control-btn"
            >
              拍照识别
            </el-button>
            <el-button
              size="large"
              @click="stopCamera"
              v-if="cameraActive"
              class="control-btn"
            >
              关闭摄像头
            </el-button>
          </div>
        </div>

        <div class="result-section" v-if="recognizeResult">
          <el-card class="result-card">
            <template #header>
              <div class="result-header">
                <span class="result-title">识别结果</span>
                <el-tag :type="resultType">{{ recognizeResult.category }}</el-tag>
              </div>
            </template>
            
            <div class="result-content">
              <div class="result-image">
                <img :src="capturedImage" alt="识别图片" class="captured-img" />
              </div>
              
              <el-descriptions :column="1" border class="result-details">
                <el-descriptions-item label="识别物品">{{ recognizeResult.item }}</el-descriptions-item>
                <el-descriptions-item label="分类类型">{{ recognizeResult.category }}</el-descriptions-item>
                <el-descriptions-item label="置信度">{{ recognizeResult.confidence }}%</el-descriptions-item>
                <el-descriptions-item label="投放建议">{{ recognizeResult.advice }}</el-descriptions-item>
              </el-descriptions>
              
              <div class="result-actions">
                <el-button type="primary" @click="resetRecognition">重新识别</el-button>
              </div>
            </div>
          </el-card>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { VideoCamera } from '@element-plus/icons-vue'
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
const uploadedImageUrl = ref('')

let stream = null

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
    console.error('摄像头启动失败:', error)
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
    ElMessage.info('正在上传图片...')
    
    const blob = dataURLtoBlob(capturedImage.value)
    const file = new File([blob], 'capture.jpg', { type: 'image/jpeg' })
    
    const uploadResponse = await trashcanApi.uploadImage(file)
    if (uploadResponse && uploadResponse.url) {
      uploadedImageUrl.value = uploadResponse.url
      await performClassification(uploadResponse.url)
    } else {
      throw new Error('上传失败')
    }
  } catch (error) {
    console.error('上传图片失败:', error)
    ElMessage.error('上传图片失败，请重试')
  }
}

const performClassification = async (imageUrl) => {
  try {
    ElMessage.info('正在识别...')
    
    const response = await trashcanApi.getClassification(imageUrl)
    
    if (response && response.data) {
      const result = response.data
      
      recognizeResult.value = {
        item: result.item || '未知物品',
        category: result.category || '其他垃圾',
        confidence: result.confidence || 85,
        advice: result.advice || '请正确分类投放'
      }
      
      await submitClassification(result)
      
      ElMessage.success('识别完成')
    } else {
      throw new Error('识别失败')
    }
  } catch (error) {
    console.error('识别失败:', error)
    ElMessage.error('识别失败，请重试')
  }
}

const submitClassification = async (result) => {
  try {
    const token = localStorage.getItem('token')
    if (token) {
      await trashcanApi.submitClassification({
        imageUrl: uploadedImageUrl.value,
        categoryId: result.categoryId,
        confidence: result.confidence
      })
    }
  } catch (error) {
    console.error('提交分类记录失败:', error)
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

const resetRecognition = () => {
  recognizeResult.value = null
  capturedImage.value = ''
  uploadedImageUrl.value = ''
}

onUnmounted(() => {
  stopCamera()
})
</script>

<style scoped>
.recognize-container {
  min-height: 100vh;
  background: #f5f7fa;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

.recognize-screen {
  width: 100%;
  max-width: 900px;
  background: white;
  padding: 30px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.recognize-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
}

.recognize-title {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin: 0;
}

.placeholder {
  width: 80px;
}

.recognize-content {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.camera-section {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.camera-container {
  width: 100%;
  height: 480px;
  background: #000;
  border-radius: 4px;
  overflow: hidden;
  position: relative;
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
  background: #f5f7fa;
}

.placeholder-icon {
  color: #909399;
  margin-bottom: 20px;
}

.placeholder-text {
  font-size: 16px;
  color: #606266;
  margin: 0;
}

.camera-controls {
  display: flex;
  gap: 15px;
  justify-content: center;
}

.control-btn {
  min-width: 160px;
  height: 48px;
  font-size: 16px;
}

.result-section {
  margin-top: 20px;
}

.result-card {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.result-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.result-title {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}

.result-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.result-image {
  text-align: center;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 4px;
}

.captured-img {
  max-width: 100%;
  max-height: 300px;
  border-radius: 4px;
}

.result-details {
  margin-top: 20px;
}

.result-actions {
  display: flex;
  justify-content: center;
  padding-top: 20px;
}

@media (max-width: 768px) {
  .recognize-screen {
    padding: 20px;
  }
  
  .camera-container {
    height: 360px;
  }
  
  .camera-controls {
    flex-direction: column;
  }
  
  .control-btn {
    width: 100%;
  }
}
</style>
