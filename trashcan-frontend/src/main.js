import { createApp } from "vue";
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";

import App from "./App.vue";
import router from "./router.js";

const app = createApp(App);

app.config.errorHandler = (err, instance, info) => {
  const ignoreErrors = ['ResizeObserver loop', 'ResizeObserver loop limit exceeded'];
  if (!ignoreErrors.some(msg => err.message?.includes(msg))) {
  }
};

window.addEventListener('error', (event) => {
  const ignoreErrors = ['ResizeObserver loop', 'ResizeObserver loop limit exceeded', 'Non-Error promise rejection'];
  if (ignoreErrors.some(msg => event.message?.includes(msg))) {
    event.preventDefault();
    event.stopPropagation();
    return false;
  }
});

window.addEventListener('unhandledrejection', (event) => {
  event.preventDefault();
});

const IDLE_TIMEOUT = 1 * 60 * 1000;
const HEARTBEAT_INTERVAL = 30 * 60 * 1000;
let idleTimer = null;
let heartbeatTimer = null;

const resetIdleTimer = () => {
  if (idleTimer) {
    clearTimeout(idleTimer);
  }
  idleTimer = setTimeout(() => {
    const isInitialized = localStorage.getItem('deviceInitialized');
    if (isInitialized && window.location.pathname !== '/work') {
      sessionStorage.removeItem('userToken');
      sessionStorage.removeItem('user');
      localStorage.removeItem('trashcanAdminLoggedIn');
      if (window.location.pathname !== '/work') {
        window.location.href = '/work';
      }
    }
  }, IDLE_TIMEOUT);
};

const startHeartbeat = () => {
  if (heartbeatTimer) {
    clearInterval(heartbeatTimer);
  }
  
  const sendHeartbeat = async () => {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        await fetch(`${process.env.VUE_APP_API_BASE_URL || 'http://localhost:8081'}/api/trashcan/heartbeat`, {
          method: 'POST',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });
      } catch (error) {
      }
    }
  };
  
  sendHeartbeat();
  heartbeatTimer = setInterval(sendHeartbeat, HEARTBEAT_INTERVAL);
};

const stopHeartbeat = () => {
  if (heartbeatTimer) {
    clearInterval(heartbeatTimer);
    heartbeatTimer = null;
  }
};

const activityEvents = [
  'mousedown',
  'mousemove',
  'keypress',
  'scroll',
  'touchstart',
  'click',
  'keydown'
];

activityEvents.forEach(event => {
  document.addEventListener(event, resetIdleTimer, true);
});

resetIdleTimer();

const isInitialized = localStorage.getItem('deviceInitialized');
if (isInitialized) {
  startHeartbeat();
}

router.beforeEach((to, from, next) => {
  const isDeviceInitialized = localStorage.getItem('deviceInitialized');
  
  if (isDeviceInitialized) {
    if (!heartbeatTimer) {
      startHeartbeat();
    }
  } else {
    stopHeartbeat();
  }
  
  next();
});

app.use(router).use(ElementPlus).mount("#app");
