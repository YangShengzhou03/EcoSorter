<template>
    <div class="login-container">
        <div class="login-left">
            <h2 class="brand-title">EcoSorter</h2>
            <p class="brand-desc">智能垃圾分类系统</p>
        </div>
        <div class="login-right">
            <div class="login-card">
                <div class="card-header">
                    <h1>欢迎回来</h1>
                    <p class="subtitle">登录您的账户</p>
                </div>
                <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" class="login-form"
                    @submit.prevent="handleLogin">
                    <el-form-item prop="username">
                        <el-input v-model="loginForm.username" placeholder="用户名" size="large">
                            <template #prefix>
                                <el-icon>
                                    <User />
                                </el-icon>
                            </template>
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input v-model="loginForm.password" type="password" placeholder="密码" size="large"
                            show-password @keyup.enter="handleLogin">
                            <template #prefix>
                                <el-icon>
                                    <Lock />
                                </el-icon>
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
                <div class="quick-login">
                    <p class="quick-login-title">快速登录</p>
                    <div class="quick-login-buttons">
                        <el-button type="info" size="small" @click="quickLogin('resident')" plain circle>
                            <el-icon>
                                <HomeFilled />
                            </el-icon>
                        </el-button>
                        <el-button type="success" size="small" @click="quickLogin('collector')" plain circle>
                            <el-icon>
                                <Van />
                            </el-icon>
                        </el-button>
                        <el-button type="warning" size="small" @click="quickLogin('admin')" plain circle>
                            <el-icon>
                                <Monitor />
                            </el-icon>
                        </el-button>
                        <el-button type="primary" size="small" @click="quickLogin('bin')" plain circle>
                            <el-icon>
                                <Box />
                            </el-icon>
                        </el-button>
                    </div>
                </div>
                <div class="register-link">
                    <span>还没有账户？</span>
                    <a href="#" @click.prevent="$router.push('/register')">立即注册</a>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { useAuthStore } from "@/stores/auth";
import { User, Lock, HomeFilled, Van, Monitor, Box } from '@element-plus/icons-vue';

const router = useRouter();
const authStore = useAuthStore();
const loginFormRef = ref();
const loginForm = reactive({ username: "", password: "" });
const loginRules = {
    username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
    password: [{ required: true, message: "请输入密码", trigger: "blur" }],
};

const handleLogin = async () => {
    try {
        await loginFormRef.value.validate();
        const result = await authStore.login({
            username: loginForm.username,
            password: loginForm.password,
        });
        if (result.success) {
            const roleRedirects = {
                resident: "/resident/dashboard",
                collector: "/collector/dashboard",
                admin: "/admin/dashboard",
                bin: "/bin/main",
            };
            router.push(roleRedirects[result.role]);
        } else {
            ElMessage.error(result.message || "登录失败");
        }
    } catch (error) {
    }
};

const quickLogin = async (role) => {
    try {
        const result = await authStore.login({
            username: role,
            password: '123456',
        });
        if (result.success) {
            const roleRedirects = {
                resident: "/resident/dashboard",
                collector: "/collector/dashboard",
                admin: "/admin/dashboard",
                bin: "/bin/main",
            };
            router.push(roleRedirects[result.role]);
        } else {
            ElMessage.error(result.message || "快速登录失败");
        }
    } catch (error) {
    }
};
</script>

<style scoped>
.login-container {
    min-height: 100vh;
    display: flex;
    background: #f0f9f4;
}

.login-left {
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

.login-right {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 60px;
}

.login-card {
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

.login-form {
    margin-bottom: 0;
}

.login-form :deep(.el-input__wrapper) {
    border-radius: 8px;
    padding: 4px 14px;
}

.login-form :deep(.el-input__inner) {
    height: 42px;
    font-size: 14px;
}

.login-button {
    width: 100%;
    height: 44px;
    font-size: 15px;
    font-weight: 500;
    border-radius: 8px;
    background: #10b981;
    border: none;
}

.quick-login {
    margin-top: 24px;
    text-align: center;
}

.quick-login-title {
    color: #9ca3af;
    font-size: 12px;
    margin-bottom: 12px;
}

.quick-login-buttons {
    display: flex;
    gap: 8px;
    justify-content: center;
}

.quick-login-buttons .el-button {
    width: 38px;
    height: 38px;
}

.register-link {
    text-align: center;
    margin-top: 24px;
    color: #6b7280;
    font-size: 13px;
}

.register-link a {
    color: #10b981;
    text-decoration: none;
    font-weight: 500;
    margin-left: 4px;
}

.register-link a:hover {
    text-decoration: underline;
}

@media (max-width: 1024px) {
    .login-left {
        display: none;
    }

    .login-right {
        padding: 40px 24px;
    }

    .login-card {
        padding: 32px 28px;
    }
}

@media (max-width: 480px) {
    .login-card {
        padding: 28px 20px;
    }

    .card-header h1 {
        font-size: 22px;
    }
}
</style>
