package com.ablaze.common;

/**
 * 基于ThreadLocal封装工具类，用于保存和获取当前登录用户id
 *
 * @author ablaze
 * @Date: 2023/07/02/23:24
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置值
     * @param id
     */
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    /**
     * 获取值
     * @return
     */
    public static Long getCurrentId() {
        return threadLocal.get();
    }
}
