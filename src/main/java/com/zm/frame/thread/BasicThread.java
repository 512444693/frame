package com.zm.frame.thread;

import com.zm.frame.conf.Definition;
import com.zm.frame.thread.msg.ThreadMsg;
import com.zm.frame.thread.msg.ThreadMsgBody;
import com.zm.frame.thread.server.Server;

/**
 * Created by Administrator on 2016/7/2.
 */
public abstract class BasicThread extends Thread {

    private Server server = Server.getInstance();
    private int threadType;
    private int threadId;

    public BasicThread(int threadType, int threadId) {
        this.threadId = threadId;
    }

    // 供ThreadGroup使用
    public abstract void putThreadMsg(ThreadMsg msg);

    //发送消息，从非task发送
    protected void sendThreadMsgTo
    (int desThreadType, int desThreadId, int desTaskId, int msgType, ThreadMsgBody msgBody) {
        ThreadMsg msg = new ThreadMsg(this.threadType, this.threadId, Definition.NONE,
                desThreadType, desThreadId, desTaskId, msgType, msgBody);
        server.sendThreadMsgTo(msg);
    }

    //发送消息，从task发送
    protected void sendThreadMsgTo
    (int srcTaskId, int desThreadType, int desThreadId, int desTaskId, int msgType, ThreadMsgBody msgBody) {
        ThreadMsg msg = new ThreadMsg(this.threadType, this.threadId, srcTaskId,
                desThreadType, desThreadId, desTaskId, msgType, msgBody);
        server.sendThreadMsgTo(msg);
    }

    //回包
    protected void replayThreadMsg(ThreadMsg msg, int msgType, ThreadMsgBody msgBody) {
        ThreadMsg replyMsg = new ThreadMsg(msg.desThreadType, msg.desThreadId, msg.desTaskId,
                msg.srcThreadType, msg.srcThreadId, msg.srcTaskId, msgType, msgBody);
        server.sendThreadMsgTo(replyMsg);
    }
}
