# EcoSorter - 智能垃圾分类督导系统

基于 Spring Boot + Vue.js 的智能垃圾分类管理系统

## 项目简介

EcoSorter 是一个基于 Spring Boot + Vue.js 技术栈的智能垃圾分类督导系统，提供完整的垃圾分类管理解决方案，包括用户管理、垃圾分类识别、数据统计分析等功能。

### 核心功能

- **用户认证** - 支持用户注册登录，多角色权限管理
- **垃圾分类识别** - 集成百度AI图像识别API，实现垃圾类型识别
- **数据统计** - 可视化图表展示分类统计
- **用户管理** - 多角色权限管理系统（管理员、回收员、居民、垃圾桶设备）
- **任务管理** - 垃圾收集任务调度和管理
- **投诉管理** - 居民投诉反馈系统
- **通知公告** - 系统通知和公告管理
- **积分系统** - 居民垃圾分类积分奖励

### 系统特色

- **现代化技术栈** - Spring Boot 3.x + Vue 3
- **安全可靠** - Spring Security + JWT认证
- **数据可视化** - Element Plus组件库
- **高性能** - 优化的构建配置

## 项目结构

```
EcoSorter/
├── ecosorter-backend/          # Spring Boot后端服务
│   ├── src/main/java/com/ecosorter/
│   │   ├── config/            # 配置类
│   │   ├── controller/         # 控制器
│   │   ├── dto/               # 数据传输对象
│   │   ├── exception/          # 异常处理
│   │   ├── model/             # 实体类
│   │   ├── repository/         # 数据访问层
│   │   ├── service/           # 业务逻辑层
│   │   └── EcoSorterApplication.java
│   ├── src/main/resources/
│   │   └── application.yml   # 应用配置
│   └── pom.xml              # Maven依赖
├── ecosorter-frontend/       # Vue.js前端应用
│   ├── src/
│   │   ├── api/              # API接口
│   │   ├── stores/           # 状态管理
│   │   ├── styles/           # 样式文件
│   │   ├── utils/            # 工具函数
│   │   ├── views/            # 页面组件
│   │   ├── App.vue           # 根组件
│   │   ├── main.js           # 入口文件
│   │   └── router.js         # 路由配置
│   ├── package.json          # 项目依赖
│   └── vue.config.js        # Vue CLI配置
├── UML代码/                 # UML设计文档
├── data.sql                 # 数据库初始化脚本
├── README.md                # 项目说明
└── README.en.md             # 英文项目说明
```

## 技术栈

### 前端技术

- **Vue.js 3.3.11** - 渐进式JavaScript框架
- **Vue Router 4.2.5** - 官方路由管理器
- **Pinia 2.1.7** - 状态管理库
- **Element Plus 2.4.4** - Vue 3组件库
- **Axios 1.6.2** - HTTP客户端

### 后端技术

- **Spring Boot 3.5.6** - 微服务框架
- **Spring Security** - 安全框架
- **Spring Data JPA** - 数据访问框架
- **MySQL** - 关系型数据库
- **Jakarta Validation** - 数据验证
- **Jackson** - JSON处理

### 开发工具

- **Node.js** - JavaScript运行时
- **Java 17** - 编程语言
- **Maven** - 项目构建工具
- **Vue CLI** - Vue项目脚手架

## 快速开始

### 环境要求

- Node.js >= 16.0.0
- npm >= 8.0.0
- Java >= 17
- Maven >= 3.6
- MySQL >= 8.0

### 后端部署

1. **配置数据库**

```sql
CREATE DATABASE ecosorter CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. **修改配置文件**

编辑 `ecosorter-backend/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ecosorter?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&characterEncoding=utf8
    username: root
    password: your_password

baidu:
  api:
    key: your_baidu_api_key
    secret: your_baidu_secret_key
```

3. **初始化数据库**

```bash
mysql -u root -p ecosorter < data.sql
```

4. **构建和运行**

```bash
cd ecosorter-backend
mvn clean install
mvn spring-boot:run
```

后端服务将在 `http://localhost:8081` 启动

### 前端部署

1. **安装依赖**

```bash
cd ecosorter-frontend
npm install
```

2. **开发环境**

```bash
npm run serve
```

访问 `http://localhost:8080`

3. **生产构建**

```bash
npm run build
```

构建产物将输出到 `dist` 目录

### 测试账户

系统提供以下测试账户：

- **管理员账户**: admin / 123456
- **回收员账户**: collector / 123456
- **居民账户**: resident / 123456
- **垃圾桶设备**: trashcan / 123456

## API文档

### 认证相关

- **POST** `/api/auth/register` - 用户注册
- **POST** `/api/auth/login` - 用户登录
- **POST** `/api/auth/logout` - 用户登出
- **POST** `/api/auth/refresh` - 刷新令牌
- **GET** `/api/auth/me` - 获取当前用户信息

### 用户管理

- **GET** `/api/users` - 获取用户列表
- **GET** `/api/users/{id}` - 获取用户详情
- **PUT** `/api/users/{id}` - 更新用户信息
- **DELETE** `/api/users/{id}` - 删除用户

### 垃圾分类

- **POST** `/api/classification/recognize` - 垃圾识别
- **GET** `/api/classification/history` - 获取分类历史

### 收集任务

