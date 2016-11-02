package com.zm.frame.thread.msg;


/**
 * Created by Administrator on 2016/7/2.
 */
public class ThreadMsg {
    public int srcThreadType;
    public int srcThreadId;
    public int srcTaskId;
    public int desThreadType;
    public int desThreadId;
    public int desTaskId;
    public int msgType;
    public ThreadMsgBody msgBody;

    public ThreadMsg(int srcThreadType, int srcThreadId, int srcTaskId, int desThreadType,
                     int desThreadId, int desTaskId, int msgType, ThreadMsgBody msgBody) {
        this.srcThreadType = srcThreadType;
        this.srcThreadId = srcThreadId;
        this.srcTaskId = srcTaskId;
        this.desThreadType = desThreadType;
        this.desThreadId = desThreadId;
        this.desTaskId = desTaskId;
        this.msgType = msgType;
        this.msgBody = msgBody;
    }
}
