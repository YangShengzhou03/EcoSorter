<template>
  <div class="admin-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>管理员个人信息</span>
          <el-button type="primary" @click="showEditDialog = true">修改信息</el-button>
        </div>
      </template>

      <div class="profile-info">
        <div class="avatar-container">
          <div class="avatar-wrapper" @click="openAvatarDialog">
            <el-avatar :size="120" :src="userInfo.avatar || ''" class="profile-avatar">
              {{ userInfo.username?.charAt(0)?.toUpperCase() }}
            </el-avatar>
            <div class="avatar-overlay">
              <el-icon class="upload-icon"><Camera /></el-icon>
              <span class="upload-text">更换头像</span>
            </div>
          </div>
        </div>
        <div class="info-content">
          <div class="info-header">
            <h2 class="user-name">{{ userInfo.username }}</h2>
            <p class="user-role">{{ roleText }}</p>
          </div>
          <div class="info-grid">
            <div class="info-item">
              <span class="info-label">用户名</span>
              <span class="info-value">{{ userInfo.username }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">邮箱</span>
              <span class="info-value">{{ userInfo.email }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">电话</span>
              <span class="info-value">{{ profileForm.phone || '未设置' }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">地址</span>
              <span class="info-value">{{ profileForm.address || '未设置' }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">人脸识别</span>
              <span class="info-value">
                <el-tag v-if="userInfo.faceVerified" type="success">已认证</el-tag>
                <el-button v-else type="primary" @click="openFaceDialog">上传人脸</el-button>
              </span>
            </div>
          </div>
        </div>
      </div>
    </el-card>

    <el-dialog v-model="showEditDialog" title="修改个人信息" width="500px">
      <el-form :model="profileForm" :rules="profileRules" label-width="100px" ref="profileFormRef">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="profileForm.username" placeholder="请输入用户名" disabled />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="profileForm.email" placeholder="请输入邮箱" disabled />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="profileForm.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="profileForm.address" placeholder="请输入地址" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showEditDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSaveProfile" :loading="saving">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="showAvatarDialog" title="更换头像" width="480px" :close-on-click-modal="false">
      <div class="avatar-upload-container">
        <el-upload
          ref="uploadRef"
          class="avatar-uploader"
          :show-file-list="false"
          :before-upload="beforeUpload"
          :on-change="handleFileChange"
          :auto-upload="false"
          accept="image/*"
        >
          <div v-if="previewUrl" class="avatar-preview">
            <img :src="previewUrl" alt="预览" />
            <div class="preview-actions">
              <el-icon class="change-icon" @click.stop="triggerUpload"><Camera /></el-icon>
            </div>
          </div>
          <div v-else class="avatar-upload-placeholder">
            <el-icon class="upload-placeholder-icon"><Plus /></el-icon>
            <div class="upload-placeholder-text">点击或拖拽上传</div>
            <div class="upload-placeholder-hint">支持 JPG、PNG、GIF 格式，不超过 512KB</div>
          </div>
        </el-upload>
        <div v-if="previewUrl" class="upload-tips">
          <el-icon><InfoFilled /></el-icon>
          <span>点击图片可重新选择</span>
        </div>
      </div>
      <template #footer>
        <el-button @click="closeAvatarDialog">取消</el-button>
        <el-button type="primary" @click="handleUploadAvatar" :loading="uploading">确认上传</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="showFaceDialog" title="上传人脸" width="480px" :close-on-click-modal="false">
      <div class="avatar-upload-container">
        <el-upload
          ref="faceUploadRef"
          class="avatar-uploader"
          :show-file-list="false"
          :before-upload="beforeFaceUpload"
          :on-change="handleFaceFileChange"
          :auto-upload="false"
          accept="image/*"
        >
          <div v-if="facePreviewUrl" class="avatar-preview">
            <img :src="facePreviewUrl" alt="人脸预览" />
            <div class="preview-actions">
              <el-icon class="change-icon" @click.stop="triggerFaceUpload"><Camera /></el-icon>
            </div>
          </div>
          <div v-else class="avatar-upload-placeholder">
            <el-icon class="upload-placeholder-icon"><Plus /></el-icon>
            <div class="upload-placeholder-text">点击或拖拽上传人脸照片</div>
            <div class="upload-placeholder-hint">请上传清晰的正脸照片，光线充足</div>
          </div>
        </el-upload>
        <div v-if="facePreviewUrl" class="upload-tips">
          <el-icon><InfoFilled /></el-icon>
          <span>点击图片可重新选择</span>
        </div>
      </div>
      <template #footer>
        <el-button @click="closeFaceDialog">取消</el-button>
        <el-button type="primary" @click="handleUploadFace" :loading="uploadingFace">确认上传</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Camera, Plus, InfoFilled } from '@element-plus/icons-vue'
import { profileApi } from '@/api/profile'
import { useAuthStore } from '@/stores/auth'

defineOptions({
  name: 'AdminUserProfile'
})

const authStore = useAuthStore()
const saving = ref(false)
const uploading = ref(false)
const uploadingFace = ref(false)
const profileFormRef = ref(null)
const uploadRef = ref(null)
const faceUploadRef = ref(null)
const showEditDialog = ref(false)
const showAvatarDialog = ref(false)
const showFaceDialog = ref(false)
const previewUrl = ref('')
const facePreviewUrl = ref('')
const selectedFile = ref(null)
const selectedFaceFile = ref(null)

const userInfo = computed(() => authStore.userInfo)

const roleText = computed(() => {
  const role = userInfo.value.role?.toUpperCase() || ''
  const roleMap = {
    'ADMIN': '管理员',
    'RESIDENT': '居民',
    'COLLECTOR': '收集员'
  }
  return roleMap[role] || '未知'
})

const profileForm = reactive({
  username: '',
  email: '',
  phone: '',
  address: ''
})

const profileRules = {
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^[0-9-\s()]{6,20}$/, message: '请输入正确的电话号码', trigger: 'blur' }
  ],
  address: [
    { required: true, message: '请输入地址', trigger: 'blur' }
  ]
}

const loadProfile = async () => {
  try {
    const response = await profileApi.getProfile()
    profileForm.username = response.username || ''
    profileForm.email = response.email || ''
    profileForm.phone = response.phone || ''
    profileForm.address = response.address || ''
  } catch (error) {
    ElMessage.error('加载个人信息失败')
  }
}

const openAvatarDialog = () => {
  showAvatarDialog.value = true
  previewUrl.value = ''
  selectedFile.value = null
}

const closeAvatarDialog = () => {
  showAvatarDialog.value = false
  previewUrl.value = ''
  selectedFile.value = null
}

const triggerUpload = () => {
  uploadRef.value?.$el.querySelector('input[type="file"]').click()
}

const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt512K = file.size / 1024 < 512

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt512K) {
    ElMessage.error('图片大小不能超过512KB!')
    return false
  }
  return false
}

