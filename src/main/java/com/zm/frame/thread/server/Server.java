package com.zm.frame.thread.server;

import com.zm.frame.thread.thread.ThreadGroup;
import com.zm.frame.thread.msg.ThreadMsg;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import static com.zm.frame.log.Log.log;

/**
 * Created by Administrator on 2016/7/2.
 */
public class Server {

    private static final Server instance = new Server();

    private ClassFactory factory;

    //thread type and thread group
    private ConcurrentHashMap<Integer, ThreadGroup> threadGroupMap = new ConcurrentHashMap<>();

    public static Server getInstance() {
        return instance;
    }

    public void setClassFactory(ClassFactory factory) {
        this.factory = factory;
    }

    public ClassFactory getClassFactory() {
        return factory;
    }

    public void addThreadGroup(ThreadGroup threadGroup) {
        threadGroupMap.put(threadGroup.getThreadType(), threadGroup);
    }

    public void sendThreadMsgTo(ThreadMsg msg) {
        ThreadGroup threadGroup = threadGroupMap.get(msg.desThreadType);
        if(threadGroup != null) {
            threadGroup.putThreadMsg(msg);
        } else {
            log.error("发送线程消息失败, 找不到thread type为" + msg.desThreadType + "的线程");
        }
    }

    public void start() {
        Iterator<Map.Entry<Integer, ThreadGroup>> iterator = threadGroupMap.entrySet().iterator();
        Map.Entry<Integer, ThreadGroup> tmp;
        while (iterator.hasNext()) {
            tmp = iterator.next();
            tmp.getValue().start();
        }
    }
}
