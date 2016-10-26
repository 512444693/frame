package com.zm.frame.log;

import com.zm.frame.conf.Definition;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Created by Administrator on 2016/7/1.
 */
public class Log {
    static {
        PropertyConfigurator.configure(Definition.CONFIGURATION_DIRECTORY_PATH +  "log4j.properties");
    }
    public static final Logger log = Logger.getLogger(Log.class);
}