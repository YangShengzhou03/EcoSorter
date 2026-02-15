# EcoSorter API接口文档

## 项目概述

EcoSorter是一个垃圾分类管理系统，包含以下模块：
- **Java后端**：基于Spring Boot的RESTful API服务
- **Vue3前端**：用户界面
- **Python垃圾桶后端**：垃圾桶设备管理
- **Vue垃圾桶前端**：垃圾桶设备界面

## 基础信息

- **Base URL**: `http://localhost:8081`
- **Content-Type**: `application/json`
- **认证方式**: Bearer Token (JWT)

## 通用响应格式

### 成功响应
```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

### 错误响应
```json
{
  "code": 400,
  "message": "错误描述",
  "data": null
}
```

### HTTP状态码
- `200`: 请求成功
- `201`: 创建成功
- `400`: 请求参数错误
- `401`: 未授权/登录过期
- `403`: 无权限访问
- `404`: 资源不存在
- `429`: 请求过于频繁
- `500`: 服务器内部错误

---

## 1. 认证模块 (Authentication)

### 1.1 用户注册
**接口**: `POST /api/auth/register`

**请求参数**:
```json
{
  "username": "string (3-20字符, 仅字母数字下划线)",
  "email": "string (有效邮箱)",
  "password": "string (最少6字符)",
  "firstName": "string (可选, 最多50字符)",
  "lastName": "string (可选, 最多50字符)",
  "phone": "string (可选, 有效手机号)"
}
```

**响应**:
```json
{
  "token": "string",
  "refreshToken": "string",
  "user": {
    "id": "string",
    "username": "string",
    "email": "string",
    "role": "string"
  }
}
```

### 1.2 用户登录
**接口**: `POST /api/auth/login`

**请求参数**:
```json
{
  "email": "string",
  "password": "string",
  "twoFactorCode": "string (可选)",
  "rememberMe": "boolean (默认false)"
}
```

**响应**: 同注册响应

### 1.3 刷新Token
**接口**: `POST /api/auth/refresh`

**请求参数**: 
- Query参数: `refreshToken` (string)

**响应**: 同注册响应

### 1.4 用户登出
**接口**: `POST /api/auth/logout`

**权限**: 需要登录

**响应**:
```json
"Logged out successfully"
```

### 1.5 获取当前用户信息
**接口**: `GET /api/auth/me`

**权限**: 需要登录

**响应**:
```json
{
  "id": "string",
  "username": "string",
  "email": "string",
  "role": "string",
  "isActive": "boolean",
  "lastLogin": "string",
  "createdAt": "string",
  "updatedAt": "string",
  "profile": {
    "avatar": "string",
    "phone": "string",
    "fullName": "string"
  }
}
```

---

## 2. 用户模块 (User)

### 2.1 获取用户统计数据
**接口**: `GET /api/user/statistics`

**权限**: 需要登录

**响应**:
```json
{
  "totalClassifications": "integer",
  "totalPoints": "integer",
  "totalBookings": "integer",
  "totalOrders": "integer"
}
```

---

## 3. 管理员模块 (Admin)

### 3.1 获取管理员仪表盘数据
**接口**: `GET /api/admin/dashboard`

**权限**: ADMIN

**响应**:
```json
{
  "totalUsers": "integer",
  "totalBookings": "integer",
  "totalOrders": "integer",
  "totalComplaints": "integer",
  "pendingTasks": "integer",
  "deviceStatus": {
    "online": "integer",
    "offline": "integer",
    "maintenance": "integer"
  }
}
```

### 3.2 获取设备状态
**接口**: `GET /api/admin/device-status`

**权限**: ADMIN

**响应**:
```json
{
  "totalDevices": "integer",
  "onlineDevices": "integer",
  "offlineDevices": "integer",
  "fullTrashcans": "integer",
  "nearFullTrashcans": "integer"
}
```

### 3.3 获取活动记录
**接口**: `GET /api/admin/activities`

**权限**: ADMIN

**响应**:
```json
[
  {
    "id": "string",
    "type": "string",
    "description": "string",
    "timestamp": "string",
    "userId": "string"
  }
]
```

### 3.4 获取用户列表
**接口**: `GET /api/admin/users`

**权限**: ADMIN

**响应**:
```json
[
  {
    "id": "string",
    "username": "string",
    "email": "string",
    "role": "string",
    "isActive": "boolean",
    "createdAt": "string"
  }
]
```

### 3.5 创建用户
**接口**: `POST /api/admin/users`

**权限**: ADMIN

**请求参数**: 同注册请求

**响应**: UserListResponse

### 3.6 更新用户
**接口**: `PUT /api/admin/users/{userId}`

**权限**: ADMIN

**请求参数**:
```json
{
  "role": "string",
  "isActive": "boolean"
}
```

**响应**: UserListResponse

### 3.7 调整用户积分
**接口**: `PUT /api/admin/users/{userId}/points`

**权限**: ADMIN

**请求参数**:
```json
{
  "points": "integer",
  "reason": "string"
}
```

**响应**: 204 No Content

### 3.8 删除用户
**接口**: `DELETE /api/admin/users/{userId}`

**权限**: ADMIN

**响应**: 204 No Content

### 3.9 获取设备列表
**接口**: `GET /api/admin/devices`

**权限**: ADMIN

**响应**:
```json
[
  {
    "id": "string",
    "deviceId": "string",
    "location": "string",
    "status": "string",
    "capacityLevel": "integer",
    "maxCapacity": "integer",
    "threshold": "integer",
    "statusText": "string",
    "lastUpdate": "string"
  }
]
```

### 3.10 创建设备
**接口**: `POST /api/admin/devices`

**权限**: ADMIN

**请求参数**: DeviceListResponse

**响应**: DeviceListResponse

### 3.11 更新设备
**接口**: `PUT /api/admin/devices/{deviceId}`

**权限**: ADMIN

**请求参数**: DeviceListResponse

**响应**: DeviceListResponse

### 3.12 删除设备
**接口**: `DELETE /api/admin/devices/{deviceId}`

**权限**: ADMIN

**响应**: 204 No Content

### 3.13 重新生成设备认证令牌
**接口**: `POST /api/admin/devices/{deviceId}/regenerate-token`

**权限**: ADMIN

**响应**: DeviceListResponse

### 3.14 获取报告列表
**接口**: `GET /api/admin/reports`

**权限**: ADMIN

**响应**:
```json
[
  {
    "id": "string",
    "type": "string",
    "title": "string",
    "description": "string",
    "status": "string",
    "createdAt": "string"
  }
]
```

---

## 4. 预约模块 (Booking)

### 4.1 获取用户预约列表
**接口**: `GET /api/bookings`

**权限**: 需要登录

**查询参数**:
- `page`: 页码 (默认1)
- `pageSize`: 每页数量 (默认10)

**响应**:
```json
{
  "records": [
    {
      "id": "string",
      "type": "string",
      "description": "string",
      "estimatedWeight": "integer",
      "appointmentDate": "string",
      "timeSlot": "string",
      "contactName": "string",
      "contactPhone": "string",
      "address": "string",
      "remark": "string",
      "status": "string",
      "createdAt": "string"
    }
  ],
  "total": "integer",
  "size": "integer",
  "current": "integer"
}
```

### 4.2 获取预约详情
**接口**: `GET /api/bookings/{id}`

**权限**: 需要登录

**响应**: BookingResponse

### 4.3 创建预约
**接口**: `POST /api/bookings`

**权限**: 需要登录

**请求参数**:
```json
{
  "type": "string",
  "description": "string (可选, 最多500字符)",
  "estimatedWeight": "integer",
  "appointmentDate": "string (YYYY-MM-DD格式)",
  "timeSlot": "string",
  "contactName": "string (最多50字符)",
  "contactPhone": "string (有效手机号)",
  "address": "string (最多200字符)",
  "remark": "string (可选, 最多500字符)"
}
```

**响应**: BookingResponse

### 4.4 取消预约
**接口**: `POST /api/bookings/{id}/cancel`

**权限**: 需要登录

**响应**: BookingResponse

---

## 5. 订单模块 (Order)

### 5.1 获取用户订单列表
**接口**: `GET /api/orders`

**权限**: 需要登录

**查询参数**:
- `page`: 页码 (默认1)
- `pageSize`: 每页数量 (默认10)
- `status`: 订单状态 (可选)

**响应**:
```json
{
  "records": [
    {
      "id": "string",
      "userId": "string",
      "productId": "string",
      "productName": "string",
      "quantity": "integer",
      "totalPoints": "integer",
      "status": "string",
      "createdAt": "string",
      "updatedAt": "string"
    }
  ],
  "total": "integer",
  "size": "integer",
  "current": "integer"
}
```

### 5.2 获取所有订单（管理员）
**接口**: `GET /api/orders/all`

**权限**: ADMIN

**查询参数**: 同5.1

**响应**: 同5.1

### 5.3 获取订单详情
**接口**: `GET /api/orders/{id}`

**权限**: 需要登录

**响应**: OrderResponse

### 5.4 创建订单
**接口**: `POST /api/orders`

**权限**: 需要登录

**请求参数**: Order实体

**响应**: OrderResponse

### 5.5 更新订单状态
**接口**: `PUT /api/orders/{id}/status`

**权限**: ADMIN

**查询参数**:
- `status`: 订单状态

**响应**: OrderResponse

### 5.6 更新物流单号
**接口**: `PUT /api/orders/{id}/tracking-number`

**权限**: ADMIN

**查询参数**:
- `trackingNumber`: 物流单号

**响应**: OrderResponse

---

## 6. 积分模块 (Point)

### 6.1 获取积分记录列表
**接口**: `GET /api/points/records`

**权限**: 需要登录

**响应**:
```json
[
  {
    "id": "string",
    "userId": "string",
    "points": "integer",
    "type": "string",
    "reason": "string",
    "createdAt": "string"
  }
]
```

### 6.2 获取积分记录（分页）
**接口**: `GET /api/points/records/page`

**权限**: 需要登录

**查询参数**:
- `page`: 页码 (默认0)
- `size`: 每页数量 (默认10)

**响应**: 分页数据

### 6.3 获取总积分
**接口**: `GET /api/points/total`

**权限**: 需要登录

**响应**: `integer`

---

## 7. 商品模块 (Product)

### 7.1 获取商品列表
**接口**: `GET /api/products`

**查询参数**:
- `page`: 页码 (默认1)
- `pageSize`: 每页数量 (默认10)
- `category`: 商品分类 (可选)
- `status`: 商品状态 (可选)

**响应**:
```json
{
  "records": [
    {
      "id": "string",
      "name": "string",
      "description": "string",
      "category": "string",
      "points": "integer",
      "stock": "integer",
      "image": "string",
      "status": "string",
      "createdAt": "string",
      "updatedAt": "string"
    }
  ],
  "total": "integer",
  "size": "integer",
  "current": "integer"
}
```

### 7.2 获取商品详情
**接口**: `GET /api/products/{id}`

**响应**: ProductResponse

### 7.3 创建商品
**接口**: `POST /api/products`

**权限**: ADMIN

**请求参数**:
```json
{
  "name": "string",
  "description": "string",
  "category": "string",
  "points": "integer",
  "stock": "integer",
  "image": "string",
  "status": "string"
}
```

**响应**: ProductResponse

### 7.4 更新商品
**接口**: `PUT /api/products/{id}`

**权限**: ADMIN

**请求参数**: 同7.3

**响应**: ProductResponse

### 7.5 删除商品
**接口**: `DELETE /api/products/{id}`

**权限**: ADMIN

**响应**: 204 No Content

---

## 8. 分类模块 (Classification)

### 8.1 获取分类历史记录
**接口**: `GET /api/classification/history`

**权限**: 需要登录

**查询参数**:
- `page`: 页码 (默认0)
- `size`: 每页数量 (默认10)
- `sortBy`: 排序字段 (默认createdAt)
- `sortDirection`: 排序方向 (默认desc)

**响应**:
```json
{
  "records": [
    {
      "id": "string",
      "userId": "string",
      "category": "string",
      "confidence": "float",
      "imageUrl": "string",
      "createdAt": "string"
    }
  ],
  "total": "integer",
  "size": "integer",
  "current": "integer"
}
```

### 8.2 获取垃圾类别列表
**接口**: `GET /api/classification/categories`

**响应**:
```json
[
  {
    "id": "string",
    "name": "string",
    "description": "string",
    "color": "string",
    "icon": "string",
    "examples": ["string"]
  }
]
```

### 8.3 创建垃圾类别
**接口**: `POST /api/classification/categories`

**权限**: ADMIN

**请求参数**: WasteCategoryRequest

**响应**: WasteCategoryResponse

### 8.4 更新垃圾类别
**接口**: `PUT /api/classification/categories/{categoryId}`

**权限**: ADMIN

**请求参数**: WasteCategoryRequest

**响应**: WasteCategoryResponse

### 8.5 删除垃圾类别
**接口**: `DELETE /api/classification/categories/{categoryId}`

**权限**: ADMIN

**响应**: 204 No Content

---

## 9. 收集员模块 (Collector)

### 9.1 获取收集员仪表盘数据
**接口**: `GET /api/collector/dashboard`

**权限**: COLLECTOR

**响应**:
```json
{
  "totalTasks": "integer",
  "completedTasks": "integer",
  "pendingTasks": "integer",
  "totalCollections": "integer",
  "todayCollections": "integer"
}
```

### 9.2 获取任务列表
**接口**: `GET /api/collector/tasks`

**权限**: COLLECTOR

**响应**:
```json
[
  {
    "id": "string",
    "trashcanId": "string",
    "trashcanLocation": "string",
    "status": "string",
    "priority": "string",
    "assignedAt": "string",
    "completedAt": "string"
  }
]
```

### 9.3 获取任务详情
**接口**: `GET /api/collector/tasks/{taskId}`

**权限**: COLLECTOR

**响应**: CollectorTaskResponse

### 9.4 开始任务
**接口**: `POST /api/collector/tasks/{taskId}/start`

**权限**: COLLECTOR

**响应**: CollectorTaskResponse

### 9.5 完成任务
**接口**: `POST /api/collector/tasks/{taskId}/complete`

**权限**: COLLECTOR

**响应**: CollectorTaskResponse

### 9.6 报告异常
**接口**: `POST /api/collector/tasks/{taskId}/exception`

**权限**: COLLECTOR

**请求参数**:
```json
{
  "description": "string"
}
```

**响应**: 204 No Content

### 9.7 获取设备列表
**接口**: `GET /api/collector/devices`

**权限**: COLLECTOR

**响应**: DeviceListResponse数组

---

## 10. 收集任务模块 (Collection Task)

### 10.1 根据状态获取任务
**接口**: `GET /api/collection-tasks/status/{status}`

**权限**: ADMIN

**响应**: CollectionTaskResponse数组

### 10.2 生成任务
**接口**: `POST /api/collection-tasks/generate`

**权限**: ADMIN

**响应**: 204 No Content

### 10.3 重新分配任务
**接口**: `POST /api/collection-tasks/{taskId}/reassign`

**权限**: ADMIN

**请求参数**:
```json
{
  "newCollectorId": "integer"
}
```

**响应**: CollectionTaskResponse

### 10.4 获取待处理异常
**接口**: `GET /api/collection-tasks/exceptions/pending`

**权限**: ADMIN

**响应**:
```json
[
  {
    "id": "string",
    "taskId": "string",
    "type": "string",
    "description": "string",
    "status": "string",
    "reportedAt": "string"
  }
]
```

### 10.5 审核异常
**接口**: `POST /api/collection-tasks/exceptions/{exceptionId}/review`

**权限**: ADMIN

**请求参数**:
```json
{
  "status": "string",
  "reviewNotes": "string"
}
```

**响应**: TaskExceptionResponse

---

## 11. 投诉模块 (Complaint)

### 11.1 提交投诉
**接口**: `POST /api/complaints`

**权限**: 需要登录

**请求参数**:
```json
{
  "classificationId": "string",
  "type": "string (最多50字符)",
  "description": "string (5-500字符)"
}
```

**响应**:
```json
{
  "id": "string",
  "userId": "string",
  "classificationId": "string",
  "type": "string",
  "description": "string",
  "status": "string",
  "createdAt": "string",
  "processedAt": "string",
  "processNotes": "string"
}
```

### 11.2 获取我的投诉
**接口**: `GET /api/complaints/my`

**权限**: 需要登录

**响应**: ComplaintResponse数组

### 11.3 获取所有投诉（管理员）
**接口**: `GET /api/complaints/admin`

**权限**: ADMIN

**查询参数**:
- `page`: 页码 (默认1)
- `pageSize`: 每页数量 (默认10)
- `status`: 投诉状态 (可选)

**响应**: 分页数据

### 11.4 获取待处理投诉数量
**接口**: `GET /api/complaints/admin/pending-count`

**权限**: ADMIN

**响应**: `integer`

### 11.5 处理投诉
**接口**: `PUT /api/complaints/admin/{id}`

**权限**: ADMIN

**请求参数**:
```json
{
  "status": "string",
  "processNotes": "string"
}
```

**响应**: ComplaintResponse

### 11.6 删除投诉
**接口**: `DELETE /api/complaints/{id}`

**权限**: 需要登录

**响应**: 204 No Content

---

## 12. 通知模块 (Notice)

### 12.1 获取通知列表
**接口**: `GET /api/notices`

**查询参数**:
- `page`: 页码 (默认1)
- `pageSize`: 每页数量 (默认10)
- `keyword`: 关键词搜索 (可选)

**响应**:
```json
{
  "records": [
    {
      "id": "string",
      "title": "string",
      "content": "string",
      "type": "string",
      "status": "string",
      "priority": "string",
      "createdAt": "string",
      "updatedAt": "string"
    }
  ],
  "total": "integer",
  "size": "integer",
  "current": "integer"
}
```

### 12.2 获取已发布通知
**接口**: `GET /api/notices/published`

**响应**: NoticeResponse数组

### 12.3 获取通知详情
**接口**: `GET /api/notices/{id}`

**响应**: NoticeResponse

### 12.4 创建通知
**接口**: `POST /api/notices`

**权限**: ADMIN

**请求参数**:
```json
{
  "title": "string",
  "content": "string",
  "type": "string",
  "priority": "string",
  "status": "string"
}
```

**响应**: NoticeResponse

### 12.5 更新通知
**接口**: `PUT /api/notices/{id}`

**权限**: ADMIN

**请求参数**: 同12.4

**响应**: NoticeResponse

### 12.6 删除通知
**接口**: `DELETE /api/notices/{id}`

**权限**: ADMIN

**响应**: 204 No Content

---

## 13. 轮播图模块 (Banner)

### 13.1 获取活跃轮播图
**接口**: `GET /api/banners`

**查询参数**:
- `target`: 目标位置 (可选)

**响应**:
```json
[
  {
    "id": "string",
    "title": "string",
    "imageUrl": "string",
    "link": "string",
    "target": "string",
    "order": "integer",
    "isActive": "boolean",
    "createdAt": "string",
    "updatedAt": "string"
  }
]
```

### 13.2 获取轮播图详情
**接口**: `GET /api/banners/{id}`

**响应**: BannerResponse

### 13.3 创建轮播图
**接口**: `POST /api/banners`

**权限**: ADMIN

**请求参数**:
```json
{
  "title": "string",
  "imageUrl": "string",
  "link": "string",
  "target": "string",
  "order": "integer",
  "isActive": "boolean"
}
```

**响应**: BannerResponse

### 13.4 更新轮播图
**接口**: `PUT /api/banners/{id}`

**权限**: ADMIN

**请求参数**: 同13.3

**响应**: BannerResponse

### 13.5 删除轮播图
**接口**: `DELETE /api/banners/{id}`

**权限**: ADMIN

**响应**: 204 No Content

---

## 14. 个人资料模块 (Profile)

### 14.1 获取个人资料
**接口**: `GET /api/profile`

**权限**: 需要登录

**响应**:
```json
{
  "id": "string",
  "username": "string",
  "email": "string",
  "phone": "string",
  "fullName": "string",
  "avatarUrl": "string",
  "address": "string",
  "createdAt": "string",
  "updatedAt": "string"
}
```

### 14.2 更新个人资料
**接口**: `PUT /api/profile`

**权限**: 需要登录

**请求参数**: User实体

**响应**: ProfileResponse

### 14.3 更新头像
**接口**: `PUT /api/profile/avatar`

**权限**: 需要登录

**请求参数**:
```json
{
  "avatar": "string (头像URL)"
}
```

**响应**: ProfileResponse

### 14.4 修改密码
**接口**: `POST /api/profile/change-password`

**权限**: 需要登录

**请求参数**:
```json
{
  "oldPassword": "string",
  "newPassword": "string"
}
```

**响应**: 204 No Content

---

## 15. 文件上传模块 (Upload)

### 15.1 上传头像
**接口**: `POST /api/upload/avatar`

**请求类型**: `multipart/form-data`

**请求参数**:
- `file`: 文件对象

**响应**:
```json
{
  "url": "string",
  "filename": "string",
  "size": "integer"
}
```

---

## 16. 垃圾桶设备模块 (Trashcan)

### 16.1 获取设备信息
**接口**: `GET /api/trashcan/me`

**权限**: 需要设备认证令牌

**响应**:
```json
{
  "id": "string",
  "deviceId": "string",
  "location": "string",
  "capacityLevel": "integer",
  "maxCapacity": "integer",
  "threshold": "integer",
  "status": "string",
  "statusText": "string",
  "lastUpdate": "string"
}
```

### 16.2 更新设备状态
**接口**: `PUT /api/trashcan/status`

**权限**: 需要设备认证令牌

**请求参数**:
```json
{
  "capacityLevel": "integer"
}
```

**响应**: DeviceListResponse

### 16.3 提交分类记录
**接口**: `POST /api/trashcan/classification`

**权限**: 需要设备认证令牌

**请求参数**:
```json
{
  "imageUrl": "string",
  "categoryId": "integer",
  "confidence": "float"
}
```

**响应**: 204 No Content

---

## 接口问题汇总

### 🔴 严重问题

1. **密码明文存储和比较** [ProfileController.java:74](file:///d:/Code/web/eco-sorter/ecosorter-backend/src/main/java/com/ecosorter/controller/ProfileController.java#L74)
   - 问题：`user.getPassword().equals(request.getOldPassword())` 直接比较明文密码
   - 影响：严重的安全漏洞，密码应该加密存储
   - 建议：使用BCrypt等加密算法存储和验证密码

2. **订单接口直接使用实体对象** [OrderController.java:58](file:///d:/Code/web/eco-sorter/ecosorter-backend/src/main/java/com/ecosorter/controller/OrderController.java#L58)
   - 问题：`createOrder` 方法直接接收 `Order` 实体对象
   - 影响：违反分层架构原则，可能导致安全问题和数据泄露
   - 建议：创建专门的 DTO 对象

### 🟡 中等问题

3. **分页参数不一致**
   - 问题：不同接口的分页参数命名不统一
     - 有的使用 `page` 和 `pageSize` (Booking, Order, Product, Notice)
     - 有的使用 `page` 和 `size` (Point, Classification)
   - 影响：开发混乱，容易出错
   - 建议：统一使用 `page` 和 `pageSize`，统一起始页码

4. **任务ID类型不一致** [CollectionTaskController.java:43](file:///d:/Code/web/eco-sorter/ecosorter-backend/src/main/java/com/ecosorter/controller/CollectionTaskController.java#L43)
   - 问题：`reassignTask` 方法中 `taskId` 参数类型为 `String`，但其他地方使用 `Long`
   - 影响：类型转换错误，可能导致数据不一致
   - 建议：统一使用 `Long` 类型

5. **积分类型转换** [AdminController.java:54](file:///d:/Code/web/eco-sorter/ecosorter-backend/src/main/java/com/ecosorter/controller/AdminController.java#L54)
   - 问题：`adjustUserPoints` 方法将 `Long` 类型的 `points` 转换为 `Integer`
   - 影响：可能丢失精度
   - 建议：统一使用 `Long` 类型或添加边界检查

6. **文件上传缺少验证** [UploadController.java:20](file:///d:/Code/web/eco-sorter/ecosorter-backend/src/main/java/com/ecosorter/controller/UploadController.java#L20)
   - 问题：只检查文件是否为空，没有文件类型、大小限制
   - 影响：可能上传恶意文件或过大文件
   - 建议：添加文件类型白名单和大小限制

7. **垃圾桶认证令牌安全性** [TrashcanController.java:28](file:///d:/Code/web/eco-sorter/ecosorter-backend/src/main/java/com/ecosorter/controller/TrashcanController.java#L28)
   - 问题：直接从请求头获取令牌，没有验证令牌格式和有效期
   - 影响：可能被绕过认证
   - 建议：使用JWT或其他安全令牌机制

### 🟢 轻微问题

8. **缺少未读通知数量接口**
   - 问题：文档中提到 `GET /api/notices/unread/count` 接口，但实际代码中不存在
   - 影响：前端无法获取未读通知数量
   - 建议：实现该接口或从文档中移除

9. **缺少通知已读标记接口**
   - 问题：文档中提到 `POST /api/notices/{id}/read` 和 `POST /api/notices/read-all` 接口，但实际代码中不存在
   - 影响：无法标记通知为已读
   - 建议：实现该接口或从文档中移除

10. **缺少图片上传接口**
    - 问题：文档中提到 `POST /api/upload/image` 接口，但实际代码中只有 `POST /api/upload/avatar`
    - 影响：无法上传通用图片
    - 建议：实现该接口或从文档中移除
