<template>
    <div class="home">
        <header class="header" :class="{ 'header-scrolled': isScrolled }">
            <div class="header-content">
                <div class="header-left">
                    <div class="logo">
                        <span class="logo-text">ECO-Sorter</span>
                    </div>
                    <nav class="nav-links">
                        <a href="#" class="nav-link" @click.prevent="router.push('/index/recyclable')">垃圾分类指南</a>
                        <a href="#" class="nav-link" @click.prevent="router.push('/index/booking')">回收服务</a>
                        <a href="#" class="nav-link" @click.prevent="router.push('/resident/points')">积分商城</a>
                        <a href="#" class="nav-link" @click.prevent="router.push('/index/about')">环保资讯</a>
                    </nav>
                </div>
                <div class="header-right">
                    <template v-if="authStore.isAuthenticated">
                        <div class="user-info" @click="handleUserClick">
                            <el-avatar :size="32" :src="authStore.userInfo?.avatar || ''">{{ authStore.userInfo?.username?.charAt(0) || 'U' }}</el-avatar>
                            <span class="username">{{ authStore.userInfo?.username || '用户' }}</span>
                        </div>
                    </template>
                    <template v-else>
                        <a href="#" class="login-btn" @click.prevent="handleLogin">登录</a>
                        <span class="divider">|</span>
                        <a href="#" class="register-btn" @click.prevent="handleRegister">注册</a>
                    </template>
                </div>
            </div>
        </header>

        <section class="hero-banner">
            <h1 class="hero-title">ECO-Sorter</h1>
            <p class="hero-subtitle">杨圣洲毕业设计</p>
            <a href="#" class="learn-more" @click.prevent="handleLearnMore">了解详情</a>
        </section>

        <footer class="footer">
            <div class="footer-content">
                <div class="footer-top">
                    <div class="footer-nav">
                        <div class="footer-nav-column">
                            <h4 class="footer-nav-title">垃圾分类</h4>
                            <ul class="footer-nav-list">
                                <li><a href="#" @click.prevent="router.push('/index/recyclable')">可回收物</a></li>
                                <li><a href="#" @click.prevent="router.push('/index/hazardous')">有害垃圾</a></li>
                                <li><a href="#" @click.prevent="router.push('/index/kitchen')">厨余垃圾</a></li>
                                <li><a href="#" @click.prevent="router.push('/index/other')">其他垃圾</a></li>
                            </ul>
                        </div>
                        <div class="footer-nav-column">
                            <h4 class="footer-nav-title">服务中心</h4>
                            <ul class="footer-nav-list">
                                <li><a href="#" @click.prevent="router.push('/index/booking')">回收预约</a></li>
                                <li><a href="#" @click.prevent="router.push('/resident/complaint')">投诉建议</a></li>
                                <li><a href="#" @click.prevent="router.push('/resident/points')">积分兑换</a></li>
                                <li><a href="#" @click.prevent="router.push('/index/order-query')">订单查询</a></li>
                            </ul>
                        </div>
                        <div class="footer-nav-column">
                            <h4 class="footer-nav-title">关于我们</h4>
                            <ul class="footer-nav-list">
                                <li><a href="#" @click.prevent="router.push('/index/about')">了解ECO</a></li>
                                <li><a href="#" @click.prevent="router.push('/index/join')">加入我们</a></li>
                                <li><a href="#" @click.prevent="router.push('/index/contact')">联系我们</a></li>
                            </ul>
                        </div>
                        <div class="footer-nav-column">
                            <h4 class="footer-nav-title">关注我们</h4>
                            <ul class="footer-nav-list">
                                <li><a href="#" @click.prevent="router.push('/index/contact')">官方微博</a></li>
                                <li><a href="#" @click.prevent="router.push('/index/contact')">官方微信</a></li>
                                <li><a href="#" @click.prevent="router.push('/index/contact')">联系我们</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="footer-bottom">
                    <div class="footer-info">
                        <div class="footer-logo">
                            <span class="logo-text">ECO-Sorter</span>
                        </div>
                        <div class="footer-copyright">
                                    <p>江西易控环保科技有限公司</p>
                                    <p>赣ICP备2025075576号 赣公网安备36010802001254号</p>
                                </div>
                    </div>
                    <div class="footer-back-to-top">
                        <button class="back-to-top-btn" @click="handleBackToTop">↑</button>
                    </div>
                </div>
            </div>
        </footer>
    </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores/auth'

defineOptions({
  name: 'IndexPage'
})

