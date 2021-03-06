package com.zm.frame.utils;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

import static com.zm.frame.log.Log.log;

/**
 * Created by zhangmin on 2016/10/25.
 */
public class HttpsClientUtils {

    //response code为200时返回http body
    public static String get(String urlStr) {
        String ret = "";
        BufferedReader in = null;
        try {
            URL url = new URL(urlStr);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            conn.connect();

            if(conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String tmp;
                while ((tmp = in.readLine()) != null) {
                    ret += tmp + "\r\n";
                }
            }
        } catch (MalformedURLException e) {
            log.error("Url error :" + urlStr);
            log.error(e);
        } catch (IOException e) {
            log.error(e);
        } finally {
            try {
                if(in != null) {
                    in.close();
                }
            } catch (IOException e) {
                log.error(e);
            }
        }
        return ret.trim();
    }

    //response code为200时返回http body
    public static String post(String urlStr, String content) {
        String ret = "";
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            URL url = new URL(urlStr);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(content);
            out.flush();

            if(conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String tmp;
                while ((tmp = in.readLine()) != null) {
                    ret += tmp + "\r\n";
                }
            }
        } catch (MalformedURLException e) {
            log.error("Url error :" + urlStr);
            log.error(e);
        } catch (IOException e) {
            log.error(e);
        } finally {
            try {
                if(in != null) {
                    in.close();
                }
                if(out != null) {
                    out.close();
                }
            } catch (IOException e) {
                log.error(e);
            }
        }
        return ret.trim();
    }
}
