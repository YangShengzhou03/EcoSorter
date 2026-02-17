<template>
    <div class="login-container">
        <div class="login-card">
            <div class="page-title">登录</div>
            <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" class="login-form">
                <el-form-item prop="email">
                    <el-input v-model="loginForm.email" placeholder="邮箱" size="large">
                        <template #prefix>
                            <el-icon><User /></el-icon>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input v-model="loginForm.password" type="password" placeholder="密码" size="large"
                        show-password @keyup.enter="handleLogin">
                        <template #prefix>
                            <el-icon><Lock /></el-icon>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" size="large" :loading="authStore.isLoading" class="login-button"
                        @click="handleLogin">
                        登录
                    </el-button>
                </el-form-item>
            </el-form>
            <div class="register-link">
                <span>还没有账户？</span>
                <a href="#" @click.prevent="$router.push('/register')">立即注册</a>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { useAuthStore } from "@/stores/auth";
import { User, Lock } from '@element-plus/icons-vue';

defineOptions({
  name: 'UserLogin'
})

const router = useRouter();
const authStore = useAuthStore();
const loginFormRef = ref();

const loginForm = reactive({ email: "", password: "" });

const loginRules = {
    email: [
        { required: true, message: "请输入邮箱", trigger: "blur" },
        { type: "email", message: "请输入正确的邮箱格式", trigger: "blur" }
    ],
    password: [{ required: true, message: "请输入密码", trigger: "blur" }]
};

const handleLogin = async () => {
    try {
        await loginFormRef.value.validate();
        const result = await authStore.login({
            email: loginForm.email,
            password: loginForm.password,
        });
        if (result.success) {
            const roleRedirects = {
                RESIDENT: "/resident/dashboard",
                COLLECTOR: "/collector/dashboard",
                ADMIN: "/admin/dashboard"
            };
            router.push(roleRedirects[result.role] || "/resident/dashboard");
        }
    } catch (error) {
        ElMessage.error(error.message || "登录失败");
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

.login-form {
    margin-bottom: 24px;
}

.login-button {
    width: 100%;
    background: #ff6700;
    border: none;
}

.login-button:hover {
    background: #ff8533;
}

.register-link {
    text-align: center;
    color: #666;
    font-size: 14px;
}

.register-link a {
    color: #ff6700;
    margin-left: 4px;
    text-decoration: none;
}

.register-link a:hover {
    text-decoration: underline;
}
</style>