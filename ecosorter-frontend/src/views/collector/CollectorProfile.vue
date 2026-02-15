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
          <el-avatar :size="100" :src="userInfo.avatar || ''" class="profile-avatar">
            {{ userInfo.username?.charAt(0)?.toUpperCase() }}
          </el-avatar>
        </div>
        <div class="info-content">
          <div class="info-header">
            <h2 class="user-name">{{ profileForm.name || userInfo.username }}</h2>
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
              <span class="info-label">姓名</span>
              <span class="info-value">{{ profileForm.name || '未设置' }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">部门</span>
              <span class="info-value">{{ profileForm.department || '未设置' }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">职位</span>
              <span class="info-value">{{ profileForm.position || '未设置' }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">联系电话</span>
              <span class="info-value">{{ profileForm.phone || '未设置' }}</span>
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { profileApi } from '@/api/profile'
import { useAuthStore } from '@/stores/auth'

defineOptions({
  name: 'AdminUserProfile'
})

const authStore = useAuthStore()
const saving = ref(false)
const profileFormRef = ref(null)
const showEditDialog = ref(false)

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
  gap: 24px;
  padding: 16px;
}

.avatar-container {
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.profile-avatar {
  border: 2px solid #e4e7ed;
}

.info-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.info-header {
  padding-bottom: 12px;
  border-bottom: 1px solid #e4e7ed;
}

.user-name {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 6px 0;
}

.user-role {
  font-size: 13px;
  color: #606266;
  margin: 0;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 12px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 3px;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.info-label {
  font-weight: 500;
  color: #606266;
  font-size: 12px;
}

.info-value {
  color: #303133;
  font-size: 13px;
  font-weight: 500;
}

@media (max-width: 768px) {
  .profile-info {
    flex-direction: column;
    align-items: center;
    text-align: center;
    padding: 16px;
  }
  
  .info-grid {
    grid-template-columns: 1fr;
  }
  
  .info-item {
    align-items: center;
  }
}
</style>
