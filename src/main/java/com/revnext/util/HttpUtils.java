package com.revnext.util;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.List;

public class HttpUtils {

    public static List<String> getClientIPs(HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        String getWay = request.getHeader("VIA");   // Gateway
        String forwardedFor = request.getHeader("X-FORWARDED-FOR");   // proxy
        return Arrays.asList(ipAddress, getWay, forwardedFor);
    }
}
