<template>
  <div class="login-page">
    <div class="left-panel">
      <div class="brand-section">
        <h1 class="brand-title">智能垃圾分类</h1>
        <p class="brand-subtitle">请选择登录方式</p>
      </div>

      <div class="device-info-section">
        <div class="device-info-header">
          <el-icon :size="20" class="info-icon"><InfoFilled /></el-icon>
          <span class="info-title">设备信息</span>
        </div>
        <div class="device-info-list">
          <div class="info-item">
            <span class="info-label">设备名称</span>
            <span class="info-value">{{ deviceInfo.deviceName }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">设备位置</span>
            <span class="info-value">{{ deviceInfo.location }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">垃圾桶类型</span>
            <el-tag :type="binTypeTag" size="small">{{ binTypeLabel }}</el-tag>
          </div>
          <div class="info-item">
            <span class="info-label">设备状态</span>
            <el-tag type="success" size="small">在线</el-tag>
          </div>
        </div>
      </div>
    </div>

    <div class="right-panel">
      <div class="login-tabs">
        <div class="tab-header">
          <div 
            v-for="tab in tabs" 
            :key="tab.name"
            :class="['tab-item', { active: activeTab === tab.name }]"
            @click="activeTab = tab.name"
          >
            <el-icon><component :is="tab.icon" /></el-icon>
            <span>{{ tab.label }}</span>
          </div>
        </div>

        <div class="tab-content">
          <div v-show="activeTab === 'qrcode'" class="tab-pane center-content">
            <el-icon :size="120" color="#c0c4cc"><Iphone /></el-icon>
            <p class="tip-text">请使用手机APP扫描二维码登录</p>
          </div>

          <div v-show="activeTab === 'nfc'" class="tab-pane center-content">
            <el-icon :size="120" color="#409eff"><Connection /></el-icon>
            <p class="tip-text">请将NFC卡片靠近设备感应区</p>
          </div>

          <div v-show="activeTab === 'face'" class="tab-pane center-content">
            <div class="camera-container">
              <video 
                ref="videoRef" 
                autoplay 
                playsinline 
                muted
                class="camera-preview"
              ></video>
              <div class="camera-overlay">
                <el-icon :size="80" color="white"><User /></el-icon>
                <p class="camera-tip">请正对摄像头</p>
              </div>
            </div>
            <div class="face-actions">
              <el-button type="primary" size="large" @click="captureFace" :loading="faceLoginLoading">
                拍照识别
              </el-button>
            </div>
          </div>

          <div v-show="activeTab === 'guest'" class="tab-pane center-content">
            <el-icon :size="120" color="#67c23a"><Pointer /></el-icon>
            <p class="tip-text">无需登录，直接使用垃圾分类功能</p>
            <el-button type="primary" size="large" @click="handleGuestLogin">
              进入系统
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Iphone, Connection, User, Pointer, InfoFilled } from '@element-plus/icons-vue'
import { trashcanApi } from '@/api/trashcan'

defineOptions({
  name: 'UserLogin'
})

const router = useRouter()

const deviceInfo = ref({
  deviceName: '',
  location: '',
  binType: ''
})

const binTypeMap = {
  recyclable: { label: '可回收物', tag: 'success' },
  hazardous: { label: '有害垃圾', tag: 'danger' },
  kitchen: { label: '厨余垃圾', tag: 'warning' },
  other: { label: '其他垃圾', tag: 'info' }
}

const binTypeLabel = computed(() => binTypeMap[deviceInfo.value.binType]?.label || deviceInfo.value.binType)
const binTypeTag = computed(() => binTypeMap[deviceInfo.value.binType]?.tag || 'info')

const tabs = [
  { name: 'guest', label: '游客', icon: 'Pointer' },
  { name: 'qrcode', label: '扫码', icon: 'Iphone' },
  { name: 'nfc', label: 'NFC', icon: 'Connection' },
  { name: 'face', label: '人脸', icon: 'User' }
]

const activeTab = ref('guest')
const videoStream = ref(null)
const videoRef = ref(null)
const faceLoginLoading = ref(false)
const canvasRef = ref(null)

const startCamera = async () => {
  try {
    if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
      const stream = await navigator.mediaDevices.getUserMedia({ 
        video: { 
          facingMode: 'user',
          width: { ideal: 640 },
          height: { ideal: 480 }
        } 
      })
      videoStream.value = stream
      if (videoRef.value) {
        videoRef.value.srcObject = stream
      }
      ElMessage.success('摄像头已启动')
    } else {
      ElMessage.error('您的浏览器不支持摄像头访问')
    }
  } catch (error) {
    ElMessage.error('无法访问摄像头，请检查权限设置')
  }
}

const stopCamera = () => {
  if (videoStream.value) {
    videoStream.value.getTracks().forEach(track => track.stop())
    videoStream.value = null
  }
  if (videoRef.value) {
    videoRef.value.srcObject = null
  }
}

watch(activeTab, (newTab) => {
  if (newTab === 'face') {
    startCamera()
  } else {
    stopCamera()
  }
})

