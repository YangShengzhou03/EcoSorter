import { createRouter, createWebHistory } from "vue-router";

const routes = [
  { 
    path: "/", 
    redirect: "/init"
  },
  { 
    path: "/init", 
    component: () => import("./views/DeviceInit.vue"),
    meta: { title: "设备初始化" }
  },
  { 
    path: "/work", 
    component: () => import("./views/WorkStatus.vue"),
    meta: { title: "工作状态" }
  },
  { 
    path: "/login", 
    component: () => import("./views/UserLogin.vue"),
    meta: { title: "用户登录" }
  },
  { 
    path: "/recognize", 
    component: () => import("./views/GarbageRecognize.vue"),
    meta: { title: "垃圾识别" }
  },
  { path: "/:pathMatch(.*)*", redirect: "/" },
];

const router = createRouter({ history: createWebHistory(), routes });

router.beforeEach((to, from, next) => {
  const isInitialized = localStorage.getItem('deviceInitialized')
  
  if (to.path !== '/init' && !isInitialized) {
    next('/init')
  } else if (to.path === '/init' && isInitialized) {
    next('/work')
  } else {
    next()
  }
})

router.afterEach((to) => {
  document.title = to.meta?.title || "易控智能垃圾桶";
  window.scrollTo(0, 0);
});

router.onError((error) => {
  console.error("页面加载失败，请刷新重试" + error.message);
});

export default router;
