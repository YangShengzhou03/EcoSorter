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
          <div class="avatar-wrapper" @click="showCropDialog = true">
            <el-avatar :size="100" :src="userInfo.avatar || ''" class="profile-avatar">
              {{ userInfo.username?.charAt(0)?.toUpperCase() }}
            </el-avatar>
            <div class="avatar-overlay">
              <el-icon class="upload-icon"><Edit /></el-icon>
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

    <VueImageCropUpload
      v-model="showCropDialog"
      :width="200"
      :height="200"
      :lang-type="'zh'"
      :img-format="'jpg'"
      :max-size="10240"
      @crop-success="handleCropSuccess"
      @crop-upload-fail="handleCropFail"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Edit } from '@element-plus/icons-vue'
import VueImageCropUpload from 'vue-image-crop-upload'
import 'vue-image-crop-upload/upload.css'
import { profileApi } from '@/api/profile'
import { useAuthStore } from '@/stores/auth'

defineOptions({
  name: 'AdminUserProfile'
})

const authStore = useAuthStore()
const saving = ref(false)
const profileFormRef = ref(null)
const showEditDialog = ref(false)
const showCropDialog = ref(false)

const userInfo = computed(() => authStore.userInfo)

const roleText = computed(() => {
  const role = userInfo.value.role?.toUpperCase() || ''
  const roleMap = {
    'ADMIN': '管理员',
    'RESIDENT': '居民',
    'COLLECTOR': '收集员',
    'TRASHCAN': '垃圾桶'
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

const handleCropSuccess = async (imgDataUrl) => {
  try {
    const response = await fetch(imgDataUrl)
    const blob = await response.blob()
    const file = new File([blob], 'avatar.jpg', { type: 'image/jpeg' })
    
    const uploadResponse = await profileApi.uploadAvatar(file)
    
    await profileApi.updateAvatar(uploadResponse.url)
    
    authStore.updateUserInfo({ avatar: uploadResponse.url })
    
    ElMessage.success('头像上传成功')
  } catch (error) {
    ElMessage.error('头像上传失败')
  }
}

const handleCropFail = () => {
  ElMessage.error('图片裁剪失败')
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
}

.profile-avatar {
  border: 2px solid #e4e7ed;
}

.avatar-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s;
}

.avatar-wrapper:hover .avatar-overlay {
  opacity: 1;
}

.upload-icon {
  font-size: 32px;
  color: #fff;
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
