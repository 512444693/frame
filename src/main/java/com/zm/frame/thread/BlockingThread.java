package com.zm.frame.thread;


import com.zm.frame.thread.msg.ThreadMsg;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static com.zm.frame.log.Log.log;

/**
 * Created by Administrator on 2016/7/2.
 */
public abstract class BlockingThread extends BasicThread {

    private BlockingQueue<ThreadMsg> msgQueue = new LinkedBlockingDeque();

    public BlockingThread(int threadType, int threadId) {
        super(threadType, threadId);
    }

    @Override
    public void putThreadMsg(ThreadMsg msg) {
        try {
            msgQueue.put(msg);
        } catch (InterruptedException e) {
            log.error("id为" + threadId + "的" + threadType + "类型阻塞线程" + "塞消息中断异常", e);
        }
    }


    @Override
    public void process() {
        while(true) {
            try {
                ThreadMsg msg= msgQueue.take();
                processMsg(msg);
            } catch (InterruptedException e) {
                log.error("id为" + threadId + "的" + threadType + "类型阻塞线程"  + "取消息中断异常", e);
            }
        }
    }

    protected abstract void processMsg(ThreadMsg msg);
}