const router = useRouter()
const authStore = useAuthStore()
const isScrolled = ref(false)

const handleScroll = () => {
    isScrolled.value = window.scrollY > 100
}

const handleLogin = () => {
    router.push('/login')
}

const handleRegister = () => {
    router.push('/register')
}

const handleUserClick = () => {
    const role = authStore.userRole
    if (role === 'ADMIN') {
        router.push('/admin/dashboard')
    } else if (role === 'COLLECTOR') {
        router.push('/collector/dashboard')
    } else {
        router.push('/resident/dashboard')
    }
}

const handleLearnMore = () => {
    router.push('/index/about')
}

const handleBackToTop = () => {
    window.scrollTo({ top: 0, behavior: 'smooth' })
}

onMounted(() => {
    window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
    window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
.home {
    min-height: 100vh;
    background: #ffffff;
}

/* 顶部导航栏 */
.header {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    z-index: 1000;
    background: transparent;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.header-scrolled {
    background: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(10px);
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.header-content {
    width: 100%;
    margin: 0 auto;
    padding: 0 32px;
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: space-between;
}

.header-left {
    display: flex;
    align-items: center;
    gap: 48px;
}

.logo {
    font-size: 24px;
    font-weight: 700;
    color: #ff6700;
    transition: all 0.3s ease;
}

.logo-text {
    font-family: 'Arial', sans-serif;
    letter-spacing: -1px;
}

.nav-links {
    display: flex;
    gap: 24px;
    align-items: center;
}

.nav-link {
    color: #ffffff;
    text-decoration: none;
    font-size: 14px;
    font-weight: 500;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    padding: 8px 0;
    position: relative;
}

.nav-link::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 50%;
    transform: translateX(-50%);
    width: 0;
    height: 2px;
    background: #ff6700;
    transition: width 0.3s ease;
}

.nav-link:hover::after {
    width: 80%;
}

.header-scrolled .nav-link {
    color: #333333;
}

.header-scrolled .nav-link:hover {
    color: #ff6700;
}

.header-right {
    display: flex;
    align-items: center;
    gap: 16px;
}

.user-info {
    display: flex;
    align-items: center;
    gap: 8px;
    cursor: pointer;
    padding: 4px 8px;
    border-radius: 4px;
    transition: all 0.3s ease;
}

.user-info:hover {
    background: rgba(255, 255, 255, 0.1);
}

.header-scrolled .user-info:hover {
    background: rgba(255, 103, 0, 0.1);
}

.username {
    color: #ffffff;
    font-size: 14px;
    font-weight: 500;
}

.header-scrolled .username {
    color: #333333;
}

.login-btn,
.register-btn {
    color: #ffffff;
    text-decoration: none;
    font-size: 14px;
    transition: all 0.3s ease;
    padding: 6px 12px;
    border-radius: 4px;
}

.login-btn:hover,
.register-btn:hover {
    background: rgba(255, 255, 255, 0.1);
}

.header-scrolled .login-btn,
.header-scrolled .register-btn {
    color: #333333;
}

.header-scrolled .login-btn:hover,
.header-scrolled .register-btn:hover {
    color: #ff6700;
    background: rgba(255, 103, 0, 0.1);
}

.divider {
    color: #ffffff;
    opacity: 0.6;
    transition: all 0.3s ease;
}

.header-scrolled .divider {
    color: #333333;
    opacity: 0.4;
}

/* 主横幅 */
.hero-banner {
    position: relative;
    height: 100vh;
    min-height: 600px;
    background: #1a1a1a;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    overflow: hidden;
}

.hero-banner::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-image: url('../assets/index/hero.jpg');
    background-size: cover;
    background-position: center;
    opacity: 0.8;
    animation: parallax 20s ease-in-out infinite;
}

@keyframes parallax {
    0%, 100% {
        transform: scale(1);
    }
    50% {
        transform: scale(1.05);
    }
}

.hero-banner > * {
    position: relative;
    z-index: 1;
    text-align: center;
    color: #ffffff;
}

@keyframes fadeInUp {
    from {
        opacity: 0;
    }
    to {
        opacity: 1;
    }
}

.hero-title {
    font-size: 80px;
    margin: 0 0 8px;
    letter-spacing: 8px;
    text-shadow: 0 4px 12px rgba(0, 0, 0, 0.5);
    animation: scaleIn 1.2s ease-out;
}

@keyframes scaleIn {
    from {
        opacity: 0;
        transform: scale(0.8);
    }
    to {
        opacity: 1;
        transform: scale(1);
    }
}

.hero-subtitle {
    font-size: 36px;
    margin: 0 0 32px;
    opacity: 0.9;
    animation: fadeInUp 1s ease-out 0.2s both;
}

.learn-more {
    display: inline-block;
    padding: 12px 32px;
    border: 1px solid rgba(255, 255, 255, 0.3);
    border-radius: 24px;
    color: #ffffff;
    text-decoration: none;
    font-size: 14px;
    font-weight: 500;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    animation: fadeInUp 1s ease-out 0.4s both;
    backdrop-filter: blur(10px);
}

.learn-more:hover {
    background: rgba(255, 255, 255, 0.1);
    border-color: rgba(255, 255, 255, 0.6);
    box-shadow: 0 4px 12px rgba(255, 255, 255, 0.2);
}

.footer {
    background: #f9f9f9;
    color: #333333;
    transition: all 0.3s ease;
}

.footer:hover {
    background: #f5f5f5;
}

.footer-content {
    max-width: 1400px;
    margin: 0 auto;
    padding: 0 32px;
}

.footer-top {
    padding: 48px 0;
    border-bottom: 1px solid #e5e5e5;
}

.footer-nav {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 48px;
}

.footer-nav-column h4 {
    font-size: 14px;
    font-weight: 600;
    margin: 0 0 16px;
    color: #333333;
    transition: all 0.3s ease;
    position: relative;
    padding-bottom: 8px;
}

.footer-nav-column h4::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 0;
    width: 20px;
    height: 2px;
    background: #ff6700;
    transition: width 0.3s ease;
}

.footer-nav-column:hover h4::after {
    width: 40px;
}

.footer-nav-list {
    list-style: none;
    padding: 0;
    margin: 0;
}

.footer-nav-list li {
    margin-bottom: 12px;
    transition: transform 0.3s ease;
}

.footer-nav-list li:hover {
    transform: translateX(4px);
}

.footer-nav-list a {
    text-decoration: none;
    color: #666666;
    font-size: 12px;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    position: relative;
    padding: 4px 0;
}

.footer-nav-list a::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 0;
    width: 0;
    height: 1px;
    background: #ff6700;
    transition: width 0.3s ease;
}

