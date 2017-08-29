package com.zm.frame.conf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.zm.frame.log.Log.log;

/**
 * Created by zhangmin on 2016/6/29.
 */
public class Config {
    protected Properties properties;

    public Config(String filePath) throws IOException {
        properties = new Properties();
        try {
            properties.load(this.getInputStream(filePath));
        } catch (IOException e) {
            log.error("读取配置文件失败");
            throw e;
        }
    }

    protected boolean getBoolean(String property) {
        boolean ret;
        String retStr = properties.getProperty(property);
        if(retStr == null || (!retStr.equals("true") && !retStr.equals("false"))) {
            log.error(property + "配置错误");
            throw new IllegalStateException(property + "配置错误");
        } else {
            ret = Boolean.parseBoolean(retStr);
        }
        return ret;
    }

    protected int getInt(String property) {
        int ret;
        String retStr = properties.getProperty(property);
        if(retStr == null) {
            log.error(property + "配置错误");
            throw new IllegalStateException(property + "配置错误");
        } else {
            try {
                ret = Integer.parseInt(retStr);
            } catch (Exception e) {
                log.error(property + "配置错误", e);
                throw new IllegalStateException(property + "配置错误: " + e.getMessage());
            }
        }
        return ret;
    }

    protected String getString(String property) {
        String ret = properties.getProperty(property);
        if(ret == null) {
            log.error(property + "配置错误");
            throw new IllegalStateException(property + "配置错误");
        }
        return ret;
    }

    //单独写一个函数是为了测试
    protected InputStream getInputStream(String filePath) {
        InputStream ret;
        try {
            ret = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            log.error("Can not find file " + filePath);
            throw new IllegalArgumentException("Can not find file " + filePath);
        }
        return ret;
    }
}
