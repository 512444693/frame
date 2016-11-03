package com.zm.frame.thread;

import com.zm.frame.conf.Definition;
import com.zm.frame.thread.msg.ThreadMsg;
import com.zm.frame.thread.msg.ThreadMsgBody;
import com.zm.frame.thread.server.Server;

import static com.zm.frame.log.Log.log;

/**
 * Created by Administrator on 2016/7/2.
 */
public abstract class BasicThread extends Thread {

    private Server server = Server.getInstance();
    protected int threadType;
    protected int threadId;

    public BasicThread(int threadType, int threadId) {
        this.threadType = threadType;
        this.threadId = threadId;
    }

    @Override
    public void run() {
        init();
        process();
    }

    protected abstract void init();
    protected abstract void process();

    // 供ThreadGroup使用，默认什么也不做
    public void putThreadMsg(ThreadMsg msg){}

    // 所有参数，从task发送
    protected void sendThreadMsgTo
    (int msgType, ThreadMsgBody msgBody, int srcTaskId, int desThreadType, int desThreadId, int desTaskId) {
        ThreadMsg msg = new ThreadMsg(this.threadType, this.threadId, srcTaskId,
                desThreadType, desThreadId, desTaskId, msgType, msgBody);
        server.sendThreadMsgTo(msg);
    }

    //发送消息，从非task发送，发往task
    protected void sendThreadMsgTo
    (int msgType, ThreadMsgBody msgBody, int desThreadType, int desThreadId, int desTaskId) {
        this.sendThreadMsgTo(msgType, msgBody, Definition.NONE, desThreadType, desThreadId, desTaskId);
    }

    //发送消息，从非task发送，发往非task
    protected void sendThreadMsgTo
    (int msgType, ThreadMsgBody msgBody, int desThreadType, int desThreadId) {
        this.sendThreadMsgTo(msgType, msgBody, Definition.NONE, desThreadType, desThreadId, Definition.NONE);
    }

    //发送消息，从非task发送，发往非task，不指定thread id
    protected void sendThreadMsgTo
    (int msgType, ThreadMsgBody msgBody, int desThreadType) {
        this.sendThreadMsgTo(msgType, msgBody, Definition.NONE, desThreadType, Definition.NONE, Definition.NONE);
    }

    //从task回包
    protected void replayThreadMsg(ThreadMsg msg, int msgType, ThreadMsgBody msgBody, int srcTaskId) {
        ThreadMsg replyMsg = new ThreadMsg(this.threadType, this.threadId, srcTaskId,
                msg.srcThreadType, msg.srcThreadId, msg.srcTaskId, msgType, msgBody);
        server.sendThreadMsgTo(replyMsg);
    }

    //从非task回包
    protected void replayThreadMsg(ThreadMsg msg, int msgType, ThreadMsgBody msgBody) {
        this.replayThreadMsg(msg, msgType, msgBody, Definition.NONE);
    }
}
