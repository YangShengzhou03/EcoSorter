<template>
    <div class="home">
        <nav class="nav">
            <div class="nav-content">
                <div class="logo">EcoSorter</div>
                <div class="nav-buttons">
                    <el-button @click="$router.push('/login')">登录</el-button>
                    <el-button type="primary" @click="$router.push('/register')">注册</el-button>
                </div>
            </div>
        </nav>

        <section class="hero">
            <div class="hero-content">
                <h1 class="hero-title">智能垃圾分类系统</h1>
                <p class="hero-desc">基于深度学习技术，一键拍照识别垃圾类型</p>
                <div class="hero-actions">
                    <el-button type="primary" size="large" @click="$router.push('/register')">立即开始</el-button>
                    <el-button size="large" @click="$router.push('/login')">了解更多</el-button>
                </div>
            </div>
        </section>

        <section class="features">
            <h2 class="section-title">核心功能</h2>
            <div class="feature-list">
                <div class="feature-item">
                    <div class="feature-icon">
                        <el-icon><Camera /></el-icon>
                    </div>
                    <h3>智能识别</h3>
                    <p>基于深度学习快速识别垃圾类型</p>
                </div>
                <div class="feature-item">
                    <div class="feature-icon">
                        <el-icon><DataAnalysis /></el-icon>
                    </div>
                    <h3>数据统计</h3>
                    <p>详细的分类记录和统计分析</p>
                </div>
                <div class="feature-item">
                    <div class="feature-icon">
                        <el-icon><Upload /></el-icon>
                    </div>
                    <h3>批量处理</h3>
                    <p>支持多图同时上传识别</p>
                </div>
                <div class="feature-item">
                    <div class="feature-icon">
                        <el-icon><Coin /></el-icon>
                    </div>
                    <h3>积分奖励</h3>
                    <p>正确分类可获得积分奖励</p>
                </div>
            </div>
        </section>

        <footer class="footer">
            <p>&copy; 2026 EcoSorter. 智能垃圾分类系统</p>
        </footer>
    </div>
</template>

<script setup>
import { Camera, DataAnalysis, Upload, Coin } from '@element-plus/icons-vue'
</script>

<style scoped>
.home {
    min-height: 100vh;
    background: #ffffff;
}

.nav {
    padding: 0 24px;
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    z-index: 100;
    background: #ffffff;
    border-bottom: 1px solid #e5e7eb;
}

.nav-content {
    max-width: 1200px;
    margin: 0 auto;
    height: 64px;
    display: flex;
    align-items: center;
    justify-content: space-between;
}

.logo {
    font-size: 20px;
    font-weight: 600;
    color: #1f2937;
}

.nav-buttons {
    display: flex;
    gap: 12px;
}

.hero {
    padding: 120px 24px 80px;
    background: #f9fafb;
    text-align: center;
}

.hero-content {
    max-width: 600px;
    margin: 0 auto;
}

.hero-title {
    font-size: 48px;
    font-weight: 600;
    margin-bottom: 16px;
    color: #1f2937;
}

.hero-desc {
    font-size: 18px;
    margin-bottom: 32px;
    line-height: 1.5;
    color: #6b7280;
}

.hero-actions {
    display: flex;
    gap: 16px;
    justify-content: center;
}

.features {
    padding: 80px 24px;
    background: #ffffff;
}

.section-title {
    font-size: 32px;
    font-weight: 600;
    color: #1f2937;
    margin-bottom: 48px;
    text-align: center;
}

.feature-list {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 24px;
    max-width: 1200px;
    margin: 0 auto;
}

.feature-item {
    padding: 24px;
    background: #f9fafb;
    border-radius: 8px;
    border: 1px solid #e5e7eb;
    text-align: center;
}

.feature-icon {
    width: 64px;
    height: 64px;
    border-radius: 50%;
    background: #10b981;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 auto 20px;
}

.feature-icon .el-icon {
    font-size: 32px;
    color: white;
}

.feature-item h3 {
    font-size: 18px;
    font-weight: 600;
    color: #1f2937;
    margin: 0 0 12px;
}

.feature-item p {
    font-size: 14px;
    color: #6b7280;
    line-height: 1.5;
    margin: 0;
}

.footer {
    padding: 24px;
    text-align: center;
    background: #1f2937;
    color: white;
}

.footer p {
    margin: 0;
    font-size: 14px;
}

@media (max-width: 1024px) {
    .feature-list {
        grid-template-columns: repeat(2, 1fr);
    }
}

@media (max-width: 768px) {
    .hero-title {
        font-size: 32px;
    }

    .hero-desc {
        font-size: 16px;
    }

    .feature-list {
        grid-template-columns: 1fr;
    }

    .hero-actions {
        flex-direction: column;
    }
}
</style>