const captureFace = async () => {
  if (!videoRef.value || !videoStream.value) {
    ElMessage.warning('请先启动摄像头')
    return
  }

  try {
    faceLoginLoading.value = true
    
    const canvas = document.createElement('canvas')
    canvas.width = videoRef.value.videoWidth
    canvas.height = videoRef.value.videoHeight
    const ctx = canvas.getContext('2d')
    ctx.drawImage(videoRef.value, 0, 0, canvas.width, canvas.height)
    
    canvas.toBlob(async (blob) => {
      try {
        const file = new File([blob], 'face.jpg', { type: 'image/jpeg' })
        
        const loginResponse = await trashcanApi.faceLoginWithFile(file)
        
        if (loginResponse && loginResponse.accessToken) {
          sessionStorage.setItem('userToken', loginResponse.accessToken)
          sessionStorage.setItem('user', JSON.stringify(loginResponse.user))
          
          stopCamera()
          router.push('/recognize')
        } else {
          ElMessage.error('人脸识别失败，未找到匹配的用户')
        }
      } catch (error) {
        ElMessage.error('人脸识别失败，请重试')
      } finally {
        faceLoginLoading.value = false
      }
    }, 'image/jpeg', 0.95)
  } catch (error) {
    ElMessage.error('拍照失败，请重试')
    faceLoginLoading.value = false
  }
}

const handleGuestLogin = () => {
  ElMessage.success('进入游客模式')
  router.push('/recognize')
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
  loadDeviceInfo()
})
</script>

<style scoped>
.login-page {
  width: 100vw;
  height: 100vh;
  display: flex;
  background: #ffffff;
}

.left-panel {
  width: 360px;
  height: 100%;
  background: #1a1a2e;
  display: flex;
  flex-direction: column;
  padding: 20px 16px;
  border-right: 1px solid #e4e7ed;
}

.brand-section {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.logo-wrapper {
  width: 48px;
  height: 48px;
  background: #409eff;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16px;
  border-radius: 8px;
}

.logo-icon {
  color: white;
}

.brand-title {
  font-size: 28px;
  font-weight: 600;
  color: white;
  margin: 0 0 8px;
}

.brand-subtitle {
  font-size: 14px;
  color: #909399;
  margin: 0;
}

.device-info-section {
  margin-top: 48px;
  padding: 20px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 8px;
}

.device-info-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
}

.info-icon {
  color: #409eff;
}

.info-title {
  font-size: 14px;
  font-weight: 500;
  color: white;
}

.device-info-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
}

.info-label {
  font-size: 13px;
  color: #909399;
}

.info-value {
  font-size: 14px;
  color: white;
  font-weight: 500;
}

.right-panel {
  flex: 1;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  padding: 24px;
}

.login-tabs {
  width: 100%;
}

.tab-header {
  display: flex;
  border-bottom: 1px solid #e4e7ed;
  margin-bottom: 32px;
}

.tab-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  padding: 16px;
  cursor: pointer;
  color: #909399;
  font-size: 14px;
  border-bottom: 2px solid transparent;
  transition: all 0.2s;
}

.tab-item:hover {
  color: #409eff;
}

.tab-item.active {
  color: #409eff;
  border-bottom-color: #409eff;
}

.tab-item .el-icon {
  font-size: 24px;
}

.tab-content {
  min-height: 320px;
}

.tab-pane {
  padding: 16px;
}

.center-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 24px;
  padding: 48px 0;
}

.tip-text {
  font-size: 15px;
  color: #606266;
  margin: 0;
}

.camera-container {
  position: relative;
  width: 100%;
  max-width: 480px;
  aspect-ratio: 4/3;
  background: #000;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.camera-preview {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transform: scaleX(-1);
}

.camera-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: rgba(0, 0, 0, 0.3);
  pointer-events: none;
}

.camera-tip {
  font-size: 16px;
  color: white;
  margin: 16px 0 0;
  font-weight: 500;
}

.face-actions {
  display: flex;
  gap: 12px;
  margin-top: 24px;
}

@media (max-width: 1024px) {
  .left-panel {
    width: 300px;
    padding: 24px 20px;
  }
  
  .right-panel {
    padding: 32px;
  }
}

@media (max-width: 768px) {
  .login-page {
    flex-direction: column;
  }
  
  .left-panel {
    width: 100%;
    padding: 20px;
    border-right: none;
    border-bottom: 1px solid #e4e7ed;
  }
  
  .device-info-section {
    margin-top: 24px;
  }
  
  .logo-wrapper {
    width: 40px;
    height: 40px;
  }
  
  .brand-title {
    font-size: 24px;
  }
  
  .right-panel {
    flex: 1;
    padding: 24px;
  }
  
  .tab-item {
    padding: 12px;
    font-size: 13px;
  }
  
  .tab-item .el-icon {
    font-size: 20px;
  }
  
  .center-content {
    padding: 32px 0;
  }
}
</style>
