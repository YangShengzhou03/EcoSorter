package com.ecosorter.util;

public class PaginationUtil {
    
    private PaginationUtil() {
    }
    
    public static long normalizePage(Long page) {
        if (page == null || page <= 0) {
            return 1L;
        }
        return page;
    }
    
    public static long normalizePage(Integer page) {
        if (page == null || page <= 0) {
            return 1L;
        }
        return page.longValue();
    }
    
    public static int normalizePageSize(Integer pageSize) {
        if (pageSize == null || pageSize <= 0) {
            return 10;
        }
        if (pageSize > 100) {
            return 100;
        }
        return pageSize;
    }
    
    public static int normalizePageSize(Long pageSize) {
        if (pageSize == null || pageSize <= 0) {
            return 10;
        }
        if (pageSize > 100) {
            return 100;
        }
        return pageSize.intValue();
    }
}
