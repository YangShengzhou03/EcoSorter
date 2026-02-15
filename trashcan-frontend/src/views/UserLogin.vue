<template>
  <div class="login-page">
    <div class="left-panel">
      <div class="brand-section">
        <el-button @click="goBack" text class="back-btn">
          <el-icon><ArrowLeft /></el-icon>
          返回
        </el-button>
        <div class="logo-wrapper">
          <el-icon :size="40" class="logo-icon"><Delete /></el-icon>
        </div>
        <h1 class="brand-title">智能垃圾分类</h1>
        <p class="brand-subtitle">请选择登录方式</p>
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
          <div v-show="activeTab === 'phone'" class="tab-pane">
            <el-form ref="phoneFormRef" :model="phoneForm" :rules="phoneRules" label-position="top" @submit.prevent="handlePhoneLogin">
              <el-form-item prop="phone">
                <el-input
                  v-model="phoneForm.phone"
                  placeholder="请输入手机号"
                  size="large"
                  clearable
                >
                  <template #prefix>
                    <el-icon><Phone /></el-icon>
                  </template>
                </el-input>
              </el-form-item>
              <el-form-item prop="code">
                <div class="code-input-group">
                  <el-input
                    v-model="phoneForm.code"
                    placeholder="请输入验证码"
                    size="large"
                    clearable
                  >
                    <template #prefix>
                      <el-icon><Lock /></el-icon>
                    </template>
                  </el-input>
                  <el-button
                    type="primary"
                    size="large"
                    :disabled="codeCountdown > 0"
                    @click="sendCode"
                  >
                    {{ codeCountdown > 0 ? `${codeCountdown}s` : '获取验证码' }}
                  </el-button>
                </div>
              </el-form-item>
              <el-button
                type="primary"
                size="large"
                class="submit-btn"
                native-type="submit"
              >
                登录
              </el-button>
            </el-form>
          </div>

          <div v-show="activeTab === 'qrcode'" class="tab-pane center-content">
            <el-icon :size="120" color="#c0c4cc"><Iphone /></el-icon>
            <p class="tip-text">请使用手机APP扫描二维码登录</p>
          </div>

          <div v-show="activeTab === 'nfc'" class="tab-pane center-content">
            <el-icon :size="120" color="#409eff"><Connection /></el-icon>
            <p class="tip-text">请将NFC卡片靠近设备感应区</p>
          </div>

          <div v-show="activeTab === 'face'" class="tab-pane center-content">
            <el-icon :size="120" color="#c0c4cc"><User /></el-icon>
            <p class="tip-text">点击下方按钮启动人脸识别</p>
            <el-button type="primary" size="large" @click="handleFaceLogin">
              启动人脸识别
            </el-button>
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
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Phone, Lock, Iphone, Connection, User, Pointer, ArrowLeft } from '@element-plus/icons-vue'
import { trashcanApi } from '@/api/trashcan'

defineOptions({
  name: 'UserLogin'
})

const router = useRouter()
const phoneFormRef = ref(null)

const tabs = [
  { name: 'guest', label: '游客', icon: 'Pointer' },
  { name: 'phone', label: '手机', icon: 'Phone' },
  { name: 'qrcode', label: '扫码', icon: 'Iphone' },
  { name: 'nfc', label: 'NFC', icon: 'Connection' },
  { name: 'face', label: '人脸', icon: 'User' }
]

const activeTab = ref('guest')
const codeCountdown = ref(0)

const phoneForm = reactive({
  phone: '',
  code: ''
})

const phoneRules = reactive({
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { len: 6, message: '验证码为6位数字', trigger: 'blur' }
  ]
})

const goBack = () => {
  router.push('/work')
}

const sendCode = () => {
  if (!phoneForm.phone) {
    ElMessage.warning('请输入手机号')
    return
  }
  if (!/^1[3-9]\d{9}$/.test(phoneForm.phone)) {
    ElMessage.warning('请输入正确的手机号')
    return
  }
  codeCountdown.value = 60
  const timer = setInterval(() => {
    codeCountdown.value--
    if (codeCountdown.value <= 0) {
      clearInterval(timer)
    }
  }, 1000)
  ElMessage.success('验证码已发送')
}

const handlePhoneLogin = async () => {
  const valid = await phoneFormRef.value.validate().catch(() => false)
  if (!valid) return

  try {
    const response = await trashcanApi.login({
      email: phoneForm.phone + '@ecosorter.com',
      password: phoneForm.code
    })
    if (response && response.accessToken) {
      localStorage.setItem('token', response.accessToken)
      ElMessage.success('登录成功')
      router.push('/recognize')
    } else {
      throw new Error('登录失败')
    }
  } catch (error) {
    console.error('登录失败:', error)
    ElMessage.error(error.response?.data?.message || '登录失败，请检查手机号和验证码')
  }
}

const handleFaceLogin = () => {
  ElMessage.info('正在启动摄像头...')
  setTimeout(() => {
    ElMessage.success('人脸识别成功')
    router.push('/recognize')
  }, 2000)
}

const handleGuestLogin = () => {
  ElMessage.success('进入游客模式')
  router.push('/recognize')
}
</script>

<style scoped>
.login-page {
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
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.back-btn {
  align-self: flex-start;
  color: #909399;
  margin-bottom: 40px;
}

.back-btn:hover {
  color: white;
}

.logo-wrapper {
  width: 72px;
  height: 72px;
  background: #409eff;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
}

.logo-icon {
  color: white;
}

.brand-title {
  font-size: 24px;
  font-weight: 600;
  color: white;
  margin: 0 0 8px;
}

.brand-subtitle {
  font-size: 14px;
  color: #909399;
  margin: 0;
}

.right-panel {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
}

.login-tabs {
  width: 100%;
  max-width: 500px;
}

.tab-header {
  display: flex;
  border-bottom: 1px solid #e4e7ed;
  margin-bottom: 24px;
}

.tab-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  padding: 12px;
  cursor: pointer;
  color: #909399;
  font-size: 13px;
  border-bottom: 2px solid transparent;
  transition: all 0.3s;
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
  min-height: 280px;
}

.tab-pane {
  padding: 8px;
}

.center-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 16px;
}

.tip-text {
  font-size: 14px;
  color: #909399;
  margin: 0;
}

.code-input-group {
  display: flex;
  gap: 12px;
}

.code-input-group .el-input {
  flex: 1;
}

.submit-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  margin-top: 8px;
}

@media (max-width: 1024px) {
  .left-panel {
    width: 280px;
    padding: 20px;
  }
  
  .right-panel {
    padding: 30px;
  }
}

@media (max-width: 768px) {
  .login-page {
    flex-direction: column;
  }
  
  .left-panel {
    width: 100%;
    padding: 16px;
  }
  
  .back-btn {
    margin-bottom: 20px;
  }
  
  .logo-wrapper {
    width: 56px;
    height: 56px;
  }
  
  .brand-title {
    font-size: 20px;
  }
  
  .right-panel {
    flex: 1;
    padding: 20px;
  }
  
  .tab-item {
    padding: 10px;
    font-size: 12px;
  }
  
  .tab-item .el-icon {
    font-size: 20px;
  }
}
</style>
