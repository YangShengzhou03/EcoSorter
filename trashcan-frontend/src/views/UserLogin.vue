<template>
  <div class="login-page">
    <div class="left-panel">
      <div class="device-info-section">
        <div class="device-info-header">
          <el-icon :size="20" class="info-icon">
            <InfoFilled />
          </el-icon>
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

      <div class="copyright">
        <p>© {{ currentYear }} ECO-Sorter Yangshengzhou. All Rights Reserved.</p>
      </div>
    </div>

    <div class="right-panel">
      <div class="login-tabs">
        <div class="tab-header">
          <div v-for="tab in tabs" :key="tab.name" :class="['tab-item', { active: activeTab === tab.name }]"
            @click="activeTab = tab.name">
            <el-icon>
              <component :is="tab.icon" />
            </el-icon>
            <span>{{ tab.label }}</span>
          </div>
        </div>

        <div class="tab-content">
          <div v-show="activeTab === 'qrcode'" class="tab-pane center-content">
            <div v-if="qrCodeUrl" class="qrcode-container">
              <img :src="qrCodeUrl" alt="扫码登录" class="qrcode-image" />
              <div class="qrcode-overlay">
                <el-icon :size="40" color="#409eff">
                  <Iphone />
                </el-icon>
              </div>
            </div>
            <div v-else class="qrcode-loading">
              <el-icon :size="80" color="#c0c4cc">
                <Iphone />
              </el-icon>
            </div>
            <p class="tip-text">请使用手机APP扫描二维码登录</p>
            <el-link type="primary" href="https://example.com/download" target="_blank" class="download-link">
              <el-icon>
                <Download />
              </el-icon>
              手机端下载
            </el-link>
          </div>

          <div v-show="activeTab === 'face'" class="tab-pane center-content">
            <div class="camera-container">
              <video ref="videoRef" autoplay playsinline muted class="camera-preview"></video>
              <div class="camera-overlay">
                <el-icon :size="80" color="white">
                  <User />
                </el-icon>
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
            <svg class="guest-icon" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg" width="120" height="120">
              <path d="M117.44 921.28l14.08-16c20.48 23.04 49.28 37.12 80.96 37.12 41.6 0 67.2-21.76 67.2-53.76 0-33.92-21.76-44.48-49.6-56.96l-41.92-18.56c-24.96-11.2-56.64-28.8-56.64-69.76 0-41.6 35.2-70.4 82.88-70.4 33.92 0 61.44 14.72 79.04 33.92l-13.12 15.04c-16.64-16.96-38.4-28.16-65.92-28.16-35.84 0-59.52 18.88-59.52 47.68 0 32 27.2 43.84 47.68 52.48l41.92 18.56c32 14.08 58.56 32 58.56 74.56 0 42.88-35.84 76.16-90.56 76.16-40.64 0-72.96-16.96-95.04-41.92zM361.28 678.4h23.04v152h0.64l131.2-152h27.2l-88.96 104 101.76 175.68h-26.56l-90.56-157.76L384 863.68v94.4h-23.04V678.4zM600.32 678.4h23.04v279.68h-23.04V678.4zM704.96 678.4h72.64c63.36 0 102.08 20.8 102.08 78.72 0 56-38.4 82.88-100.48 82.88h-50.88v118.08h-23.04V678.4z m69.44 142.4c55.36 0 81.6-18.88 81.6-63.68 0-45.44-27.52-59.52-83.2-59.52h-44.8v123.2h46.4z" fill="#409eff"></path>
            </svg>
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
import { ref, reactive, onMounted, watch, computed, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Iphone, User, InfoFilled, Download } from '@element-plus/icons-vue'
import { trashcanApi } from '@/api/trashcan'
import QRCode from 'qrcode'

defineOptions({
  name: 'UserLogin'
})

const router = useRouter()

const currentYear = new Date().getFullYear()

