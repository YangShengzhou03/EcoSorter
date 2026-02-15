<template>
  <div class="admin-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
          <el-button type="primary" @click="showCreateDialog">添加用户</el-button>
        </div>
      </template>
      
      <el-table :data="users" style="width: 100%" border v-loading="loading" empty-text="暂无用户">
        <el-table-column prop="id" label="ID"/>
        <el-table-column prop="username" label="用户名"/>
        <el-table-column prop="email" label="邮箱"/>
        <el-table-column prop="role" label="角色">
          <template #default="{ row }">
            <el-tag :type="getRoleType(row.role)">{{ getRoleText(row.role) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.status === '正常' ? 'success' : 'danger'">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="points" label="积分" />
        <el-table-column label="操作" width="280">
          <template #default="{ row }">
            <el-button size="small" @click="editUser(row)">编辑</el-button>
            <el-button size="small" type="warning" @click="adjustPoints(row)">调整积分</el-button>
            <el-button size="small" type="danger" @click="deleteUser(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑用户' : '添加用户'" width="500px">
      <el-form :model="userForm" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!isEdit">
          <el-input v-model="userForm.password" type="password" />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="userForm.role" placeholder="请选择角色">
            <el-option label="居民" value="RESIDENT" />
            <el-option label="收集员" value="COLLECTOR" />
            <el-option label="管理员" value="ADMIN" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="userForm.status" active-text="启用" inactive-text="禁用" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitting">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="pointsDialogVisible" title="调整积分">
      <div class="points-info">
        <span>用户：</span>
        <strong>{{ currentUserName }}</strong>
      </div>
      <el-form :model="pointsForm" :rules="pointsRules" ref="pointsFormRef">
        <el-form-item label="积分变动" prop="points">
          <el-input-number 
            v-model="pointsForm.points" 
            :min="-10000" 
            :max="10000" 
            placeholder="正数为增加，负数为减少" 
          />
        </el-form-item>
        <el-form-item label="调整原因" prop="reason">
          <el-input v-model="pointsForm.reason" type="textarea" :rows="3" placeholder="请输入调整原因" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="pointsDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitPointsAdjustment" :loading="submitting">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminApi } from '@/api/admin'

defineOptions({
  name: 'AdminUsers'
})

const users = ref([])
const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)
const currentUserId = ref(null)
const pointsDialogVisible = ref(false)
const pointsFormRef = ref(null)
const currentUserName = ref('')

const userForm = ref({
  username: '',
  email: '',
  password: '',
  role: 'RESIDENT',
  status: true
})

const pointsForm = ref({
  points: 0,
  reason: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在3到20个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在6到20个字符', trigger: 'blur' }
  ],
  role: [
    { required: true, message: '请选择角色', trigger: 'change' }
  ]
}

const pointsRules = {
  points: [
    { required: true, message: '请输入积分变动', trigger: 'blur' }
  ],
  reason: [
    { required: true, message: '请输入调整原因', trigger: 'blur' },
    { min: 5, max: 200, message: '原因长度在5到200个字符', trigger: 'blur' }
  ]
}

const loadUsers = async () => {
  loading.value = true
  try {
    const response = await adminApi.getUsers()
    users.value = response || []
  } catch (error) {
    ElMessage.error('加载用户数据失败')
  } finally {
    loading.value = false
  }
}

const showCreateDialog = () => {
  isEdit.value = false
  currentUserId.value = null
  userForm.value = {
    username: '',
    email: '',
    password: '',
    role: 'RESIDENT',
    status: true
  }
  dialogVisible.value = true
}

const adjustPoints = (row) => {
  currentUserId.value = row.id
  currentUserName.value = row.username
  pointsForm.value = {
    points: 0,
    reason: ''
  }
  pointsDialogVisible.value = true
}

const submitPointsAdjustment = async () => {
  try {
    await pointsFormRef.value.validate()
    submitting.value = true
    
    await adminApi.adjustUserPoints(currentUserId.value, pointsForm.value)
    ElMessage.success('积分调整成功')
    pointsDialogVisible.value = false
    loadUsers()
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '调整积分失败')
  } finally {
    submitting.value = false
  }
}

const editUser = (row) => {
  isEdit.value = true
  currentUserId.value = row.id
  userForm.value = {
    username: row.username,
    email: row.email || '',
    password: '',
    role: row.role,
    status: row.status === '正常'
  }
  dialogVisible.value = true
}

const submitForm = async () => {
  try {
    await formRef.value.validate()
    submitting.value = true
    
    if (isEdit.value) {
      await adminApi.updateUser(currentUserId.value, {
        role: userForm.value.role,
        isActive: userForm.value.status
      })
      ElMessage.success('更新用户成功')
    } else {
      await adminApi.createUser({
        username: userForm.value.username,
        email: userForm.value.email,
        password: userForm.value.password
      })
      ElMessage.success('添加用户成功')
    }
    
    dialogVisible.value = false
    loadUsers()
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '操作失败')
  } finally {
    submitting.value = false
  }
}

const deleteUser = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该用户吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await adminApi.deleteUser(row.id)
    ElMessage.success('删除成功')
    loadUsers()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const getRoleType = (role) => {
  const typeMap = {
    'ADMIN': 'danger',
    'COLLECTOR': 'warning',
    'RESIDENT': 'success'
  }
  return typeMap[role] || 'info'
}

const getRoleText = (role) => {
  const textMap = {
    'ADMIN': '管理员',
    'COLLECTOR': '收集员',
    'RESIDENT': '居民'
  }
  return textMap[role] || role
}

onMounted(() => {
  loadUsers()
})
</script>

<style scoped>
.admin-page {
  padding: 0;
}

.points-info {
  padding: 12px;
  background: #f5f7fa;
  margin-bottom: 16px;
  font-size: 14px;
}

.points-info strong {
  color: #409eff;
  margin-left: 8px;
}
</style>
