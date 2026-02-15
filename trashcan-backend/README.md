# YOLO26 垃圾识别服务

## 概述

这是一个基于 FastAPI 的 YOLO26 垃圾识别服务，目前为**示例模式**，使用随机模拟识别结果。

## 功能特性

- ✅ 垃圾图片识别（示例模式）
- ✅ 图片上传
- ✅ 分类记录保存到数据库
- ✅ JWT Token 验证
- ✅ CORS 支持
- ✅ 健康检查接口

## 安装依赖

```bash
pip install -r requirements.txt
```

## 配置

### 数据库配置

在 `yolo_recognition.py` 中修改数据库配置：

```python
DATABASE_CONFIG = {
    'host': 'localhost',
    'user': 'root',
    'password': '123456',
    'database': 'eco_sorter',
    'charset': 'utf8mb4'
}
```

### JWT Secret

确保 JWT secret 与主后端一致：

```python
secret = "ecosorter-jwt-secret-key-2024-change-in-production"
```

## 启动服务

```bash
python yolo_recognition.py
```

服务将在 `http://localhost:9000` 启动

## API 接口

### 1. 垃圾识别

**POST** `/api/recognition/recognize`

请求参数：
- `image_url`: 图片URL（字符串）
- `authorization`: JWT Token（可选，Bearer Token）

响应示例：
```json
{
  "data": {
    "item": "塑料瓶",
    "category": "可回收物",
    "confidence": 85,
    "advice": "请投放到可回收物垃圾桶",
    "categoryId": 1
  },
  "success": true
}
```

### 2. 提交分类记录

**POST** `/api/recognition/classification`

请求参数：
- `image_url`: 图片URL（字符串）
- `category_id`: 分类ID（可选）
- `confidence`: 置信度（可选，默认0.85）
- `authorization`: JWT Token（可选）
- `ip_address`: IP地址（可选）
- `user_agent`: 用户代理（可选）

响应示例：
```json
{
  "success": true,
  "message": "分类记录保存成功"
}
```

### 3. 图片上传

**POST** `/api/upload`

请求参数：
- `file`: 图片文件（multipart/form-data）

响应示例：
```json
{
  "url": "/uploads/20231215_143025_test.jpg",
  "filename": "20231215_143025_test.jpg",
  "message": "上传成功"
}
```

### 4. 健康检查

**GET** `/api/health`

响应示例：
```json
{
  "status": "healthy",
  "port": 9000,
  "service": "YOLO26 Recognition",
  "mode": "示例模式"
}
```

### 5. 测试识别

**GET** `/api/test/recognize`

响应示例：
```json
{
  "test_image": "http://example.com/test.jpg",
  "recognition_result": {
    "item": "塑料瓶",
    "category": "可回收物",
    "confidence": 85
  },
  "message": "YOLO26识别测试成功"
}
```

## 示例模式说明

当前实现使用随机模拟识别结果，包含以下垃圾类别：

### 可回收物
- 塑料瓶
- 纸箱
- 玻璃瓶
- 金属罐

### 有害垃圾
- 废电池
- 废灯管
- 过期药品

### 厨余垃圾
- 果皮
- 菜叶
- 剩饭
- 鱼骨

### 其他垃圾
- 塑料袋
- 烟头
- 纸巾
- 陶瓷碎片

## 集成真正的 YO26 模型

要集成真正的 YO26 模型，需要：

1. 安装 YO26 相关依赖
2. 加载预训练模型
3. 替换 `yolo26_recognize()` 函数的实现

示例：

```python
from ultralytics import YOLO

def yolo26_recognize(image_url: str) -> Dict[str, Any]:
    model = YOLO("yolov8n.pt")
    
    results = model(image_url)
    
    class_names = model.names
    detected_class = results[0].boxes[0].cls
    confidence = results[0].boxes[0].conf
    
    item = class_names[int(detected_class)]
    category = map_to_waste_category(item)
    
    return {
        "item": item,
        "category": category,
        "confidence": int(confidence * 100)
    }
```

## 垃圾桶前端集成

垃圾桶前端已配置为调用此服务：

- 图片上传：`POST http://localhost:9000/api/upload`
- 垃圾识别：`POST http://localhost:9000/api/recognition/recognize`
- 提交分类：`POST http://localhost:9000/api/recognition/classification`

## 测试

启动服务后，可以访问：

- 服务状态：`http://localhost:9000/`
- 健康检查：`http://localhost:9000/api/health`
- 测试识别：`http://localhost:9000/api/test/recognize`

## 注意事项

1. **示例模式**：当前实现使用随机模拟，不是真正的 AI 识别
2. **数据库连接**：确保 MySQL 数据库正在运行且配置正确
3. **JWT Token**：确保 JWT secret 与主后端一致
4. **端口占用**：确保 9000 端口未被占用
5. **文件上传**：确保 `uploads` 目录有写入权限

## 未来改进

- [ ] 集成真正的 YO26 模型
- [ ] 添加批量识别功能
- [ ] 优化识别准确率
- [ ] 添加识别历史记录
- [ ] 支持更多垃圾类别
- [ ] 添加图片预处理功能

## 许可证

MIT License