const deviceInfo = ref({
  deviceId: '',
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
  { name: 'guest', label: '游客', icon: 'User' },
  { name: 'qrcode', label: '扫码', icon: 'Iphone' },
  { name: 'face', label: '人脸', icon: 'User' }
]

const activeTab = ref('guest')
const videoStream = ref(null)
const videoRef = ref(null)
const faceLoginLoading = ref(false)
const qrCodeUrl = ref('')
const qrCodeId = ref('')
const pollingTimer = ref(null)
const isPolling = ref(false)
const pollingStartTime = ref(null)
const QR_CODE_TIMEOUT = 5 * 60 * 1000

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
        console.log('[DEBUG] 开始人脸识别，文件大小:', blob.size, 'bytes')
        const file = new File([blob], 'face.jpg', { type: 'image/jpeg' })

        const loginResponse = await trashcanApi.faceLoginWithFile(file)
        console.log('[DEBUG] 人脸识别响应:', loginResponse)

        if (loginResponse && loginResponse.verified && loginResponse.userId) {
          sessionStorage.setItem('userId', loginResponse.userId)
          sessionStorage.setItem('username', loginResponse.username)

          stopCamera()
          router.push('/recognize')
        } else {
          console.log('[DEBUG] 人脸识别失败，verified:', loginResponse?.verified, 'userId:', loginResponse?.userId)
          ElMessage.error('人脸识别失败，未找到匹配的用户')
        }
      } catch (error) {
        console.error('[ERROR] 人脸识别请求失败:', error)
        ElMessage.error('人脸识别失败，请重试')
      } finally {
        faceLoginLoading.value = false
      }
    }, 'image/jpeg', 0.95)
  } catch (error) {
    console.error('[ERROR] 拍照失败:', error)
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
        deviceId: response.deviceId || '',
        deviceName: response.deviceName || '',
        location: response.location || '',
        binType: response.binType || ''
      }
    }
  } catch (error) {
  }
}

const generateQRCode = async () => {
  try {
    const response = await trashcanApi.createQRSession()
    if (response && response.qrCode) {
      qrCodeId.value = response.qrCode

      qrCodeUrl.value = await QRCode.toDataURL(response.qrCode, {
        width: 200,
        margin: 2,
        color: {
          dark: '#000000',
          light: '#FFFFFF'
        }
      })

      startPolling()
    }
  } catch (error) {
    ElMessage.error('生成二维码失败')
  }
}

const startPolling = () => {
  if (isPolling.value) {
    return
  }

  isPolling.value = true
  pollingStartTime.value = Date.now()

  pollingTimer.value = setInterval(async () => {
    if (Date.now() - pollingStartTime.value > QR_CODE_TIMEOUT) {
      stopPolling()
      ElMessage.warning('二维码已过期，请重新获取')
      qrCodeUrl.value = ''
      qrCodeId.value = ''
      return
    }

    try {
      const response = await trashcanApi.checkQRStatus(qrCodeId.value)

      if (response) {
        if (response.status === 'confirmed' && response.qrCode) {
          stopPolling()
          sessionStorage.setItem('userToken', response.qrCode)
          ElMessage.success('登录成功')
          router.push('/recognize')
        } else if (response.status === 'expired') {
          stopPolling()
          ElMessage.warning('二维码已过期，请刷新页面重新获取')
          qrCodeUrl.value = ''
          qrCodeId.value = ''
        }
      }
    } catch (error) {
      stopPolling()
    }
  }, 2000)
}

const stopPolling = () => {
  if (pollingTimer.value) {
    clearInterval(pollingTimer.value)
    pollingTimer.value = null
  }
  isPolling.value = false
  pollingStartTime.value = null
}

watch(activeTab, (newTab) => {
  if (newTab === 'face') {
    startCamera()
  } else {
    stopCamera()
  }

  if (newTab !== 'qrcode') {
    stopPolling()
  }

  if (newTab === 'qrcode') {
    nextTick(() => {
      generateQRCode()
    })
  }
})

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

.device-info-section {
  margin: auto 0;
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

.copyright {
  text-align: center;
  padding: 16px 0;
  margin-top: auto;
}

.copyright p {
  font-size: 12px;
  color: #909399;
  margin: 0;
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

.download-link {
  margin-top: 12px;
  font-size: 14px;
}

.qrcode-container {
  position: relative;
  width: 200px;
  height: 200px;
  background: white;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.qrcode-image {
  width: 100%;
  height: 100%;
  display: block;
}

.qrcode-overlay {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 48px;
  height: 48px;
  background: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.qrcode-loading {
  width: 200px;
  height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
  border-radius: 12px;
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

.guest-icon {
  display: block;
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

  .copyright {
    text-align: center;
    padding: 16px 0;
  }

  .copyright p {
    font-size: 12px;
    color: #909399;
    margin: 0;
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
