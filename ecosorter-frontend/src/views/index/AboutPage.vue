<template>
  <div class="about-page">
    <div class="page-header">
      <h1 class="page-title">了解ECO</h1>
      <p class="page-subtitle">About ECO-Sorter</p>
    </div>

    <div class="page-content">
      <div class="section-card">
        <h2 class="section-title">关于ECO-Sorter</h2>
        <div class="intro-content">
          <p>ECO-Sorter是一款智能垃圾分类督导系统，致力于通过人工智能和物联网技术，推动垃圾分类的智能化、便捷化和普及化。我们的目标是通过技术创新，让每个人都能轻松参与环保，共同建设绿色家园。</p>
        </div>
      </div>

      <div class="section-card">
        <h2 class="section-title">核心功能</h2>
        <div class="features-list">
          <div class="feature-item">
            <h3>智能识别</h3>
            <p>采用先进的图像识别技术，自动识别垃圾类型，准确率高达95%以上</p>
          </div>
          <div class="feature-item">
            <h3>积分激励</h3>
            <p>完善的积分奖励机制，鼓励用户积极参与垃圾分类</p>
          </div>
          <div class="feature-item">
            <h3>数据统计</h3>
            <p>实时统计投放数据，帮助用户了解自己的环保贡献</p>
          </div>
          <div class="feature-item">
            <h3>在线商城</h3>
            <p>积分兑换商城，让环保行为获得实实在在的回报</p>
          </div>
          <div class="feature-item">
            <h3>回收预约</h3>
            <p>便捷的上门回收服务，让大件物品处理更轻松</p>
          </div>
          <div class="feature-item">
            <h3>投诉建议</h3>
            <p>畅通的反馈渠道，持续优化用户体验</p>
          </div>
        </div>
      </div>

      <div class="section-card">
        <h2 class="section-title">开发团队</h2>
        <div class="team-content">
          <p>ECO-Sorter由江西易控环保科技有限公司开发，团队由一群热爱环保、精通技术的年轻人组成。我们相信，科技可以改变生活，环保可以从点滴做起。</p>
          <p>我们的愿景是：让垃圾分类成为一种生活方式，让绿色环保成为每个人的自觉行动。</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'

defineOptions({
  name: 'IndexAboutPage'
})

onMounted(() => {
  document.title = '了解ECO - ECO-Sorter'
})
</script>

<style scoped>
.about-page {
  min-height: calc(100vh - 60px);
  background: #f5f5f5;
  padding-bottom: 40px;
}

.page-header {
  background: #1a1a1a;
  padding: 40px 32px 30px;
  text-align: center;
  color: white;
}

.page-title {
  font-size: 32px;
  margin: 0 0 8px;
  font-weight: 600;
}

.page-subtitle {
  font-size: 16px;
  margin: 0;
  opacity: 0.9;
}

.page-content {
  margin: 20px auto 0;
  padding: 0 32px;
}

.section-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  border: 1px solid #e0e0e0;
  margin-bottom: 20px;
}

.section-title {
  font-size: 20px;
  margin: 0 0 16px;
  color: #333;
  font-weight: 600;
}

.intro-content p {
  font-size: 14px;
  line-height: 1.6;
  color: #666;
  margin: 0;
}

.features-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 16px;
}

.feature-item {
  padding: 16px;
  background: #f9f9f9;
  border-radius: 4px;
  border: 1px solid #e0e0e0;
}

.feature-item h3 {
  font-size: 16px;
  margin: 0 0 8px;
  color: #333;
  font-weight: 600;
}

.feature-item p {
  font-size: 13px;
  color: #666;
  margin: 0;
  line-height: 1.5;
}

.team-content p {
  font-size: 14px;
  line-height: 1.6;
  color: #666;
  margin: 0 0 12px 0;
}

.team-content p:last-child {
  margin-bottom: 0;
}

@media (max-width: 768px) {
  .page-header {
    padding: 40px 20px 30px;
  }

  .page-title {
    font-size: 28px;
  }

  .page-subtitle {
    font-size: 14px;
  }

  .page-content {
    margin: -10px auto 30px;
    padding: 0 20px;
  }

  .section-card {
    padding: 20px;
  }

  .section-title {
    font-size: 18px;
  }

  .features-list {
    grid-template-columns: 1fr;
  }
}
</style>
