from fastapi import FastAPI, File, UploadFile, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse
from pydantic import BaseModel
from typing import Optional, List, Dict, Any
import uvicorn
import pymysql
import random
from datetime import datetime
import os
import shutil
from pathlib import Path

app = FastAPI(title="YOLO26 Garbage Recognition API", version="1.0.0")

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

JWT_SECRET = os.getenv('JWT_SECRET', 'ecosorter-jwt-secret-key-2024-change-in-production')

UPLOAD_DIR = Path("uploads")
UPLOAD_DIR.mkdir(exist_ok=True)

class RecognitionResult(BaseModel):
    item: str
    category: str
    confidence: int
    advice: str
    categoryId: Optional[int] = None

class RecognitionResponse(BaseModel):
    data: RecognitionResult
    success: bool

GARBAGE_ITEMS = [
    {"item": "塑料瓶", "category": "可回收物"},
    {"item": "纸箱", "category": "可回收物"},
    {"item": "玻璃瓶", "category": "可回收物"},
    {"item": "金属罐", "category": "可回收物"},
    {"item": "废电池", "category": "有害垃圾"},
    {"item": "废灯管", "category": "有害垃圾"},
    {"item": "过期药品", "category": "有害垃圾"},
    {"item": "果皮", "category": "厨余垃圾"},
    {"item": "菜叶", "category": "厨余垃圾"},
    {"item": "剩饭", "category": "厨余垃圾"},
    {"item": "鱼骨", "category": "厨余垃圾"},
    {"item": "塑料袋", "category": "其他垃圾"},
    {"item": "烟头", "category": "其他垃圾"},
    {"item": "纸巾", "category": "其他垃圾"},
    {"item": "陶瓷碎片", "category": "其他垃圾"}
]

def get_db_connection():
    return pymysql.connect(**DATABASE_CONFIG)

def get_advice(category: str) -> str:
    advice_map = {
        '可回收物': '请投放到可回收物垃圾桶',
        '有害垃圾': '请投放到有害垃圾桶',
        '厨余垃圾': '请投放到厨余垃圾桶',
        '其他垃圾': '请投放到其他垃圾桶'
    }
    return advice_map.get(category, '请正确分类投放')

def get_category_id(category_name: str) -> Optional[int]:
    try:
        conn = get_db_connection()
        cursor = conn.cursor(pymysql.cursors.DictCursor)
        cursor.execute("SELECT id FROM waste_categories WHERE name = %s", (category_name,))
        result = cursor.fetchone()
        cursor.close()
        conn.close()
        return result['id'] if result else None
    except Exception as e:
        return None

def yolo26_recognize(image_url: str) -> Dict[str, Any]:
    garbage_item = random.choice(GARBAGE_ITEMS)
    confidence = random.randint(75, 95)
    
    return {
        "item": garbage_item["item"],
        "category": garbage_item["category"],
        "confidence": confidence
    }

@app.get("/")
async def root():
    return {
        "message": "YOLO26 Garbage Recognition API is running", 
        "port": 9000,
        "status": "示例模式",
        "description": "这是一个YOLO26识别的示例实现，使用随机模拟识别结果"
    }

@app.post("/api/recognition/recognize", response_model=RecognitionResponse)
async def recognize_garbage(image_url: str, authorization: Optional[str] = None):
    try:
        recognition_result = yolo26_recognize(image_url)
        
        category_id = get_category_id(recognition_result["category"])
        advice = get_advice(recognition_result["category"])
        
        result = RecognitionResult(
            item=recognition_result["item"],
            category=recognition_result["category"],
            confidence=recognition_result["confidence"],
            advice=advice,
            categoryId=category_id
        )
        
        return RecognitionResponse(data=result, success=True)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/api/upload")
async def upload_image(file: UploadFile = File(...)):
    try:
        file_extension = os.path.splitext(file.filename)[1]
        new_filename = f"{datetime.now().strftime('%Y%m%d_%H%M%S')}_{file.filename}"
        file_path = UPLOAD_DIR / new_filename
        
        with open(file_path, "wb") as buffer:
            shutil.copyfileobj(file.file, buffer)
        
        file_url = f"/uploads/{new_filename}"
        
        return {"url": file_url, "filename": new_filename, "message": "上传成功"}
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"上传失败: {str(e)}")

@app.get("/api/health")
async def health_check():
    return {
        "status": "healthy", 
        "port": 9000, 
        "service": "YOLO26 Recognition",
        "mode": "示例模式"
    }
