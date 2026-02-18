import json
import os
from datetime import datetime
from pathlib import Path
from typing import Optional, List

import face_recognition
import numpy as np
import pymysql
import requests
from PIL import Image
from fastapi import FastAPI, File, UploadFile, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from ultralytics import YOLO

app = FastAPI(title="EcoSorter Recognition API", version="2.0.0")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

DATABASE_CONFIG = {
    'host': os.getenv('DB_HOST', 'localhost'),
    'user': os.getenv('DB_USERNAME', 'root'),
    'password': os.getenv('DB_PASSWORD', '123456'),
    'database': os.getenv('DB_NAME', 'eco_sorter'),
    'charset': 'utf8mb4'
}

UPLOAD_BASE_DIR = Path(__file__).parent / "uploads"
FACE_UPLOAD_DIR = UPLOAD_BASE_DIR / "face"
RECOGNITION_UPLOAD_DIR = UPLOAD_BASE_DIR / "recognition"
TEMP_UPLOAD_DIR = UPLOAD_BASE_DIR / "temp"

FACE_UPLOAD_DIR.mkdir(parents=True, exist_ok=True)
RECOGNITION_UPLOAD_DIR.mkdir(parents=True, exist_ok=True)
TEMP_UPLOAD_DIR.mkdir(parents=True, exist_ok=True)

MODEL_DIR = Path(__file__).parent / "models"
MODEL_DIR.mkdir(exist_ok=True)

yolo_model = None

