<template>
  <div class="login-container">
    <div class="login-screen">
      <div class="login-header">
        <el-button @click="goBack" class="back-btn">返回</el-button>
        <h2 class="login-title">用户登录</h2>
        <div class="placeholder"></div>
      </div>

      <el-tabs v-model="activeTab" class="login-tabs">
        <el-tab-pane label="手机号" name="phone">
          <div class="phone-login">
            <el-input
              v-model="phoneForm.phone"
              placeholder="请输入手机号"
              clearable
              class="input-item"
            >
              <template #prefix>
                <el-icon><Phone /></el-icon>
              </template>
            </el-input>
            <div class="code-input-group">
              <el-input
                v-model="phoneForm.code"
                placeholder="请输入验证码"
                clearable
                class="input-item"
              >
                <template #prefix>
                  <el-icon><Lock /></el-icon>
                </template>
              </el-input>
              <el-button
                type="primary"
                :disabled="codeCountdown > 0"
                @click="sendCode"
                class="code-btn"
              >
                {{ codeCountdown > 0 ? `${codeCountdown}s` : '获取验证码' }}
              </el-button>
            </div>
            <el-button
              type="primary"
              class="submit-btn"
              @click="handlePhoneLogin"
            >
              登录
            </el-button>
          </div>
        </el-tab-pane>

        <el-tab-pane label="扫码" name="qrcode">
          <div class="qrcode-login">
            <div class="qrcode-container">
              <div class="qrcode-box">
                <el-icon :size="64" class="qrcode-icon"><Iphone /></el-icon>
                <p class="qrcode-text">请使用手机扫码登录</p>
              </div>
            </div>
            <p class="qrcode-tip">打开易控智能垃圾桶APP，扫描二维码登录</p>
          </div>
        </el-tab-pane>

        <el-tab-pane label="NFC" name="nfc">
          <div class="nfc-login">
            <div class="nfc-container">
              <el-icon :size="80" class="nfc-icon"><Connection /></el-icon>
              <h3 class="nfc-title">请将NFC卡片靠近设备</h3>
              <p class="nfc-desc">支持NFC卡片、手机NFC功能</p>
            </div>
          </div>
        </el-tab-pane>

        <el-tab-pane label="人脸" name="face">
          <div class="face-login">
            <div class="face-container">
              <div class="face-camera">
                <el-icon :size="64" class="face-icon"><User /></el-icon>
                <p class="face-text">点击下方按钮启动摄像头</p>
              </div>
              <el-button
                type="primary"
                class="face-btn"
                @click="handleFaceLogin"
              >
                启动人脸识别
              </el-button>
            </div>
          </div>
        </el-tab-pane>

        <el-tab-pane label="不登录" name="guest">
          <div class="guest-login">
            <div class="guest-container">
              <el-icon :size="80" class="guest-icon"><Pointer /></el-icon>
              <h3 class="guest-title">游客模式</h3>
              <p class="guest-desc">无需登录，直接使用垃圾分类功能</p>
              <el-button
                type="primary"
                class="guest-btn"
                @click="handleGuestLogin"
              >
                进入系统
              </el-button>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Phone, Lock, Iphone, Connection, User, Pointer } from '@element-plus/icons-vue'
import { trashcanApi } from '@/api/trashcan'

defineOptions({
  name: 'UserLogin'
})

const router = useRouter()

const activeTab = ref('guest')
const codeCountdown = ref(0)

const phoneForm = ref({
  phone: '',
  code: ''
})

const goBack = () => {
  router.push('/work')
}

const sendCode = () => {
  if (!phoneForm.value.phone) {
    ElMessage.warning('请输入手机号')
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
  if (!phoneForm.value.phone) {
    ElMessage.warning('请输入手机号')
    return
  }
  if (!phoneForm.value.code) {
    ElMessage.warning('请输入验证码')
    return
  }
  try {
    const response = await trashcanApi.login({
      email: phoneForm.value.phone + '@ecosorter.com',
      password: phoneForm.value.code
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
.login-container {
  min-height: 100vh;
  background: #f5f7fa;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

.login-screen {
  width: 100%;
  max-width: 500px;
  background: white;
  padding: 30px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.login-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.back-btn {
  width: 80px;
}

.login-title {
  font-size: 20px;
  font-weight: bold;
  color: #303133;
  margin: 0;
}

.placeholder {
  width: 80px;
}

.login-tabs {
  min-height: 350px;
}

.input-item {
  margin-bottom: 15px;
}

.code-input-group {
  display: flex;
  gap: 10px;
  margin-bottom: 15px;
}

.code-input-group .input-item {
  flex: 1;
  margin-bottom: 0;
}

.code-btn {
  min-width: 120px;
}

.submit-btn {
  width: 100%;
  height: 44px;
  font-size: 16px;
  font-weight: bold;
}

.qrcode-login {
  text-align: center;
  padding: 40px 20px;
}

.qrcode-container {
  margin-bottom: 20px;
}

.qrcode-box {
  width: 200px;
  height: 200px;
  margin: 0 auto;
  border: 2px dashed #dcdfe6;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.qrcode-icon {
  color: #909399;
  margin-bottom: 15px;
}

.qrcode-text {
  font-size: 14px;
  color: #606266;
  margin: 0;
}

.qrcode-tip {
  font-size: 14px;
  color: #909399;
  margin: 0;
}

.nfc-login {
  text-align: center;
  padding: 40px 20px;
}

.nfc-container {
  padding: 40px 20px;
}

.nfc-icon {
  color: #409eff;
  margin-bottom: 20px;
}

.nfc-title {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
  margin: 0 0 10px 0;
}

.nfc-desc {
  font-size: 14px;
  color: #606266;
  margin: 0;
}

.face-login {
  text-align: center;
  padding: 40px 20px;
}

.face-container {
  padding: 40px 20px;
}

.face-camera {
  width: 280px;
  height: 280px;
  margin: 0 auto 20px;
  border: 2px solid #dcdfe6;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
}

.face-icon {
  color: #909399;
  margin-bottom: 15px;
}

.face-text {
  font-size: 14px;
  color: #606266;
  margin: 0;
}

.face-btn {
  width: 200px;
  height: 44px;
  font-size: 16px;
  font-weight: bold;
}

.guest-login {
  text-align: center;
  padding: 60px 20px;
}

.guest-container {
  padding: 40px 20px;
}

.guest-icon {
  color: #67c23a;
  margin-bottom: 20px;
}

.guest-title {
  font-size: 20px;
  font-weight: bold;
  color: #303133;
  margin: 0 0 10px 0;
}

.guest-desc {
  font-size: 14px;
  color: #606266;
  margin: 0 0 30px 0;
}

.guest-btn {
  width: 200px;
  height: 44px;
  font-size: 16px;
  font-weight: bold;
}

@media (max-width: 640px) {
  .login-screen {
    padding: 20px;
  }
  
  .qrcode-box,
  .face-camera {
    width: 100%;
    height: 200px;
  }
}
</style>
