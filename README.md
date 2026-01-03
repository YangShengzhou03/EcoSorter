<div align="center">
  <h1>🎯 EcoSorter - 智能垃圾分类督导系统</h1>
  
  <p>
    <em>基于 Spring Boot + Vue.js 的智能垃圾分类管理系统</em>
  </p>

  <div>
    <a href="https://github.com/YangShengzhou03/EcoSorter/stargazers">
      <img src="https://img.shields.io/github/stars/YangShengzhou03/EcoSorter?style=for-the-badge&logo=github&color=ffd33d&labelColor=000000" alt="GitHub Stars">
    </a>
    <a href="https://github.com/YangShengzhou03/EcoSorter/forks">
      <img src="https://img.shields.io/github/forks/YangShengzhou03/EcoSorter?style=for-the-badge&logo=github&color=green&labelColor=000000" alt="GitHub Forks">
    </a>
    <a href="https://opensource.org/licenses/MIT">
      <img src="https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge&logo=open-source-initiative&color=blue&labelColor=000000" alt="MIT License">
    </a>
    <a href="https://github.com/YangShengzhou03/EcoSorter/issues">
      <img src="https://img.shields.io/github/issues/YangShengzhou03/EcoSorter?style=for-the-badge&logo=github&color=purple&labelColor=000000" alt="GitHub Issues">
    </a>
  </div>

  <div>
    <a href="https://spring.io/projects/spring-boot">
      <img src="https://img.shields.io/badge/Spring%20Boot-3.2.1-green?style=for-the-badge&logo=springboot" alt="Spring Boot Version">
    </a>
    <a href="https://vuejs.org/">
      <img src="https://img.shields.io/badge/Vue.js-3.4.0-4FC08D?style=for-the-badge&logo=vue.js" alt="Vue.js Version">
    </a>
    <a href="https://www.mysql.com/">
      <img src="https://img.shields.io/badge/MySQL-8.0+-4479A1?style=for-the-badge&logo=mysql" alt="MySQL Version">
    </a>
    <a href="https://www.tensorflow.org/js">
      <img src="https://img.shields.io/badge/TensorFlow.js-4.15.0-FF6F00?style=for-the-badge&logo=tensorflow" alt="TensorFlow.js Version">
    </a>
  </div>

</div>

## 🌍 项目简介

**EcoSorter** 是一个基于 **Spring Boot + Vue.js** 技术栈的智能垃圾分类督导系统，提供完整的垃圾分类管理解决方案，包括用户管理、垃圾分类识别、数据统计分析等功能。

### ✨ 核心功能
- **🔐 用户认证** - JWT认证，支持用户注册登录
- **🤖 AI垃圾分类** - 基于TensorFlow.js的图像识别
- **📊 数据统计** - 可视化图表展示分类统计
- **👥 用户管理** - 多角色权限管理系统
- **📱 响应式设计** - 支持PC端和移动端访问

### 🌟 系统特色
- **⚡ 现代化技术栈** - Spring Boot 3.x + Vue 3 + TypeScript
- **🔒 安全可靠** - JWT认证 + Spring Security
- **📈 数据可视化** - Chart.js图表展示
- **🎨 优雅界面** - Element Plus组件库
- **🚀 高性能** - 优化的构建配置和缓存策略

## 📁 项目结构

当前项目包含以下核心模块：

```
EcoSorter/
├── 📁 frontend/                    # Vue.js前端应用 (当前实现)
│   ├── 📁 src/
│   │   ├── 📁 components/          # 可复用组件
│   │   ├── 📁 views/              # 页面组件
│   │   │   ├── 📄 Login.vue       # 登录页面
│   │   │   ├── 📄 Dashboard.vue   # 仪表板
│   │   │   ├── 📄 Classification.vue # 垃圾分类
│   │   │   ├── 📄 Statistics.vue   # 数据统计
│   │   │   └── 📄 Management.vue   # 用户管理
│   │   ├── 📁 router/             # 路由配置
│   │   ├── 📁 utils/              # 工具函数
│   │   │   ├── 📄 auth.js         # 认证相关
│   │   │   └── 📄 request.js      # HTTP请求封装
│   │   ├── 📁 assets/             # 静态资源
│   │   ├── 📁 App.vue             # 根组件
│   │   └── 📁 main.js             # 入口文件
│   ├── 📄 package.json            # 项目依赖
│   ├── 📄 vue.config.js           # Vue CLI配置
│   └── 📄 babel.config.js         # Babel配置
├── 📁 backend/                     # Spring Boot后端服务 (当前实现)
│   ├── 📁 src/main/java/com/ecosorter/
│   │   ├── 📁 entity/             # 实体类
│   │   │   └── 📄 User.java       # 用户实体
│   │   ├── 📁 controller/         # 控制器
│   │   ├── 📁 service/            # 业务逻辑
│   │   ├── 📁 repository/         # 数据访问层
│   │   ├── 📁 config/             # 配置类
│   │   ├── 📁 security/           # 安全配置
│   │   └── 📄 Application.java     # 启动类
│   ├── 📁 src/main/resources/
│   │   └── 📄 application.yml     # 应用配置
│   └── 📄 pom.xml                 # Maven依赖
├── 📁 UML代码/                     # UML设计文档
│   ├── 📄 用例图.txt
│   ├── 📄 类图.txt
│   ├── 📄 顺序图.txt
│   └── 📄 状态图.txt
├── 📁 模型图/                      # UML图表
│   ├── 📄 用例图.png
│   ├── 📄 类图.png
│   └── 📄 顺序图.png
└── 📁 原型/                        # 系统原型
    └── 📄 index.html
```

