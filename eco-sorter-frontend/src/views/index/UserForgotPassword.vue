<template>
    <div class="login-container">
        <div class="login-card">
            <div class="page-title">忘记密码</div>
            <el-form ref="forgotFormRef" :model="forgotForm" :rules="forgotRules" class="forgot-form">
                <el-form-item prop="email">
                    <el-input v-model="forgotForm.email" placeholder="请输入注册邮箱" size="large">
                        <template #prefix>
                            <el-icon><Message /></el-icon>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" size="large" :loading="isLoading" class="forgot-button"
                        @click="handleSubmit">
                        发送重置邮件
                    </el-button>
                </el-form-item>
            </el-form>
            <div class="login-link">
                <span>想起密码了？</span>
                <a href="#" @click.prevent="$router.push('/login')">返回登录</a>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { Message } from '@element-plus/icons-vue';

defineOptions({
  name: 'UserForgotPassword'
})

const router = useRouter();
const forgotFormRef = ref();
const isLoading = ref(false);

const forgotForm = reactive({ email: "" });

const forgotRules = {
    email: [
        { required: true, message: "请输入邮箱", trigger: "blur" },
        { type: "email", message: "请输入正确的邮箱格式", trigger: "blur" }
    ]
};

const handleSubmit = async () => {
    try {
        await forgotFormRef.value.validate();
        isLoading.value = true;
        
        ElMessage.success("重置密码邮件已发送，请查收邮箱");
        
        setTimeout(() => {
            router.push('/login');
        }, 2000);
    } catch (error) {
        ElMessage.error(error.message || "发送失败，请重试");
    } finally {
        isLoading.value = false;
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

.forgot-form {
    margin-bottom: 24px;
}

.forgot-button {
    width: 100%;
    background: #ff6700;
    border: none;
}

.forgot-button:hover {
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
