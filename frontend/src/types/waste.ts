export type WasteCategory = 'recyclable' | 'hazardous' | 'household' | 'kitchen' | 'other'

export interface WasteItem {
  id: string
  name: string
  category: WasteCategory
  description?: string
  imageUrl?: string
  disposalInstructions: string
  recyclingTips?: string
  hazardous: boolean
  commonMisconceptions?: string[]
  createdAt: string
  updatedAt: string
}

export interface WasteClassification {
  id: string
  itemName: string
  predictedCategory: WasteCategory
  confidence: number
  imageUrl?: string
  userId: string
  timestamp: string
  location?: {
    latitude: number
    longitude: number
  }
  verified?: boolean
  actualCategory?: WasteCategory
  notes?: string
}

export interface WasteStatistics {
  totalClassifications: number
  correctClassifications: number
  accuracyRate: number
  categoryBreakdown: {
    category: WasteCategory
    count: number
    percentage: number
  }[]
  dailyStats: {
    date: string
    count: number
    accuracy: number
  }[]
  topMisclassifiedItems: {
    itemName: string
    predictedCategory: WasteCategory
    actualCategory: WasteCategory
    count: number
  }[]
}

export interface ClassificationRequest {
  image: File | string
  itemName?: string
  location?: {
    latitude: number
    longitude: number
  }
}

export interface ClassificationResponse {
  success: boolean
  data?: {
    id: string
    category: WasteCategory
    confidence: number
    suggestions: WasteItem[]
    disposalInstructions: string
  }
  error?: string
}

export interface WasteCollectionSchedule {
  id: string
  area: string
  wasteType: WasteCategory
  collectionDay: string
  frequency: 'daily' | 'weekly' | 'bi-weekly' | 'monthly'
  startTime: string
  endTime: string
  notes?: string
}

export interface RecyclingCenter {
  id: string
  name: string
  address: string
  coordinates: {
    latitude: number
    longitude: number
  }
  acceptedCategories: WasteCategory[]
  operatingHours: {
    day: string
    open: string
    close: string
    isClosed: boolean
  }[]
  contact: {
    phone?: string
    email?: string
    website?: string
  }
  rating?: number
  distance?: number
}

export interface WasteDisposalGuide {
  category: WasteCategory
  title: string
  description: string
  icon: string
  color: string
  items: WasteItem[]
  disposalMethods: string[]
  doNot: string[]
  recyclingInfo?: string
  environmentalImpact: string
}

export interface UserWasteStats {
  userId: string
  totalItemsClassified: number
  correctClassifications: number
  accuracy: number
  favoriteCategory: WasteCategory
  lastClassification?: string
  streak: number
  achievements: WasteAchievement[]
}

export interface WasteAchievement {
  id: string
  title: string
  description: string
  icon: string
  unlocked: boolean
  unlockedAt?: string
  progress?: number
  target: number
}

export interface WasteSearchResult {
  item: WasteItem
  similarity: number
  alternativeNames?: string[]
}