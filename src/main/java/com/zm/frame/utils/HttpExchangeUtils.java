package com.zm.frame.utils;

import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import static com.zm.frame.log.Log.log;

/**
 * Created by zhangmin on 2016/10/25.
 */
public class HttpExchangeUtils {

    public static Map<String, String> getParameters(HttpExchange t) {
        Map<String, String> ret = new HashMap<>();
        try {
            String rawQuery = t.getRequestURI().getRawQuery();
            if(rawQuery != null && !rawQuery.equals("")) {
                for(String param : rawQuery.split("&")) {
                    String pair[] = param.split("=");
                    if(pair.length >= 2) {
                        ret.put(URLDecoder.decode(pair[0], "utf-8").trim(),
                                URLDecoder.decode(pair[1], "utf-8").trim());
                    } else {
                        ret.put(URLDecoder.decode(pair[0], "utf-8").trim(), "");
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            log.error(e);
        }
        return ret;
    }

    public static String getContent(HttpExchange t) {
        String ret = "";
        BufferedReader in = null;
        try {
            in =  new BufferedReader(new InputStreamReader(t.getRequestBody()));
            String tmp;
            while ((tmp = in.readLine()) != null) {
                ret += tmp + "\r\n";
            }
        } catch (IOException e) {
            log.error(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                log.error(e);
            }
        }
        return ret.trim();
    }

    public static void response(HttpExchange t, String response) {
        OutputStream os = null;
        try {
            t.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.getBytes().length);
            os = t.getResponseBody();
            os.write(response.getBytes());
        } catch (IOException e) {
            log.error(e);
        } finally {
            try {
                if(os != null) {
                    os.close();
                }
            } catch (IOException e) {
                log.error(e);
            }
        }
    }
}