> 📌 **注意**: 根据原始设计文档，本系统规划了多端协同架构，包括安卓APP、微信小程序等移动端应用。当前仓库主要包含Web端前后端实现，移动端应用可能需要额外的开发工作或位于其他代码仓库中。

## 🛠️ 技术栈

### 🌐 前端技术 (当前实现)
- **Vue.js 3.4.0** - 渐进式JavaScript框架
- **Vue Router 4.2.5** - 官方路由管理器
- **Pinia 2.1.7** - 状态管理库
- **Element Plus 2.4.4** - Vue 3组件库
- **Chart.js 4.4.1** - 数据可视化图表库
- **TensorFlow.js 4.15.0** - 机器学习库
- **Axios 1.6.2** - HTTP客户端
- **Socket.io-client 4.7.4** - 实时通信

### ⚙️ 后端技术 (当前实现)
- **Spring Boot 3.2.1** - 微服务框架
- **Spring Security** - 安全框架
- **Spring Data JPA** - 数据访问框架
- **MySQL 8.0+** - 关系型数据库
- **JWT** - 身份认证
- **Fastjson2** - JSON处理
- **Lombok** - 代码简化工具

### 🎯 规划中的技术栈
根据UML设计文档，系统规划还包含：
- **📱 安卓APP**: Kotlin + Jetpack Compose
- **💬 微信小程序**: 微信原生开发框架  
- **⚙️ 后端服务**: NestJS + PostgreSQL (规划中)
- **🤖 AI服务**: TensorFlow Serving + OpenVINO

### 🔧 开发工具
- **Node.js 16+** - JavaScript运行时
- **Java 17** - 编程语言
- **Maven** - 项目构建工具
- **Vue CLI 5.0** - Vue项目脚手架

## 🚀 快速开始

### 📋 环境要求
- Node.js >= 16.0.0
- npm >= 8.0.0
- Java >= 17
- Maven >= 3.6
- MySQL >= 8.0

### 🔧 后端部署

1. **配置数据库**
   ```bash
   # 创建数据库
   CREATE DATABASE eco_sorter CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. **修改配置文件**
   ```yaml
   # backend/src/main/resources/application.yml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/eco_sorter?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
       username: your_username
       password: your_password
   
   jwt:
     secret: your_jwt_secret_key
   ```

3. **构建和运行**
   ```bash
   cd backend
   mvn clean install
   mvn spring-boot:run
   ```

### 📱 前端部署

1. **安装依赖**
   ```bash
   cd frontend
   npm install
   ```

2. **开发环境**
   ```bash
   npm run serve
   # 访问 http://localhost:3000
   ```

3. **生产构建**
   ```bash
   npm run build
   ```

### 🧪 测试账户
- 管理员账户: admin / admin123
- 用户账户: user / user123

## 📖 API文档

### 🔐 认证相关
- **POST** `/api/auth/login` - 用户登录
- **POST** `/api/auth/register` - 用户注册
- **POST** `/api/auth/logout` - 用户登出

### 👥 用户管理
- **GET** `/api/users` - 获取用户列表
- **GET** `/api/users/{id}` - 获取用户详情
- **PUT** `/api/users/{id}` - 更新用户信息
- **DELETE** `/api/users/{id}` - 删除用户

### 🗂️ 垃圾分类
- **POST** `/api/classification/predict` - 垃圾识别预测
- **GET** `/api/classification/history` - 获取分类历史
- **POST** `/api/classification/upload` - 上传垃圾图片

### 📊 数据统计
- **GET** `/api/statistics/overview` - 获取统计概览
- **GET** `/api/statistics/trend` - 获取趋势数据
- **GET** `/api/statistics/ranking` - 获取用户排名

## 🎨 界面预览

### 🔑 登录页面
- 简洁的登录界面，基于Element Plus组件
- 支持用户名密码登录
- 响应式设计适配移动端

### 📊 仪表板
- 数据统计概览
- Chart.js图表展示分类趋势
- 实时数据更新

### 🤖 垃圾分类
- TensorFlow.js图像识别
- 图片上传和实时分类
- 分类历史记录管理

### 👥 用户管理
- 用户列表展示
- 用户信息编辑
- 基于角色的权限管理

## 🔧 配置说明

### 前端配置 (vue.config.js)
```javascript
module.exports = {
  devServer: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8000',
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  }
}
```

### 后端配置 (application.yml)
```yaml
server:
  port: 8000
  servlet:
    context-path: /api

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/eco_sorter
    username: root
    password: your_password

