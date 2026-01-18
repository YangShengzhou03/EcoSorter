-- EcoSorter 完整数据库初始化脚本
-- 包含：建库、建表、插入测试数据

-- 删除数据库（如果存在）
DROP DATABASE IF EXISTS ecosorter;

-- 创建数据库
CREATE DATABASE ecosorter CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE ecosorter;

-- 创建表

-- 创建用户表
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'RESIDENT',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    avatar_url VARCHAR(500),
    phone VARCHAR(20),
    address TEXT,
    last_login DATETIME,
    login_attempts INT DEFAULT 0,
    lock_until DATETIME,
    password_reset_token VARCHAR(255),
    password_reset_expires DATETIME,
    email_verification_token VARCHAR(255),
    email_verified BOOLEAN DEFAULT FALSE,
    two_factor_secret VARCHAR(255),
    two_factor_enabled BOOLEAN DEFAULT FALSE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 创建垃圾桶设备表
CREATE TABLE trashcan_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    device_id VARCHAR(50) UNIQUE NOT NULL,
    location VARCHAR(255) NOT NULL,
    capacity_level INT DEFAULT 0,
    max_capacity INT DEFAULT 100,
    threshold INT DEFAULT 80,
    status VARCHAR(20) DEFAULT 'online',
    last_maintenance DATE,
    installation_date DATE,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    user_id BIGINT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- 创建垃圾类别表
CREATE TABLE waste_categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    name_en VARCHAR(50),
    description TEXT,
    color VARCHAR(7),
    icon VARCHAR(50),
    disposal_instructions TEXT,
    environmental_impact TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    sort_order INT DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 创建垃圾类别示例表
CREATE TABLE waste_category_examples (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id BIGINT NOT NULL,
    example VARCHAR(100) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES waste_categories(id) ON DELETE CASCADE
);

-- 创建分类记录表
CREATE TABLE classifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    trashcan_id BIGINT,
    waste_category_id BIGINT,
    image_url VARCHAR(500),
    confidence_score DECIMAL(5, 4),
    ai_suggestion VARCHAR(100),
    user_feedback VARCHAR(20),
    corrected_category_id BIGINT,
    notes TEXT,
    ip_address VARCHAR(45),
    user_agent VARCHAR(500),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (trashcan_id) REFERENCES trashcan_data(id) ON DELETE SET NULL,
    FOREIGN KEY (waste_category_id) REFERENCES waste_categories(id) ON DELETE SET NULL,
    FOREIGN KEY (corrected_category_id) REFERENCES waste_categories(id) ON DELETE SET NULL
);

-- 创建用户资料表
CREATE TABLE user_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL,
    full_name VARCHAR(100),
    bio TEXT,
    birth_date DATE,
    gender VARCHAR(20),
    occupation VARCHAR(100),
    company VARCHAR(100),
    website VARCHAR(255),
    location VARCHAR(255),
    timezone VARCHAR(50) DEFAULT 'Asia/Shanghai',
    language VARCHAR(10) DEFAULT 'zh-CN',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 创建用户统计表
CREATE TABLE user_statistics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL,
    total_classifications INT DEFAULT 0,
    correct_classifications INT DEFAULT 0,
    total_waste_weight DECIMAL(10, 2) DEFAULT 0.00,
    carbon_saved DECIMAL(10, 2) DEFAULT 0.00,
    total_points INT DEFAULT 0 COMMENT '累计获得积分',
    streak_days INT DEFAULT 0 COMMENT '连续打卡天数',
    longest_streak INT DEFAULT 0 COMMENT '最长连续打卡天数',
    weekly_goal INT DEFAULT 50 COMMENT '每周目标',
    monthly_goal INT DEFAULT 200 COMMENT '每月目标',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 创建轮播图表
CREATE TABLE banners (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    background VARCHAR(500) NOT NULL,
    sort_order INT NOT NULL DEFAULT 0,
    target VARCHAR(20) NOT NULL DEFAULT 'user' COMMENT '目标端: user-用户端, bin-垃圾桶端',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 创建通知表
CREATE TABLE notices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'draft',
    author_id BIGINT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE SET NULL
);

