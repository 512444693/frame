package com.zm.frame.thread.server;

import com.zm.frame.thread.thread.MyThreadGroup;
import com.zm.frame.thread.msg.ThreadMsg;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import static com.zm.frame.log.Log.log;

/**
 * Created by Administrator on 2016/7/2.
 * 服务于线程间通讯
 */
public class ThreadServer {

    private static final ThreadServer instance = new ThreadServer();

    private ClassFactory factory;

    //thread type and thread group
    private ConcurrentHashMap<Integer, MyThreadGroup> threadGroupMap = new ConcurrentHashMap<>();

    public static ThreadServer getInstance() {
        return instance;
    }

    public void setClassFactory(ClassFactory factory) {
        this.factory = factory;
    }

    public ClassFactory getClassFactory() {
        return factory;
    }

    public void addThreadGroup(MyThreadGroup threadGroup) {
        threadGroupMap.put(threadGroup.getThreadType(), threadGroup);
    }

    public void sendThreadMsgTo(ThreadMsg msg) {
        MyThreadGroup threadGroup = threadGroupMap.get(msg.desThreadType);
        if(threadGroup != null) {
            threadGroup.putThreadMsg(msg);
        } else {
            log.error("发送线程消息失败, 找不到thread type为" + msg.desThreadType + "的线程");
        }
    }

    // TODO : 按照java8重写框架
    public void startThreads() {
        Iterator<Map.Entry<Integer, MyThreadGroup>> iterator = threadGroupMap.entrySet().iterator();
        Map.Entry<Integer, MyThreadGroup> tmp;
        while (iterator.hasNext()) {
            tmp = iterator.next();
            tmp.getValue().start();
        }
    }
}
