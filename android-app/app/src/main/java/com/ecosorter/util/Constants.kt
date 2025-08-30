package com.ecosorter.util

/**
 * Application constants and configuration
 */
object Constants {
    // API Configuration
    const val BASE_URL = "https://api.ecosorter.com/v1/"
    const val API_TIMEOUT = 30L
    const val MAX_RETRY_ATTEMPTS = 3

    // Camera Configuration
    const val CAMERA_REQUEST_CODE = 1001
    const val GALLERY_REQUEST_CODE = 1002
    const val PERMISSION_REQUEST_CODE = 1003
    const val MAX_IMAGE_SIZE = 1024 // pixels
    const val IMAGE_QUALITY = 85

    // Database Configuration
    const val DATABASE_VERSION = 1
    const val PAGE_SIZE = 20
    const val CACHE_SIZE = 50

    // Classification Configuration
    const val CONFIDENCE_THRESHOLD = 0.7f
    const val MAX_PREDICTIONS = 5
    const val MIN_CONFIDENCE = 0.3f

    // File Configuration
    const val IMAGE_DIRECTORY = "EcoSorter/Images"
    const val CACHE_DIRECTORY = "EcoSorter/Cache"
    const val MAX_FILE_SIZE = 10 * 1024 * 1024L // 10MB
    const val SUPPORTED_IMAGE_TYPES = "image/jpeg,image/png,image/webp"

    // Notification Configuration
    const val NOTIFICATION_CHANNEL_ID = "ecosorter_channel"
    const val NOTIFICATION_ID = 1004
    const val NOTIFICATION_TIMEOUT = 5000L

    // Preferences Keys
    const val PREF_NAME = "ecosorter_preferences"
    const val PREF_THEME = "theme_mode"
    const val PREF_LANGUAGE = "language"
    const val PREF_FIRST_RUN = "first_run"
    const val PREF_CAMERA_PERMISSION = "camera_permission_granted"
    const val PREF_ANALYTICS = "analytics_enabled"
    const val PREF_NOTIFICATIONS = "notifications_enabled"

    // Error Messages
    const val ERROR_NETWORK = "network_error"
    const val ERROR_CAMERA = "camera_error"
    const val ERROR_STORAGE = "storage_error"
    const val ERROR_CLASSIFICATION = "classification_error"
    const val ERROR_UNKNOWN = "unknown_error"

    // Success Messages
    const val SUCCESS_CLASSIFICATION = "classification_success"
    const val SUCCESS_SAVE = "save_success"
    const val SUCCESS_DELETE = "delete_success"

    // Animation Durations
    const val ANIMATION_DURATION_SHORT = 200L
    const val ANIMATION_DURATION_MEDIUM = 300L
    const val ANIMATION_DURATION_LONG = 500L

    // Validation Rules
    const val MIN_NOTES_LENGTH = 2
    const val MAX_NOTES_LENGTH = 500
    const val MAX_CATEGORY_LENGTH = 50
    const val MAX_SUBCATEGORY_LENGTH = 50

    // Analytics Events
    const val EVENT_SCAN_STARTED = "scan_started"
    const val EVENT_SCAN_COMPLETED = "scan_completed"
    const val EVENT_SCAN_FAILED = "scan_failed"
    const val EVENT_ITEM_SAVED = "item_saved"
    const val EVENT_ITEM_DELETED = "item_deleted"
    const val EVENT_FAVORITE_TOGGLED = "favorite_toggled"
    const val EVENT_SETTINGS_CHANGED = "settings_changed"

    // Build Configuration
    const val BUILD_TYPE_DEBUG = "debug"
    const val BUILD_TYPE_RELEASE = "release"
    const val VERSION_CODE = 1
    const val VERSION_NAME = "1.0.0"

    // Date/Time Formats
    const val DATE_FORMAT_DISPLAY = "yyyy-MM-dd HH:mm:ss"
    const val DATE_FORMAT_FILE = "yyyyMMdd_HHmmss"
    const val DATE_FORMAT_SHORT = "MM/dd HH:mm"
    const val DATE_FORMAT_LONG = "yyyy年MM月dd日 HH:mm:ss"

    // Localization
    const val DEFAULT_LANGUAGE = "zh"
    const val SUPPORTED_LANGUAGES = "zh,en"

    // Performance
    const val DEBOUNCE_TIMEOUT = 300L
    const val THROTTLE_TIMEOUT = 1000L
    const val CACHE_EXPIRY = 3600000L // 1 hour
}