DROP DATABASE IF EXISTS eco_sorter;

-- 创建数据库
CREATE DATABASE eco_sorter CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE eco_sorter;

-- 创建用户表
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(20) UNIQUE NOT NULL COMMENT '用户名',
    email VARCHAR(100) UNIQUE NOT NULL COMMENT '邮箱',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    role VARCHAR(20) NOT NULL DEFAULT 'RESIDENT' COMMENT '角色: RESIDENT-居民, COLLECTOR-收集员, ADMIN-管理员, TRASHCAN-垃圾桶',
    is_active BOOLEAN NOT NULL DEFAULT TRUE COMMENT '是否激活',
    avatar_url VARCHAR(500) COMMENT '头像URL',
    phone VARCHAR(20) COMMENT '电话',
    address TEXT COMMENT '地址',
    current_points INT DEFAULT 0 COMMENT '当前积分',
    face_encoding TEXT COMMENT '人脸特征向量（128维浮点数数组，JSON格式存储）',
    face_verified BOOLEAN DEFAULT FALSE COMMENT '是否已通过人脸验证',
    last_login DATETIME COMMENT '最后登录时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 创建垃圾桶设备表
CREATE TABLE trashcan_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '设备ID',
    device_id VARCHAR(50) UNIQUE NOT NULL COMMENT '设备编号',
    device_name VARCHAR(100) COMMENT '设备名称',
    location VARCHAR(255) NOT NULL COMMENT '位置',
    capacity_level INT DEFAULT 0 COMMENT '当前容量级别',
    max_capacity INT DEFAULT 100 COMMENT '最大容量',
    threshold INT DEFAULT 80 COMMENT '告警阈值',
    status VARCHAR(20) DEFAULT 'online' COMMENT '状态: online-在线, offline-离线, maintenance-维护中',
    bin_type VARCHAR(20) DEFAULT 'recyclable' COMMENT '垃圾桶类型: recyclable-可回收物, hazardous-有害垃圾, kitchen-厨余垃圾, other-其他垃圾',
    latitude DECIMAL(10, 8) COMMENT '纬度',
    longitude DECIMAL(11, 8) COMMENT '经度',
    auth_token VARCHAR(255) COMMENT '设备认证令牌',
    admin_password VARCHAR(255) DEFAULT '123456' COMMENT '设备管理员密码',
    last_active DATETIME COMMENT '最后活跃时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_device_id (device_id),
    INDEX idx_auth_token (auth_token)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='垃圾桶设备表';

-- 创建垃圾类别表
CREATE TABLE waste_categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '类别ID',
    name VARCHAR(50) NOT NULL UNIQUE COMMENT '类别名称',
    name_en VARCHAR(50) COMMENT '英文名称',
    description TEXT COMMENT '描述',
    color VARCHAR(7) COMMENT '颜色代码',
    icon VARCHAR(50) COMMENT '图标',
    disposal_instructions TEXT COMMENT '投放说明',
    disposal_method VARCHAR(200) COMMENT '处理方式',
    environmental_impact TEXT COMMENT '环境影响',
    recycling_rate DECIMAL(5,2) DEFAULT 0.00 COMMENT '回收率',
    common_items TEXT COMMENT '常见物品（逗号分隔）',
    special_handling BOOLEAN DEFAULT FALSE COMMENT '是否需要特殊处理',
    hazardous BOOLEAN DEFAULT FALSE COMMENT '是否为危险品',
    is_active BOOLEAN DEFAULT TRUE COMMENT '是否启用',
    sort_order INT DEFAULT 0 COMMENT '排序',
    points INT DEFAULT 0 COMMENT '积分',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='垃圾类别表';



