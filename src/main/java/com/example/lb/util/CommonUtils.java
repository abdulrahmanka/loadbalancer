package com.example.lb.util;

import org.springframework.http.server.reactive.ServerHttpRequest;

public class CommonUtils {

    public static String getRootPath(ServerHttpRequest request) {
        String path = request.getURI().getRawPath();
        return path.split("/")[1];
    }

    public static String createUrl(String serverUrl, String path) {
        if (!serverUrl.endsWith("/")) serverUrl += "/";
        if (path.startsWith("/")) path = path.substring(1);

        return serverUrl + path;
    }


}
