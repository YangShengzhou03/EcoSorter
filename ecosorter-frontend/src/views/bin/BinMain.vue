<template>
  <div class="bin-main">
    <!-- 轮播图区域 -->
    <div class="carousel-container">
      <el-carousel :interval="5000" arrow="hover" :autoplay="true" height="100%" indicator-position="bottom">
        <!-- 欢迎页 -->
        <el-carousel-item>
          <div class="carousel-slide welcome-slide">
            <div class="welcome-content">
              <h1 class="slide-title">智能垃圾分类</h1>
              <p class="slide-subtitle">让垃圾分类更简单、更高效</p>
              <div class="welcome-actions">
                <el-button type="primary" size="large" @click="goToLogin">开始使用</el-button>
              </div>
            </div>
          </div>
        </el-carousel-item>

        <!-- 垃圾分类指南 -->
        <el-carousel-item>
          <div class="carousel-slide guide-slide">
            <h2 class="slide-title">垃圾分类指南</h2>
            <div class="guide-grid">
              <div class="guide-card recyclable">
                <div class="guide-icon"></div>
                <h3 class="guide-name">可回收物</h3>
                <p class="guide-desc">纸张、塑料、玻璃、金属</p>
              </div>
              <div class="guide-card harmful">
                <div class="guide-icon"></div>
                <h3 class="guide-name">有害垃圾</h3>
                <p class="guide-desc">电池、灯管、药品</p>
              </div>
              <div class="guide-card wet">
                <div class="guide-icon"></div>
                <h3 class="guide-name">湿垃圾</h3>
                <p class="guide-desc">厨余垃圾、果皮</p>
              </div>
              <div class="guide-card dry">
                <div class="guide-icon"></div>
                <h3 class="guide-name">干垃圾</h3>
                <p class="guide-desc">其他难以回收的垃圾</p>
              </div>
            </div>
          </div>
        </el-carousel-item>

        <!-- 积分奖励 -->
        <el-carousel-item>
          <div class="carousel-slide points-slide">
            <h2 class="slide-title">积分奖励机制</h2>
            <div class="points-content">
              <div class="points-item">
                <div class="points-icon"></div>
                <div class="points-text">
                  <h3>正确投放</h3>
                  <p>获得5-15积分</p>
                </div>
              </div>
              <div class="points-item">
                <div class="points-icon"></div>
                <div class="points-text">
                  <h3>连续投放</h3>
                  <p>额外奖励积分</p>
                </div>
              </div>
              <div class="points-item">
                <div class="points-icon"></div>
                <div class="points-text">
                  <h3>积分兑换</h3>
                  <p>环保礼品等你拿</p>
                </div>
              </div>
            </div>
          </div>
        </el-carousel-item>

        <!-- 设备功能 -->
        <el-carousel-item>
          <div class="carousel-slide features-slide">
            <h2 class="slide-title">智能功能</h2>
            <div class="features-grid">
              <div class="feature-card">
                <div class="feature-icon"></div>
                <h3 class="feature-name">多种登录方式</h3>
                <p class="feature-desc">NFC、二维码、人脸识别</p>
              </div>
              <div class="feature-card">
                <div class="feature-icon"></div>
                <h3 class="feature-name">智能识别</h3>
                <p class="feature-desc">自动识别垃圾类型</p>
              </div>
              <div class="feature-card">
                <div class="feature-icon"></div>
                <h3 class="feature-name">投放记录</h3>
                <p class="feature-desc">查看历史投放数据</p>
              </div>
              <div class="feature-card">
                <div class="feature-icon"></div>
                <h3 class="feature-name">实时通知</h3>
                <p class="feature-desc">积分到账及时提醒</p>
              </div>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>

    <!-- 登录页面 -->
    <div class="login-page" v-if="currentStep === 'login'">
      <div class="login-content">
        <h2 class="page-title">请选择登录方式</h2>
        <div class="login-grid">
          <div class="login-option" @click="loginWithNFC">
            <div class="login-icon nfc">
              <el-icon :size="60" color="#409EFF"><Promotion /></el-icon>
            </div>
            <h3 class="login-name">NFC刷卡</h3>
            <p class="login-desc">将卡片靠近感应区</p>
          </div>
          <div class="login-option" @click="loginWithQRCode">
            <div class="login-icon qr">
              <el-icon :size="60" color="#67C23A"><Document /></el-icon>
            </div>
            <h3 class="login-name">二维码</h3>
            <p class="login-desc">扫描手机二维码</p>
          </div>
          <div class="login-option" @click="loginWithFace">
            <div class="login-icon face">
              <el-icon :size="60" color="#E6A23C"><User /></el-icon>
            </div>
            <h3 class="login-name">人脸识别</h3>
            <p class="login-desc">正对摄像头识别</p>
          </div>
        </div>
        <el-button class="back-button" @click="currentStep = 'welcome'">返回</el-button>
      </div>
    </div>

    <!-- 扫描页面 -->
    <div class="status-page" v-if="currentStep === 'scanning'">
      <div class="status-content">
        <el-icon :size="120" color="#409EFF" class="status-icon"><Loading /></el-icon>
        <p class="status-text">{{ scanStatusText }}</p>
        <el-button class="cancel-button" @click="resetToLogin">取消</el-button>
      </div>
    </div>

    <!-- 检测页面 -->
    <div class="status-page" v-if="currentStep === 'detecting'">
      <div class="status-content">
        <el-progress type="circle" :percentage="detectionProgress" :width="200" stroke-width="15" />
        <p class="status-text">正在识别垃圾类型...</p>
      </div>
    </div>

    <!-- 结果页面 -->
    <div class="result-page" v-if="currentStep === 'result'">
      <div class="result-content">
        <h2 class="page-title">识别结果</h2>
        <div class="result-card" :class="trashType">
          <div class="result-icon">{{ getTrashIcon(trashType) }}</div>
          <p class="result-type">{{ trashTypeConfig[trashType].label }}</p>
          <p class="result-confidence">识别准确度: {{ confidence }}%</p>
        </div>
        <div class="action-buttons">
          <el-button type="primary" size="large" @click="confirmDisposal">确认投放</el-button>
          <el-button size="large" @click="resetToLogin">重新识别</el-button>
        </div>
      </div>
    </div>

    <!-- 投放页面 -->
    <div class="status-page" v-if="currentStep === 'disposing'">
      <div class="status-content">
        <el-icon :size="120" color="#409EFF" class="status-icon"><Box /></el-icon>
        <p class="status-text">投放口已开启</p>
        <p class="countdown-text">{{ countdown }}秒后自动关闭</p>
      </div>
    </div>

    <!-- 完成页面 -->
    <div class="status-page" v-if="currentStep === 'complete'">
      <div class="status-content">
        <el-icon :size="120" color="#67C23A" class="status-icon"><CircleCheck /></el-icon>
        <h2 class="page-title">投放完成</h2>
        <p class="points-text">获得积分: +{{ ecoPoints }}</p>
        <div class="action-buttons">
          <el-button type="primary" size="large" @click="resetToLogin">继续投放</el-button>
          <el-button size="large" @click="resetToWelcome">返回首页</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { Promotion, Document, User, Loading, Box, CircleCheck } from '@element-plus/icons-vue'

