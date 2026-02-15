<template>
    <div class="login-container">
        <div class="login-card">
            <div class="page-title">注册</div>
            <el-form ref="registerFormRef" :model="registerForm" :rules="registerRules" class="register-form">
                <el-form-item prop="username">
                    <el-input v-model="registerForm.username" placeholder="用户名" size="large">
                        <template #prefix>
                            <el-icon><User /></el-icon>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item prop="email">
                    <el-input v-model="registerForm.email" placeholder="邮箱" size="large">
                        <template #prefix>
                            <el-icon><Message /></el-icon>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input v-model="registerForm.password" type="password" placeholder="设置密码" size="large"
                        show-password>
                        <template #prefix>
                            <el-icon><Lock /></el-icon>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item prop="confirmPassword">
                    <el-input v-model="registerForm.confirmPassword" type="password" placeholder="确认密码" size="large"
                        show-password>
                        <template #prefix>
                            <el-icon><Lock /></el-icon>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" size="large" class="register-button"
                        @click="handleRegister">
                        注册
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
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import { User, Lock, Message } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';

defineOptions({
  name: 'UserRegister'
})

const router = useRouter();
const authStore = useAuthStore();
const registerFormRef = ref();
const registerForm = reactive({ username: "", email: "", password: "", confirmPassword: "" });

const registerRules = {
    username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
    email: [
        { required: true, message: "请输入邮箱", trigger: "blur" },
        { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
    ],
    password: [{ required: true, message: "请设置密码", trigger: "blur" }],
    confirmPassword: [
        { required: true, message: "请确认密码", trigger: "blur" },
        {
            validator: (rule, value, callback) => {
                if (value !== registerForm.password) {
                    callback(new Error('两次输入的密码不一致'));
                } else {
                    callback();
                }
            },
            trigger: 'blur'
        }
    ]
};

const handleRegister = async () => {
    try {
        await registerFormRef.value.validate();
        await authStore.register(registerForm);
        setTimeout(() => { router.push('/resident/dashboard'); }, 1500);
    } catch (error) {
        ElMessage.error(error.message || '注册失败，请重试');
    }
};
</script>

<style scoped>
.login-container {
    min-height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
    background: #f5f5f5;
}

.login-card {
    width: 400px;
    padding: 40px;
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.page-title {
    font-size: 24px;
    font-weight: 600;
    color: #333;
    text-align: center;
    margin-bottom: 32px;
}

.register-form {
    margin-bottom: 24px;
}

.register-button {
    width: 100%;
    background: #ff6700;
    border: none;
}

.register-button:hover {
    background: #ff8533;
}

.login-link {
    text-align: center;
    color: #666;
    font-size: 14px;
}

.login-link a {
    color: #ff6700;
    margin-left: 4px;
    text-decoration: none;
}

.login-link a:hover {
    text-decoration: underline;
}
</style>