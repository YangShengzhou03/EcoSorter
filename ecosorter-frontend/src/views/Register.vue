<template>
    <div class="register-container">
        <div class="register-left">
            <h2 class="brand-title">EcoSorter</h2>
            <p class="brand-desc">智能垃圾分类系统</p>
        </div>
        <div class="register-right">
            <div class="register-card">
                <div class="card-header">
                    <h1>创建账户</h1>
                    <p class="subtitle">注册成为新用户</p>
                </div>
                <el-form :model="form" :rules="rules" ref="formRef" @submit.prevent="handleRegister">
                    <el-form-item prop="username">
                        <el-input v-model="form.username" placeholder="用户名" size="large">
                            <template #prefix>
                                <el-icon>
                                    <User />
                                </el-icon>
                            </template>
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input v-model="form.password" type="password" placeholder="设置密码" size="large" show-password>
                            <template #prefix>
                                <el-icon>
                                    <Lock />
                                </el-icon>
                            </template>
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="confirmPassword">
                        <el-input v-model="form.confirmPassword" type="password" placeholder="确认密码" size="large"
                            show-password>
                            <template #prefix>
                                <el-icon>
                                    <Lock />
                                </el-icon>
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
    </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'

const authStore = useAuthStore()
const router = useRouter()
const formRef = ref()
const form = reactive({ username: '', password: '', confirmPassword: '', role: 'user' })

const validateConfirmPassword = (rule, value, callback) => {
    if (value !== form.password) {
        callback(new Error('两次输入的密码不一致'))
    } else {
        callback()
    }
}

const rules = {
    username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
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
    background: #f0f9f4;
}

.register-left {
    flex: 1;
    background: #10b981;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    padding: 60px;
    color: white;
}

.brand-title {
    font-size: 32px;
    font-weight: 700;
    margin-bottom: 12px;
}

.brand-desc {
    font-size: 18px;
    opacity: 0.9;
}

.register-right {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 60px;
}

.register-card {
    background: white;
    width: 100%;
    max-width: 420px;
    padding: 40px 32px;
    border-radius: 8px;
    border: 1px solid #e8eaed;
}

.card-header {
    text-align: center;
    margin-bottom: 28px;
}

.card-header h1 {
    font-size: 24px;
    color: #1f2937;
    font-weight: 600;
    margin-bottom: 6px;
}

.card-header .subtitle {
    color: #6b7280;
    font-size: 14px;
}

.register-card .el-form :deep(.el-input__wrapper) {
    border-radius: 8px;
    padding: 4px 14px;
}

.register-card .el-form :deep(.el-input__inner) {
    height: 42px;
    font-size: 14px;
}

.register-button {
    width: 100%;
    height: 44px;
    font-size: 15px;
    font-weight: 500;
    border-radius: 8px;
    background: #10b981;
    border: none;
}

.login-link {
    text-align: center;
    margin-top: 24px;
    color: #6b7280;
    font-size: 13px;
}

.login-link a {
    color: #10b981;
    text-decoration: none;
    font-weight: 500;
    margin-left: 4px;
}

.login-link a:hover {
    text-decoration: underline;
}

@media (max-width: 1024px) {
    .register-left {
        display: none;
    }

    .register-right {
        padding: 40px 24px;
    }

    .register-card {
        padding: 32px 28px;
    }
}

@media (max-width: 480px) {
    .register-card {
        padding: 28px 20px;
    }

    .card-header h1 {
        font-size: 22px;
    }
}
</style>