const currentStep = ref('welcome')
const detectionProgress = ref(0)
const trashType = ref('recyclable')
const confidence = ref(0)
const countdown = ref(10)
const ecoPoints = ref(0)
const scanStatusText = ref('正在扫描...')

let detectionTimer = null
let countdownTimer = null
let scanTimer = null

const trashTypeConfig = {
  recyclable: { label: '可回收物', color: '#67C23A', bgColor: '#f0f9eb' },
  harmful: { label: '有害垃圾', color: '#F56C6C', bgColor: '#fef0f0' },
  wet: { label: '湿垃圾', color: '#E6A23C', bgColor: '#fdf6ec' },
  dry: { label: '干垃圾', color: '#909399', bgColor: '#f4f4f5' }
}

// 获取垃圾类型对应的图标
const getTrashIcon = (type) => {
  const icons = {
    recyclable: '',
    harmful: '',
    wet: '',
    dry: ''
  }
  return icons[type] || ''
}

// 进入登录页面
const goToLogin = () => {
  currentStep.value = 'login'
}

// 返回登录页面
const resetToLogin = () => {
  currentStep.value = 'login'
  detectionProgress.value = 0
  if (countdownTimer) clearInterval(countdownTimer)
  if (scanTimer) clearTimeout(scanTimer)
}

