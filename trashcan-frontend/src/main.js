import { createApp } from "vue";
import { createPinia } from "pinia";
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";

import App from "./App.vue";
import router from "./router.js";

const app = createApp(App);

app.config.errorHandler = (err, instance, info) => {
  const ignoreErrors = ['ResizeObserver loop', 'ResizeObserver loop limit exceeded'];
  if (!ignoreErrors.some(msg => err.message?.includes(msg))) {
    console.error('[Vue Error]', err, info);
  }
};

window.addEventListener('error', (event) => {
  const ignoreErrors = ['ResizeObserver loop', 'ResizeObserver loop limit exceeded', 'Non-Error promise rejection'];
  if (ignoreErrors.some(msg => event.message?.includes(msg))) {
    event.preventDefault();
    event.stopPropagation();
    return false;
  }
  console.error('[Window Error]', event.error);
});

window.addEventListener('unhandledrejection', (event) => {
  console.error('[Unhandled Rejection]', event.reason);
  event.preventDefault();
});

app.use(createPinia()).use(router).use(ElementPlus).mount("#app");
