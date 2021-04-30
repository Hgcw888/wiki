package com.hgcw.wiki.util;


import java.io.Serializable;

/**
 * 线程本地变量相当于一条线，线程有值后，线后面的就可以获取线程中的值
 */
public class RequestContext implements Serializable {

    private static ThreadLocal<String> remoteAddr = new ThreadLocal<>();

    public static String getRemoteAddr() {
        return remoteAddr.get();
    }

    public static void setRemoteAddr(String remoteAddr) {
        RequestContext.remoteAddr.set(remoteAddr);
    }

}