// 返回欢迎页面
const resetToWelcome = () => {
  currentStep.value = 'welcome'
  detectionProgress.value = 0
  if (countdownTimer) clearInterval(countdownTimer)
  if (scanTimer) clearTimeout(scanTimer)
}

// NFC登录
const loginWithNFC = () => {
  currentStep.value = 'scanning'
  scanStatusText.value = '请将NFC卡片靠近感应区...'
  scanTimer = setTimeout(() => {
    currentStep.value = 'detecting'
    startDetection()
  }, 2000)
}

// 二维码登录
const loginWithQRCode = () => {
  currentStep.value = 'scanning'
  scanStatusText.value = '请将二维码对准扫描区...'
  scanTimer = setTimeout(() => {
    currentStep.value = 'detecting'
    startDetection()
  }, 2000)
}

// 人脸识别登录
const loginWithFace = () => {
  currentStep.value = 'scanning'
  scanStatusText.value = '请正对摄像头...'
  scanTimer = setTimeout(() => {
    currentStep.value = 'detecting'
    startDetection()
  }, 2000)
}

// 开始垃圾检测
const startDetection = () => {
  detectionProgress.value = 0
  
  detectionTimer = setInterval(() => {
    detectionProgress.value += 5
    if (detectionProgress.value >= 100) {
      clearInterval(detectionTimer)
      const types = Object.keys(trashTypeConfig)
      trashType.value = types[Math.floor(Math.random() * types.length)]
      confidence.value = Math.floor(Math.random() * 15) + 85
      currentStep.value = 'result'
    }
  }, 100)
}

// 确认投放
const confirmDisposal = () => {
  currentStep.value = 'disposing'
  countdown.value = 10
  
  countdownTimer = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) {
      clearInterval(countdownTimer)
      ecoPoints.value = Math.floor(Math.random() * 10) + 5
      currentStep.value = 'complete'
    }
  }, 1000)
}
</script>

<style scoped>
/* 主容器样式 */
.bin-main {
  height: 100%;
  width: 100%;
  background: #f5f5f5;
  position: relative;
  overflow: hidden;
}

/* 轮播图容器 */
.carousel-container {
  height: 100%;
  width: 100%;
}

/* 轮播图幻灯片通用样式 */
.carousel-slide {
  height: 100%;
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px;
  color: #ffffff;
  text-align: center;
}

/* 标题样式 */
.slide-title {
  font-size: 48px;
  font-weight: bold;
  margin-bottom: 30px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
}

/* 副标题样式 */
.slide-subtitle {
  font-size: 24px;
  margin-bottom: 40px;
  opacity: 0.9;
}

/* 欢迎页样式 */
.welcome-slide {
  background: #409EFF;
}

.welcome-content {
  max-width: 800px;
  margin: 0 auto;
}

.welcome-actions {
  margin-top: 60px;
}

/* 指南页样式 */
.guide-slide {
  background: #E6A23C;
}

.guide-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 30px;
  max-width: 800px;
  margin-top: 40px;
}

.guide-card {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 12px;
  padding: 30px;
  border: 1px solid rgba(0, 0, 0, 0.1);
}

.guide-card.recyclable {
  border-color: #67C23A;
}

.guide-card.harmful {
  border-color: #F56C6C;
}

.guide-card.wet {
  border-color: #E6A23C;
}

.guide-card.dry {
  border-color: #909399;
}

.guide-icon {
  font-size: 60px;
  margin-bottom: 20px;
}

.guide-name {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 10px;
}

.guide-desc {
  font-size: 18px;
  opacity: 0.8;
}

/* 积分页样式 */
.points-slide {
  background: #67C23A;
}

