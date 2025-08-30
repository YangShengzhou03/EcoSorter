# 🗑️ EcoSorter Trash Bin Web - 智能垃圾桶Web客户端

专为智能垃圾桶设备设计的Web应用程序，搭载在嵌入式浏览器中，提供摄像头识别、用户认证、垃圾分类和积分领取等功能。

## 🌟 核心特性

- 📷 **摄像头集成** - 实时视频流捕获和图像处理
- 🤖 **AI识别** - TensorFlow.js本地垃圾识别模型
- 👤 **用户识别** - 人脸识别和二维码扫描登录
- 🎯 **实时反馈** - 即时垃圾分类结果和积分奖励
- 📱 **触摸优化** - 大按钮设计，适合触摸屏操作
- 🔒 **离线功能** - PWA支持，网络不稳定时仍可工作
- 🎨 **设备适配** - 针对嵌入式浏览器优化

## 🏗️ 项目结构

```
trash-bin-web/
├── 📁 src/
│   ├── 📁 components/         # 可复用组件
│   │   ├── 📁 camera/        # 摄像头相关组件
│   │   │   ├── 📄 CameraCapture.tsx      # 视频捕获组件
│   │   │   ├── 📄 FaceRecognition.tsx   # 人脸识别组件
│   │   │   └── 📄 QRScanner.tsx         # 二维码扫描组件
│   │   ├── 📁 ai/            # AI识别组件
│   │   │   ├── 📄 ObjectDetection.tsx   # 物体检测
│   │   │   ├── 📄 GarbageClassifier.tsx # 垃圾分类器
│   │   │   └── 📄 ModelLoader.tsx       # 模型加载器
│   │   ├── 📁 ui/            # UI组件
│   │   │   ├── 📄 TouchButton.tsx       # 触摸按钮
│   │   │   ├── 📄 ProgressIndicator.tsx # 进度指示器
│   │   │   └── 📄 FeedbackOverlay.tsx   # 反馈覆盖层
│   │   └── 📁 layout/        # 布局组件
│   │       ├── 📄 FullScreenLayout.tsx  # 全屏布局
│   │       └── 📄 KioskMode.tsx          # 信息亭模式
│   ├── 📁 pages/             # 页面组件
│   │   ├── 📄 HomePage.tsx              # 主页
│   │   ├── 📄 RecognitionPage.tsx      # 识别页面
│   │   ├── 📄 ResultPage.tsx           # 结果页面
│   │   ├── 📄 RewardPage.tsx           # 奖励页面
│   │   ├── 📄 LoginPage.tsx            # 登录页面
│   │   └── 📄 ErrorPage.tsx            # 错误页面
│   ├── 📁 hooks/             # 自定义Hooks
│   │   ├── 📄 useCamera.ts            # 摄像头Hook
│   │   ├── 📄 useTensorFlow.ts        # TensorFlow Hook
│   │   ├── 📄 useFaceRecognition.ts   # 人脸识别Hook
│   │   ├── 📄 useGarbageDetection.ts  # 垃圾检测Hook
│   │   └── 📄 useFullScreen.ts        # 全屏Hook
│   ├── 📁 store/             # 状态管理
│   │   ├── 📄 cameraStore.ts          # 摄像头状态
│   │   ├── 📄 recognitionStore.ts    # 识别状态
│   │   ├── 📄 userStore.ts           # 用户状态
│   │   └── 📄 rewardStore.ts          # 奖励状态
│   ├── 📁 services/          # API服务
│   │   ├── 📄 api.ts                 # HTTP客户端
│   │   ├── 📄 authService.ts         # 认证服务
│   │   ├── 📄 garbageService.ts      # 垃圾服务
│   │   ├── 📄 rewardService.ts       # 奖励服务
│   │   └── 📄 deviceService.ts       # 设备服务
│   ├── 📁 utils/             # 工具函数
│   │   ├── 📄 cameraUtils.ts        # 摄像头工具
│   │   ├── 📄 imageProcessing.ts    # 图像处理
│   │   ├── 📄 dateUtils.ts          # 日期工具
│   │   └── 📄 storage.ts             # 本地存储
│   ├── 📁 types/             # TypeScript类型定义
│   │   ├── 📄 camera.ts              # 摄像头类型
│   │   ├── 📄 garbage.ts             # 垃圾类型
│   │   ├── 📄 user.ts                # 用户类型
│   │   └── 📄 api.ts                 # API类型
│   ├── 📁 assets/            # 静态资源
│   │   ├── 📁 models/        # AI模型文件
│   │   │   ├── 📄 coco-ssd/          # COCO-SSD模型
│   │   │   └── 📄 garbage-classifier/ # 自定义分类模型
│   │   ├── 📁 images/        # 图片资源
│   │   ├── 📁 icons/         # 图标资源
│   │   └── 📁 sounds/        # 音效资源
│   ├── 📁 ai/                # AI相关代码
│   │   ├── 📄 tensorflowService.ts   # TensorFlow服务
│   │   ├── 📄 modelManager.ts        # 模型管理器
│   │   ├── 📄 garbageDetector.ts    # 垃圾检测器
│   │   └── 📄 faceRecognizer.ts     # 人脸识别器
│   ├── 📄 App.tsx                    # 主应用组件
│   ├── 📄 main.tsx                  # 应用入口
│   └── 📄 vite-env.d.ts             # Vite类型定义
├── 📁 public/                # 公共资源
│   ├── 📄 manifest.json             # PWA清单
│   ├── 📄 sw.js                    # Service Worker
│   └── 📁 models/                  # 公共模型文件
├── 📄 package.json                 # 项目配置
├── 📄 vite.config.ts              # Vite配置
├── 📄 tsconfig.json                # TypeScript配置
├── 📄 tailwind.config.js           # Tailwind配置
└── 📄 index.html                   # HTML入口
```