COCO_TO_GARBAGE = {
    0: {"name": "person", "category": "其他垃圾", "cn_name": "人体"},
    1: {"name": "bicycle", "category": "可回收物", "cn_name": "自行车"},
    2: {"name": "car", "category": "可回收物", "cn_name": "汽车"},
    3: {"name": "motorcycle", "category": "可回收物", "cn_name": "摩托车"},
    4: {"name": "airplane", "category": "可回收物", "cn_name": "飞机"},
    5: {"name": "bus", "category": "可回收物", "cn_name": "公交车"},
    6: {"name": "train", "category": "可回收物", "cn_name": "火车"},
    7: {"name": "truck", "category": "可回收物", "cn_name": "卡车"},
    8: {"name": "boat", "category": "可回收物", "cn_name": "船"},
    9: {"name": "traffic light", "category": "可回收物", "cn_name": "交通灯"},
    10: {"name": "fire hydrant", "category": "其他垃圾", "cn_name": "消防栓"},
    11: {"name": "stop sign", "category": "可回收物", "cn_name": "停车标志"},
    12: {"name": "parking meter", "category": "可回收物", "cn_name": "停车计时器"},
    13: {"name": "bench", "category": "其他垃圾", "cn_name": "长椅"},
    14: {"name": "bird", "category": "厨余垃圾", "cn_name": "鸟类"},
    15: {"name": "cat", "category": "厨余垃圾", "cn_name": "猫"},
    16: {"name": "dog", "category": "厨余垃圾", "cn_name": "狗"},
    17: {"name": "horse", "category": "厨余垃圾", "cn_name": "马"},
    18: {"name": "sheep", "category": "厨余垃圾", "cn_name": "羊"},
    19: {"name": "cow", "category": "厨余垃圾", "cn_name": "牛"},
    20: {"name": "elephant", "category": "厨余垃圾", "cn_name": "大象"},
    21: {"name": "bear", "category": "厨余垃圾", "cn_name": "熊"},
    22: {"name": "zebra", "category": "厨余垃圾", "cn_name": "斑马"},
    23: {"name": "giraffe", "category": "厨余垃圾", "cn_name": "长颈鹿"},
    24: {"name": "backpack", "category": "可回收物", "cn_name": "背包"},
    25: {"name": "umbrella", "category": "可回收物", "cn_name": "雨伞"},
    26: {"name": "handbag", "category": "可回收物", "cn_name": "手提包"},
    27: {"name": "tie", "category": "可回收物", "cn_name": "领带"},
    28: {"name": "suitcase", "category": "可回收物", "cn_name": "行李箱"},
    29: {"name": "frisbee", "category": "可回收物", "cn_name": "飞盘"},
    30: {"name": "skis", "category": "可回收物", "cn_name": "滑雪板"},
    31: {"name": "snowboard", "category": "可回收物", "cn_name": "滑雪单板"},
    32: {"name": "sports ball", "category": "可回收物", "cn_name": "运动球"},
    33: {"name": "kite", "category": "其他垃圾", "cn_name": "风筝"},
    34: {"name": "baseball bat", "category": "可回收物", "cn_name": "棒球棒"},
    35: {"name": "baseball glove", "category": "可回收物", "cn_name": "棒球手套"},
    36: {"name": "skateboard", "category": "可回收物", "cn_name": "滑板"},
    37: {"name": "surfboard", "category": "可回收物", "cn_name": "冲浪板"},
    38: {"name": "tennis racket", "category": "可回收物", "cn_name": "网球拍"},
    39: {"name": "bottle", "category": "可回收物", "cn_name": "瓶子"},
    40: {"name": "wine glass", "category": "可回收物", "cn_name": "玻璃杯"},
    41: {"name": "cup", "category": "可回收物", "cn_name": "杯子"},
    42: {"name": "fork", "category": "可回收物", "cn_name": "叉子"},
    43: {"name": "knife", "category": "可回收物", "cn_name": "刀"},
    44: {"name": "spoon", "category": "可回收物", "cn_name": "勺子"},
    45: {"name": "bowl", "category": "可回收物", "cn_name": "碗"},
    46: {"name": "banana", "category": "厨余垃圾", "cn_name": "香蕉"},
    47: {"name": "apple", "category": "厨余垃圾", "cn_name": "苹果"},
    48: {"name": "sandwich", "category": "厨余垃圾", "cn_name": "三明治"},
    49: {"name": "orange", "category": "厨余垃圾", "cn_name": "橙子"},
    50: {"name": "broccoli", "category": "厨余垃圾", "cn_name": "西兰花"},
    51: {"name": "carrot", "category": "厨余垃圾", "cn_name": "胡萝卜"},
    52: {"name": "hot dog", "category": "厨余垃圾", "cn_name": "热狗"},
    53: {"name": "pizza", "category": "厨余垃圾", "cn_name": "披萨"},
    54: {"name": "donut", "category": "厨余垃圾", "cn_name": "甜甜圈"},
    55: {"name": "cake", "category": "厨余垃圾", "cn_name": "蛋糕"},
    56: {"name": "chair", "category": "可回收物", "cn_name": "椅子"},
    57: {"name": "couch", "category": "可回收物", "cn_name": "沙发"},
    58: {"name": "potted plant", "category": "厨余垃圾", "cn_name": "盆栽"},
    59: {"name": "bed", "category": "可回收物", "cn_name": "床"},
    60: {"name": "dining table", "category": "可回收物", "cn_name": "餐桌"},
    61: {"name": "toilet", "category": "其他垃圾", "cn_name": "马桶"},
    62: {"name": "tv", "category": "可回收物", "cn_name": "电视"},
    63: {"name": "laptop", "category": "可回收物", "cn_name": "笔记本电脑"},
    64: {"name": "mouse", "category": "可回收物", "cn_name": "鼠标"},
    65: {"name": "remote", "category": "可回收物", "cn_name": "遥控器"},
    66: {"name": "keyboard", "category": "可回收物", "cn_name": "键盘"},
    67: {"name": "cell phone", "category": "可回收物", "cn_name": "手机"},
    68: {"name": "microwave", "category": "可回收物", "cn_name": "微波炉"},
    69: {"name": "oven", "category": "可回收物", "cn_name": "烤箱"},
    70: {"name": "toaster", "category": "可回收物", "cn_name": "烤面包机"},
    71: {"name": "sink", "category": "可回收物", "cn_name": "水槽"},
    72: {"name": "refrigerator", "category": "可回收物", "cn_name": "冰箱"},
    73: {"name": "book", "category": "可回收物", "cn_name": "书籍"},
    74: {"name": "clock", "category": "可回收物", "cn_name": "时钟"},
    75: {"name": "vase", "category": "可回收物", "cn_name": "花瓶"},
    76: {"name": "scissors", "category": "可回收物", "cn_name": "剪刀"},
    77: {"name": "teddy bear", "category": "可回收物", "cn_name": "泰迪熊"},
    78: {"name": "hair drier", "category": "可回收物", "cn_name": "吹风机"},
    79: {"name": "toothbrush", "category": "其他垃圾", "cn_name": "牙刷"},
}