.points-content {
  max-width: 800px;
  margin-top: 40px;
}

.points-item {
  display: flex;
  align-items: center;
  gap: 30px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 12px;
  padding: 25px;
  margin-bottom: 20px;
  border: 1px solid rgba(0, 0, 0, 0.1);
}

.points-icon {
  font-size: 50px;
}

.points-text h3 {
  font-size: 24px;
  font-weight: bold;
  margin: 0 0 5px 0;
}

.points-text p {
  font-size: 18px;
  opacity: 0.8;
  margin: 0;
}

/* 功能页样式 */
.features-slide {
  background: #F56C6C;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 30px;
  max-width: 800px;
  margin-top: 40px;
}

.feature-card {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 12px;
  padding: 30px;
  border: 1px solid rgba(0, 0, 0, 0.1);
}

.feature-icon {
  font-size: 60px;
  margin-bottom: 20px;
}

.feature-name {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 10px;
}

.feature-desc {
  font-size: 18px;
  opacity: 0.8;
}

/* 登录页面 */
.login-page {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(102, 126, 234, 0.98);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.login-content {
  max-width: 900px;
  width: 100%;
  padding: 40px;
  text-align: center;
  color: #ffffff;
}

.login-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 30px;
  margin: 40px 0;
}

.login-option {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 12px;
  padding: 40px 20px;
  cursor: pointer;
  border: 1px solid rgba(0, 0, 0, 0.1);
}

.login-icon {
  margin-bottom: 20px;
}

.login-name {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 10px;
}

.login-desc {
  font-size: 16px;
  opacity: 0.8;
}

.back-button {
  margin-top: 20px;
  color: #ffffff;
  border-color: #ffffff;
}

/* 状态页面通用样式 */
.status-page {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.98);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.status-content {
  text-align: center;
  padding: 40px;
}

.status-icon {
  margin-bottom: 30px;
}

.status-text {
  font-size: 28px;
  color: #303133;
  margin: 30px 0;
  font-weight: 500;
}

.countdown-text {
  font-size: 24px;
  color: #67C23A;
  font-weight: bold;
  margin-top: 10px;
}

.cancel-button {
  margin-top: 20px;
}

/* 结果页面 */
.result-page {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.98);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.result-content {
  text-align: center;
  padding: 40px;
  max-width: 600px;
}

.result-card {
  background: #f0f9eb;
  border-radius: 16px;
  padding: 50px;
  margin: 40px 0;
  border: 2px solid #67C23A;
}

.result-card.recyclable {
  background: #f0f9eb;
  border-color: #67C23A;
}

.result-card.harmful {
  background: #fef0f0;
  border-color: #F56C6C;
}

.result-card.wet {
  background: #fdf6ec;
  border-color: #E6A23C;
}

.result-card.dry {
  background: #f4f4f5;
  border-color: #909399;
}

.result-icon {
  font-size: 80px;
  margin-bottom: 20px;
}

.result-type {
  font-size: 48px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #303133;
}

.result-confidence {
  font-size: 24px;
  color: #606266;
}

/* 完成页面 */
.points-text {
  font-size: 48px;
  color: #67C23A;
  font-weight: bold;
  margin: 30px 0;
}

/* 页面标题 */
.page-title {
  font-size: 36px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 20px;
}

/* 操作按钮 */
.action-buttons {
  display: flex;
  gap: 20px;
  justify-content: center;
  margin-top: 40px;
}

/* 轮播图指示器样式 */
.el-carousel__indicator {
  width: 12px;
  height: 12px;
  background: rgba(255, 255, 255, 0.5);
  border-radius: 50%;
  margin: 0 6px;
  transition: all 0.3s ease;
}

.el-carousel__indicator.is-active {
  width: 30px;
  background: #ffffff;
  border-radius: 6px;
}

/* 轮播图箭头样式 */
.el-carousel__arrow {
  width: 40px;
  height: 40px;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  color: #ffffff;
  font-size: 20px;
}

.el-carousel__arrow:hover {
  background: rgba(255, 255, 255, 0.5);
}
</style>
