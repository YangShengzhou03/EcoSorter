# ECO-SORTER 智能垃圾分类系统

<div align="center">

[![GitHub stars](https://img.shields.io/github/stars/YangShengzhou03/EcoSorter?style=for-the-badge&logo=github)](https://github.com/YangShengzhou03/EcoSorter/stargazers)&nbsp;[![GitHub forks](https://img.shields.io/github/forks/YangShengzhou03/EcoSorter?style=for-the-badge&logo=github)](https://github.com/YangShengzhou03/EcoSorter/network/members)&nbsp;[![GitHub issues](https://img.shields.io/github/issues/YangShengzhou03/EcoSorter?style=for-the-badge&logo=github)](https://github.com/YangShengzhou03/EcoSorter/issues)&nbsp;[![GitHub license](https://img.shields.io/github/license/YangShengzhou03/EcoSorter?style=for-the-badge)](https://github.com/YangShengzhou03/EcoSorter/blob/main/LICENSE)&nbsp;[![Vue.js](https://img.shields.io/badge/Vue.js-3.3.11-4FC08D?style=for-the-badge&logo=vue.js)](https://vuejs.org/)&nbsp;[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.0-6DB33F?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)&nbsp;[![Python](https://img.shields.io/badge/Python-3.8+-3776AB?style=for-the-badge&logo=python)](https://www.python.org/)

<div align="center">
  <h3>一个现代化的智能垃圾分类管理系统，采用前后端分离架构</h3>
  <p>基于AI识别的垃圾分类平台，连接居民、收集员、管理员和智能垃圾桶设备</p>
</div>

[快速开始](#快速开始) • [功能特性](#功能特性) • [技术架构](#技术架构) • [API文档](#api文档)

</div>

## 项目简介

ECO-SORTER 是一个智能垃圾分类管理系统，采用前后端分离架构，集成AI图像识别技术，实现垃圾自动分类。系统包含居民端、收集员端、管理后台和智能垃圾桶端四个子系统，提供完整的垃圾分类解决方案。

系统核心功能包括：
- AI智能识别垃圾类别（可回收物、有害垃圾、厨余垃圾、其他垃圾）
- 积分奖励机制，鼓励居民正确分类
- 智能垃圾桶设备管理
- 收集任务调度与管理
- 数据统计与分析

## 功能特性

### 居民端功能

**用户认证** - 注册登录、密码修改、个人信息管理

**垃圾分类** - 拍照识别垃圾类别、查看分类历史记录、获取分类指导

**积分系统** - 查看积分余额、积分明细、积分兑换商品

**商品兑换** - 浏览积分商城、兑换商品、查看订单状态

**回收预约** - 预约上门回收服务、查看预约记录、取消预约

**投诉申诉** - 对分类结果提出申诉、查看申诉处理进度

**通知公告** - 查看系统通知、公告信息

**个人中心** - 个人信息管理、修改密码、查看统计数据

### 收集员端功能

**任务管理** - 查看分配的收集任务、开始/完成任务、报告异常

**设备监控** - 查看垃圾桶设备状态、容量监控

**数据统计** - 查看个人工作统计、任务完成情况

**个人中心** - 个人信息管理、修改密码

### 管理后台功能

**数据看板** - 系统整体数据统计、图表展示

**用户管理** - 用户列表、用户信息管理、积分调整

**设备管理** - 垃圾桶设备管理、设备状态监控、设备添加/编辑/删除

**任务管理** - 收集任务管理、任务分配、异常处理

**商品管理** - 积分商城商品管理、商品上架/下架

**订单管理** - 兑换订单管理、订单状态更新

**轮播图管理** - 首页轮播图管理

**分类管理** - 垃圾类别管理、分类示例管理

**通知管理** - 系统通知发布、管理

**投诉管理** - 用户投诉处理、申诉审核

**报表统计** - 数据报表、统计分析

### 智能垃圾桶端功能

**设备初始化** - 设备激活、设备信息配置

**用户登录** - 手机验证码登录、扫码登录、NFC登录、人脸识别、游客模式

**垃圾识别** - 摄像头拍照、AI识别垃圾类别、投放指导

**工作状态** - 设备状态显示、轮播图展示

## 快速开始

### 环境要求

**开发环境**

- Java 17+（后端开发）
- Python 3.8+（AI识别服务）
- Node.js 16+（前端开发）
- MySQL 8.0+（数据库）
- Maven 3.6+（后端构建）

**生产环境**

- 服务器：Linux/Windows Server
- 内存：4GB+ RAM
- 存储：10GB+ 可用空间

### 环境配置

**Java 17**
```bash
# 下载并安装 Java 17
# 配置 JAVA_HOME 环境变量
# 将 bin 目录添加到 PATH
java -version
```

**Python 3.8+**
```bash
# 下载并安装 Python 3.8+
# 将 Scripts 目录添加到 PATH
python --version
```

**Node.js 16+**
```bash
# 下载并安装 Node.js 16+
node -v
npm -v
```

**MySQL 8.0**
```bash
# 下载并安装 MySQL 8.0
# 创建数据库并执行初始化脚本
mysql -u root -p < data.sql
```

### 安装部署

**1. 克隆项目**

```bash
git clone https://github.com/YangShengzhou03/EcoSorter.git
cd eco-sorter
```

**2. 数据库配置**

```sql
-- 创建数据库
CREATE DATABASE eco_sorter CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 导入数据
USE eco_sorter;
SOURCE data.sql;
```

**3. 后端部署（Java）**

编辑 `ecosorter-backend/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/eco_sorter?useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password

app:
  jwt:
    secret: ecosorter-jwt-secret-key-2024-change-in-production
    expires-ms: 86400000

server:
  port: 8081
```

启动后端服务：

```bash
cd ecosorter-backend
mvn clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

**4. AI识别服务部署（Python）**

编辑 `trashcan-backend/yolo_recognition.py` 中的数据库配置：

```python
DATABASE_CONFIG = {
    'host': 'localhost',
    'user': 'root',
    'password': 'your_password',
    'database': 'eco_sorter',
    'charset': 'utf8mb4'
}
```

安装依赖并启动：

```bash
cd trashcan-backend
pip install -r requirements.txt
python App.py
```

**5. 主系统前端部署**

```bash
cd ecosorter-frontend
npm install
npm run serve
```

访问地址：http://localhost:8080

**6. 智能垃圾桶端前端部署**

```bash
cd trashcan-frontend
npm install
npm run serve
```

访问地址：http://localhost:3000

## 默认账号

系统初始化时会创建默认用户供测试使用：

| 角色 | 邮箱 | 密码 |
| --- | --- | --- |
| 居民 | resident@qq.com | 123456 |
| 收集员 | collector@qq.com | 123456 |
| 管理员 | admin@qq.com | 123456 |
| 垃圾桶 | trashcan@qq.com | 123456 |

## 技术架构

### 系统架构图

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   居民端前端     │    │   收集员端前端   │    │   管理后台前端   │
│                 │    │                 │    │                 │
│  Vue 3.3.11     │    │  Vue 3.3.11     │    │  Vue 3.3.11     │
│  Element Plus   │    │  Element Plus   │    │  Element Plus   │
│  Port: 8080     │    │  Port: 8080     │    │  Port: 8080     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                        │                        │
         └────────────────────────┼────────────────────────┘
                                  │
                                  ▼
                    ┌─────────────────────────┐
                    │    Spring Boot 后端     │
                    │                         │
                    │  Spring Boot 3.1.0     │
                    │  MyBatis Plus 3.5.7    │
                    │  Port: 8081             │
                    └─────────────────────────┘
                                  │
                    ┌─────────────┴─────────────┐
                    │                           │
                    ▼                           ▼
          ┌─────────────────┐         ┌─────────────────┐
          │   MySQL 数据库   │         │  AI识别服务      │
          │                 │         │                 │
          │  MySQL 8.0      │         │  Python 3.8+    │
          │  Port: 3306     │         │  FastAPI        │
          │                 │         │  Port: 9000      │
          └─────────────────┘         └─────────────────┘
                    │                           │
                    └─────────────┬─────────────┘
                                  │
                                  ▼
                    ┌─────────────────────────┐
                    │   智能垃圾桶前端       │
                    │                         │
                    │  Vue 3.3.11            │
                    │  Element Plus          │
                    │  Port: 3000            │
                    └─────────────────────────┘
```

### 技术栈详情

**居民端/收集员端/管理后台技术栈**

| 技术 | 版本 | 用途 |
| --- | --- | --- |
| Vue.js | 3.3.11 | 前端框架 |
| Element Plus | 2.4.4 | UI组件库 |
| Vue Router | 4.2.5 | 路由管理 |
| Pinia | 2.1.7 | 状态管理 |
| Axios | 1.6.2 | HTTP客户端 |

**后端技术栈（Java）**

| 技术 | 版本 | 用途 |
| --- | --- | --- |
| Spring Boot | 3.1.0 | Java企业级开发框架 |
| MyBatis Plus | 3.5.7 | 数据持久层框架 |
| MySQL | 8.0+ | 关系型数据库 |
| JWT | 0.11.5 | JSON Web Token认证 |
| Spring Security | 6.1.0 | 安全框架 |
| Aliyun OSS | 3.17.4 | 阿里云对象存储 |

**AI识别服务技术栈（Python）**

| 技术 | 版本 | 用途 |
| --- | --- | --- |
| Python | 3.8+ | 编程语言 |
| FastAPI | 0.104.1 | Web框架 |
| Uvicorn | 0.24.0 | ASGI服务器 |
| PyMySQL | 1.1.0 | MySQL驱动 |
| python-jose | 3.3.0 | JWT处理 |

**智能垃圾桶端技术栈**

| 技术 | 版本 | 用途 |
| --- | --- | --- |
| Vue.js | 3.3.11 | 前端框架 |
| Element Plus | 2.4.4 | UI组件库 |
| Vue Router | 4.2.5 | 路由管理 |

### 项目结构

```
eco-sorter/
├── data.sql                              # 数据库初始化脚本
├── ecosorter-backend/                     # Spring Boot后端
│   ├── src/main/java/com/ecosorter/
│   │   ├── config/                       # 配置类
│   │   ├── controller/                   # 控制器
│   │   ├── dto/                         # 数据传输对象
│   │   ├── enums/                       # 枚举类
│   │   ├── exception/                    # 异常处理
│   │   ├── model/                       # 实体类
│   │   ├── repository/                   # 数据访问层
│   │   └── service/                     # 业务逻辑层
│   ├── src/main/resources/
│   │   └── application.yml              # 配置文件
│   └── pom.xml                         # Maven配置
├── ecosorter-frontend/                  # 居民端/收集员端/管理后台
│   ├── src/
│   │   ├── api/                         # API接口
│   │   ├── assets/                      # 静态资源
│   │   ├── components/                  # 布局组件
│   │   ├── router.js                    # 路由配置
│   │   ├── stores/                      # 状态管理
│   │   ├── styles/                      # 全局样式
│   │   ├── utils/                       # 工具类
│   │   ├── views/                       # 页面组件
│   │   │   ├── admin/                  # 管理后台页面
│   │   │   ├── collector/              # 收集员端页面
│   │   │   ├── index/                  # 首页页面
│   │   │   └── resident/               # 居民端页面
│   │   ├── App.vue                      # 根组件
│   │   └── main.js                     # 入口文件
│   ├── package.json                     # 依赖配置
│   └── vue.config.js                   # Vue配置
├── trashcan-backend/                    # AI识别服务
│   ├── App.py                          # 应用入口
│   ├── yolo_recognition.py             # YOLO识别模块
│   └── requirements.txt                # Python依赖
└── trashcan-frontend/                  # 智能垃圾桶端
    ├── src/
    │   ├── api/                        # API接口
    │   ├── router.js                   # 路由配置
    │   ├── utils/                      # 工具类
    │   ├── views/                      # 页面组件
    │   ├── App.vue                     # 根组件
    │   └── main.js                     # 入口文件
    ├── package.json                    # 依赖配置
    └── vue.config.js                  # Vue配置
```

## API文档

### 认证接口

| 接口 | 方法 | 路径 | 说明 |
| --- | --- | --- | --- |
| 用户注册 | POST | /api/auth/register | 注册新用户 |
| 用户登录 | POST | /api/auth/login | 用户登录 |
| 刷新Token | POST | /api/auth/refresh | 刷新访问令牌 |
| 用户登出 | POST | /api/auth/logout | 用户登出 |
| 获取当前用户 | GET | /api/auth/me | 获取当前登录用户信息 |

### 用户管理接口

| 接口 | 方法 | 路径 | 说明 |
| --- | --- | --- | --- |
| 获取用户统计 | GET | /api/user/statistics | 获取用户统计数据 |

### 垃圾分类接口

| 接口 | 方法 | 路径 | 说明 |
| --- | --- | --- | --- |
| 获取分类历史 | GET | /api/classification/history | 获取用户分类历史记录 |
| 获取分类列表 | GET | /api/classification/categories | 获取所有垃圾类别 |
| 创建分类 | POST | /api/classification/categories | 创建新垃圾类别（管理员） |
| 更新分类 | PUT | /api/classification/categories/{id} | 更新垃圾类别（管理员） |
| 删除分类 | DELETE | /api/classification/categories/{id} | 删除垃圾类别（管理员） |

### 垃圾桶设备接口

| 接口 | 方法 | 路径 | 说明 |
| --- | --- | --- | --- |
| 获取设备信息 | GET | /api/trashcan/me | 获取垃圾桶设备信息 |
| 更新设备状态 | PUT | /api/trashcan/status | 更新垃圾桶设备状态 |
| 提交分类记录 | POST | /api/trashcan/classification | 提交垃圾分类记录 |

### 积分接口

| 接口 | 方法 | 路径 | 说明 |
| --- | --- | --- | --- |
| 获取积分记录 | GET | /api/points/records | 获取用户积分记录 |
| 获取积分记录分页 | GET | /api/points/records/page | 分页获取积分记录 |
| 获取总积分 | GET | /api/points/total | 获取用户总积分 |

### 商品接口

| 接口 | 方法 | 路径 | 说明 |
| --- | --- | --- | --- |
| 获取商品列表 | GET | /api/products | 获取商品列表 |
| 获取商品详情 | GET | /api/products/{id} | 获取商品详情 |
| 创建商品 | POST | /api/products | 创建商品（管理员） |
| 更新商品 | PUT | /api/products/{id} | 更新商品（管理员） |
| 删除商品 | DELETE | /api/products/{id} | 删除商品（管理员） |

### 订单接口

| 接口 | 方法 | 路径 | 说明 |
| --- | --- | --- | --- |
| 获取用户订单 | GET | /api/orders | 获取当前用户订单 |
| 获取所有订单 | GET | /api/orders/all | 获取所有订单（管理员） |
| 获取订单详情 | GET | /api/orders/{id} | 获取订单详情 |
| 创建订单 | POST | /api/orders | 创建兑换订单 |
| 更新订单状态 | PUT | /api/orders/{id}/status | 更新订单状态（管理员） |
| 更新物流单号 | PUT | /api/orders/{id}/tracking-number | 更新物流单号（管理员） |

### 预约接口

| 接口 | 方法 | 路径 | 说明 |
| --- | --- | --- | --- |
| 获取用户预约 | GET | /api/bookings | 获取当前用户预约 |
| 获取预约详情 | GET | /api/bookings/{id} | 获取预约详情 |
| 创建预约 | POST | /api/bookings | 创建回收预约 |
| 取消预约 | POST | /api/bookings/{id}/cancel | 取消预约 |

### 收集任务接口

| 接口 | 方法 | 路径 | 说明 |
| --- | --- | --- | --- |
| 获取任务列表 | GET | /api/collection-tasks/status/{status} | 按状态获取任务（管理员） |
| 生成任务 | POST | /api/collection-tasks/generate | 生成收集任务（管理员） |
| 重新分配任务 | POST | /api/collection-tasks/{taskId}/reassign | 重新分配任务（管理员） |
| 获取待处理异常 | GET | /api/collection-tasks/exceptions/pending | 获取待处理异常（管理员） |
| 审核异常 | POST | /api/collection-tasks/exceptions/{exceptionId}/review | 审核异常（管理员） |

### 投诉接口

| 接口 | 方法 | 路径 | 说明 |
| --- | --- | --- | --- |
| 提交投诉 | POST | /api/complaints | 提交投诉 |
| 获取我的投诉 | GET | /api/complaints/my | 获取当前用户投诉 |
| 获取所有投诉 | GET | /api/complaints/admin | 获取所有投诉（管理员） |
| 获取待处理数量 | GET | /api/complaints/admin/pending-count | 获取待处理投诉数量（管理员） |
| 处理投诉 | PUT | /api/complaints/admin/{id} | 处理投诉（管理员） |
| 删除投诉 | DELETE | /api/complaints/{id} | 删除投诉 |

### 轮播图接口

| 接口 | 方法 | 路径 | 说明 |
| --- | --- | --- | --- |
| 获取轮播图列表 | GET | /api/banners | 获取轮播图列表 |
| 获取轮播图详情 | GET | /api/banners/{id} | 获取轮播图详情 |
| 创建轮播图 | POST | /api/banners | 创建轮播图（管理员） |
| 更新轮播图 | PUT | /api/banners/{id} | 更新轮播图（管理员） |
| 删除轮播图 | DELETE | /api/banners/{id} | 删除轮播图（管理员） |

### 通知接口

| 接口 | 方法 | 路径 | 说明 |
| --- | --- | --- | --- |
| 获取通知列表 | GET | /api/notices | 获取通知列表 |
| 获取已发布通知 | GET | /api/notices/published | 获取已发布通知 |
| 获取通知详情 | GET | /api/notices/{id} | 获取通知详情 |
| 创建通知 | POST | /api/notices | 创建通知（管理员） |
| 更新通知 | PUT | /api/notices/{id} | 更新通知（管理员） |
| 删除通知 | DELETE | /api/notices/{id} | 删除通知（管理员） |

### 管理员接口

| 接口 | 方法 | 路径 | 说明 |
| --- | --- | --- | --- |
| 获取仪表板数据 | GET | /api/admin/dashboard | 获取管理后台仪表板数据 |
| 获取设备状态 | GET | /api/admin/device-status | 获取设备状态统计 |
| 获取活动记录 | GET | /api/admin/activities | 获取活动记录 |
| 获取用户列表 | GET | /api/admin/users | 获取用户列表 |
| 创建用户 | POST | /api/admin/users | 创建用户 |
| 更新用户 | PUT | /api/admin/users/{userId} | 更新用户信息 |
| 删除用户 | DELETE | /api/admin/users/{userId} | 删除用户 |
| 调整用户积分 | PUT | /api/admin/users/{userId}/points | 调整用户积分 |
| 获取设备列表 | GET | /api/admin/devices | 获取设备列表 |
| 创建设备 | POST | /api/admin/devices | 创建设备 |
| 更新设备 | PUT | /api/admin/devices/{deviceId} | 更新设备信息 |
| 删除设备 | DELETE | /api/admin/devices/{deviceId} | 删除设备 |
| 重新生成设备令牌 | POST | /api/admin/devices/{deviceId}/regenerate-token | 重新生成设备认证令牌 |
| 获取报表 | GET | /api/admin/reports | 获取数据报表 |

### 收集员接口

| 接口 | 方法 | 路径 | 说明 |
| --- | --- | --- | --- |
| 获取仪表板 | GET | /api/collector/dashboard | 获取收集员仪表板数据 |
| 获取任务列表 | GET | /api/collector/tasks | 获取收集员任务列表 |
| 获取任务详情 | GET | /api/collector/tasks/{taskId} | 获取任务详情 |
| 开始任务 | POST | /api/collector/tasks/{taskId}/start | 开始任务 |
| 完成任务 | POST | /api/collector/tasks/{taskId}/complete | 完成任务 |
| 报告异常 | POST | /api/collector/tasks/{taskId}/exception | 报告异常 |
| 获取设备列表 | GET | /api/collector/devices | 获取设备列表 |

### 个人资料接口

| 接口 | 方法 | 路径 | 说明 |
| --- | --- | --- | --- |
| 获取个人资料 | GET | /api/profile | 获取个人资料 |
| 更新个人资料 | PUT | /api/profile | 更新个人资料 |
| 更新头像 | PUT | /api/profile/avatar | 更新头像 |
| 修改密码 | POST | /api/profile/change-password | 修改密码 |

### 文件上传接口

| 接口 | 方法 | 路径 | 说明 |
| --- | --- | --- | --- |
| 上传头像 | POST | /api/upload/avatar | 上传头像 |

### AI识别接口（Python后端）

| 接口 | 方法 | 路径 | 说明 |
| --- | --- | --- | --- |
| 健康检查 | GET | / | API健康检查 |
| 垃圾识别 | POST | /api/recognition/recognize | 识别垃圾类别 |
| 上传图片 | POST | /api/upload | 上传图片 |
| 健康检查 | GET | /api/health | 健康检查 |

## 端口配置

| 服务 | 端口 | 说明 |
| --- | --- | --- |
| 主系统前端 | 8080 | Vue.js开发服务器 |
| 主系统后端 | 8081 | Spring Boot应用 |
| 垃圾桶前端 | 3000 | Vue.js开发服务器 |
| AI识别服务 | 9000 | FastAPI应用 |
| MySQL数据库 | 3306 | 数据库服务 |

## 开发指南

### 前端开发

```bash
# 主系统前端
cd ecosorter-frontend
npm install
npm run serve

# 垃圾桶前端
cd trashcan-frontend
npm install
npm run serve
```

### 后端开发

```bash
# Java后端
cd ecosorter-backend
mvn clean install
mvn spring-boot:run

# Python后端
cd trashcan-backend
pip install -r requirements.txt
python App.py
```

### 数据库操作

```bash
# 连接数据库
mysql -u root -p

# 选择数据库
USE eco_sorter;

# 查看表结构
SHOW TABLES;

# 执行SQL脚本
SOURCE data.sql;
```

## 常见问题

### 1. 端口被占用

如果端口被占用，可以修改配置文件中的端口号：

- 主系统前端：`ecosorter-frontend/vue.config.js`
- 主系统后端：`ecosorter-backend/src/main/resources/application.yml`
- 垃圾桶前端：`trashcan-frontend/vue.config.js`
- AI识别服务：`trashcan-backend/App.py`

### 2. 数据库连接失败

检查 `application.yml` 中的数据库配置是否正确：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/eco_sorter
    username: root
    password: your_password
```

### 3. 跨域问题

确保后端配置了CORS，允许前端访问：

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*");
    }
}
```

## 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

## 联系方式

- 项目地址：[https://github.com/YangShengzhou03/EcoSorter](https://github.com/YangShengzhou03/EcoSorter)
- 问题反馈：[Issues](https://github.com/YangShengzhou03/EcoSorter/issues)

## 贡献指南

欢迎贡献代码！请遵循以下步骤：

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

## 致谢

感谢所有为本项目做出贡献的开发者！

---

**注意**: 本项目仅供学习和研究使用，请勿用于商业用途。
