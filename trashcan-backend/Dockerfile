FROM python:3.10-slim

WORKDIR /app

RUN apt-get update && apt-get install -y --no-install-recommends \
    gcc g++ make cmake build-essential \
    libglib2.0-0 libsm6 libxext6 libxrender1 libgl1 libgomp1 \
    libboost-python-dev libboost-thread-dev \
    && rm -rf /var/lib/apt/lists/* /var/cache/apt/archives/* && \
    mkdir -p uploads/face uploads/recognition uploads/temp models

COPY requirements.txt models/yolov8s.pt App.py yolo_recognition.py ./

RUN pip config set global.index-url https://pypi.tuna.tsinghua.edu.cn/simple && \
    pip install --no-cache-dir torch==2.1.0+cpu torchvision==0.16.0+cpu -f https://download.pytorch.org/whl/cpu/torch_stable.html && \
    pip install --no-cache-dir -r requirements.txt

EXPOSE 9000

CMD ["python", "-O", "App.py"]