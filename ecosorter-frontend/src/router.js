import { createRouter, createWebHashHistory } from "vue-router";
import { ElMessage } from "element-plus";
const routes = [
  { path: "/", component: () => import("./views/HomePage.vue") },
  { path: "/login", component: () => import("./views/Login.vue") },
  { path: "/register", component: () => import("./views/Register.vue") },
  {
    path: "/resident",
    component: () => import("./views/resident/ResidentLayout.vue"),
    meta: { requiresAuth: true },
    children: [
      {
        path: "dashboard",
        component: () => import("./views/resident/Dashboard.vue"),
        meta: { requiresAuth: true },
      },
      {
        path: "records",
        component: () => import("./views/resident/Records.vue"),
        meta: { requiresAuth: true },
      },
      {
        path: "points",
        component: () => import("./views/resident/Points.vue"),
        meta: { requiresAuth: true },
      },
      {
        path: "complaint",
        component: () => import("./views/resident/Complaint.vue"),
        meta: { requiresAuth: true },
      },
    ],
  },
  {
    path: "/collector",
    component: () => import("./views/collector/CollectorLayout.vue"),
    meta: { requiresAuth: true, roles: ["collector"] },
    children: [
      {
        path: "dashboard",
        component: () => import("./views/collector/Dashboard.vue"),
        meta: { requiresAuth: true, roles: ["collector"] },
      },
      {
        path: "tasks",
        component: () => import("./views/collector/Tasks.vue"),
        meta: { requiresAuth: true, roles: ["collector"] },
      },
      {
        path: "collection",
        component: () => import("./views/collector/Collection.vue"),
        meta: { requiresAuth: true, roles: ["collector"] },
      },
      {
        path: "records",
        component: () => import("./views/collector/Records.vue"),
        meta: { requiresAuth: true, roles: ["collector"] },
      },
      {
        path: "device-status",
        component: () => import("./views/collector/DeviceStatus.vue"),
        meta: { requiresAuth: true, roles: ["collector"] },
      },
    ],
  },
  {
    path: "/admin",
    component: () => import("./views/admin/AdminLayout.vue"),
    meta: { requiresAuth: true, roles: ["admin"] },
    children: [
      {
        path: "dashboard",
        component: () => import("./views/admin/Dashboard.vue"),
        meta: { requiresAuth: true, roles: ["admin"] },
      },
      {
        path: "configuration",
        component: () => import("./views/admin/Configuration.vue"),
        meta: { requiresAuth: true, roles: ["admin"] },
      },
      {
        path: "reports",
        component: () => import("./views/admin/Reports.vue"),
        meta: { requiresAuth: true, roles: ["admin"] },
      },
      {
        path: "users",
        component: () => import("./views/admin/Users.vue"),
        meta: { requiresAuth: true, roles: ["admin"] },
      },
      {
        path: "devices",
        component: () => import("./views/admin/Devices.vue"),
        meta: { requiresAuth: true, roles: ["admin"] },
      },
      {
        path: "logs",
        component: () => import("./views/admin/Logs.vue"),
        meta: { requiresAuth: true, roles: ["admin"] },
      },
    ],
  },
  {
    path: "/bin",
    component: () => import("./views/bin/BinLayout.vue"),
    meta: { binAccess: true },
    children: [
      {
        path: "main",
        component: () => import("./views/bin/BinMain.vue"),
        meta: { binAccess: true },
      },
    ],
  },
];
const router = createRouter({ history: createWebHashHistory(), routes });
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem("token");
  const userInfo = JSON.parse(localStorage.getItem("userInfo") || "{}");
  const publicPaths = ["/", "/login", "/register"];
  const binPaths = ["/bin", "/bin/main"];
  if (publicPaths.includes(to.path)) {
    next();
    return;
  }
  if (binPaths.includes(to.path)) {
    next();
    return;
  }
  if (to.meta?.requiresAuth) {
    if (!token) {
      ElMessage.warning("请先登录");
      next("/login");
      return;
    }
    if (to.meta?.roles && !to.meta.roles.includes(userInfo.role)) {
      ElMessage.error("您没有权限访问该页面");
      switch (userInfo.role) {
        case "admin":
          next("/admin/dashboard");
          break;
        case "collector":
          next("/collector/dashboard");
          break;
        case "resident":
          next("/resident/dashboard");
          break;
        case "bin":
          next("/bin/main");
          break;
        default:
          next("/");
      }
      return;
    }
  }
  next();
});
export default router;