-- 创建商品表
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL COMMENT '商品名称',
    description TEXT COMMENT '商品描述',
    image_url VARCHAR(500) COMMENT '商品图片URL',
    images TEXT COMMENT '商品轮播图（JSON数组）',
    points INT NOT NULL COMMENT '所需积分',
    stock INT NOT NULL COMMENT '库存数量',
    max_purchase INT DEFAULT 10 COMMENT '限购数量（每人最多购买数量）',
    status VARCHAR(20) NOT NULL DEFAULT 'available' COMMENT '商品状态: available-上架, unavailable-下架',
    category VARCHAR(50) COMMENT '商品分类',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_status (status),
    INDEX idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品表';

-- 创建订单表
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    quantity INT NOT NULL COMMENT '商品数量',
    total_points INT NOT NULL COMMENT '消耗总积分',
    contact_name VARCHAR(100) NOT NULL COMMENT '联系人姓名',
    contact_phone VARCHAR(20) NOT NULL COMMENT '联系电话',
    shipping_address TEXT NOT NULL COMMENT '收货地址',
    status VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '订单状态: pending-待发货, shipped-已发货, completed-已完成, cancelled-已取消',
    remark TEXT COMMENT '备注',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_product_id (product_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- 创建积分记录表
CREATE TABLE point_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    points INT NOT NULL COMMENT '积分变动数量（正数为获得，负数为消耗）',
    type VARCHAR(50) NOT NULL COMMENT '积分类型: classification-分类获得, order-订单消耗, admin-管理员调整',
    reference_id BIGINT COMMENT '关联ID（分类ID、订单ID等）',
    description TEXT COMMENT '描述',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_type (type),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='积分记录表';

-- 创建收集任务表
CREATE TABLE collection_tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_id VARCHAR(50) UNIQUE NOT NULL,
    trashcan_id BIGINT NOT NULL,
    collector_id BIGINT,
    status VARCHAR(20) NOT NULL DEFAULT 'pending',
    priority VARCHAR(20) NOT NULL DEFAULT 'medium',
    estimated_weight DECIMAL(10, 2),
    actual_weight DECIMAL(10, 2),
    garbage_type VARCHAR(50) DEFAULT '混合垃圾',
    start_time DATETIME,
    end_time DATETIME,
    notes TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (trashcan_id) REFERENCES trashcan_data(id) ON DELETE CASCADE,
    FOREIGN KEY (collector_id) REFERENCES users(id) ON DELETE SET NULL
);

-- 创建索引
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_classifications_user_id ON classifications(user_id);
CREATE INDEX idx_classifications_created_at ON classifications(created_at);
CREATE INDEX idx_classifications_waste_category_id ON classifications(waste_category_id);
CREATE INDEX idx_waste_category_examples_category_id ON waste_category_examples(category_id);

-- 插入测试用户
INSERT INTO users (username, email, password, role, is_active, created_at, updated_at) VALUES
('resident', 'resident@example.com', '123456', 'RESIDENT', true, NOW(), NOW()),
('collector', 'collector@example.com', '123456', 'COLLECTOR', true, NOW(), NOW()),
('admin', 'admin@example.com', '123456', 'ADMIN', true, NOW(), NOW()),
('trashcan', 'trashcan@example.com', '123456', 'TRASHCAN', true, NOW(), NOW());

-- 插入垃圾类别数据
INSERT INTO waste_categories (name, description, color, icon, disposal_instructions, is_active, created_at, updated_at) VALUES
('可回收物', '可回收物是指适宜回收利用和资源化利用的生活废弃物，如废纸、废塑料、废玻璃、废金属、废织物等。', '#10b981', 'recycle', '请保持清洁干燥，避免污染。可回收物应投放到蓝色垃圾桶。', true, NOW(), NOW()),
('有害垃圾', '有害垃圾是指对人体健康或者自然环境造成直接或者潜在危害的生活废弃物，如废电池、废灯管、废药品、废油漆等。', '#ef4444', 'warning', '请轻放。有害垃圾应投放到红色垃圾桶，并注意密封包装。', true, NOW(), NOW()),
('厨余垃圾', '厨余垃圾是指居民日常生活及食品加工、饮食服务、单位供餐等活动中产生的垃圾，包括丢弃不用的菜叶、剩菜、剩饭、果皮、蛋壳、茶渣、骨头等。', '#f59e0b', 'food', '请沥干水分。厨余垃圾应投放到绿色垃圾桶。', true, NOW(), NOW()),
('其他垃圾', '其他垃圾是指除可回收物、有害垃圾、厨余垃圾以外的其他生活废弃物，如砖瓦陶瓷、渣土、卫生间废纸、瓷器碎片等难以回收的废弃物。', '#6b7280', 'delete', '请尽量沥干水分。其他垃圾应投放到灰色垃圾桶。', true, NOW(), NOW());

