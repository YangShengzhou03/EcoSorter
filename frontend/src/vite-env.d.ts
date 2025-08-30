/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_API_BASE_URL: string
  readonly VITE_API_TIMEOUT: string
  readonly VITE_APP_NAME: string
  readonly VITE_APP_VERSION: string
  readonly VITE_APP_DESCRIPTION: string
  readonly VITE_AUTH_TOKEN_KEY: string
  readonly VITE_AUTH_REFRESH_TOKEN_KEY: string
  readonly VITE_AUTH_TOKEN_EXPIRY: string
  readonly VITE_ENABLE_ANALYTICS: string
  readonly VITE_ENABLE_DEBUG: string
  readonly VITE_ENABLE_MAINTENANCE: string
  readonly VITE_GOOGLE_ANALYTICS_ID: string
  readonly VITE_SENTRY_DSN: string
  readonly VITE_STRIPE_PUBLISHABLE_KEY: string
  readonly VITE_MAX_UPLOAD_SIZE: string
  readonly VITE_ALLOWED_FILE_TYPES: string
  readonly VITE_PWA_ENABLED: string
  readonly VITE_PWA_NAME: string
  readonly VITE_PWA_SHORT_NAME: string
  readonly VITE_DEV_SERVER_PORT: string
  readonly VITE_DEV_SERVER_HOST: string
  readonly VITE_SOURCEMAP: string
  readonly VITE_MINIFY: string
  readonly VITE_DEFAULT_LANGUAGE: string
  readonly VITE_SUPPORTED_LANGUAGES: string
  readonly VITE_DEFAULT_THEME: string
  readonly VITE_AVAILABLE_THEMES: string
  readonly VITE_NOTIFICATION_TIMEOUT: string
  readonly VITE_NOTIFICATION_POSITION: string
  readonly VITE_CACHE_ENABLED: string
  readonly VITE_CACHE_DURATION: string
  readonly VITE_LAZY_LOADING_ENABLED: string
  readonly VITE_IMAGE_OPTIMIZATION: string
  readonly VITE_CSP_ENABLED: string
  readonly VITE_XSS_PROTECTION: string
  readonly VITE_PERFORMANCE_MONITORING: string
  readonly VITE_ERROR_TRACKING: string
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}