.footer-nav-list a:hover {
    color: #ff6700;
}

.footer-nav-list a:hover::after {
    width: 100%;
}

.footer-bottom {
    padding: 32px 0;
}

.footer-info {
    display: flex;
    align-items: flex-start;
    gap: 24px;
    margin-bottom: 16px;
}

.footer-logo {
    font-size: 20px;
    font-weight: 700;
    color: #ff6700;
    margin-top: 4px;
}

.footer-copyright {
    flex: 1;
}

.footer-copyright p {
    font-size: 12px;
    color: #999999;
    margin: 0 0 8px;
    line-height: 1.4;
    transition: color 0.3s ease;
}

.footer-copyright:hover p {
    color: #666666;
}

.footer-back-to-top {
    position: fixed;
    bottom: 32px;
    right: 32px;
    z-index: 999;
    opacity: 0;
    transform: translateY(20px);
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.footer-back-to-top.visible {
    opacity: 1;
    transform: translateY(0);
}

.back-to-top-btn {
    width: 40px;
    height: 40px;
    background: rgba(0, 0, 0, 0.5);
    color: white;
    border: none;
    border-radius: 50%;
    font-size: 16px;
    cursor: pointer;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    position: relative;
    overflow: hidden;
}

.back-to-top-btn:hover {
    background: rgba(255, 103, 0, 0.8);
    scale: 1.1;
    box-shadow: 0 4px 16px rgba(255, 103, 0, 0.4);
}

@media (max-width: 1200px) {
    .header-content {
        padding: 0 24px;
    }
    
    .nav-links {
        gap: 16px;
    }
    
    .footer-nav {
        grid-template-columns: repeat(2, 1fr);
        gap: 32px;
    }
}

@media (max-width: 768px) {
    .header {
        background: rgba(0, 0, 0, 0.9);
    }
    
    .header-left {
        gap: 24px;
    }
    
    .nav-links {
        display: none;
    }
    
    .footer-content {
        padding: 0 20px;
    }
    
    .footer-nav {
        grid-template-columns: 1fr;
        gap: 24px;
    }
    
    .footer-info {
        flex-direction: column;
        align-items: flex-start;
        gap: 16px;
    }
}
</style>
