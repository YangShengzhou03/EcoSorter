# EcoSorter 智能垃圾分类督导系统 - 项目结构

## 📁 项目根目录结构
```
d:\Code\web\eco-sorter\
├── 📁 backend/                 # 后端服务
├── 📁 frontend/                # Web前端（管理后台）
├── 📁 trash-bin-web/           # 垃圾桶Web客户端
├── 📁 android-app/             # 安卓原生应用
├── 📁 mobile/                  # 移动端应用
├── 📁 miniprogram/             # 小程序端
├── 📁 docs/                    # 项目文档
├── 📁 scripts/                 # 部署脚本
├── 📁 infrastructure/          # 基础设施配置
├── 📁 shared/                  # 共享代码库
├── 📁 tests/                   # 测试代码
├── 📁 tools/                   # 开发工具
├── 📁 .github/                 # GitHub配置
├── 📁 .gitee/                  # Gitee配置
├── 📁 UML代码/                 # UML设计文档
├── 📁 原型/                    # 原型设计
├── 📁 模型图/                  # 模型图
├── 📄 README.md                # 项目说明
├── 📄 LICENSE                  # 开源协议
└── 📄 package.json             # 项目配置
```

## 🚀 后端服务 (backend/)
```
backend/
├── 📁 src/
│   ├── 📁 modules/
│   │   ├── 📁 auth/           # 认证模块
│   │   ├── 📁 user/           # 用户管理
│   │   ├── 📁 garbage/        # 垃圾分类
│   │   ├── 📁 iot/            # IoT设备管理
│   │   ├── 📁 reward/         # 积分奖励
│   │   └── 📁 statistics/     # 数据统计
│   ├── 📁 common/
│   │   ├── 📁 constants/      # 常量定义
│   │   ├── 📁 decorators/     # 装饰器
│   │   ├── 📁 filters/        # 过滤器
│   │   ├── 📁 guards/         # 守卫
│   │   ├── 📁 interceptors/   # 拦截器
│   │   └── 📁 pipes/          # 管道
│   ├── 📁 config/             # 配置文件
│   ├── 📁 database/           # 数据库配置
│   ├── 📁 middleware/         # 中间件
│   ├── 📁 utils/              # 工具函数
│   └── 📄 main.ts             # 入口文件
├── 📁 test/                   # 测试文件
├── 📁 migrations/             # 数据库迁移
├── 📁 seeds/                  # 种子数据
├── 📄 package.json
├── 📄 tsconfig.json
├── 📄 nest-cli.json
└── 📄 Dockerfile
```

## 🌐 Web前端管理后台 (frontend/)
```
frontend/
├── 📁 src/
│   ├── 📁 components/         # 公共组件
│   │   ├── 📁 ui/            # UI基础组件
│   │   ├── 📁 layout/        # 布局组件
│   │   ├── 📁 garbage/       # 垃圾分类组件
│   │   └── 📁 auth/          # 认证组件
│   ├── 📁 pages/             # 页面组件
│   │   ├── 📁 dashboard/     # 仪表板
│   │   ├── 📁 garbage/       # 垃圾分类
│   │   ├── 📁 reward/        # 积分奖励
│   │   ├── 📁 statistics/    # 数据统计
│   │   ├── 📁 profile/       # 个人中心
│   │   └── 📁 admin/         # 管理后台
│   ├── 📁 hooks/             # React Hooks
│   ├── 📁 store/             # 状态管理
│   ├── 📁 services/          # API服务
│   ├── 📁 utils/             # 工具函数
│   ├── 📁 types/             # TypeScript类型定义
│   ├── 📁 assets/            # 静态资源
│   │   ├── 📁 images/
│   │   ├── 📁 icons/
│   │   └── 📁 styles/
│   └── 📄 App.tsx
├── 📁 public/                # 公共资源
├── 📄 package.json
├── 📄 vite.config.ts
├── 📄 tsconfig.json
└── 📄 index.html
```

