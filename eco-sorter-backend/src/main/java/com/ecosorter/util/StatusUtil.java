package com.ecosorter.util;

public class StatusUtil {
    
    private StatusUtil() {
    }
    
    public static String getTrashcanStatusText(String status) {
        if (status == null) return "未知";
        switch (status) {
            case "online": return "在线";
            case "offline": return "离线";
            case "maintenance": return "维护中";
            case "error": return "故障";
            default: return "未知";
        }
    }
    
    public static String getCollectorStatusText(String status) {
        if (status == null) return "未知";
        switch (status) {
            case "online": return "正常";
            case "offline": return "异常";
            case "maintenance": return "维护中";
            case "error": return "异常";
            default: return "未知";
        }
    }
}
