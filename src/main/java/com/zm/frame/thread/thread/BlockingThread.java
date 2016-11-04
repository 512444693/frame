package com.zm.frame.thread.thread;


import com.zm.frame.thread.msg.ThreadMsg;
import com.zm.frame.thread.task.Task;

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

    protected void processMsg(ThreadMsg msg) {
        Task task = tasks.get(msg.desTaskId);
        if(task != null) {
            task.processMsg(msg);
        } else {
            log.error("处理线程消息失败, 找不到task id为" + msg.desTaskId + "的task");
        }
    }
}
