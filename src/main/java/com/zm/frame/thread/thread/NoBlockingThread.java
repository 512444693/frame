package com.zm.frame.thread.thread;


import com.zm.frame.thread.msg.ThreadMsg;
import com.zm.frame.thread.task.Task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static com.zm.frame.log.Log.log;

/**
 * Created by Administrator on 2016/7/2.
 */
public abstract class NoBlockingThread extends BasicThread {
    private BlockingQueue<ThreadMsg> msgQueue = new LinkedBlockingDeque();
    private int delayTime;//单位ms

    public NoBlockingThread(int threadType, int threadId, int delayTime) {
        super(threadType, threadId);
        this.delayTime = delayTime;
    }

    @Override
    public void putThreadMsg(ThreadMsg msg) {
        msgQueue.offer(msg);
    }

    @Override
    public void process() {
        while (true) {
            ThreadMsg msg = msgQueue.poll();
            if(msg != null) {
                processMsg(msg);
            }
            afterProcessMsg();
            if(delayTime > 0) {
                try {
                    sleep(delayTime);
                } catch (InterruptedException e) {
                    log.error("id为" + threadId + "的" + threadType + "类型非阻塞线程"   + "睡眠被中断", e);
                }
            }
        }
    }

    protected abstract void afterProcessMsg();
}