## 🗑️ 垃圾桶Web客户端 (trash-bin-web/)
```
trash-bin-web/
├── 📁 src/
│   ├── 📁 components/         # 可复用组件
│   │   ├── 📁 camera/        # 摄像头相关组件
│   │   ├── 📁 ai/            # AI识别组件
│   │   ├── 📁 ui/            # UI组件
│   │   └── 📁 layout/        # 布局组件
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
│   ├── 📁 services/          # API服务
│   ├── 📁 utils/             # 工具函数
│   ├── 📁 types/             # TypeScript类型定义
│   ├── 📁 assets/            # 静态资源
│   │   ├── 📁 models/        # AI模型文件
│   │   ├── 📁 images/        # 图片资源
│   │   ├── 📁 icons/         # 图标资源
│   │   └── 📁 sounds/        # 音效资源
│   ├── 📁 ai/                # AI相关代码
│   ├── 📄 App.tsx                    # 主应用组件
│   ├── 📄 main.tsx                  # 应用入口
│   └── 📄 vite-env.d.ts             # Vite类型定义
├── 📁 public/                # 公共资源
├── 📄 package.json                 # 项目配置
├── 📄 vite.config.ts              # Vite配置
├── 📄 tsconfig.json                # TypeScript配置
├── 📄 tailwind.config.js           # Tailwind配置
└── 📄 index.html                   # HTML入口
```

## 📱 移动端应用 (mobile/)
```
mobile/
├── 📁 android/               # Android原生应用
│   ├── 📁 app/
│   │   ├── 📁 src/
│   │   │   ├── 📁 main/
│   │   │   │   ├── 📁 java/
│   │   │   │   ├── 📁 res/
│   │   │   │   └── 📁 assets/
│   │   │   └── 📁 androidTest/
│   │   └── 📄 build.gradle
│   ├── 📄 settings.gradle
│   └── 📄 gradle.properties
├── 📁 ios/                   # iOS原生应用
│   ├── 📁 EcoSorter/
│   │   ├── 📁 Sources/
│   │   ├── 📁 Resources/
│   │   └── 📄 Info.plist
│   └── 📁 EcoSorterTests/
├── 📁 react-native/          # React Native跨平台
│   ├── 📁 src/
│   │   ├── 📁 components/
│   │   ├── 📁 screens/
│   │   ├── 📁 navigation/
│   │   ├── 📁 services/
│   │   └── 📁 utils/
│   ├── 📄 App.tsx
│   ├── 📄 package.json
│   └── 📄 index.js
└── 📁 flutter/              # Flutter跨平台
    ├── 📁 lib/
    │   ├── 📁 models/
    │   ├── 📁 widgets/
    │   ├── 📁 pages/
    │   ├── 📁 services/
    │   └── 📄 main.dart
    ├── 📁 android/
    ├── 📁 ios/
    └── 📄 pubspec.yaml
```

## 💬 小程序端 (miniprogram/)
```
miniprogram/
├── 📁 wechat/               # 微信小程序
│   ├── 📁 pages/            # 页面文件
│   ├── 📁 components/       # 自定义组件
│   ├── 📁 utils/            # 工具函数
│   ├── 📁 images/           # 图片资源
│   ├── 📄 app.js
│   ├── 📄 app.json
│   ├── 📄 app.wxss
│   └── 📄 project.config.json
├── 📁 alipay/               # 支付宝小程序
├── 📁 baidu/                # 百度小程序
├── 📁 toutiao/              # 字节跳动小程序
└── 📁 uni-app/              # UniApp跨端方案
    ├── 📁 pages/
    ├── 📁 components/
    ├── 📁 static/
    ├── 📄 App.vue
    ├── 📄 main.js
    └── 📄 manifest.json
```

## 🔐 统一身份认证系统 (backend/src/modules/auth/)
```
auth/
├── 📁 entities/             # 数据实体
│   ├── 📄 user.entity.ts
│   ├── 📄 role.entity.ts
│   ├── 📄 permission.entity.ts
│   └── 📄 session.entity.ts
├── 📁 controllers/          # 控制器
│   ├── 📄 auth.controller.ts
│   ├── 📄 user.controller.ts
│   └── 📄 oauth.controller.ts
├── 📁 services/             # 服务层
│   ├── 📄 auth.service.ts
│   ├── 📄 user.service.ts
│   ├── 📄 jwt.service.ts
│   ├── 📄 oauth.service.ts
│   └── 📄 session.service.ts
├── 📁 strategies/           # 认证策略
│   ├── 📄 jwt.strategy.ts
│   ├── 📄 local.strategy.ts
│   ├── 📄 wechat.strategy.ts
│   ├── 📄 alipay.strategy.ts
│   └── 📄 oauth2.strategy.ts
├── 📁 guards/               # 守卫
│   ├── 📄 jwt-auth.guard.ts
│   ├── 📄 roles.guard.ts
│   └── 📄 permissions.guard.ts
├── 📁 decorators/           # 装饰器
│   ├── 📄 current-user.decorator.ts
│   ├── 📄 roles.decorator.ts
│   └── 📄 public.decorator.ts
├── 📁 interfaces/           # 接口定义
│   ├── 📄 auth.interface.ts
│   ├── 📄 token.interface.ts
│   └── 📄 oauth.interface.ts
├── 📁 dtos/                 # 数据传输对象
│   ├── 📄 login.dto.ts
│   ├── 📄 register.dto.ts
│   ├── 📄 token.dto.ts
│   └── 📄 user.dto.ts
└── 📄 auth.module.ts        # 模块定义
```

