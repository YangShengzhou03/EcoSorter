<template>
  <div class="join-page">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">加入我们</h1>
        <p class="page-subtitle">Join Our Team</p>
      </div>
    </div>

    <div class="page-content">
      <div class="section-card">
        <h2 class="section-title">热招职位</h2>
        <div class="jobs-list">
          <div class="job-card" v-for="job in jobs" :key="job.id">
            <div class="job-header">
              <div class="job-title">{{ job.title }}</div>
              <el-tag :type="job.type === 'fulltime' ? 'primary' : 'success'">
                {{ job.type === 'fulltime' ? '全职' : '实习' }}
              </el-tag>
            </div>
            <div class="job-info">
              <div class="info-item">
                <span class="info-label">工作地点:</span>
                <span class="info-value">{{ job.location }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">薪资范围:</span>
                <span class="info-value">{{ job.salary }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">学历要求:</span>
                <span class="info-value">{{ job.education }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">经验要求:</span>
                <span class="info-value">{{ job.experience }}</span>
              </div>
            </div>
            <div class="job-description">
              <h4>岗位职责</h4>
              <ul>
                <li v-for="duty in job.duties" :key="duty">{{ duty }}</li>
              </ul>
              <h4>任职要求</h4>
              <ul>
                <li v-for="requirement in job.requirements" :key="requirement">{{ requirement }}</li>
              </ul>
            </div>
            <div class="job-actions">
              <el-button type="primary" @click="applyJob(job)">立即申请</el-button>
            </div>
          </div>
        </div>
      </div>

      <div class="section-card">
        <h2 class="section-title">联系我们</h2>
        <div class="contact-info">
          <div class="contact-item">
            <div class="contact-label">简历投递邮箱</div>
            <div class="contact-value">hr@ecosorter.com</div>
          </div>
          <div class="contact-item">
            <div class="contact-label">联系电话</div>
            <div class="contact-value">0791-88888888</div>
          </div>
          <div class="contact-item">
            <div class="contact-label">公司地址</div>
            <div class="contact-value">江西省南昌市高新区XX路XX号</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

defineOptions({
  name: 'IndexJoinPage'
})

const jobs = ref([
  {
    id: 1,
    title: '前端开发工程师',
    type: 'fulltime',
    location: '南昌',
    salary: '10K-18K',
    education: '本科及以上',
    experience: '1-3年',
    duties: [
      '负责公司产品前端开发工作',
      '参与产品需求分析和技术方案设计',
      '优化前端性能，提升用户体验',
      '与后端团队协作，完成接口对接'
    ],
    requirements: [
      '熟练掌握Vue.js/React等前端框架',
      '熟悉HTML/CSS/JavaScript等前端技术',
      '有良好的代码风格和编程习惯',
      '具备良好的沟通能力和团队协作精神'
    ]
  },
  {
    id: 2,
    title: '后端开发工程师',
    type: 'fulltime',
    location: '南昌',
    salary: '12K-20K',
    education: '本科及以上',
    experience: '2-4年',
    duties: [
      '负责公司产品后端开发工作',
      '设计并实现高可用、高性能的系统架构',
      '参与数据库设计和优化',
      '编写技术文档，进行代码审查'
    ],
    requirements: [
      '熟练掌握Java/Python等后端开发语言',
      '熟悉Spring Boot等主流开发框架',
      '熟悉MySQL、Redis等数据库技术',
      '具备良好的问题分析和解决能力'
    ]
  },
  {
    id: 3,
    title: '产品经理',
    type: 'fulltime',
    location: '南昌',
    salary: '15K-25K',
    education: '本科及以上',
    experience: '3-5年',
    duties: [
      '负责产品规划、需求分析和产品设计',
      '撰写产品文档，制定产品路线图',
      '协调各部门资源，推动产品落地',
      '分析产品数据，持续优化产品体验'
    ],
    requirements: [
      '具备3年以上产品经理工作经验',
      '有环保或互联网产品经验者优先',
      '具备良好的沟通协调能力',
      '有较强的数据分析能力'
    ]
  },
  {
    id: 4,
    title: 'UI设计师',
    type: 'fulltime',
    location: '南昌',
    salary: '8K-15K',
    education: '本科及以上',
    experience: '1-3年',
    duties: [
      '负责公司产品UI设计工作',
      '参与产品用户体验设计',
      '制定设计规范，维护设计系统',
      '与开发团队协作，确保设计落地'
    ],
    requirements: [
      '熟练使用Figma、Sketch等设计工具',
      '具备良好的审美能力和设计思维',
      '了解前端开发流程者优先',
      '有良好的沟通能力和团队协作精神'
    ]
  }
])

const applyJob = (job) => {
  ElMessage.info(`请将简历发送至 hr@ecosorter.com，邮件标题注明：应聘${job.title}`)
}
</script>

<style scoped>
.join-page {
  min-height: calc(100vh - 60px);
  background: #f5f5f5;
  padding-bottom: 40px;
}

.page-header {
  background: #1a1a1a;
  padding: 32px 32px 24px;
  text-align: center;
  color: white;
}

.header-content {
  margin: 0 auto;
}

.page-title {
  font-size: 28px;
  margin: 0 0 6px;
  font-weight: 600;
}

.page-subtitle {
  font-size: 14px;
  margin: 0;
  opacity: 0.9;
}

.page-content {
  margin: 16px auto 0;
  padding: 0 32px;
}

.section-card {
  background: white;
  border-radius: 12px;
  padding: 16px;
  border: 1px solid #e0e0e0;
  margin-bottom: 16px;
}

.section-title {
  font-size: 16px;
  margin: 0 0 12px;
  color: #333;
  font-weight: 600;
}

.jobs-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.job-card {
  background: #f9f9f9;
  border-radius: 4px;
  padding: 14px;
  border: 1px solid #e0e0e0;
}

.job-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
  padding-bottom: 10px;
  border-bottom: 1px solid #e0e0e0;
}

.job-title {
  font-size: 15px;
  font-weight: 600;
  color: #333;
}

.job-info {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 6px;
  margin-bottom: 12px;
}

.info-item {
  display: flex;
  gap: 8px;
}

.info-label {
  font-size: 12px;
  color: #666;
  flex-shrink: 0;
}

.info-value {
  font-size: 12px;
  color: #333;
  font-weight: 500;
}

.job-description {
  margin-bottom: 12px;
}

.job-description h4 {
  font-size: 13px;
  margin: 10px 0 5px;
  color: #333;
  font-weight: 600;
}

.job-description ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.job-description li {
  padding: 3px 0;
  padding-left: 14px;
  position: relative;
  font-size: 12px;
  color: #555;
  line-height: 1.4;
}

.job-description li::before {
  content: '•';
  position: absolute;
  left: 0;
  color: #9b59b6;
  font-weight: bold;
}

.job-actions {
  display: flex;
  justify-content: flex-end;
}

.contact-info {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.contact-item {
  padding: 16px;
  background: #f9f9f9;
  border-radius: 4px;
  border: 1px solid #e0e0e0;
}

.contact-label {
  font-size: 13px;
  color: #666;
  margin-bottom: 4px;
}

.contact-value {
  font-size: 14px;
  color: #333;
  font-weight: 600;
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
    font-size: 16px;
  }

  .job-info {
    grid-template-columns: 1fr;
  }
}
</style>