CATEGORY_ADVICE = {
    '可回收物': '请投放到可回收物垃圾桶（蓝色）',
    '有害垃圾': '请投放到有害垃圾桶（红色）',
    '厨余垃圾': '请投放到厨余垃圾桶（绿色）',
    '其他垃圾': '请投放到其他垃圾桶（灰色）'
}

def get_yolo_model():
    global yolo_model
    if yolo_model is None:
        model_path = MODEL_DIR / "yolov8s.pt"
        if model_path.exists():
            yolo_model = YOLO(str(model_path))
        else:
            print(f"[INFO] 正在下载 YOLOv8s 模型到 {model_path}...")
            MODEL_DIR.mkdir(parents=True, exist_ok=True)
            temp_model = YOLO("yolov8s.pt")
            temp_model.save(str(model_path))
            yolo_model = YOLO(str(model_path))
            root_model = Path(__file__).parent / "yolov8s.pt"
            if root_model.exists():
                os.remove(root_model)
        print(f"[INFO] YOLOv8s 模型加载完成，已映射 {len(COCO_TO_GARBAGE)} 类物品")
    return yolo_model

def get_db_connection():
    return pymysql.connect(**DATABASE_CONFIG)

def get_category_id(category_name: str) -> Optional[int]:
    try:
        conn = get_db_connection()
        cursor = conn.cursor(pymysql.cursors.DictCursor)
        cursor.execute("SELECT id FROM waste_categories WHERE name = %s", (category_name,))
        result = cursor.fetchone()
        cursor.close()
        conn.close()
        return result['id'] if result else None
    except:
        return None

def process_image_for_face(image_path: str):
    try:
        pil_image = Image.open(image_path)
        if pil_image.mode == 'RGBA':
            background = Image.new('RGB', pil_image.size, (255, 255, 255))
            background.paste(pil_image, mask=pil_image.split()[3])
            pil_image = background
        elif pil_image.mode != 'RGB':
            pil_image = pil_image.convert('RGB')
        return np.array(pil_image, dtype=np.uint8)
    except Exception as e:
        print(f"[ERROR] 图片处理失败: {e}")
        return None

class RecognitionResult(BaseModel):
    item: str
    category: str
    confidence: int
    advice: str
    categoryId: Optional[int] = None

class RecognitionResponse(BaseModel):
    data: RecognitionResult
    success: bool

class MultiRecognitionItem(BaseModel):
    item: str
    category: str
    confidence: float
    advice: str
    categoryId: Optional[int] = None

class MultiRecognitionResponse(BaseModel):
    data: List[MultiRecognitionItem]
    success: bool
    count: int

class FaceRegisterResponse(BaseModel):
    success: bool
    message: str

class FaceVerifyResponse(BaseModel):
    success: bool
    verified: bool
    userId: Optional[int] = None
    username: Optional[str] = None
    confidence: float = 0.0
    message: str

@app.get("/")
async def root():
    return {
        "message": "EcoSorter Recognition API v2.0",
        "port": 9000,
        "status": "running",
        "model": "YOLOv8s",
        "classes": len(COCO_TO_GARBAGE)
    }

@app.post("/api/recognition/recognize", response_model=RecognitionResponse)
async def recognize_garbage(image_url: str, authorization: Optional[str] = None):
    try:
        model = get_yolo_model()
        
        temp_file = None
        image_path = image_url
        
        if image_url.startswith('http://localhost') or image_url.startswith('http://127.0.0.1'):
            print(f"[INFO] 检测到本地URL，正在下载图片: {image_url}")
            response = requests.get(image_url, timeout=10)
            if response.status_code == 200:
                temp_file = TEMP_UPLOAD_DIR / f"temp_{datetime.now().strftime('%Y%m%d_%H%M%S_%f')}.jpg"
                with open(temp_file, "wb") as f:
                    f.write(response.content)
                image_path = str(temp_file)
                print(f"[INFO] 图片已下载到本地: {temp_file}")
            else:
                raise HTTPException(status_code=400, detail=f"无法下载图片: HTTP {response.status_code}")
        
        results = model(image_path, verbose=False)
        
        if temp_file and temp_file.exists():
            os.remove(temp_file)
        
        best_detection = None
        best_confidence = 0
        
        for result in results:
            for box in result.boxes:
                cls_id = int(box.cls[0])
                conf = float(box.conf[0])
                
                if cls_id in COCO_TO_GARBAGE and conf > best_confidence:
                    best_confidence = conf
                    best_detection = COCO_TO_GARBAGE[cls_id].copy()
                    best_detection['confidence'] = conf
        
        if best_detection:
            category = best_detection['category']
            item_name = best_detection['cn_name']
            confidence = int(best_detection['confidence'] * 100)
        else:
            category = "其他垃圾"
            item_name = "未识别物品"
            confidence = 50
        
        category_id = get_category_id(category)
        advice = CATEGORY_ADVICE.get(category, '请正确分类投放')
        
        return RecognitionResponse(
            data=RecognitionResult(
                item=item_name,
                category=category,
                confidence=confidence,
                advice=advice,
                categoryId=category_id
            ),
            success=True
        )
    except Exception as e:
        print(f"[ERROR] 识别失败: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/api/recognition/recognize-with-file", response_model=RecognitionResponse)
