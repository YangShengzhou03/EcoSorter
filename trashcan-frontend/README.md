# Trashcan Frontend

易控智能垃圾桶前端应用 - Vue 3 + Element Plus

## 技术栈

- Vue 3 (Composition API)
- Element Plus UI 组件库
- Vue Router 4
- Axios
- Pinia

## 安装依赖

```bash
npm install
```

## 开发环境

```bash
npm run serve
```

应用将在 `http://localhost:8080` 启动

## 生产构建

```bash
npm run build
```

## 代码检查

```bash
npm run lint
```

## 环境配置

### 开发环境 (.env.development)

```
VUE_APP_API_BASE_URL=http://localhost:8081
VUE_APP_PYTHON_API_URL=http://localhost:9000
VUE_APP_TITLE=ECO-Sorter
VUE_APP_VERSION=2.0.0
VUE_APP_REQUEST_TIMEOUT=30000
```

### 生产环境 (.env.production)

```
VUE_APP_API_BASE_URL=http://your-production-api
VUE_APP_PYTHON_API_URL=http://your-production-python-api
VUE_APP_TITLE=ECO-Sorter
VUE_APP_VERSION=2.0.0
VUE_APP_REQUEST_TIMEOUT=30000
```

## 项目结构

```
src/
├── api/              # API 接口
│   └── trashcan.js
├── utils/            # 工具函数
│   └── request.js
├── views/            # 页面组件
│   ├── DeviceInit.vue      # 设备初始化
│   ├── WorkStatus.vue      # 工作状态
│   ├── UserLogin.vue       # 用户登录
│   └── GarbageRecognize.vue # 垃圾识别
├── App.vue           # 根组件
├── main.js           # 入口文件
└── router.js         # 路由配置
```

## 页面说明

### 设备初始化 (/init)
- 设备激活配置
- 设置设备名称、位置和类型
- 管理员密码设置

### 工作状态 (/work)
- 设备信息展示
- 轮播图展示
- 点击进入登录

### 用户登录 (/login)
- 多种登录方式
  - 游客模式
  - 手机验证码
  - 二维码扫码
  - NFC 刷卡
  - 人脸识别

### 垃圾识别 (/recognize)
- 摄像头实时预览
- 拍照识别垃圾
- 识别结果展示
- 分类指南

## 设计特点

- 横版平板布局优化
- 左右分栏设计
- Element Plus 组件风格
- 响应式设计
- 无大圆角、无渐变

## 注意事项

- 确保后端服务正常运行
- Python 识别服务端口: 9000
- Java 后端服务端口: 8081
