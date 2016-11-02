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

    public ThreadGroup(int threadType, int num, String[] args) throws Exception {
        this.threadType = threadType;
        this.num = num;
        for(int i = 0; i < num; i++) {
            threads[i] = server.getClassFactory().genThread(threadType, i, args);
        }
    }

    public void putThreadMsg(ThreadMsg msg) {
        if(msg.desThreadId == Definition.NONE) {
            threads[(index++ % num)].putThreadMsg(msg);
        }
        else if(msg.desThreadId >= 0 && msg.desThreadId < num) {
            threads[msg.desThreadId].putThreadMsg(msg);
        }
        else {
            log.error("Thread id error : " + msg.desThreadId);
        }
    }
}