async def recognize_garbage_with_file(file: UploadFile = File(...)):
    try:
        contents = await file.read()
        temp_path = RECOGNITION_UPLOAD_DIR / f"recognize_{datetime.now().strftime('%Y%m%d_%H%M%S_%f')}.jpg"
        with open(temp_path, "wb") as f:
            f.write(contents)
        
        model = get_yolo_model()
        results = model(str(temp_path), verbose=False)
        
        os.remove(temp_path)
        
        best_detection = None
        best_confidence = 0
        
        for result in results:
            for box in result.boxes:
                cls_id = int(box.cls[0])
                conf = float(box.conf[0])
                
                if cls_id in COCO_TO_GARBAGE and conf > best_confidence:
                    best_confidence = conf
                    best_detection = COCO_TO_GARBAGE[cls_id].copy()
                    best_detection['confidence'] = conf
        
        if best_detection:
            category = best_detection['category']
            item_name = best_detection['cn_name']
            confidence = int(best_detection['confidence'] * 100)
        else:
            category = "其他垃圾"
            item_name = "未识别物品"
            confidence = 50
        
        category_id = get_category_id(category)
        advice = CATEGORY_ADVICE.get(category, '请正确分类投放')
        
        return RecognitionResponse(
            data=RecognitionResult(
                item=item_name,
                category=category,
                confidence=confidence,
                advice=advice,
                categoryId=category_id
            ),
            success=True
        )
    except Exception as e:
        print(f"[ERROR] 识别失败: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/api/recognition/recognize-multi", response_model=MultiRecognitionResponse)
async def recognize_garbage_multi(file: UploadFile = File(...)):
    try:
        contents = await file.read()
        temp_path = RECOGNITION_UPLOAD_DIR / f"multi_{datetime.now().strftime('%Y%m%d_%H%M%S_%f')}.jpg"
        with open(temp_path, "wb") as f:
            f.write(contents)
        
        model = get_yolo_model()
        results = model(str(temp_path), verbose=False)
        
        detections = []
        seen_items = set()
        
        for result in results:
            for box in result.boxes:
                cls_id = int(box.cls[0])
                conf = float(box.conf[0])
                
                if cls_id in COCO_TO_GARBAGE and conf >= 0.5:
                    item_info = COCO_TO_GARBAGE[cls_id]
                    item_key = f"{item_info['cn_name']}_{item_info['category']}"
                    
                    if item_key not in seen_items:
                        seen_items.add(item_key)
                        category = item_info['category']
                        detections.append(MultiRecognitionItem(
                            item=item_info['cn_name'],
                            category=category,
                            confidence=round(conf, 2),
                            advice=CATEGORY_ADVICE.get(category, '请正确分类投放'),
                            categoryId=get_category_id(category)
                        ))
        
        os.remove(temp_path)
        
        return MultiRecognitionResponse(
            data=detections,
            success=True,
            count=len(detections)
        )
    except Exception as e:
        print(f"[ERROR] 多目标识别失败: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/api/face/register-with-file", response_model=FaceRegisterResponse)
