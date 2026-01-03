import{defineStore}from'pinia'
import{ref}from'vue'
import{ElMessage}from'element-plus'
export const useAuthStore=defineStore('auth',()=>{const token=ref(localStorage.getItem('token')||'')
const userInfo=ref(JSON.parse(localStorage.getItem('userInfo')||'{}'))
const isLoading=ref(false)
const isAuthenticated=()=>!!token.value
const testAccounts={resident:{password:'123456',role:'resident',name:'居民用户',permissions:['garbage_disposal','points_redeem','view_records','file_complaint']},collector:{password:'123456',role:'collector',name:'垃圾收集员',permissions:['receive_tasks','execute_collection','report_status','scan_bin']},admin:{password:'123456',role:'admin',name:'系统管理员',permissions:['user_management','device_monitor','data_analysis','exception_handling','rule_config']},bin:{password:'123456',role:'bin',name:'智能垃圾桶',permissions:['trash_detection','display_info','user_interaction','data_upload']}}
const login=async(credentials)=>{isLoading.value=true
try{const{username,password}=credentials
const account=testAccounts[username]
if(account&&account.password===password){const mockToken=`token-${username}-${Date.now()}`
const userData={username:username,name:account.name,role:account.role,permissions:account.permissions,id:username,avatar:`https://api.dicebear.com/7.x/avataaars/svg?seed=${username}`}
token.value=mockToken
userInfo.value=userData
localStorage.setItem('token',mockToken)
localStorage.setItem('userInfo',JSON.stringify(userData))
ElMessage.success(`欢迎${account.name}！`)
return{success:true,role:account.role}}else{throw new Error('用户名或密码错误')}}catch(error){ElMessage.error(error.message||'登录失败')
throw error}finally{isLoading.value=false}}
const logout=()=>{token.value=''
userInfo.value={}
localStorage.removeItem('token')
localStorage.removeItem('userInfo')
ElMessage.success('登出成功')}
const hasPermission=(permission)=>{return userInfo.value.permissions?.includes(permission)||false}
const hasRole=(role)=>{return userInfo.value.role===role}
const getRoleLabel=()=>{const roleMap={resident:'居民用户',collector:'垃圾收集员',admin:'系统管理员',bin:'智能垃圾桶'}
return roleMap[userInfo.value.role]||'未知用户'}
const getRoleColor=()=>{const colorMap={resident:'#1890ff',collector:'#52c41a',admin:'#ff4d4f',bin:'#722ed1'}
return colorMap[userInfo.value.role]||'#666'}
return{token,userInfo,isLoading,isAuthenticated,login,logout,hasPermission,hasRole,getRoleLabel,getRoleColor,testAccounts}})