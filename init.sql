-- EcoSorter Database Initialization Script
-- This script drops existing tables and creates new ones

-- Drop existing tables if they exist
DROP TABLE IF EXISTS classification_history CASCADE;
DROP TABLE IF EXISTS user_notifications CASCADE;
DROP TABLE IF EXISTS user_statistics CASCADE;
DROP TABLE IF EXISTS user_preferences CASCADE;
DROP TABLE IF EXISTS user_profiles CASCADE;
DROP TABLE IF EXISTS waste_categories CASCADE;
DROP TABLE IF EXISTS classifications CASCADE;
DROP TABLE IF EXISTS trashcan_data CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Create users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'user' CHECK (role IN ('trashcan', 'user', 'admin')),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP,
    avatar_url VARCHAR(255),
    phone VARCHAR(20),
    address TEXT
);

-- Create trashcan_data table for smart trash cans
CREATE TABLE trashcan_data (
    id SERIAL PRIMARY KEY,
    device_id VARCHAR(50) UNIQUE NOT NULL,
    location VARCHAR(255) NOT NULL,
    capacity_level INTEGER DEFAULT 0 CHECK (capacity_level >= 0 AND capacity_level <= 100),
    status VARCHAR(20) DEFAULT 'online' CHECK (status IN ('online', 'offline', 'maintenance')),
    last_maintenance DATE,
    installation_date DATE DEFAULT CURRENT_DATE,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    user_id INTEGER REFERENCES users(id) ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create waste_categories table
CREATE TABLE waste_categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    name_en VARCHAR(50),
    description TEXT,
    color VARCHAR(7) DEFAULT '#409EFF',
    icon VARCHAR(50),
    disposal_instructions TEXT,
    environmental_impact TEXT,
    examples TEXT[],
    is_active BOOLEAN DEFAULT true,
    sort_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create classifications table
CREATE TABLE classifications (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    trashcan_id INTEGER REFERENCES trashcan_data(id) ON DELETE SET NULL,
    waste_category_id INTEGER REFERENCES waste_categories(id) ON DELETE SET NULL,
    image_url VARCHAR(500),
    confidence_score DECIMAL(5, 4) CHECK (confidence_score >= 0 AND confidence_score <= 1),
    ai_suggestion VARCHAR(100),
    user_feedback VARCHAR(20) CHECK (user_feedback IN ('correct', 'incorrect', 'unsure')),
    corrected_category_id INTEGER REFERENCES waste_categories(id) ON DELETE SET NULL,
    notes TEXT,
    ip_address INET,
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create user_profiles table
CREATE TABLE user_profiles (
    id SERIAL PRIMARY KEY,
    user_id INTEGER UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    full_name VARCHAR(100),
    bio TEXT,
    birth_date DATE,
    gender VARCHAR(10) CHECK (gender IN ('male', 'female', 'other', 'prefer_not_to_say')),
    occupation VARCHAR(100),
    company VARCHAR(100),
    website VARCHAR(255),
    location VARCHAR(255),
    timezone VARCHAR(50) DEFAULT 'Asia/Shanghai',
    language VARCHAR(10) DEFAULT 'zh-CN',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create user_preferences table
CREATE TABLE user_preferences (
    id SERIAL PRIMARY KEY,
    user_id INTEGER UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    theme VARCHAR(20) DEFAULT 'light' CHECK (theme IN ('light', 'dark', 'auto')),
    notifications_enabled BOOLEAN DEFAULT true,
    email_notifications BOOLEAN DEFAULT true,
    push_notifications BOOLEAN DEFAULT true,
    classification_reminders BOOLEAN DEFAULT true,
    statistics_emails BOOLEAN DEFAULT true,
    privacy_level VARCHAR(20) DEFAULT 'medium' CHECK (privacy_level IN ('low', 'medium', 'high')),
    data_sharing BOOLEAN DEFAULT false,
    auto_classify BOOLEAN DEFAULT true,
    confidence_threshold DECIMAL(5, 4) DEFAULT 0.7 CHECK (confidence_threshold >= 0 AND confidence_threshold <= 1),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create user_statistics table
CREATE TABLE user_statistics (
    id SERIAL PRIMARY KEY,
    user_id INTEGER UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    total_classifications INTEGER DEFAULT 0,
    correct_classifications INTEGER DEFAULT 0,
    accuracy_rate DECIMAL(5, 2) DEFAULT 0,
    total_waste_weight DECIMAL(10, 2) DEFAULT 0,
    carbon_saved DECIMAL(10, 2) DEFAULT 0,
    streak_days INTEGER DEFAULT 0,
    longest_streak INTEGER DEFAULT 0,
    last_classification_date DATE,
    weekly_goal INTEGER DEFAULT 50,
    monthly_goal INTEGER DEFAULT 200,
    achievements TEXT[],
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create user_notifications table
CREATE TABLE user_notifications (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    type VARCHAR(20) DEFAULT 'info' CHECK (type IN ('info', 'warning', 'success', 'error')),
    is_read BOOLEAN DEFAULT false,
    action_url VARCHAR(500),
    action_text VARCHAR(100),
    expires_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create classification_history table for detailed tracking
CREATE TABLE classification_history (
    id SERIAL PRIMARY KEY,
    classification_id INTEGER REFERENCES classifications(id) ON DELETE CASCADE,
    action VARCHAR(50) NOT NULL,
    previous_value TEXT,
    new_value TEXT,
    changed_by INTEGER REFERENCES users(id) ON DELETE SET NULL,
    reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_trashcan_device_id ON trashcan_data(device_id);
CREATE INDEX idx_trashcan_status ON trashcan_data(status);
CREATE INDEX idx_classifications_user_id ON classifications(user_id);
CREATE INDEX idx_classifications_created_at ON classifications(created_at);
CREATE INDEX idx_classifications_waste_category_id ON classifications(waste_category_id);
CREATE INDEX idx_user_notifications_user_id ON user_notifications(user_id);
CREATE INDEX idx_user_notifications_is_read ON user_notifications(is_read);

-- Insert default waste categories
INSERT INTO waste_categories (name, name_en, description, color, icon, disposal_instructions, environmental_impact, examples) VALUES
('可回收垃圾', 'Recyclable', '可以回收再利用的废弃物', '#409EFF', 'recycle', '请清洁后投入蓝色垃圾桶', '减少资源浪费，降低环境污染', ARRAY['纸张', '塑料瓶', '金属罐', '玻璃瓶']),
('厨余垃圾', 'Food Waste', '易腐烂的生物质废弃物', '#67C23A', 'food', '请沥干水分后投入绿色垃圾桶', '可以制成有机肥料，循环利用', ARRAY['剩菜剩饭', '果皮', '菜叶', '茶叶渣']),
('有害垃圾', 'Hazardous', '对人体健康或环境有害的废弃物', '#E6A23C', 'warning', '请投入红色垃圾桶，避免破损', '含有有害物质，需要特殊处理', ARRAY['电池', '灯管', '药品', '油漆']),
('其他垃圾', 'Other Waste', '除上述三类外的其他生活废弃物', '#909399', 'trash', '请投入灰色垃圾桶', '需要填埋或焚烧处理', ARRAY['烟头', '尘土', '一次性餐具', '尿不湿']);

-- Insert default admin user (password: admin123)
INSERT INTO users (username, email, password_hash, role, is_active) VALUES 
('admin', 'admin@ecosorter.com', '$2b$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'admin', true);

-- Insert default trashcan user (password: trash123)
INSERT INTO users (username, email, password_hash, role, is_active) VALUES 
('trashcan001', 'trashcan@ecosorter.com', '$2b$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'trashcan', true);

-- Insert default trashcan data
INSERT INTO trashcan_data (device_id, location, capacity_level, status, last_maintenance, latitude, longitude, user_id) VALUES
('TRASH-001', 'A栋一楼大厅', 68, 'online', '2024-12-20', 31.2304, 121.4737, (SELECT id FROM users WHERE username = 'trashcan001'));

-- Insert default user profiles
INSERT INTO user_profiles (user_id, full_name, timezone, language) VALUES
((SELECT id FROM users WHERE username = 'admin'), '系统管理员', 'Asia/Shanghai', 'zh-CN'),
((SELECT id FROM users WHERE username = 'trashcan001'), '智能垃圾桶001', 'Asia/Shanghai', 'zh-CN');

-- Insert default user preferences
INSERT INTO user_preferences (user_id, theme, notifications_enabled, email_notifications, push_notifications) VALUES
((SELECT id FROM users WHERE username = 'admin'), 'light', true, true, true),
((SELECT id FROM users WHERE username = 'trashcan001'), 'auto', true, false, true);

-- Insert default user statistics
INSERT INTO user_statistics (user_id, total_classifications, correct_classifications, accuracy_rate, streak_days) VALUES
((SELECT id FROM users WHERE username = 'admin'), 0, 0, 0, 0),
((SELECT id FROM users WHERE username = 'trashcan001'), 156, 144, 92.3, 7);

-- Create a function to update user statistics
CREATE OR REPLACE FUNCTION update_user_statistics()
RETURNS TRIGGER AS $$
BEGIN
    -- Update total classifications
    UPDATE user_statistics 
    SET total_classifications = total_classifications + 1,
        updated_at = CURRENT_TIMESTAMP
    WHERE user_id = NEW.user_id;
    
    -- Update accuracy rate if classification has feedback
    IF NEW.user_feedback = 'correct' THEN
        UPDATE user_statistics 
        SET correct_classifications = correct_classifications + 1,
            accuracy_rate = ROUND((correct_classifications::DECIMAL / total_classifications) * 100, 2),
            updated_at = CURRENT_TIMESTAMP
        WHERE user_id = NEW.user_id;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create trigger to automatically update statistics
CREATE TRIGGER trigger_update_user_statistics
    AFTER INSERT ON classifications
    FOR EACH ROW
    EXECUTE FUNCTION update_user_statistics();

-- Create a function to update trashcan capacity
CREATE OR REPLACE FUNCTION update_trashcan_capacity()
RETURNS TRIGGER AS $$
BEGIN
    -- Update trashcan capacity level (simulate based on usage)
    UPDATE trashcan_data 
    SET capacity_level = LEAST(100, capacity_level + 1),
        updated_at = CURRENT_TIMESTAMP
    WHERE id = NEW.trashcan_id;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create trigger to automatically update trashcan capacity
CREATE TRIGGER trigger_update_trashcan_capacity
    AFTER INSERT ON classifications
    FOR EACH ROW
    WHEN (NEW.trashcan_id IS NOT NULL)
    EXECUTE FUNCTION update_trashcan_capacity();

-- Create views for easier querying
CREATE VIEW user_dashboard_view AS
SELECT 
    u.id,
    u.username,
    u.role,
    u.is_active,
    COALESCE(us.total_classifications, 0) as total_classifications,
    COALESCE(us.accuracy_rate, 0) as accuracy_rate,
    COALESCE(us.streak_days, 0) as streak_days,
    COALESCE(us.carbon_saved, 0) as carbon_saved,
    up.theme,
    up.notifications_enabled
FROM users u
LEFT JOIN user_statistics us ON u.id = us.user_id
LEFT JOIN user_preferences up ON u.id = up.user_id;

CREATE VIEW trashcan_status_view AS
SELECT 
    td.id,
    td.device_id,
    td.location,
    td.capacity_level,
    td.status,
    td.last_maintenance,
    COUNT(c.id) as total_classifications,
    AVG(c.confidence_score) * 100 as avg_confidence,
    td.created_at,
    td.updated_at
FROM trashcan_data td
LEFT JOIN classifications c ON td.id = c.trashcan_id
GROUP BY td.id, td.device_id, td.location, td.capacity_level, td.status, td.last_maintenance, td.created_at, td.updated_at;

-- Grant permissions (adjust based on your security requirements)
-- GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO ecosorter_user;
-- GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO ecosorter_user;

-- Add comments for documentation
COMMENT ON TABLE users IS '用户表，存储所有用户信息';
COMMENT ON TABLE trashcan_data IS '智能垃圾桶数据表';
COMMENT ON TABLE waste_categories IS '垃圾分类类别表';
COMMENT ON TABLE classifications IS '垃圾分类记录表';
COMMENT ON TABLE user_statistics IS '用户统计信息表';
COMMENT ON TABLE user_notifications IS '用户通知表';
COMMENT ON VIEW user_dashboard_view IS '用户仪表板视图';
COMMENT ON VIEW trashcan_status_view IS '垃圾桶状态视图';

-- Set default values for updated_at columns
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create triggers for updated_at columns
CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_trashcan_data_updated_at BEFORE UPDATE ON trashcan_data FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_waste_categories_updated_at BEFORE UPDATE ON waste_categories FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_classifications_updated_at BEFORE UPDATE ON classifications FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_user_profiles_updated_at BEFORE UPDATE ON user_profiles FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_user_preferences_updated_at BEFORE UPDATE ON user_preferences FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_user_statistics_updated_at BEFORE UPDATE ON user_statistics FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_user_notifications_updated_at BEFORE UPDATE ON user_notifications FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Final success message
SELECT 'EcoSorter database initialized successfully!' as message;