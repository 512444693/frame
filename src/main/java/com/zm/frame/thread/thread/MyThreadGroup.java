package com.zm.frame.thread.thread;

import com.zm.frame.conf.Definition;
import com.zm.frame.thread.msg.ThreadMsg;
import com.zm.frame.thread.server.ThreadServer;

import static com.zm.frame.log.Log.log;

/**
 * Created by Administrator on 2016/10/30.
 */
public class MyThreadGroup {

    private ThreadServer threadServer = ThreadServer.getInstance();

    private int threadType;
    private int num;
    private BasicThread[] threads;

    private int index = 0;

    public MyThreadGroup(int threadType, int num, Object arg) {
        this.threadType = threadType;
        this.num = num;
        threads = new BasicThread[num];
        for(int i = 0; i < num; i++) {
            threads[i] = threadServer.getClassFactory().genThread(threadType, i, arg);
        }
        threadServer.addThreadGroup(this);
    }

    //TODO : 给所有线程发送消息；给某个类型的所有线程发送消息
    public void putThreadMsg(ThreadMsg msg) {
        //为-1，轮询
        if(msg.desThreadId == Definition.NONE) {
            threads[(index++ % num)].putThreadMsg(msg);
        }
        else if(msg.desThreadId >= 0 && msg.desThreadId < num) {
            threads[msg.desThreadId].putThreadMsg(msg);
        }
        else {
            log.error("发送线程消息失败, 找不到thread id为" +
                    msg.desThreadId + "的" + msg.desThreadType + "类型线程");
        }
    }

    public int getThreadType() {
        return threadType;
    }

    public void start() {
        for(int i = 0; i < num; i++) {
            threads[i].start();
        }
    }
}
