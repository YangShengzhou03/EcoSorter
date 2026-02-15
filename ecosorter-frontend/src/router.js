import { createRouter, createWebHistory } from "vue-router";
import { ElMessage } from "element-plus";
import { useAuthStore } from "@/stores/auth";

const routes = [
  { 
    path: "/", 
    component: () => import("./components/indexPage.vue") 
  },
  { 
    path: "/login", 
    component: () => import("./views/index/UserLogin.vue") 
  },
  { 
    path: "/register", 
    component: () => import("./views/index/UserRegister.vue") 
  },
  {
    path: "/index",
    children: [
      { path: "", redirect: "/" },
      { path: "recyclable", component: () => import("./views/index/RecyclablePage.vue"), meta: { title: "可回收物" } },
      { path: "hazardous", component: () => import("./views/index/HazardousPage.vue"), meta: { title: "有害垃圾" } },
      { path: "kitchen", component: () => import("./views/index/KitchenPage.vue"), meta: { title: "厨余垃圾" } },
      { path: "other", component: () => import("./views/index/OtherPage.vue"), meta: { title: "其他垃圾" } },
      { path: "booking", component: () => import("./views/index/BookingPage.vue"), meta: { title: "回收预约" } },
      { path: "order-query", component: () => import("./views/index/OrderQueryPage.vue"), meta: { title: "订单查询" } },
      { path: "about", component: () => import("./views/index/AboutPage.vue"), meta: { title: "了解ECO" } },
      { path: "join", component: () => import("./views/index/JoinPage.vue"), meta: { title: "加入我们" } },
      { path: "contact", component: () => import("./views/index/ContactPage.vue"), meta: { title: "联系我们" } },
      { path: "privacy-policy", component: () => import("./views/index/PrivacyPolicyPage.vue"), meta: { title: "隐私政策" } },
      { path: "user-agreement", component: () => import("./views/index/UserAgreementPage.vue"), meta: { title: "用户协议" } },
    ]
  },
  {
    path: "/resident",
    component: () => import("./components/ResidentLayout.vue"),
    meta: { requiresAuth: true, roles: ["RESIDENT"] },
    children: [
      { path: "", redirect: "/resident/dashboard" },
      { path: "dashboard", component: () => import("./views/resident/Dashboard.vue") },
      { path: "records", component: () => import("./views/resident/Records.vue") },
      { path: "points", component: () => import("./views/resident/UserPoints.vue") },
      { path: "orders", component: () => import("./views/resident/Orders.vue") },
      { path: "point-records", component: () => import("./views/resident/PointRecords.vue") },
      { path: "complaint", component: () => import("./views/resident/UserComplaint.vue") },
      { path: "notifications", component: () => import("./views/resident/Notifications.vue") },
      { path: "profile", component: () => import("./views/resident/ResidentProfile.vue") },
      { path: "product/:id", component: () => import("./views/resident/ProductDetail.vue") },
      { path: "notifications/:id", component: () => import("./views/resident/NotificationDetail.vue") },
    ],
  },
  {
    path: "/collector",
    component: () => import("./components/CollectorLayout.vue"),
    meta: { requiresAuth: true, roles: ["COLLECTOR"] },
    children: [
      { path: "", redirect: "/collector/dashboard" },
      { path: "dashboard", component: () => import("./views/collector/Dashboard.vue") },
      { path: "tasks", component: () => import("./views/collector/CollectorTasks.vue") },
      { path: "tasks/:taskId", component: () => import("./views/collector/TaskDetail.vue") },
      { path: "device-status", component: () => import("./views/collector/DeviceStatus.vue") },
      { path: "notifications", component: () => import("./views/collector/Notifications.vue") },
      { path: "notifications/:id", component: () => import("./views/collector/NotificationDetail.vue") },
      { path: "profile", component: () => import("./views/collector/CollectorProfile.vue") },
    ],
  },
  {
    path: "/admin",
    component: () => import("./components/AdminLayout.vue"),
    meta: { requiresAuth: true, roles: ["ADMIN"] },
    children: [
      { path: "", redirect: "/admin/dashboard" },
      { path: "dashboard", component: () => import("./views/admin/Dashboard.vue") },
      { path: "reports", component: () => import("./views/admin/Reports.vue") },
      { path: "users", component: () => import("./views/admin/Users.vue") },
      { path: "task-management", component: () => import("./views/admin/TaskManagement.vue") },
      { path: "notifications", component: () => import("./views/admin/Notifications.vue") },
      { path: "complaints", component: () => import("./views/admin/Complaints.vue") },
      { path: "products", component: () => import("./views/admin/Products.vue") },
      { path: "banners", component: () => import("./views/admin/Banners.vue") },
      { path: "categories", component: () => import("./views/admin/Categories.vue") },
      { path: "orders", component: () => import("./views/admin/Orders.vue") },
      { path: "devices", component: () => import("./views/admin/Devices.vue") },
      { path: "profile", component: () => import("./views/admin/UserProfile.vue") },
    ],
  },
  { path: "/:pathMatch(.*)*", redirect: "/" },
];

const router = createRouter({ history: createWebHistory(), routes });

const PUBLIC_PATHS = ["/", "/login", "/register", "/index/recyclable", "/index/hazardous", "/index/kitchen", "/index/other", "/index/booking", "/index/order-query", "/index/about", "/index/join", "/index/contact", "/index/privacy-policy", "/index/user-agreement"];

const roleRedirectMap = {
  ADMIN: "/admin/dashboard",
  COLLECTOR: "/collector/dashboard",
  RESIDENT: "/resident/dashboard",
};

router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore();
  const isPublicPath = PUBLIC_PATHS.includes(to.path);

  if (isPublicPath) {
    next();
    return;
  }

  if (to.meta?.requiresAuth) {
    if (!authStore.isAuthenticated) {
      ElMessage.warning("请先登录");
      next("/login");
      return;
    }

    if (to.meta?.roles && !to.meta.roles.includes(authStore.userRole)) {
      ElMessage.error("您没有权限访问该页面");
      const defaultPath = roleRedirectMap[authStore.userRole] || "/";
      next(defaultPath);
      return;
    }
  }

  next();
});

router.afterEach((to) => {
  document.title = to.meta?.title || "ECO-Sorter";
  window.scrollTo(0, 0);
});

router.onError((error) => {
  ElMessage.error("页面加载失败，请刷新重试" + error.message);
});

export default router;
