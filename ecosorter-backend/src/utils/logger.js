const winston = require('winston');
const path = require('path');

const fs = require('fs');
const logsDir = path.join(__dirname, '../../logs');
if (!fs.existsSync(logsDir)) {
  fs.mkdirSync(logsDir, { recursive: true });
}

const logFormat = winston.format.combine(
  winston.format.timestamp({
    format: 'YYYY-MM-DD HH:mm:ss'
  }),
  winston.format.errors({ stack: true }),
  winston.format.json()
);

const consoleFormat = winston.format.combine(
  winston.format.colorize(),
  winston.format.timestamp({
    format: 'YYYY-MM-DD HH:mm:ss'
  }),
  winston.format.printf(({ timestamp, level, message, stack }) => {
    return `${timestamp} [${level}]: ${stack || message}`;
  })
);

const logger = winston.createLogger({
  level: process.env.LOG_LEVEL || 'info',
  format: logFormat,
  defaultMeta: { service: 'ecosorter-backend' },
  transports: [
    new winston.transports.File({
      filename: path.join(logsDir, 'error.log'),
      level: 'error',
      maxsize: 5242880,
      maxFiles: 5,
    }),
    
    new winston.transports.File({
      filename: path.join(logsDir, 'combined.log'),
      maxsize: 5242880,
      maxFiles: 5,
    }),
    
    new winston.transports.File({
      filename: path.join(logsDir, 'http.log'),
      level: 'http',
      maxsize: 5242880,
      maxFiles: 5,
    }),
    
    new winston.transports.File({
      filename: path.join(logsDir, 'security.log'),
      level: 'warn',
      maxsize: 5242880,
      maxFiles: 5,
    }),
  ],
});

if (process.env.NODE_ENV !== 'production') {
  logger.add(new winston.transports.Console({
    format: consoleFormat,
    level: 'debug'
  }));
}

logger.stream = {
  write: (message) => {
    logger.http(message.trim());
  }
};

logger.auth = (message, meta = {}) => {
  logger.info(`[AUTH] ${message}`, { ...meta, category: 'authentication' });
};

logger.security = (message, meta = {}) => {
  logger.warn(`[SECURITY] ${message}`, { ...meta, category: 'security' });
};

logger.api = (message, meta = {}) => {
  logger.info(`[API] ${message}`, { ...meta, category: 'api' });
};

logger.db = (message, meta = {}) => {
  logger.info(`[DATABASE] ${message}`, { ...meta, category: 'database' });
};

logger.ai = (message, meta = {}) => {
  logger.info(`[AI] ${message}`, { ...meta, category: 'ai_classification' });
};

logger.error = (message, meta = {}) => {
  logger.error(`[ERROR] ${message}`, { ...meta, category: 'error' });
};

module.exports = logger;