package com.zm.frame.thread;

import com.zm.frame.conf.Definition;
import com.zm.frame.thread.msg.ThreadMsg;
import com.zm.frame.thread.server.Server;

import static com.zm.frame.log.Log.log;

/**
 * Created by Administrator on 2016/10/30.
 */
public class ThreadGroup {

    private Server server = Server.getInstance();

    private int threadType;
    private int num;
    private BasicThread[] threads;

    private int index = 0;

    public ThreadGroup(int threadType, int num, String[] args) {
        this.threadType = threadType;
        this.num = num;
        threads = new BasicThread[num];
        for(int i = 0; i < num; i++) {
            threads[i] = server.getClassFactory().genThread(threadType, i, args);
        }
        server.addThreadGroup(this);
    }

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