async def register_face_with_file(userId: int, file: UploadFile = File(...)):
    try:
        contents = await file.read()
        
        if len(contents) > 512 * 1024:
            return FaceRegisterResponse(success=False, message="文件大小超过512KB限制")
        
        file_path = FACE_UPLOAD_DIR / f"face_{userId}_{datetime.now().strftime('%Y%m%d_%H%M%S_%f')}.jpg"
        with open(file_path, "wb") as f:
            f.write(contents)
        
        image = process_image_for_face(str(file_path))
        if image is None:
            return FaceRegisterResponse(success=False, message="无法读取图片文件")
        
        face_encodings = face_recognition.face_encodings(image)
        
        if len(face_encodings) == 0:
            return FaceRegisterResponse(success=False, message="无法检测到人脸，请确保照片中有清晰的正脸")
        
        encoding = json.dumps(face_encodings[0].tolist())
        
        conn = get_db_connection()
        cursor = conn.cursor()
        cursor.execute(
            "UPDATE users SET face_encoding = %s, face_verified = 1 WHERE id = %s",
            (encoding, userId)
        )
        conn.commit()
        cursor.close()
        conn.close()
        
        return FaceRegisterResponse(success=True, message="人脸注册成功")
    except Exception as e:
        print(f"[ERROR] 人脸注册失败: {str(e)}")
        raise HTTPException(status_code=500, detail=f"人脸注册失败: {str(e)}")

@app.post("/api/face/verify-with-file", response_model=FaceVerifyResponse)
async def verify_face_with_file(file: UploadFile = File(...)):
    try:
        contents = await file.read()
        
        if len(contents) > 512 * 1024:
            return FaceVerifyResponse(success=False, verified=False, message="文件大小超过512KB限制")
        
        temp_path = FACE_UPLOAD_DIR / f"verify_{datetime.now().strftime('%Y%m%d_%H%M%S_%f')}.jpg"
        with open(temp_path, "wb") as f:
            f.write(contents)
        
        image = process_image_for_face(str(temp_path))
        if image is None:
            return FaceVerifyResponse(success=False, verified=False, message="无法读取图片文件")
        
        face_encodings = face_recognition.face_encodings(image)
        
        if len(face_encodings) == 0:
            return FaceVerifyResponse(success=True, verified=False, message="无法检测到人脸")
        
        current_encoding = json.dumps(face_encodings[0].tolist())
        
        conn = get_db_connection()
        cursor = conn.cursor()
        cursor.execute(
            "SELECT id, username, face_encoding FROM users WHERE face_encoding IS NOT NULL AND face_verified = 1 AND role = 'RESIDENT'"
        )
        users = cursor.fetchall()
        cursor.close()
        conn.close()
        
        best_score = 0.0
        best_user_id = None
        best_username = None
        
        for user in users:
            user_id, username, stored_encoding = user
            if stored_encoding:
                try:
                    arr1 = json.loads(current_encoding)
                    arr2 = json.loads(stored_encoding)
                    distance = sum((a - b) ** 2 for a, b in zip(arr1, arr2)) ** 0.5
                    similarity = 1.0 / (1.0 + distance)
                    print(f"[DEBUG] 用户 {username}(ID:{user_id}) 相似度: {similarity:.4f}, 距离: {distance:.4f}")
                    
                    if similarity > best_score:
                        best_score = similarity
                        best_user_id = user_id
                        best_username = username
                except:
                    pass
        
        threshold = 0.7
        verified = best_score >= threshold
        
        if verified:
            return FaceVerifyResponse(
                success=True,
                verified=True,
                userId=best_user_id,
                username=best_username,
                confidence=best_score,
                message=f"验证成功，匹配用户: {best_username}"
            )
        else:
            return FaceVerifyResponse(
                success=True,
                verified=False,
                confidence=best_score,
                message="未找到匹配的用户"
            )
    except Exception as e:
        print(f"[ERROR] 人脸验证失败: {str(e)}")
        raise HTTPException(status_code=500, detail=f"人脸验证失败: {str(e)}")

@app.get("/api/health")
async def health_check():
    model = get_yolo_model()
    return {
        "status": "healthy",
        "port": 9000,
        "service": "EcoSorter Recognition",
        "version": "2.0.0",
        "model": "YOLOv8s",
        "classes_mapped": len(COCO_TO_GARBAGE),
        "model_loaded": model is not None
    }

@app.get("/api/model/info")
async def model_info():
    return {
        "model_name": "YOLOv8s",
        "model_type": "object_detection",
        "total_classes": 80,
        "classes_mapped": len(COCO_TO_GARBAGE),
        "categories": {
            "可回收物": "金属、塑料、玻璃、纸张、电子产品等",
            "厨余垃圾": "食物、水果、植物、动物等",
            "其他垃圾": "一次性用品、陶瓷、卫生用品等",
            "有害垃圾": "电池、药品等（需自定义训练）"
        }
    }