-- 创建分类记录表
CREATE TABLE classifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '记录ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    trashcan_id BIGINT COMMENT '垃圾桶ID',
    waste_category_id BIGINT COMMENT '垃圾类别ID',
    image_url VARCHAR(500) COMMENT '图片URL',
    confidence_score DECIMAL(5, 4) COMMENT '置信度分数',
    ai_suggestion VARCHAR(100) COMMENT 'AI建议',
    user_feedback VARCHAR(20) COMMENT '用户反馈',
    corrected_category_id BIGINT COMMENT '更正后的类别ID',
    notes TEXT COMMENT '备注',
    ip_address VARCHAR(45) COMMENT 'IP地址',
    user_agent VARCHAR(500) COMMENT '用户代理',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (trashcan_id) REFERENCES trashcan_data(id) ON DELETE SET NULL,
    FOREIGN KEY (waste_category_id) REFERENCES waste_categories(id) ON DELETE SET NULL,
    FOREIGN KEY (corrected_category_id) REFERENCES waste_categories(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分类记录表';

-- 创建用户统计表
CREATE TABLE user_statistics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '统计ID',
    user_id BIGINT UNIQUE NOT NULL COMMENT '用户ID',
    total_classifications INT DEFAULT 0 COMMENT '总分类次数',
    correct_classifications INT DEFAULT 0 COMMENT '正确分类次数',
    total_waste_weight DECIMAL(10, 2) DEFAULT 0.00 COMMENT '总垃圾重量',
    carbon_saved DECIMAL(10, 2) DEFAULT 0.00 COMMENT '节省碳排放量',
    total_points INT DEFAULT 0 COMMENT '累计获得积分',
    streak_days INT DEFAULT 0 COMMENT '连续打卡天数',
    longest_streak INT DEFAULT 0 COMMENT '最长连续打卡天数',
    weekly_goal INT DEFAULT 50 COMMENT '每周目标',
    monthly_goal INT DEFAULT 200 COMMENT '每月目标',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户统计表';

-- 创建轮播图表
CREATE TABLE banners (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '轮播图ID',
    title VARCHAR(200) NOT NULL COMMENT '标题',
    description TEXT COMMENT '描述',
    background VARCHAR(500) NOT NULL COMMENT '背景色/图片',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序',
    target VARCHAR(20) NOT NULL DEFAULT 'user' COMMENT '目标端: user-用户端, trashcan-垃圾桶端',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='轮播图表';

-- 创建通知表
CREATE TABLE notices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '通知ID',
    title VARCHAR(200) NOT NULL COMMENT '标题',
    content TEXT NOT NULL COMMENT '内容',
    status VARCHAR(20) NOT NULL DEFAULT 'draft' COMMENT '状态: draft-草稿, published-已发布',
    target_audience VARCHAR(20) NOT NULL DEFAULT 'all' COMMENT '发送群体: all-全部, resident-居民, collector-收集员, admin-管理员',
    author_id BIGINT COMMENT '作者ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知表';

-- 创建商品表
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '商品ID',
    name VARCHAR(200) NOT NULL COMMENT '商品名称',
    description TEXT COMMENT '商品描述',
    image_url VARCHAR(500) COMMENT '商品图片URL',
    points INT NOT NULL COMMENT '所需积分',
    stock INT NOT NULL COMMENT '库存数量',
    max_purchase INT DEFAULT 10 COMMENT '限购数量（每人最多购买数量）',
    status VARCHAR(20) NOT NULL DEFAULT 'available' COMMENT '商品状态: available-上架, unavailable-下架',
    category VARCHAR(50) COMMENT '商品分类',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_status (status),
    INDEX idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品表';

-- 创建订单表
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订单ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    quantity INT NOT NULL COMMENT '商品数量',
    total_points INT NOT NULL COMMENT '消耗总积分',
    contact_name VARCHAR(100) NOT NULL COMMENT '联系人姓名',
    contact_phone VARCHAR(20) NOT NULL COMMENT '联系电话',
    shipping_address TEXT NOT NULL COMMENT '收货地址',
    tracking_number VARCHAR(100) COMMENT '快递单号',
    status VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '订单状态: pending-待发货, shipped-已发货, completed-已完成, cancelled-已取消',
    remark TEXT COMMENT '备注',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_product_id (product_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- 创建预约表
CREATE TABLE bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '预约ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    type VARCHAR(20) NOT NULL COMMENT '回收类型: recyclable-可回收物, electronics-电子废弃物, large-大件物品, other-其他',
    description TEXT NOT NULL COMMENT '物品描述',
    estimated_weight INT NOT NULL COMMENT '预估重量(kg)',
    appointment_date VARCHAR(20) NOT NULL COMMENT '预约日期(YYYY-MM-DD)',
    time_slot VARCHAR(20) NOT NULL COMMENT '时间段: morning-09:00-12:00, afternoon-14:00-17:00, evening-18:00-20:00',
    contact_name VARCHAR(100) NOT NULL COMMENT '联系人姓名',
    contact_phone VARCHAR(20) NOT NULL COMMENT '联系电话',
    address TEXT NOT NULL COMMENT '回收地址',
    remark TEXT COMMENT '备注',
    status VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '预约状态: pending-待确认, confirmed-已确认, completed-已完成, cancelled-已取消',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_appointment_date (appointment_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='预约表';

-- 创建积分记录表
CREATE TABLE point_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '记录ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    points INT NOT NULL COMMENT '积分变动数量（正数为获得，负数为消耗）',
    type VARCHAR(50) NOT NULL COMMENT '积分类型: classification-分类获得, order-订单消耗, admin-管理员调整',
    reference_id BIGINT COMMENT '关联ID（分类ID、订单ID等）',
    description TEXT COMMENT '描述',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_type (type),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='积分记录表';

-- 创建收集任务表
CREATE TABLE collection_tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '任务ID',
    task_id VARCHAR(50) UNIQUE NOT NULL COMMENT '任务编号',
    trashcan_id BIGINT NOT NULL COMMENT '垃圾桶ID',
    collector_id BIGINT COMMENT '收集员ID',
    status VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '状态: pending-待处理, in_progress-进行中, completed-已完成, cancelled-已取消',
    priority VARCHAR(20) NOT NULL DEFAULT 'medium' COMMENT '优先级: low-低, medium-中, high-高',
    estimated_weight DECIMAL(10, 2) COMMENT '预估重量',
    actual_weight DECIMAL(10, 2) COMMENT '实际重量',
    garbage_type VARCHAR(50) DEFAULT '混合垃圾' COMMENT '垃圾类型',
    start_time DATETIME COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    notes TEXT COMMENT '备注',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (trashcan_id) REFERENCES trashcan_data(id) ON DELETE CASCADE,
    FOREIGN KEY (collector_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收集任务表';

-- 创建任务异常表
CREATE TABLE task_exceptions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '异常ID',
    task_id BIGINT NOT NULL COMMENT '任务ID',
    reporter_id BIGINT NOT NULL COMMENT '报告人ID',
    exception_type VARCHAR(50) NOT NULL COMMENT '异常类型',
    description TEXT NOT NULL COMMENT '描述',
    status VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '状态: pending-待处理, processing-处理中, resolved-已解决, rejected-已驳回',
    reviewer_id BIGINT COMMENT '审核人ID',
    review_notes TEXT COMMENT '审核备注',
    reviewed_at DATETIME COMMENT '审核时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (task_id) REFERENCES collection_tasks(id) ON DELETE CASCADE,
    FOREIGN KEY (reporter_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (reviewer_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务异常表';

-- 创建申诉表
CREATE TABLE complaints (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '申诉ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    classification_id BIGINT NOT NULL COMMENT '分类记录ID',
    type VARCHAR(50) NOT NULL COMMENT '申诉类型: misclassification-分类错误, weight-重量争议, points-积分争议, other-其他',
    description TEXT NOT NULL COMMENT '申诉说明',
    status VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '申诉状态: pending-待处理, processing-处理中, resolved-已解决, rejected-已驳回',
    admin_id BIGINT COMMENT '处理管理员ID',
    admin_response TEXT COMMENT '管理员回复',
    processed_at DATETIME COMMENT '处理时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (classification_id) REFERENCES classifications(id) ON DELETE CASCADE,
    FOREIGN KEY (admin_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_status (status),
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='申诉表';

-- 创建索引
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_classifications_user_id ON classifications(user_id);
CREATE INDEX idx_classifications_created_at ON classifications(created_at);
CREATE INDEX idx_classifications_waste_category_id ON classifications(waste_category_id);
CREATE INDEX idx_collection_tasks_collector_id ON collection_tasks(collector_id);
CREATE INDEX idx_collection_tasks_status ON collection_tasks(status);
CREATE INDEX idx_collection_tasks_created_at ON collection_tasks(created_at);
CREATE INDEX idx_task_exceptions_task_id ON task_exceptions(task_id);
CREATE INDEX idx_task_exceptions_reporter_id ON task_exceptions(reporter_id);
CREATE INDEX idx_task_exceptions_status ON task_exceptions(status);
CREATE INDEX idx_bookings_user_id ON bookings(user_id);
CREATE INDEX idx_bookings_status ON bookings(status);
CREATE INDEX idx_bookings_appointment_date ON bookings(appointment_date);

-- 插入测试用户
INSERT INTO users (username, email, password, role, is_active, created_at, updated_at) VALUES
('admin', 'admin@qq.com', '123456', 'ADMIN', true, NOW(), NOW()),
('collector', 'collector@qq.com', '123456', 'COLLECTOR', true, NOW(), NOW()),
('resident', 'resident@qq.com', '123456', 'RESIDENT', true, NOW(), NOW());

-- 插入用户统计记录
INSERT INTO user_statistics (user_id, total_classifications, correct_classifications, total_waste_weight, carbon_saved, total_points, streak_days, longest_streak, weekly_goal, monthly_goal, created_at, updated_at) VALUES
(1, 0, 0, 0.00, 0.00, 0, 0, 0, 50, 200, NOW(), NOW()),
(2, 0, 0, 0.00, 0.00, 0, 0, 0, 50, 200, NOW(), NOW()),
(3, 0, 0, 0.00, 0.00, 0, 0, 0, 50, 200, NOW(), NOW());

-- 插入垃圾类别数据
INSERT INTO waste_categories (name, description, color, icon, disposal_instructions, disposal_method, environmental_impact, recycling_rate, special_handling, hazardous, is_active, common_items, points, created_at, updated_at) VALUES
('可回收物', '可回收物是指适宜回收利用和资源化利用的生活废弃物，如废纸、废塑料、废玻璃、废金属、废织物等。', '#10b981', 'recycle', '请保持清洁干燥，避免污染。可回收物应投放到蓝色垃圾桶。', '回收再利用', '3', 85.00, false, false, true, '废纸,塑料瓶', 10, NOW(), NOW()),
('有害垃圾', '有害垃圾是指对人体健康或者自然环境造成直接或者潜在危害的生活废弃物，如废电池、废灯管、废药品、废油漆等。', '#ef4444', 'warning', '请轻放。有害垃圾应投放到红色垃圾桶，并注意密封包装。', '专业处理', '9', 95.00, true, true, true, '废电池', 20, NOW(), NOW()),
('厨余垃圾', '厨余垃圾是指居民日常生活及食品加工、饮食服务、单位供餐等活动中产生的垃圾，包括丢弃不用的菜叶、剩菜、剩饭、果皮、蛋壳、茶渣、骨头等。', '#f59e0b', 'food', '请沥干水分。厨余垃圾应投放到绿色垃圾桶。', '堆肥处理', '5', 60.00, false, false, true, '菜叶', 5, NOW(), NOW()),
('其他垃圾', '其他垃圾是指除可回收物、有害垃圾、厨余垃圾以外的其他生活废弃物，如砖瓦陶瓷、渣土、卫生间废纸、瓷器碎片等难以回收的废弃物。', '#6b7280', 'delete', '请尽量沥干水分。其他垃圾应投放到灰色垃圾桶。', '填埋处理', '7', 10.00, false, false, true, '砖瓦', 2, NOW(), NOW());

SELECT 'EcoSorter 数据库初始化完成！' as message;