-- 插入垃圾类别示例
INSERT INTO waste_category_examples (category_id, example, created_at, updated_at) VALUES
(1, '废纸', NOW(), NOW()),
(1, '报纸', NOW(), NOW()),
(1, '纸箱', NOW(), NOW()),
(1, '塑料瓶', NOW(), NOW()),
(1, '玻璃瓶', NOW(), NOW()),
(1, '金属罐', NOW(), NOW()),
(1, '易拉罐', NOW(), NOW()),
(2, '废电池', NOW(), NOW()),
(2, '废灯管', NOW(), NOW()),
(2, '废药品', NOW(), NOW()),
(2, '废油漆', NOW(), NOW()),
(2, '杀虫剂', NOW(), NOW()),
(3, '菜叶', NOW(), NOW()),
(3, '剩菜', NOW(), NOW()),
(3, '剩饭', NOW(), NOW()),
(3, '果皮', NOW(), NOW()),
(3, '蛋壳', NOW(), NOW()),
(3, '骨头', NOW(), NOW()),
(4, '砖瓦', NOW(), NOW()),
(4, '陶瓷', NOW(), NOW()),
(4, '渣土', NOW(), NOW()),
(4, '卫生纸', NOW(), NOW()),
(4, '烟蒂', NOW(), NOW()),
(4, '一次性餐具', NOW(), NOW());

-- 插入垃圾桶设备数据
INSERT INTO trashcan_data (device_id, location, capacity_level, max_capacity, threshold, status, latitude, longitude, created_at, updated_at) VALUES
('TRASH-001', 'A栋一楼大厅', 68, 100, 80, 'online', 39.9042, 116.4074, NOW(), NOW()),
('TRASH-002', 'B栋二楼', 45, 100, 80, 'online', 39.9045, 116.4078, NOW(), NOW()),
('TRASH-003', 'C栋一楼', 82, 100, 80, 'offline', 39.9048, 116.4082, NOW(), NOW());

-- 插入收集任务数据
INSERT INTO collection_tasks (task_id, trashcan_id, collector_id, status, priority, estimated_weight, garbage_type, created_at, updated_at) VALUES
('TASK-001', 1, 2, 'pending', 'medium', 34.0, '混合垃圾', NOW(), NOW()),
('TASK-002', 3, 2, 'pending', 'high', 41.0, '混合垃圾', NOW(), NOW());

-- 插入轮播图数据
INSERT INTO banners (title, description, background, sort_order, target, is_active, created_at, updated_at) VALUES
('垃圾分类，从我做起', '保护环境，共建美好家园', 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)', 1, 'user', true, NOW(), NOW()),
('智能识别，准确分类', 'AI技术助力环保事业', 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)', 2, 'user', true, NOW(), NOW()),
('积分奖励，激励参与', '分类正确，获得积分奖励', 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)', 3, 'user', true, NOW(), NOW()),
('垃圾桶使用指南', '正确使用智能垃圾桶', 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)', 1, 'bin', true, NOW(), NOW()),
('垃圾分类提示', '请正确投放垃圾', 'linear-gradient(135deg, #fa709a 0%, #fee140 100%)', 2, 'bin', true, NOW(), NOW());

-- 插入通知数据
INSERT INTO notices (title, content, status, author_id, created_at, updated_at) VALUES
('系统维护通知', '为了提供更好的服务，系统将于本周六凌晨2:00-4:00进行维护，届时将暂停服务，请提前做好准备。', 'published', 3, NOW(), NOW()),
('垃圾分类新规', '根据最新环保政策，从本月起垃圾分类标准有所调整，请各位用户注意查看新的分类指南。', 'published', 3, NOW(), NOW()),
('积分活动开启', '本月积分活动正式开启！每正确分类一次可获得双倍积分，快来参与吧！', 'published', 3, NOW(), NOW()),
('设备升级完成', 'A栋和B栋的智能垃圾桶已完成升级，识别准确率提升至98%，欢迎体验！', 'published', 3, NOW(), NOW()),
('环保倡议', '让我们共同行动，减少一次性用品使用，为地球环保贡献一份力量！', 'published', 3, NOW(), NOW());