const handleFileChange = (file) => {
  const reader = new FileReader()
  reader.onload = (e) => {
    previewUrl.value = e.target.result
  }
  reader.readAsDataURL(file.raw)
  selectedFile.value = file.raw
}

const handleUploadAvatar = async () => {
  if (!selectedFile.value) {
    ElMessage.warning('请先选择图片')
    return
  }

  try {
    uploading.value = true
    const uploadResponse = await profileApi.uploadAvatar(selectedFile.value)
    
    await profileApi.updateAvatar(uploadResponse.url)
    
    authStore.updateUserInfo({ avatar: uploadResponse.url })
    
    ElMessage.success('头像上传成功')
    closeAvatarDialog()
  } catch (error) {
    ElMessage.error('头像上传失败')
  } finally {
    uploading.value = false
  }
}

const handleSaveProfile = async () => {
  try {
    await profileFormRef.value.validate()
    saving.value = true
    
    const profileData = {
      phone: profileForm.phone,
      address: profileForm.address
    }
    
    await profileApi.updateProfile(profileData)
    ElMessage.success('保存成功')
    showEditDialog.value = false
  } catch (error) {
    if (error !== false) {
      ElMessage.error('保存失败')
    }
  } finally {
    saving.value = false
  }
}

const openFaceDialog = () => {
  showFaceDialog.value = true
  facePreviewUrl.value = ''
  selectedFaceFile.value = null
}

