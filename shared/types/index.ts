// 用户相关类型
export interface User {
  id: string;
  username: string;
  email: string;
  phone?: string;
  avatar?: string;
  points: number;
  creditScore: number;
  status: 'active' | 'inactive' | 'banned';
  createdAt: Date;
  updatedAt: Date;
}

export interface UserProfile extends User {
  totalRecycled: number;
  correctRate: number;
  rank: number;
  community?: Community;
}

// 垃圾分类相关类型
export interface GarbageCategory {
  id: string;
  name: string;
  color: string;
  icon: string;
  description: string;
  pointsPerKg: number;
}

export const GarbageCategories = {
  RECYCLABLE: 'recyclable',
  KITCHEN: 'kitchen',
  HAZARDOUS: 'hazardous',
  OTHER: 'other',
} as const;

export type GarbageCategoryType = typeof GarbageCategories[keyof typeof GarbageCategories];

export interface GarbageRecord {
  id: string;
  userId: string;
  category: GarbageCategoryType;
  weight: number;
  points: number;
  imageUrl?: string;
  confidence: number;
  location: Location;
  deviceId?: string;
  status: 'pending' | 'verified' | 'rejected';
  createdAt: Date;
}

// 积分相关类型
export interface Reward {
  id: string;
  userId: string;
  type: 'recycle' | 'signin' | 'invite' | 'task';
  points: number;
  description: string;
  relatedId?: string;
  createdAt: Date;
}

// IoT设备相关类型
export interface Device {
  id: string;
  name: string;
  type: 'trash_bin' | 'sensor' | 'camera';
  status: 'online' | 'offline' | 'maintenance';
  location: Location;
  communityId?: string;
  lastHeartbeat?: Date;
  firmwareVersion: string;
  capacity: number;
  currentFill: number;
}

// 社区相关类型
export interface Community {
  id: string;
  name: string;
  address: string;
  location: Location;
  totalUsers: number;
  totalRecycled: number;
  ranking: number;
}

// 通用类型
export interface Location {
  latitude: number;
  longitude: number;
  address?: string;
}

export interface PaginationParams {
  page?: number;
  limit?: number;
  sortBy?: string;
  sortOrder?: 'asc' | 'desc';
}

export interface PaginatedResponse<T> {
  data: T[];
  pagination: {
    page: number;
    limit: number;
    total: number;
    totalPages: number;
  };
}

// API响应类型
export interface ApiResponse<T = any> {
  success: boolean;
  data?: T;
  message?: string;
  error?: string;
}

// 认证相关类型
export interface AuthTokens {
  accessToken: string;
  refreshToken: string;
  expiresIn: number;
}

export interface LoginResponse {
  user: User;
  tokens: AuthTokens;
}

// 错误类型
export interface AppError {
  code: string;
  message: string;
  details?: any;
}