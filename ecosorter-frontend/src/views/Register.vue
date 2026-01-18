<template>
    <div class="register-container">
        <div class="register-card">
            <div class="card-header">
                <h1>EcoSorter</h1>
            </div>
            <el-form :model="form" :rules="rules" ref="formRef" @submit.prevent="handleRegister" class="register-form">
                <el-form-item prop="username">
                    <el-input v-model="form.username" placeholder="用户名" size="large">
                        <template #prefix>
                            <el-icon><User /></el-icon>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item prop="email">
                    <el-input v-model="form.email" placeholder="邮箱" size="large">
                        <template #prefix>
                            <el-icon><Message /></el-icon>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input v-model="form.password" type="password" placeholder="设置密码" size="large" show-password>
                        <template #prefix>
                            <el-icon><Lock /></el-icon>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item prop="confirmPassword">
                    <el-input v-model="form.confirmPassword" type="password" placeholder="确认密码" size="large" show-password>
                        <template #prefix>
                            <el-icon><Lock /></el-icon>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" size="large" :loading="authStore.isLoading" class="register-button"
                        @click="handleRegister">
                        立即注册
                    </el-button>
                </el-form-item>
            </el-form>
            <div class="login-link">
                <span>已有账户？</span>
                <a href="#" @click.prevent="$router.push('/login')">立即登录</a>
            </div>
        </div>
    </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const authStore = useAuthStore()
const router = useRouter()
const formRef = ref()
const form = reactive({ username: '', email: '', password: '', confirmPassword: '' })

const validateConfirmPassword = (rule, value, callback) => {
    if (value !== form.password) {
        callback(new Error('两次输入的密码不一致'))
    } else {
        callback()
    }
}

const rules = {
    username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
    email: [
        { required: true, message: '请输入邮箱', trigger: 'blur' },
        { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
    ],
    password: [{ required: true, message: '请设置密码', trigger: 'blur' }],
    confirmPassword: [
        { required: true, message: '请确认密码', trigger: 'blur' },
        { validator: validateConfirmPassword, trigger: 'blur' }
    ]
}

const handleRegister = async () => {
    try {
        await formRef.value.validate()
        await authStore.register(form)
        ElMessage.success('注册成功')
        setTimeout(() => { router.push('/resident/dashboard') }, 1500)
    } catch (error) {
        ElMessage.error(error.message || '注册失败')
    }
}
</script>

<style scoped>
.register-container {
    min-height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
    background: #f5f7fa;
}

.register-card {
    width: 100%;
    max-width: 400px;
    background: white;
    padding: 40px;
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.card-header {
    text-align: center;
    margin-bottom: 32px;
}

.card-header h1 {
    font-size: 28px;
    color: #303133;
    margin-bottom: 8px;
}

.card-header .subtitle {
    color: #909399;
    font-size: 14px;
}

.register-form {
    margin-bottom: 24px;
}

.register-button {
    width: 100%;
}

.login-link {
    text-align: center;
    color: #909399;
    font-size: 14px;
}

.login-link a {
    color: #409eff;
    margin-left: 4px;
}
</style>
