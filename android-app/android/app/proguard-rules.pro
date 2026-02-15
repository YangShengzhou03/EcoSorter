#导航 V7.3.0及以后：
-keep class com.amap.api.navi.**{*;}
-keep class com.alibaba.mit.alitts.*{*;}
-keep class com.google.**{*;}

#定位：
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

#搜索：
-keep class com.amap.api.services.**{*;}

#3D地图 V5.0.0之后：
-keep class com.amap.api.maps.**{*;}
-keep class com.autonavi.**{*;}
-keep class com.amap.api.trace.**{*;}

# Google Play Services - 修复 package info is null 错误
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**
-keep interface com.google.android.gms.** { *; }

# Geolocator 相关
-keep class com.baseflow.geolocator.** { *; }
-dontwarn com.baseflow.geolocator.**

# 防止混淆导致 getPackageInfo 返回 null
-keep class android.content.pm.** { *; }
-keep class android.content.Context { *; }

# 保留 Google Play Services 的客户端库
-keep class com.google.android.gms.common.** { *; }
-keep class com.google.android.gms.location.** { *; }
-keep class com.google.android.gms.tasks.** { *; }
-keep class com.google.android.gms.common.api.** { *; }
-keep class com.google.android.gms.common.internal.** { *; }

# 保留反射调用的类
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# 保留 Parcelable
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
