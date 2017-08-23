package com.zm.frame.thread.task;

import com.zm.frame.conf.Definition;
import com.zm.frame.thread.msg.ThreadMsg;
import com.zm.frame.thread.msg.ThreadMsgBody;
import com.zm.frame.thread.server.ThreadServer;
import com.zm.frame.thread.thread.BasicThread;

/**
 * Created by Administrator on 2016/11/4.
 */
public abstract class Task {

    private ThreadServer threadServer = ThreadServer.getInstance();

    private final int taskId;
    private final BasicThread thread;
    private final long createTime = System.currentTimeMillis();
    //-1则为无超时时间，超时单位为秒
    private final int time;

    public Task(int taskId, BasicThread thread, int time) {
        this.taskId = taskId;
        this.thread = thread;
        this.time = time;
    }

    public abstract void processMsg(ThreadMsg msg);

    // 所有参数，从task发送，发往task
    protected void sendThreadMsgTo
    (int msgType, ThreadMsgBody msgBody, int desThreadType, int desThreadId, int desTaskId) {
        ThreadMsg msg = new ThreadMsg(thread.getThreadType(), thread.getThreadId(), this.taskId,
                desThreadType, desThreadId, desTaskId, msgType, msgBody);
        threadServer.sendThreadMsgTo(msg);
    }

    //发送消息，发往非task
    protected void sendThreadMsgTo
    (int msgType, ThreadMsgBody msgBody, int desThreadType, int desThreadId) {
        this.sendThreadMsgTo(msgType, msgBody, desThreadType, desThreadId, Definition.NONE);
    }

    //发送消息，发往非task，不指定thread id
    protected void sendThreadMsgTo
    (int msgType, ThreadMsgBody msgBody, int desThreadType) {
        this.sendThreadMsgTo(msgType, msgBody, desThreadType, Definition.NONE, Definition.NONE);
    }

    //回包
    protected void replayThreadMsg(ThreadMsg msg, int msgType, ThreadMsgBody msgBody) {
        ThreadMsg replyMsg = new ThreadMsg(thread.getThreadType(), thread.getThreadId(), this.taskId,
                msg.srcThreadType, msg.srcThreadId, msg.srcTaskId, msgType, msgBody);
        threadServer.sendThreadMsgTo(replyMsg);
    }

    protected void remove() {
        thread.removeTask(this);
    }

    public int getTaskId() {
        return taskId;
    }

    public boolean isTimeout() {
        if(time != Definition.NONE) {
            if((System.currentTimeMillis() - createTime) >= (time * 1000)) {
                return true;
            }
        }
        return false;
    }

    protected abstract void init();
}