const closeFaceDialog = () => {
  showFaceDialog.value = false
  facePreviewUrl.value = ''
  selectedFaceFile.value = null
}

const triggerFaceUpload = () => {
  faceUploadRef.value?.$el.querySelector('input[type="file"]').click()
}

const beforeFaceUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt512K = file.size / 1024 < 512

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt512K) {
    ElMessage.error('图片大小不能超过512KB!')
    return false
  }
  return false
}

const handleFaceFileChange = (file) => {
  const reader = new FileReader()
  reader.onload = (e) => {
    facePreviewUrl.value = e.target.result
  }
  reader.readAsDataURL(file.raw)
  selectedFaceFile.value = file.raw
}

const handleUploadFace = async () => {
  if (!selectedFaceFile.value) {
    ElMessage.warning('请先选择人脸照片')
    return
  }

  try {
    uploadingFace.value = true
    await profileApi.registerFaceFromFile(selectedFaceFile.value)
    
    authStore.updateUserInfo({ faceVerified: true })
    
    ElMessage.success('人脸注册成功')
    closeFaceDialog()
  } catch (error) {
    ElMessage.error('人脸注册失败')
  } finally {
    uploadingFace.value = false
  }
}

onMounted(() => {
  loadProfile()
})
</script>

<style scoped>
.admin-page {
  padding: 0;
}

.profile-info {
  display: flex;
  align-items: flex-start;
  gap: 32px;
  padding: 20px;
}

.avatar-container {
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.avatar-wrapper {
  position: relative;
  display: inline-block;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.avatar-wrapper:hover {
  transform: scale(1.05);
}

.profile-avatar {
  border: 3px solid #e4e7ed;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.avatar-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.6);
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: all 0.3s ease;
  gap: 4px;
}

.avatar-wrapper:hover .avatar-overlay {
  opacity: 1;
}

.upload-icon {
  font-size: 28px;
  color: #fff;
}

.upload-text {
  font-size: 12px;
  color: #fff;
  font-weight: 500;
}

.info-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.info-header {
  padding-bottom: 16px;
  border-bottom: 1px solid #e4e7ed;
}

.user-name {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 8px 0;
}

.user-role {
  font-size: 14px;
  color: #606266;
  margin: 0;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 16px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
  padding: 12px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.info-label {
  font-weight: 500;
  color: #606266;
  font-size: 13px;
}

.info-value {
  color: #303133;
  font-size: 14px;
  font-weight: 500;
}

.avatar-upload-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.avatar-uploader {
  width: 100%;
}

.avatar-uploader :deep(.el-upload) {
  width: 100%;
  border: 2px dashed #d9d9d9;
  border-radius: 8px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
  background-color: #fafafa;
}

.avatar-uploader :deep(.el-upload:hover) {
  border-color: #409eff;
  background-color: #f0f7ff;
}

.avatar-preview {
  width: 100%;
  height: 320px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
  border-radius: 6px;
}

.avatar-preview img {
  width: 100%;
  height: 100%;
  object-fit: contain;
  background-color: #f5f5f5;
}

.preview-actions {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.avatar-preview:hover .preview-actions {
  opacity: 1;
}

.change-icon {
  font-size: 32px;
  color: #fff;
  cursor: pointer;
}

.avatar-upload-placeholder {
  padding: 48px 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.upload-placeholder-icon {
  font-size: 48px;
  color: #c0c4cc;
}

.upload-placeholder-text {
  font-size: 14px;
  color: #606266;
  font-weight: 500;
}

.upload-placeholder-hint {
  font-size: 12px;
  color: #909399;
}

.upload-tips {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  background-color: #ecf5ff;
  border: 1px solid #d9ecff;
  border-radius: 4px;
  color: #409eff;
  font-size: 13px;
}

@media (max-width: 768px) {
  .profile-info {
    flex-direction: column;
    align-items: center;
    text-align: center;
    padding: 20px;
  }
  
  .info-grid {
    grid-template-columns: 1fr;
  }
  
  .info-item {
    align-items: center;
  }
}
</style>
