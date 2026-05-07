# 易控垃圾 Android 应用

智能垃圾分类系统的 Android 客户端应用，提供垃圾投放记录、附近垃圾桶查询、积分商城和扫码投放等功能。

## 功能特性

### 1. 垃圾投放记录
- 查看历史投放记录
- 按垃圾分类筛选
- 显示投放时间、分类、置信度和积分
- 支持下拉刷新和上拉加载更多

### 2. 附近的垃圾桶
- 基于高德地图显示附近垃圾桶
- 实时显示垃圾桶状态（在线/离线/维护中）
- 显示垃圾桶容量和距离
- 支持不同类型垃圾桶的图标区分

### 3. 积分商城
- 显示当前积分余额
- 浏览可兑换商品
- 商品详情查看
- 积分兑换商品
- 订单管理

### 4. 扫码投放
- 二维码扫描识别垃圾桶
- 支持闪光灯控制
- 扫描结果确认

## 技术栈

- **Flutter**: 3.2.6+
- **Provider**: 状态管理
- **HTTP**: 网络请求
- **高德地图**: 地图服务
- **Mobile Scanner**: 二维码扫描
- **Cached Network Image**: 图片缓存
- **Flutter Secure Storage**: 安全存储

## 项目结构

```
lib/
├── main.dart                      # 应用入口
├── models/                        # 数据模型
│   ├── user.dart
│   ├── classification.dart
│   ├── product.dart
│   └── trashcan.dart
├── providers/                     # 状态管理
│   ├── auth_provider.dart
│   ├── user_provider.dart
│   ├── classification_provider.dart
│   ├── product_provider.dart
│   └── trashcan_provider.dart
├── screens/                       # 页面
│   ├── splash_screen.dart
│   ├── login_screen.dart
│   ├── home_screen.dart
│   ├── records_screen.dart
│   ├── points_screen.dart
│   ├── product_detail_screen.dart
│   ├── orders_screen.dart
│   ├── nearby_trashcans_screen.dart
│   └── scan_screen.dart
└── services/                      # 服务
    └── api_service.dart
```

## 安装步骤

### 1. 环境要求

- Flutter SDK 3.2.6 或更高版本
- Dart SDK 3.2.6 或更高版本
- Android Studio / VS Code
- Android SDK (API 21+)

### 2. 获取依赖

```bash
cd android-app
flutter pub get
```

### 3. 配置后端 API

编辑 `lib/services/api_service.dart`，修改 `baseUrl`：

```dart
static const String baseUrl = 'http://YOUR_SERVER_IP:8081';
```

对于 Android 模拟器，使用：
```dart
static const String baseUrl = 'http://10.0.2.2:8081';
```

对于真机，使用实际的局域网 IP 地址。

### 4. 配置高德地图

编辑 `android/app/src/main/AndroidManifest.xml`，修改高德地图 Key：

```xml
<meta-data
    android:name="com.amap.api.v2.apikey"
    android:value="YOUR_AMAP_API_KEY" />
```

### 5. 运行应用

```bash
flutter run
```

## 生成代码

在修改数据模型后，需要运行以下命令生成 JSON 序列化代码：

```bash
flutter pub run build_runner build --delete-conflicting-outputs
```

## 权限说明

应用需要以下权限：

- **INTERNET**: 网络访问
- **ACCESS_FINE_LOCATION / ACCESS_COARSE_LOCATION**: 位置服务（查找附近垃圾桶）
- **CAMERA**: 相机权限（扫码功能）

这些权限已在 `AndroidManifest.xml` 中声明，并在运行时动态请求。

## API 接口

应用使用以下后端 API：

- `/api/auth/login` - 用户登录
- `/api/auth/register` - 用户注册
- `/api/auth/me` - 获取当前用户信息
- `/api/classification/history` - 获取投放记录
- `/api/classification/categories` - 获取垃圾分类
- `/api/products` - 获取商品列表
- `/api/products/{id}` - 获取商品详情
- `/api/orders` - 获取订单列表
- `/api/orders` - 创建订单
- `/api/admin/devices` - 获取垃圾桶设备列表

## 注意事项

1. **网络配置**: 确保设备和后端服务器在同一网络，或使用公网 IP
2. **高德地图**: 需要申请高德开放平台 API Key
3. **相机权限**: 首次使用扫码功能时会请求相机权限
4. **位置权限**: 首次使用附近垃圾桶功能时会请求位置权限
5. **HTTPS**: 生产环境建议使用 HTTPS

## 开发建议

1. 使用 Flutter DevTools 进行调试
2. 使用 Provider DevTools 监控状态变化
3. 在不同 Android 版本和设备上测试
4. 注意内存管理，特别是图片加载
5. 使用网络拦截器记录 API 请求

## 常见问题

### Q: 应用无法连接后端？
A: 检查 `api_service.dart` 中的 `baseUrl` 配置，确保设备和服务器在同一网络。

### Q: 地图不显示？
A: 检查高德地图 API Key 是否正确配置。

### Q: 扫码功能无法使用？
A: 确保已授予相机权限，并在 `AndroidManifest.xml` 中声明了相机特性。

### Q: 位置权限被拒绝？
A: 在应用设置中手动授予位置权限，或重新安装应用。

## 更新日志

### v1.0.0 (2026-02-15)
- 初始版本发布
- 实现垃圾投放记录功能
- 实现附近垃圾桶查询功能
- 实现积分商城功能
- 实现扫码投放功能

## 许可证

本项目遵循项目的整体许可证。
