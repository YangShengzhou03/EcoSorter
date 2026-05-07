<template>
  <div class="recognize-page">
    <div class="left-panel">
      <div class="panel-header">
        <el-button @click="goBack" text class="back-btn">
          <el-icon>
            <ArrowLeft />
          </el-icon>
          返回
        </el-button>
        <div class="user-info">
          <el-icon>
            <User />
          </el-icon>
          <span class="username">{{ currentUser?.username || currentUser?.name || '游客' }}</span>
        </div>
      </div>

      <div class="camera-section">
        <div class="camera-container">
          <video ref="videoRef" class="camera-video" autoplay playsinline></video>
          <canvas ref="canvasRef" class="camera-canvas" style="display: none;"></canvas>
          <div v-if="!cameraActive" class="camera-placeholder">
            <el-icon :size="64" class="placeholder-icon">
              <VideoCamera />
            </el-icon>
            <p class="placeholder-text">摄像头启动失败</p>
          </div>
        </div>

        <div class="camera-controls">
          <el-button v-if="cameraActive" type="success" size="large" :loading="isRecognizing" :disabled="isRecognizing"
            @click="captureImage">
            {{ isRecognizing ? '识别中...' : '拍照识别' }}
          </el-button>
          <el-button v-if="cameraActive" size="large" @click="stopCamera">
            退出
          </el-button>
        </div>
      </div>
    </div>

    <div class="right-panel">
      <div v-if="recognizeResult" class="result-section">
        <div class="result-image">
          <el-image :src="capturedImage" fit="contain" class="captured-img" />
        </div>
        <div class="result-actions">
          <el-tag :type="resultType">{{ recognizeResult.category }}</el-tag>
          <el-button type="primary" :loading="isSubmitting" :disabled="isSubmitting" @click="confirmDisposal">
            {{ isSubmitting ? '提交中...' : '确认投放' }}
          </el-button>
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
          <el-descriptions-item label="分类指南">
            {{ categoryGuide }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <div v-else class="empty-section">
        <el-icon :size="80" color="#c0c4cc">
          <Camera />
        </el-icon>
        <p class="empty-tip">将垃圾物品对准摄像头，点击拍照即可识别</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { VideoCamera, ArrowLeft, Camera, User } from '@element-plus/icons-vue'
import { trashcanApi } from '@/api/trashcan'

defineOptions({
  name: 'GarbageRecognize'
})

const router = useRouter()

const currentUser = ref(null)
const categories = ref([])
const categoryMap = ref({})
const categoriesLoaded = ref(false)

const loadCurrentUser = () => {
  try {
    const userId = sessionStorage.getItem('userId')
    const username = sessionStorage.getItem('username')
    if (userId && username) {
      currentUser.value = {
        id: parseInt(userId),
        username: username,
        name: username
      }
    }
  } catch (error) {
    currentUser.value = null
  }
}

const loadCategories = async () => {
  if (categoriesLoaded.value) {
    return
  }

  try {
    const response = await trashcanApi.getWasteCategories()

    if (response && Array.isArray(response)) {
      categories.value = response
      const map = {}
      response.forEach(cat => {
        map[cat.name] = cat.id
      })
      categoryMap.value = map
      categoriesLoaded.value = true
    }
  } catch (error) {
    ElMessage.warning('分类数据加载失败，部分功能可能受影响')
  }
}

loadCurrentUser()
loadCategories().catch(() => {
})

const videoRef = ref(null)
const canvasRef = ref(null)
const cameraActive = ref(false)
const capturedImage = ref('')
const recognizeResult = ref(null)
const isSubmitting = ref(false)
const isRecognizing = ref(false)

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
    if (stream) {
      stopCamera()
    }

    stream = await navigator.mediaDevices.getUserMedia({
      video: {
        facingMode: 'environment',
        width: { ideal: 1280 },
        height: { ideal: 720 }
      }
    })

    if (videoRef.value) {
      videoRef.value.srcObject = stream
      await videoRef.value.play()
      cameraActive.value = true
    }
  } catch (error) {
    let errorMessage = '无法启动摄像头'

    if (error.name === 'NotAllowedError' || error.name === 'PermissionDeniedError') {
      errorMessage = '请允许访问摄像头权限'
    } else if (error.name === 'NotFoundError' || error.name === 'DevicesNotFoundError') {
      errorMessage = '未找到摄像头设备'
    } else if (error.name === 'NotReadableError' || error.name === 'TrackStartError') {
      errorMessage = '摄像头被其他应用占用'
    }

    ElMessage.error(errorMessage)
    cameraActive.value = false
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
  if (!videoRef.value || !canvasRef.value || isRecognizing.value) return

  isRecognizing.value = true

  const video = videoRef.value
  const canvas = canvasRef.value

  canvas.width = video.videoWidth
  canvas.height = video.videoHeight

  const ctx = canvas.getContext('2d')
  ctx.drawImage(video, 0, 0, canvas.width, canvas.height)

  capturedImage.value = canvas.toDataURL('image/jpeg', 0.8)

  try {
    const blob = dataURLtoBlob(capturedImage.value)
    const file = new File([blob], 'capture.jpg', { type: 'image/jpeg' })

    const response = await trashcanApi.getClassificationWithFile(file)

    if (response && response.data) {
      recognizeResult.value = {
        item: response.data.item || '未知物品',
        category: response.data.category || '其他垃圾',
        confidence: response.data.confidence || 85,
        advice: response.data.advice || '请正确分类投放'
      }
    } else {
      throw new Error('识别失败')
    }
  } catch (error) {
    ElMessage.error('识别失败，请重试')
  } finally {
    isRecognizing.value = false
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

const confirmDisposal = async () => {
  if (!recognizeResult.value || isSubmitting.value) return

  isSubmitting.value = true

  try {
    const categoryId = categoryMap.value[recognizeResult.value.category]
    if (!categoryId) {
      throw new Error('未找到对应的分类ID')
    }

    const data = {
      categoryId: categoryId,
      confidence: recognizeResult.value.confidence / 100
    }

    if (currentUser.value && currentUser.value.id) {
      data.userId = currentUser.value.id
    }

    await trashcanApi.submitClassification(data)

    const category = categories.value.find(c => c.name === recognizeResult.value.category)
    const points = category ? category.points : 0

    if (currentUser.value && currentUser.value.id) {
      ElMessage.success({
        message: `投放成功！获得 ${points} 积分`,
        duration: 3000
      })
    } else {
      ElMessage.success('投放成功！')
    }

    resetRecognition()
  } catch (error) {
    ElMessage.error('投放失败，请重试')
  } finally {
    isSubmitting.value = false
  }
}

onMounted(() => {
  startCamera()
})

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

.user-info {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  background: #f5f7fa;
  border-radius: 16px;
  color: #606266;
  font-size: 14px;
}

.username {
  font-weight: 500;
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
  gap: 16px;
}

.result-image {
  background: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  height: 450px;
  border-radius: 8px;
}

.captured-img {
  max-width: 100%;
  max-height: 100%;
}

.result-actions {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
}

.result-details {
  flex-shrink: 0;
}

.result-item {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
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
    height: 150px;
  }
}
</style>