-- 插入商品数据
INSERT INTO products (name, description, image_url, images, points, stock, max_purchase, status, category, created_at, updated_at) VALUES
('环保购物袋', '采用优质环保材料制作，结实耐用，可重复使用。容量大，承重强，适合日常购物、买菜等多种场景使用。材质无毒无害，符合环保标准，是您绿色生活的理想选择。', 'https://example.com/images/bag.jpg', '["https://example.com/images/bag.jpg", "https://example.com/images/bag-2.jpg"]', 100, 500, 10, 'available', '生活用品', NOW(), NOW()),
('太阳能充电宝', '10000mAh大容量太阳能充电宝，支持太阳能充电和USB充电两种方式。户外旅行必备，环保节能。配备双USB输出，可同时为两台设备充电。LED电量显示，实时掌握电量情况。', 'https://example.com/images/powerbank.jpg', '["https://example.com/images/powerbank.jpg", "https://example.com/images/powerbank-2.jpg"]', 500, 200, 5, 'available', '电子产品', NOW(), NOW()),
('有机茶叶礼盒', '精选高山有机茶叶，无农药残留，健康养生。礼盒包装精美，适合送礼。茶叶香气浓郁，口感醇厚，回味甘甜。富含茶多酚，有抗氧化、提神醒脑的功效。', 'https://example.com/images/tea.jpg', '["https://example.com/images/tea.jpg", "https://example.com/images/tea-2.jpg"]', 300, 150, 5, 'available', '食品饮料', NOW(), NOW()),
('环保笔记本', '采用再生纸制作，环保低碳。纸张书写流畅，不洇墨。封面设计简约大方，适合学生、办公使用。每本100页，A5尺寸，方便携带。', 'https://example.com/images/notebook.jpg', '["https://example.com/images/notebook.jpg", "https://example.com/images/notebook-2.jpg"]', 50, 1000, 20, 'available', '学习用品', NOW(), NOW()),
('竹制牙刷套装', '天然竹制牙刷，环保健康。刷毛柔软，不伤牙龈。套装包含4支牙刷，满足全家使用需求。竹柄可降解，减少塑料污染。', 'https://example.com/images/toothbrush.jpg', '["https://example.com/images/toothbrush.jpg", "https://example.com/images/toothbrush-2.jpg"]', 80, 800, 10, 'available', '生活用品', NOW(), NOW()),
('LED节能台灯', 'USB充电LED台灯，护眼节能。三档亮度调节，满足不同使用需求。可折叠设计，方便携带和收纳。内置电池，续航时间长，适合学习、阅读使用。', 'https://example.com/images/lamp.jpg', '["https://example.com/images/lamp.jpg", "https://example.com/images/lamp-2.jpg"]', 200, 300, 5, 'available', '电子产品', NOW(), NOW()),
('有机大米', '5kg有机大米，绿色健康。产自优质稻田，无农药化肥。米粒饱满，口感香甜。富含蛋白质、维生素等营养成分，是健康饮食的首选。', 'https://example.com/images/rice.jpg', '["https://example.com/images/rice.jpg", "https://example.com/images/rice-2.jpg"]', 150, 400, 10, 'available', '食品饮料', NOW(), NOW()),
('环保文具套装', '包含铅笔、橡皮、尺子、圆规等文具，满足日常学习需求。采用环保材料制作，无毒无害。包装精美，适合学生使用。', 'https://example.com/images/stationery.jpg', '["https://example.com/images/stationery.jpg", "https://example.com/images/stationery-2.jpg"]', 120, 600, 15, 'available', '学习用品', NOW(), NOW());

-- 创建申诉表
CREATE TABLE complaints (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    classification_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL COMMENT '申诉类型: misclassification-分类错误, weight-重量争议, points-积分争议, other-其他',
    description TEXT NOT NULL COMMENT '申诉说明',
    status VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '申诉状态: pending-待处理, processing-处理中, resolved-已解决, rejected-已驳回',
    admin_id BIGINT COMMENT '处理管理员ID',
    admin_response TEXT COMMENT '管理员回复',
    processed_at DATETIME COMMENT '处理时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (classification_id) REFERENCES classifications(id) ON DELETE CASCADE,
    FOREIGN KEY (admin_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_status (status),
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='申诉表';

SELECT 'EcoSorter 数据库初始化完成！' as message;
