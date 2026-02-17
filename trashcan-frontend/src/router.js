import { createRouter, createWebHistory } from "vue-router";

const routes = [
  { 
    path: "/", 
    redirect: "/init"
  },
  { 
    path: "/init", 
    name: "DeviceInit",
    component: () => import("./views/DeviceInit.vue"),
    meta: { title: "设备初始化" }
  },
  { 
    path: "/work", 
    name: "WorkStatus",
    component: () => import("./views/WorkStatus.vue"),
    meta: { title: "工作状态" }
  },
  { 
    path: "/admin", 
    name: "TrashcanAdmin",
    component: () => import("./views/TrashcanAdmin.vue"),
    meta: { title: "管理后台" }
  },
  { 
    path: "/login", 
    name: "UserLogin",
    component: () => import("./views/UserLogin.vue"),
    meta: { title: "用户登录" }
  },
  { 
    path: "/recognize", 
    name: "GarbageRecognize",
    component: () => import("./views/GarbageRecognize.vue"),
    meta: { title: "垃圾识别" }
  },
  { 
    path: "/:pathMatch(.*)*", 
    redirect: "/" 
  }
];

const router = createRouter({ 
  history: createWebHistory(), 
  routes 
});

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
  const title = to.meta?.title || "易控智能垃圾桶"
  document.title = title
  window.scrollTo(0, 0)
})

router.onError((error) => {
  const isInitialized = localStorage.getItem('deviceInitialized')
  if (!isInitialized) {
    window.location.href = '/init'
  }
})

export default router