jwt:
  secret: your_jwt_secret_key
  expiration: 604800000 # 7天
```

## 🧪 开发指南

### 🔍 代码规范
- 遵循Vue.js风格指南
- 使用ESLint进行代码检查
- 使用Prettier进行代码格式化

### 📝 提交规范
```bash
# 格式: type(scope): description
git commit -m "feat(auth): 添加用户注册功能"
git commit -m "fix(ui): 修复登录页面样式问题"
git commit -m "docs(readme): 更新API文档"
```

### 🧪 测试
```bash
# 前端测试
npm run lint        # 代码检查
npm run lint:fix    # 自动修复

# 后端测试
mvn test            # 运行测试
mvn clean install   # 构建项目
```

## 📚 文档资源

### 📄 UML设计文档
- **[用例图](模型图/用例图.png)** - 系统功能用例
- **[类图](模型图/类图.png)** - 系统类结构设计
- **[顺序图](模型图/顺序图.png)** - 核心业务流程
- **[状态图](模型图/状态图.png)** - 对象状态转换

### 📊 系统原型
- **[原型页面](原型/index.html)** - 系统交互原型

### 🔗 相关链接
- [Spring Boot文档](https://spring.io/projects/spring-boot)
- [Vue.js文档](https://vuejs.org/)
- [Element Plus文档](https://element-plus.org/)
- [TensorFlow.js文档](https://www.tensorflow.org/js)

## 🚀 系统扩展

### 📱 移动端开发计划
根据UML设计文档，系统规划包含以下移动端应用：

1. **� 安卓APP端**
   - 技术栈: Kotlin + Jetpack Compose
   - 功能: 居民垃圾分类、积分管理、NFC认证
   - 状态: 🔨 待开发

2. **💬 微信小程序**
   - 技术栈: 微信原生开发框架
   - 功能: 轻量级垃圾分类识别、相机API集成
   - 状态: 🔨 待开发

3. **🗑️ 垃圾桶Web端**
   - 技术栈: React + TensorFlow.js
   - 功能: 智能设备浏览器应用、摄像头集成
   - 状态: 🔨 待开发

### 🔧 后端服务扩展
- **⚙️ NestJS后端**: 基于TypeScript的高性能后端框架
- **🗄️ PostgreSQL**: 更强大的关系型数据库支持
- **🤖 AI推理服务**: TensorFlow Serving + OpenVINO优化

## �🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/amazing-feature`)
3. 提交更改 (`git commit -m 'Add some amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 开启 Pull Request

### 🎯 当前开发重点
- ✅ Web端前后端基础功能
- 🔄 完善垃圾分类AI识别
- 🔨 开发移动端应用
- 📈 增强数据统计分析

## 📝 开源协议

本项目基于 [MIT](LICENSE) 协议开源。

```
MIT License

Copyright (c) 2025 EcoSorter

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
```

## 📞 联系支持

- 📧 邮箱: yangsz03@foxmail.com
- 🐛 问题反馈: [GitHub Issues](https://github.com/YangShengzhou03/EcoSorter/issues)
- 💬 讨论区: [GitHub Discussions](https://github.com/YangShengzhou03/EcoSorter/discussions)

---

<div align="center">
  <p>⭐ 如果这个项目对你有帮助，请给个 Star！</p>
  <p>🚀 让我们一起构建更好的智能垃圾分类系统！</p>
  <p>📱 移动端应用开发中，敬请期待...</p>
</div>