## 🗃️ 数据库设计
```
database/
├── 📁 entities/             # 所有数据实体
│   ├── 📄 user.entity.ts           # 用户表
│   ├── 📄 garbage.entity.ts        # 垃圾分类表
│   ├── 📄 garbage-record.entity.ts # 投放记录
│   ├── 📄 reward.entity.ts         # 奖励积分
│   ├── 📄 device.entity.ts         # IoT设备
│   ├── 📄 community.entity.ts      # 社区信息
│   └── 📄 statistics.entity.ts    # 统计信息
├── 📁 migrations/           # 数据库迁移文件
├── 📁 seeds/                # 种子数据
└── 📄 database.config.ts    # 数据库配置
```

## 🧪 测试结构
```
tests/
├── 📁 unit/                 # 单元测试
│   ├── 📁 backend/
│   ├── 📁 frontend/
│   └── 📁 shared/
├── 📁 integration/          # 集成测试
│   ├── 📁 api/
│   ├── 📁 auth/
│   └── 📁 garbage/
├── 📁 e2e/                  # 端到端测试
│   ├── 📁 web/
│   ├── 📁 mobile/
│   └── 📁 miniprogram/
└── 📁 fixtures/             # 测试数据
```

## 🚀 部署配置
```
infrastructure/
├── 📁 docker/               # Docker配置
│   ├── 📄 Dockerfile.backend
│   ├── 📄 Dockerfile.frontend
│   ├── 📄 docker-compose.yml
│   └── 📄 .dockerignore
├── 📁 kubernetes/           # Kubernetes配置
│   ├── 📁 deployments/
│   ├── 📁 services/
│   ├── 📁 ingress/
│   └── 📁 configmaps/
├── 📁 nginx/               # Nginx配置
├── 📁 ci-cd/               # CI/CD流水线
│   ├── 📄 github-actions.yml
│   ├── 📄 gitlab-ci.yml
│   └── 📄 jenkinsfile
└── 📁 monitoring/          # 监控配置
    ├── 📁 prometheus/
    ├── 📁 grafana/
    └── 📁 loki/
```

## 📚 文档目录
```
docs/
├── 📁 api/                  # API文档
│   ├── 📁 auth/
│   ├── 📁 garbage/
│   ├── 📁 user/
│   └── 📄 api.md
├── 📁 deployment/           # 部署文档
├── 📁 development/          # 开发文档
├── 📁 architecture/         # 架构设计
├── 📁 database/             # 数据库设计
└── 📁 contribution/         # 贡献指南
```

## 🛠️ 开发工具
```
tools/
├── 📁 scripts/              # 工具脚本
│   ├── 📄 setup.sh
│   ├── 📄 build.sh
│   ├── 📄 deploy.sh
│   └── 📄 test.sh
├── 📁 generators/           # 代码生成器
│   ├── 📄 module.generator.ts
│   ├── 📄 entity.generator.ts
│   └── 📄 test.generator.ts
└── 📁 migration/           # 数据迁移工具
```

## 🔧 环境配置
```
.env.example                # 环境变量示例
.env.development           # 开发环境
.env.production            # 生产环境
.env.test                  # 测试环境
```

这个项目结构设计考虑了现代全栈应用的所有关键方面，包括：
- 🎯 模块化架构设计
- 🔐 统一身份认证系统  
- 📱 多端适配（Web、移动端、小程序）
- 🗃️ 清晰的数据库结构
- 🧪 完整的测试体系
- 🚀 自动化部署配置
- 📚 完善的文档体系

每个目录都有明确的职责划分，便于团队协作和代码维护。