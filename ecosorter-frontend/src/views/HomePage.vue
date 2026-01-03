<template>
    <div class="home">
        <nav class="nav">
            <div class="nav-content">
                <div class="logo">
                    <img src="@/assets/logo.svg" alt="EcoSorter" />
                    <span>EcoSorter</span>
                </div>
                <div class="nav-buttons">
                    <el-button @click="$router.push('/login')">登录</el-button>
                    <el-button type="primary" @click="$router.push('/register')">注册</el-button>
                </div>
            </div>
        </nav>

        <section class="hero">
            <h1 class="hero-title">
                <span class="title-highlight">智能</span>垃圾分类
            </h1>
            <p class="hero-desc">拍照识别垃圾类型，给出正确的分类建议</p>
        </section>

        <section class="features">
            <h2 class="section-title">功能介绍</h2>
            <div class="feature-list">
                <div class="feature-item">
                    <div class="feature-icon-wrapper">
                        <el-icon :size="36" color="var(--primary-color)">
                            <Camera />
                        </el-icon>
                    </div>
                    <h3>拍照识别</h3>
                    <p>垃圾图片识别返回分类结果</p>
                </div>
                <div class="feature-item">
                    <div class="feature-icon-wrapper">
                        <el-icon :size="36" color="var(--primary-color)">
                            <Histogram />
                        </el-icon>
                    </div>
                    <h3>分类记录</h3>
                    <p>查看历史识别记录和分类统计</p>
                </div>
                <div class="feature-item">
                    <div class="feature-icon-wrapper">
                        <el-icon :size="36" color="var(--primary-color)">
                            <Upload />
                        </el-icon>
                    </div>
                    <h3>批量上传</h3>
                    <p>支持多图同时识别，提高使用效率</p>
                </div>
                <div class="feature-item">
                    <div class="feature-icon-wrapper">
                        <el-icon :size="36" color="var(--primary-color)">
                            <Star />
                        </el-icon>
                    </div>
                    <h3>我的收藏</h3>
                    <p>收藏常用垃圾类型，方便快速查询</p>
                </div>
            </div>
        </section>

        <footer class="footer">
            <p>© 2025 EcoSorter</p>
        </footer>
    </div>
</template>

<script setup>
import { Camera, Histogram, Upload, Star } from '@element-plus/icons-vue'
</script>

<style scoped>
.home {
    min-height: 100vh;
    background: #f8faf9;
    padding-top: 56px;
}

:root {
    --primary-color: #10b981;
    --primary-light: #d1fae5;
    --primary-bg: #ecfdf5;
    --text-primary: #1f2937;
    --text-secondary: #6b7280;
    --text-muted: #9ca3af;
    --border-color: #e5e7eb;
}

.nav {
    padding: 0 16px;
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    z-index: 100;
}

.nav-content {
    max-width: 1200px;
    margin: 0 auto;
    height: 56px;
    display: flex;
    align-items: center;
    justify-content: space-between;
}

.logo {
    display: flex;
    align-items: center;
    gap: 8px;
}

.logo img {
    width: 32px;
    height: 32px;
}

.logo span {
    font-size: 18px;
    font-weight: 700;
    color: var(--primary-color);
}

.nav-buttons {
    display: flex;
    gap: 8px;
}

.nav-buttons .el-button {
    border-radius: 8px;
}

.hero {
    padding: 72px 20px 52px;
    text-align: center;
    max-width: 600px;
    margin: 0 auto;
}

.hero-title {
    font-size: 36px;
    font-weight: 700;
    color: var(--text-primary);
    margin-bottom: 10px;
    letter-spacing: -0.5px;
}

.title-highlight {
    color: var(--primary-color);
}

.hero-desc {
    font-size: 15px;
    color: var(--text-secondary);
    margin-bottom: 20px;
    line-height: 1.6;
}

.features {
    padding: 40px 20px;
    max-width: 1100px;
    margin: 0 auto;
}

.section-title {
    font-size: 22px;
    font-weight: 600;
    text-align: center;
    margin-bottom: 28px;
    color: var(--text-primary);
    letter-spacing: -0.3px;
}

.feature-list {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 14px;
}

.feature-item {
    padding: 18px 14px;
    border-radius: 8px;
    text-align: center;
    border: 1px solid var(--border-color);
}

.feature-icon-wrapper {
    width: 52px;
    height: 52px;
    margin: 0 auto 10px;
    background: var(--primary-bg);
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
}

.feature-item h3 {
    font-size: 15px;
    font-weight: 600;
    margin: 8px 0 5px;
    color: #374151;
}

.feature-item p {
    font-size: 12px;
    color: var(--text-secondary);
    line-height: 1.5;
}

.footer {
    padding: 20px 20px;
    text-align: center;
    color: var(--text-muted);
    font-size: 12px;
    background: #f9fafb;
    border-top: 1px solid #f3f4f6;
}

@media (max-width: 1024px) {
    .feature-list {
        grid-template-columns: repeat(2, 1fr);
    }
}

@media (max-width: 768px) {
    .home {
        padding-top: 52px;
    }

    .nav-content {
        height: 52px;
    }

    .logo img {
        width: 28px;
        height: 28px;
    }

    .logo span {
        font-size: 16px;
    }

    .hero {
        padding: 56px 20px 40px;
    }

    .hero-title {
        font-size: 26px;
    }

    .hero-desc {
        font-size: 14px;
    }

    .section-title {
        font-size: 18px;
    }

    .feature-list {
        grid-template-columns: 1fr;
        max-width: 360px;
        margin: 0 auto;
    }
}
</style>