## 🔧 技术栈

### 核心框架
- **React 18** - 用户界面库
- **TypeScript** - 类型安全的JavaScript
- **Vite** - 快速构建工具

### AI与识别
- **TensorFlow.js** - 浏览器端机器学习
- **COCO-SSD** - 实时物体检测模型
- **Mobilenet** - 图像分类模型
- **Face-api.js** - 人脸识别（可选）

### UI与样式
- **TailwindCSS** - 实用优先的CSS框架
- **Framer Motion** - 动画库
- **Lucide React** - 图标库
- **HeadlessUI** - 无障碍UI组件

### 状态管理
- **Zustand** - 轻量级状态管理
- **React Query** - 服务器状态管理

### 设备集成
- **WebRTC** - 摄像头访问
- **MediaDevices API** - 媒体设备控制
- **Fullscreen API** - 全屏模式
- **Vibration API** - 触觉反馈
- **Web Workers** - 后台处理

## 🚀 快速开始

### 环境要求

- Node.js 18+
- 支持WebRTC的现代浏览器
- 摄像头设备

### 安装运行

```bash
# 安装依赖
cd trash-bin-web
pnpm install

# 启动开发服务器
pnpm dev

# 构建生产版本
pnpm build

# 预览生产构建
pnpm preview
```

### 设备部署

1. **构建应用**
   ```bash
   pnpm build:production
   ```

2. **部署到设备**
   - 将 `dist` 目录内容复制到设备Web服务器
   - 配置设备浏览器自动启动URL
   - 设置全屏和kiosk模式

3. **摄像头权限**
   - 确保设备授予摄像头访问权限
   - 配置HTTPS（生产环境必需）

## 📷 摄像头集成

### 权限配置

```html
<!-- 在index.html中添加相机权限提示 -->
<meta name="theme-color" content="#22c55e">
```

### 代码示例

```tsx
// 使用摄像头Hook
const { stream, error, startCamera, stopCamera } = useCamera({
  constraints: {
    video: {
      width: { ideal: 1280 },
      height: { ideal: 720 },
      facingMode: 'environment'
    }
  }
});
```

## 🤖 AI模型配置

### 模型加载

```typescript
// 加载COCO-SSD模型
const loadModel = async () => {
  const model = await cocoSsd.load();
  return model;
};

// 自定义垃圾分类模型
const loadGarbageModel = async () => {
  const model = await tf.loadGraphModel('/models/garbage-classifier/model.json');
  return model;
};
```

### 实时检测

```typescript
// 执行物体检测
const detectObjects = async (image: HTMLImageElement) => {
  const predictions = await model.detect(image);
  return predictions.filter(pred => pred.class === 'bottle' || pred.class === 'can');
};
```

## 🎯 功能流程

### 1. 用户识别
- 人脸识别或二维码扫描登录
- 用户身份验证
- 会话管理

### 2. 垃圾识别
- 摄像头捕获图像
- AI模型进行物体检测
- 垃圾分类判断
- 实时反馈结果

### 3. 积分奖励
- 根据垃圾类型计算积分
- 实时更新用户积分
- 奖励动画和音效
- 交易记录保存

### 4. 设备管理
- 设备状态监控
- 网络连接管理
- 错误处理和恢复
- 自动更新机制

## 🔒 安全考虑

### 摄像头安全
- HTTPS强制要求
- 用户明确授权
- 隐私数据本地处理

### 数据安全
- 敏感信息加密存储
- API请求认证
- 防篡改机制

### 设备安全
- 自动注销机制
- 会话超时控制
- 防恶意使用

## 📊 性能优化

### 加载优化
- 模型懒加载
- 代码分割
- 资源预加载

### 运行优化
- Web Workers后台处理
- 内存泄漏防护
- 帧率优化

### 网络优化
- 离线功能支持
- 数据缓存策略
- 重试机制

## 🐛 故障排除

### 常见问题

1. **摄像头无法访问**
   - 检查浏览器权限
   - 验证HTTPS连接
   - 检查设备驱动程序

2. **模型加载失败**
   - 检查网络连接
   - 验证模型文件路径
   - 清除浏览器缓存

3. **性能问题**
   - 降低视频分辨率
   - 减少检测频率
   - 启用硬件加速

### 日志调试

```typescript
// 启用调试日志
localStorage.setItem('debug', 'trash-bin:*');

// 自定义日志级别
const logger = debug('trash-bin:camera');
logger('Camera initialized successfully');
```

## 🤝 贡献指南

欢迎提交Issue和Pull Request！

## 📄 许可证

MIT License - 详见 [LICENSE](../LICENSE) 文件。