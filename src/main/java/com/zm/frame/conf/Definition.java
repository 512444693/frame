package com.zm.frame.conf;

import java.io.File;

/**
 * Created by zhangmin on 2016/10/26.
 */
public class Definition {

    public static final String CONFIGURATION_DIRECTORY_PATH = "conf" + File.separator;

    //不明确指定某一个id
    public static final int NONE = -1;

    //线程类型；约定框架使用101-199，程序使用1001-1999

    //消息类型；约定框架使用201-299，程序使用2001-2999
    public static final int MSG_TYPE_CHECK_TASK_TIMEOUT = 201;

    //task类型；约定框架使用301-399，程序使用3001-3999

    //其他类型; 约定框架使用401-999, 程序使用4001-
}
