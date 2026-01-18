<template>
  <div class="page-container">
    <el-card class="profile-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">个人信息</span>
        </div>
      </template>

      <el-form 
        ref="profileFormRef" 
        :model="profileForm" 
        :rules="profileRules" 
        label-width="100px"
        class="profile-form"
      >
        <el-form-item label="头像">
          <div class="avatar-upload">
            <el-upload
              class="avatar-uploader"
              :show-file-list="false"
              :before-upload="beforeAvatarUpload"
              :http-request="uploadAvatar"
              accept="image/*"
            >
              <img v-if="avatarUrl" :src="avatarUrl" class="avatar" />
              <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            </el-upload>
            <div class="avatar-tip">支持 JPG、PNG 格式，文件大小不超过 2MB</div>
          </div>
        </el-form-item>

        <el-form-item label="用户名">
          <el-input v-model="userInfo.username" disabled />
        </el-form-item>

        <el-form-item label="邮箱">
          <el-input v-model="userInfo.email" disabled />
        </el-form-item>

        <el-form-item label="姓名" prop="fullName">
          <el-input v-model="profileForm.fullName" placeholder="请输入姓名" />
        </el-form-item>

        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="profileForm.phone" placeholder="请输入联系电话" />
        </el-form-item>

        <el-form-item label="收货地址" prop="address">
          <el-input
            v-model="profileForm.address"
            type="textarea"
            :rows="3"
            placeholder="请输入收货地址"
          />
        </el-form-item>

        <el-form-item label="修改密码">
          <el-button @click="showPasswordDialog = true">修改密码</el-button>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSaveProfile" :loading="saving">
            保存
          </el-button>
          <el-button @click="handleResetProfile">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-dialog v-model="showPasswordDialog" title="修改密码" width="400px">
      <el-form :model="passwordForm" :rules="passwordRules" label-width="100px" ref="passwordFormRef">
        <el-form-item label="原密码" prop="oldPassword">
          <el-input v-model="passwordForm.oldPassword" type="password" show-password placeholder="请输入原密码" />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="passwordForm.newPassword" type="password" show-password placeholder="请输入新密码" />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="passwordForm.confirmPassword" type="password" show-password placeholder="请再次输入新密码" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showPasswordDialog = false">取消</el-button>
        <el-button type="primary" @click="handleChangePassword" :loading="changingPassword">确认修改</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { profileApi } from '@/api/profile'
import { uploadApi } from '@/api/upload'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
const saving = ref(false)
const changingPassword = ref(false)
const uploadingAvatar = ref(false)
const profileFormRef = ref(null)
const passwordFormRef = ref(null)
const showPasswordDialog = ref(false)
const avatarUrl = ref('')

const userInfo = computed(() => authStore.userInfo)

const profileForm = reactive({
  fullName: '',
  phone: '',
  address: ''
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const profileRules = {
  fullName: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { max: 100, message: '姓名长度不能超过100个字符', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  address: [
    { required: true, message: '请输入收货地址', trigger: 'blur' },
    { max: 500, message: '地址长度不能超过500个字符', trigger: 'blur' }
  ]
}

const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入原密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const loadProfile = async () => {
  try {
    const response = await profileApi.getProfile()
    avatarUrl.value = response.avatar || ''
    Object.assign(profileForm, {
      fullName: response.fullName || '',
      phone: response.phone || '',
      address: response.address || ''
    })
  } catch (error) {
    console.error('加载个人信息失败:', error)
    ElMessage.error('加载个人信息失败')
  }
}

const beforeAvatarUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!')
    return false
  }
  return true
}

const uploadAvatar = async (options) => {
  const { file } = options
  try {
    uploadingAvatar.value = true
    const response = await uploadApi.uploadAvatar(file)
    avatarUrl.value = response.url
    await profileApi.updateAvatar(response.url)
    ElMessage.success('头像上传成功')
    authStore.updateUserInfo({ avatar: response.url })
  } catch (error) {
    console.error('上传头像失败:', error)
    ElMessage.error('上传头像失败')
  } finally {
    uploadingAvatar.value = false
  }
}

const handleSaveProfile = async () => {
  try {
    await profileFormRef.value.validate()
    saving.value = true
    
    const profileData = {
      fullName: profileForm.fullName,
      phone: profileForm.phone,
      address: profileForm.address
    }
    
    await profileApi.updateProfile(profileData)
    ElMessage.success('保存成功')
  } catch (error) {
    if (error !== false) {
      console.error('保存个人信息失败:', error)
      ElMessage.error('保存失败')
    }
  } finally {
    saving.value = false
  }
}

const handleResetProfile = () => {
  profileFormRef.value.resetFields()
  loadProfile()
}

const handleChangePassword = async () => {
  try {
    await passwordFormRef.value.validate()
    changingPassword.value = true
    
    await profileApi.changePassword({
      oldPassword: passwordForm.oldPassword,
      newPassword: passwordForm.newPassword
    })
    
    ElMessage.success('密码修改成功')
    showPasswordDialog.value = false
    passwordFormRef.value.resetFields()
  } catch (error) {
    if (error !== false) {
      console.error('修改密码失败:', error)
      ElMessage.error('修改密码失败')
    }
  } finally {
    changingPassword.value = false
  }
}

onMounted(() => {
  loadProfile()
})
</script>

<style scoped>
.page-container {
  padding: 0;
}

.profile-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.card-title {
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
}

.profile-form {
  padding: 20px;
}

.avatar-upload {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.avatar-uploader {
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-uploader :deep(.el-upload) {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: all 0.3s;
}

.avatar-uploader :deep(.el-upload:hover) {
  border-color: #409eff;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar {
  width: 100px;
  height: 100px;
  display: block;
  object-fit: cover;
}

.avatar-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}
</style>
