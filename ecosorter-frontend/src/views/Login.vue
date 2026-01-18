<template>
    <div class="login-container">
        <div class="login-card">
            <div class="card-header">
                <h1>EcoSorter</h1>
                <p class="subtitle">智能垃圾分类督导系统</p>
            </div>
            <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" class="login-form"
                @submit.prevent="handleLogin">
                <el-form-item prop="identifier">
                    <el-input v-model="loginForm.identifier" placeholder="用户名" size="large">
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
            <div class="quick-login">
                <el-select v-model="selectedRole" placeholder="选择测试账号" size="default" @change="quickLogin" class="role-select">
                    <el-option label="居民账号" value="resident" />
                    <el-option label="回收员账号" value="collector" />
                    <el-option label="管理员账号" value="admin" />
                </el-select>
            </div>
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

const router = useRouter();
const authStore = useAuthStore();
const loginFormRef = ref();
const loginForm = reactive({ identifier: "", password: "" });
const selectedRole = ref("");
const loginRules = {
    identifier: [{ required: true, message: "请输入用户名", trigger: "blur" }],
    password: [{ required: true, message: "请输入密码", trigger: "blur" }],
};

const handleLogin = async () => {
    try {
        await loginFormRef.value.validate();
        const result = await authStore.login({
            identifier: loginForm.identifier,
            password: loginForm.password,
        });
        if (result.success) {
            const roleRedirects = {
                resident: "/resident/dashboard",
                collector: "/collector/dashboard",
                admin: "/admin/dashboard",
                bin: "/bin/main",
            };
            router.push(roleRedirects[result.role] || "/resident/dashboard");
        }
    } catch (error) {
    }
};

const quickLogin = (role) => {
    const credentials = {
        resident: { identifier: "resident", password: "123456" },
        collector: { identifier: "collector", password: "123456" },
        admin: { identifier: "admin", password: "123456" },
        trashcan: { identifier: "trashcan", password: "123456" },
    };
    
    const creds = credentials[role];
    if (creds) {
        loginForm.identifier = creds.identifier;
        loginForm.password = creds.password;
        ElMessage.success("已填充测试账号");
    }
};
</script>

<style scoped>
.login-container {
    min-height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
    background: #f9fafb;
}

.login-card {
    width: 100%;
    max-width: 400px;
    background: white;
    padding: 40px;
    border-radius: 8px;
    border: 1px solid #e5e7eb;
}

.card-header {
    text-align: center;
    margin-bottom: 32px;
}

.card-header h1 {
    font-size: 28px;
    color: #1f2937;
    margin-bottom: 8px;
}

.card-header .subtitle {
    color: #6b7280;
    font-size: 14px;
}

.login-form {
    margin-bottom: 24px;
}

.login-button {
    width: 100%;
}

.quick-login {
    margin-bottom: 24px;
}

.role-select {
    width: 100%;
}

.register-link {
    text-align: center;
    color: #6b7280;
    font-size: 14px;
}

.register-link a {
    color: #10b981;
    margin-left: 4px;
}
</style>
