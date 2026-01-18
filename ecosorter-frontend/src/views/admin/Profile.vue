<template>
  <div class="page-container">
    <el-card class="profile-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">管理员个人信息</span>
        </div>
      </template>

      <el-form 
        ref="profileFormRef" 
        :model="profileForm" 
        :rules="profileRules" 
        label-width="100px"
        class="profile-form"
      >
        <el-form-item label="用户名">
          <el-input v-model="userInfo.username" disabled />
        </el-form-item>

        <el-form-item label="邮箱">
          <el-input v-model="userInfo.email" disabled />
        </el-form-item>

        <el-form-item label="角色">
          <el-input v-model="roleText" disabled />
        </el-form-item>

        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="profileForm.phone" placeholder="请输入联系电话" />
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
import { profileApi } from '@/api/profile'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
const saving = ref(false)
const changingPassword = ref(false)
const profileFormRef = ref(null)
const passwordFormRef = ref(null)
const showPasswordDialog = ref(false)

const userInfo = computed(() => authStore.userInfo)

const roleText = computed(() => {
  const roleMap = {
    'ADMIN': '管理员',
    'RESIDENT': '居民',
    'COLLECTOR': '收集员',
    'TRASHCAN': '垃圾桶'
  }
  return roleMap[userInfo.value.role] || '未知'
})

const profileForm = reactive({
  phone: ''
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const profileRules = {
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
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
    profileForm.phone = response.phone || ''
  } catch (error) {
    console.error('加载个人信息失败:', error)
    ElMessage.error('加载个人信息失败')
  }
}

const handleSaveProfile = async () => {
  try {
    await profileFormRef.value.validate()
    saving.value = true
    
    const profileData = {
      phone: profileForm.phone
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
</style>