- **GET** `/api/collection/tasks` - 获取收集任务列表
- **PUT** `/api/collection/tasks/{id}/complete` - 完成任务

### 投诉管理

- **POST** `/api/complaint` - 提交投诉
- **GET** `/api/complaint` - 获取投诉列表

### 通知公告

- **GET** `/api/notice` - 获取通知列表
- **POST** `/api/notice` - 创建通知

### 个人资料

- **GET** `/api/profile` - 获取个人资料
- **PUT** `/api/profile` - 更新个人资料

### 管理员功能

- **GET** `/api/admin/dashboard` - 管理员仪表板
- **GET** `/api/admin/users` - 用户管理
- **GET** `/api/admin/devices` - 设备管理
- **GET** `/api/admin/reports` - 数据报表
- **GET** `/api/admin/logs` - 系统日志

### 回收员功能

- **GET** `/api/collector/dashboard` - 回收员仪表板
- **GET** `/api/collector/tasks` - 任务列表
- **GET** `/api/collector/navigation` - 导航路线

### 垃圾桶设备

- **GET** `/api/bin/status` - 设备状态
- **POST** `/api/bin/upload` - 上传数据

## 界面预览

### 登录页面

简洁的登录界面，支持用户名密码登录，提供测试账号快速登录功能。

### 首页

展示系统介绍和核心功能，包括智能识别、数据统计、批量处理、积分奖励等。

### 居民端

- **仪表板** - 查看个人垃圾分类统计和积分
- **分类记录** - 查看垃圾分类历史记录
- **积分** - 查看积分详情和兑换记录
- **投诉** - 提交垃圾分类相关投诉
- **个人资料** - 管理个人信息

### 回收员端

- **仪表板** - 查看任务统计和工作进度
- **任务** - 查看和管理收集任务
- **导航** - 查看最优收集路线
- **收集** - 执行垃圾收集操作
- **记录** - 查看收集记录
- **设备状态** - 查看垃圾桶设备状态

### 管理员端

- **仪表板** - 查看系统整体数据统计
- **配置** - 系统参数配置
- **报表** - 查看各类数据报表
- **用户** - 用户管理
- **通知** - 发布系统通知
- **设备** - 设备管理
- **日志** - 查看系统日志

### 垃圾桶端

- **主界面** - 设备主界面，显示设备状态和操作按钮

## 配置说明

### 前端配置 (vue.config.js)

```javascript
module.exports = {
  devServer: {
    port: 8080,
    proxy: {
      '/api': {
        target: 'http://localhost:8081',
        changeOrigin: true
      }
    }
  }
}
```

### 后端配置 (application.yml)

```yaml
server:
  port: 8081

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ecosorter
    username: root
    password: your_password
  jpa:
    database-platform: org.hibernate.dialect.MySQLDialect
    hibernate:
      ddl-auto: none

baidu:
  api:
    key: your_baidu_api_key
    secret: your_baidu_secret_key
```

## 开发指南

### 代码规范

- 遵循Vue.js风格指南
- 使用ESLint进行代码检查
- 使用Prettier进行代码格式化

### 提交规范

```bash
# 格式: type(scope): description
git commit -m "feat(auth): 添加用户注册功能"
git commit -m "fix(ui): 修复登录页面样式问题"
git commit -m "docs(readme): 更新API文档"
```

### 测试

```bash
# 前端测试
npm run lint        # 代码检查
npm run lint:fix    # 自动修复

# 后端测试
mvn test            # 运行测试
mvn clean install   # 构建项目
```

## 系统架构

### 系统角色

系统包含四种角色：

1. **管理员 (ADMIN)** - 系统管理，用户管理，设备管理，数据统计
2. **回收员 (COLLECTOR)** - 垃圾收集任务管理，路线规划，设备监控
3. **居民 (RESIDENT)** - 垃圾分类识别，积分管理，投诉反馈
4. **垃圾桶设备 (TRASHCAN)** - 设备数据上传，状态上报

### 业务流程

1. **用户注册登录** - 用户通过注册或登录进入系统
2. **垃圾分类** - 居民上传垃圾图片进行识别
3. **积分奖励** - 正确分类获得积分奖励
4. **任务调度** - 系统自动生成收集任务
5. **任务执行** - 回收员查看并执行收集任务
6. **数据统计** - 管理员查看各类统计数据

## 数据库设计

### 主要数据表

- **users** - 用户表
- **user_profiles** - 用户资料表
- **classifications** - 垃圾分类记录表
- **collection_tasks** - 收集任务表
- **complaints** - 投诉表
- **notices** - 通知公告表
- **banners** - 横幅表
- **waste_categories** - 垃圾类别表
- **trashcan_data** - 垃圾桶数据表

## 常见问题

### 1. 后端启动失败

检查MySQL服务是否启动，数据库配置是否正确。

### 2. 前端无法访问后端

检查后端服务是否启动，端口是否正确，跨域配置是否正确。

### 3. 垃圾识别失败

检查百度API密钥是否配置正确，网络连接是否正常。

## 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/amazing-feature`)
3. 提交更改 (`git commit -m 'Add some amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 开启 Pull Request

## 开源协议

本项目基于 MIT 协议开源。

## 联系支持

- 邮箱: yangsz03@foxmail.com
- 问题反馈: GitHub Issues

---

如果这个项目对你有帮助，请给个 Star！让我们一起构建更好的智能垃圾分类系统！
