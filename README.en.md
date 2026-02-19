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

![首页宣传页](https://gitee.com/Yangshengzhou/eco-sorter/raw/master/assets/landing-page.png)

ECO-SORTER 是一个智能垃圾分类管理系统，采用前后端分离架构，集成AI图像识别技术，实现垃圾自动分类。系统包含居民端、收集员端、管理后台和智能垃圾桶端四个子系统，提供完整的垃圾分类解决方案。

![可回收物分类示例](https://gitee.com/Yangshengzhou/eco-sorter/raw/master/assets/recyclable.png)

系统核心功能包括AI智能识别垃圾类别（可回收物、有害垃圾、厨余垃圾、其他垃圾），积分奖励机制鼓励居民正确分类，智能垃圾桶设备管理，收集任务调度与管理，以及数据统计与分析。

## 功能特性

### 居民端功能

The resident end provides convenient waste classification services for users. Users can identify waste categories by taking photos, view classification history records, and obtain classification guidance.

![居民端首页仪表盘](https://gitee.com/Yangshengzhou/eco-sorter/raw/master/assets/resident-dashboard.png)

The system provides a points reward mechanism. Users can view their points balance and details, and redeem products in the points mall. In addition, it supports the recycling appointment function. Users can make appointments for door-to-door recycling services, view appointment records, or cancel appointments. If there are objections to the classification results, they can also submit complaints and appeals, and view the progress of appeal processing. The system will regularly publish notifications and announcements, and users can view system notifications and announcements in a timely manner. In the personal center, users can manage personal information, change passwords, and view statistical data.

### 收集员端功能

The collector end focuses on task management and equipment monitoring. Collectors can view assigned collection tasks, start or complete tasks, and report exceptions when encountering abnormal situations. At the same time, the system provides equipment monitoring functions. Collectors can view the status and capacity monitoring of trash can equipment, and reasonably arrange collection work. The data statistics module allows collectors to view personal work statistics and task completion status, facilitating work summary and improvement. The personal center provides personal information management and password modification functions.

### 管理后台功能

The management backend is the core management platform of the system, providing a comprehensive data dashboard to display overall system data statistics and chart analysis.

![管理员端首页仪表盘](https://gitee.com/Yangshengzhou/eco-sorter/raw/master/assets/admin-dashboard.png)

The user management module supports user list viewing, user information management, and points adjustment functions.

![管理员端用户管理](https://gitee.com/Yangshengzhou/eco-sorter/raw/master/assets/admin-users.png)

The equipment management module provides trash can equipment management, equipment status monitoring, as well as equipment addition, editing, and deletion functions.

![管理员垃圾桶设备管理](https://gitee.com/Yangshengzhou/eco-sorter/raw/master/assets/admin-devices.png)

The task management module supports collection task management, task assignment, and exception handling. The product management module can manage points mall products, including product listing and delisting operations. The order management module is used to manage redemption orders and update order status. The carousel management module can manage homepage carousels. The classification management module supports waste category management and classification example management. The notification management module is used to publish and manage system notifications. The complaint management module handles user complaints and reviews appeals. The report statistics module provides data reports and statistical analysis functions.

![管理员个人中心](https://gitee.com/Yangshengzhou/eco-sorter/raw/master/assets/admin-profile.png)

### 智能垃圾桶端功能

The smart trash can end is the front-end display device of the system, supporting device initialization, including device activation and device information configuration. User login methods are diverse, supporting mobile verification code login, QR code login, NFC login, face recognition, and guest mode. The waste recognition function recognizes waste categories through camera photography using AI and provides dumping guidance. The work status module displays device status and carousel display.

## 功能实现状态

### ✅ 已实现功能

| 模块 | 功能 | 状态 |
|------|------|------|
| 用户管理 | 用户注册、登录、信息管理 | ✅ 100% |
| 垃圾分类 | AI识别、分类历史、分类指导 | ✅ 100% |
| 积分系统 | 积分获取、积分兑换、积分明细 | ✅ 100% |
| 商品管理 | 商品列表、商品兑换、订单管理 | ✅ 100% |
| 预约管理 | 回收预约、预约取消、预约记录 | ✅ 100% |
| 投诉申诉 | 投诉提交、申诉处理、审核 | ✅ 100% |
| 通知公告 | 通知发布、通知查看 | ✅ 100% |
| 设备管理 | 设备列表、设备状态、设备控制 | ✅ 100% |
| 任务管理 | 任务分配、任务执行、异常处理 | ✅ 100% |
| 轮播图管理 | 轮播图上传、轮播图展示 | ✅ 100% |
| 数据统计 | 仪表盘、报表分析 | ✅ 100% |
| 心跳机制 | 设备心跳上报、在线状态判断 | ✅ 100% |
| 人脸识别 | 人脸注册、人脸登录 | ✅ 100% |

**人脸识别详细说明**：
- ✅ 前端界面完整（支持拍照、上传）
- ✅ 后端接口完整（注册、登录）
- ✅ 数据库存储完整（face_encoding字段）
- ✅ Python端使用真实人脸识别模型（face_recognition库）
- ✅ 从图片中提取真实的人脸特征向量
- ✅ 同一个人的人脸图片会提取出相似的特征向量

**技术实现**：
- 使用`face_recognition`库进行人脸检测和特征提取
- 注册时：从用户上传的人脸图片中提取真实特征向量并存储到数据库
- 登录时：从用户上传的人脸图片中提取真实特征向量，与数据库中的特征向量进行比对
- 相似度计算：使用欧氏距离计算两个特征向量的相似度
- 阈值设置：相似度 > 0.7 时认为匹配成功

### 📊 接口实现统计

| 模块 | 接口数量 | 已实现 | 实现率 |
|------|----------|--------|--------|
| 认证模块 | 7 | 7 | 100% |
| 用户管理 | 1 | 1 | 100% |
| 垃圾分类 | 5 | 5 | 100% |
| 垃圾桶设备 | 8 | 8 | 100% |
| 积分 | 1 | 1 | 100% |
| 商品 | 5 | 5 | 100% |
| 订单 | 6 | 6 | 100% |
| 预约 | 4 | 4 | 100% |
| 收集任务 | 5 | 5 | 100% |
| 投诉 | 5 | 5 | 100% |
| 轮播图 | 5 | 5 | 100% |
| 通知 | 6 | 6 | 100% |
| 管理员 | 14 | 14 | 100% |
| 收集员 | 13 | 13 | 100% |
| 个人中心 | 3 | 3 | 100% |
| 上传 | 2 | 2 | 100% |
| Python后端 | 8 | 8 | 100% |
| **总计** | **98** | **98** | **100%** |

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
    secret: ecosorter-jwt-secret-key-2026-change-in-production
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

配置数据库连接（可通过环境变量或直接修改代码）：

**方式一：使用环境变量**
```bash
# Windows
set DB_HOST=localhost
set DB_USERNAME=root
set DB_PASSWORD=your_password
set DB_NAME=eco_sorter

# Linux/Mac
export DB_HOST=localhost
export DB_USERNAME=root
export DB_PASSWORD=your_password
export DB_NAME=eco_sorter
```

**方式二：直接修改配置文件**

编辑 `trashcan-backend/yolo_recognition.py` 中的数据库配置：

```python
DATABASE_CONFIG = {
    'host': os.getenv('DB_HOST', 'localhost'),
    'user': os.getenv('DB_USERNAME', 'root'),
    'password': os.getenv('DB_PASSWORD', 'your_password'),
    'database': os.getenv('DB_NAME', 'eco_sorter'),
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

访问地址：http://localhost:8080

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
│   Resident Frontend│    │  Collector Frontend│    │   Admin Frontend │
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
                    │    Spring Boot Backend   │
                    │                         │
                    │  Spring Boot 3.1.0     │
                    │  MyBatis Plus 3.5.7    │
                    │  Port: 8081             │
                    └─────────────────────────┘
                                  │
                    ┌─────────────┬─────────────┐
                    │             │             │
                    ▼             ▼             ▼
          ┌─────────────────┐ ┌─────────────────────────┐
          │   MySQL Database │ │  AI/Face Recognition     │
          │                 │ │                         │
          │  MySQL 8.0      │ │  Python 3.8+            │
          │  Port: 3306     │ │  FastAPI                │
          │                 │ │  Port: 9000             │
          └─────────────────┘ └─────────────────────────┘
                                  │
                                  ▼
                    ┌─────────────────────────┐
                    │   Trashcan Frontend    │
                    │                         │
                    │  Vue 3.3.11            │
                    │  Element Plus          │
                    │  Port: 8080            │
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
| pydantic | 2.5.0 | 数据验证 |
| python-multipart | 0.0.6 | 文件上传支持 |

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
├── android-app/                          # Android移动应用（Flutter）
│   ├── android/                         # Android原生代码
│   ├── README.md                         # Android应用说明
│   ├── pubspec.yaml                      # Flutter依赖配置
│   └── analysis_options.yaml             # Flutter分析配置
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
| 设备激活 | POST | /api/auth/device/activate | 激活垃圾桶设备 |
| 刷新Token | POST | /api/auth/refresh | 刷新访问令牌 |
| 用户登出 | POST | /api/auth/logout | 用户登出 |
| 人脸登录 | POST | /api/auth/face-login-with-file | 人脸识别登录（上传图片文件） |
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
| 更新设备信息 | PUT | /api/trashcan/me | 更新垃圾桶设备信息 |
| 更新设备状态 | PUT | /api/trashcan/status | 更新垃圾桶设备状态 |
| 提交分类记录 | POST | /api/trashcan/classification | 提交垃圾分类记录 |
| 心跳上报 | POST | /api/trashcan/heartbeat | 设备心跳上报 |
| 管理员登录 | POST | /api/trashcan/admin-login | 垃圾桶管理员登录 |
| 重置管理员密码 | POST | /api/trashcan/reset-password | 重置垃圾桶管理员密码 |
| 清除设备数据 | POST | /api/trashcan/clear-data | 清除垃圾桶设备数据 |

### 积分接口

| 接口 | 方法 | 路径 | 说明 |
| --- | --- | --- | --- |
| 获取积分记录分页 | GET | /api/points/records/page | 分页获取积分记录 |

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
| 重置管理员密码 | POST | /api/admin/devices/{deviceId}/reset-admin-password | 重置设备管理员密码 |
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
| 获取统计数据 | GET | /api/collector/statistics | 获取统计数据 |
| 获取积分记录 | GET | /api/collector/point-records | 获取积分记录 |
| 获取订单列表 | GET | /api/collector/orders | 获取订单列表 |
| 创建订单 | POST | /api/collector/orders | 创建订单 |
| 获取订单详情 | GET | /api/collector/orders/{orderId} | 获取订单详情 |
| 取消订单 | POST | /api/collector/orders/{orderId}/cancel | 取消订单 |

### 个人资料接口

| 接口 | 方法 | 路径 | 说明 |
| --- | --- | --- | --- |
| 获取个人资料 | GET | /api/profile | 获取个人资料 |
| 更新个人资料 | PUT | /api/profile | 更新个人资料 |
| 更新头像 | PUT | /api/profile/avatar | 更新头像 |

### 文件上传接口

| 接口 | 方法 | 路径 | 说明 |
| --- | --- | --- | --- |
| 上传头像 | POST | /api/upload/avatar | 上传头像 |

### AI识别接口（Python后端）

| 接口 | 方法 | 路径 | 说明 |
| --- | --- | --- | --- |
| 服务状态 | GET | / | API服务状态 |
| 健康检查 | GET | /api/health | 健康检查 |
| 模型信息 | GET | /api/model/info | 获取模型信息 |
| 垃圾识别（URL） | POST | /api/recognition/recognize | 通过URL识别垃圾类别 |
| 垃圾识别（文件） | POST | /api/recognition/recognize-with-file | 上传文件识别垃圾类别 |
| 多目标识别 | POST | /api/recognition/recognize-multi | 多目标垃圾识别 |
| 人脸注册 | POST | /api/face/register-with-file | 人脸注册（上传图片文件） |
| 人脸验证 | POST | /api/face/verify-with-file | 人脸验证（上传图片文件） |

## 端口配置

| 服务 | 端口 | 说明 |
| --- | --- | --- |
| 主系统前端 | 8080 | Vue.js开发服务器 |
| 主系统后端 | 8081 | Spring Boot应用 |
| 垃圾桶前端 | 8080 | Vue.js开发服务器 |
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
