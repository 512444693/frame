package com.zm.frame.thread.server;

import com.zm.frame.thread.msg.ThreadMsg;

/**
 * Created by Administrator on 2016/7/2.
 */
public class Server {

    private static final Server instance = new Server();

    private ClassFactory factory;

    public static Server getInstance() {
        return instance;
    }

    public void setClassFactory(ClassFactory factory) {
        this.factory = factory;
    }

    public ClassFactory getClassFactory() {
        return factory;
    }

    public void sendThreadMsgTo(ThreadMsg msg) {}
}
