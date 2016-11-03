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

    private final static String formatStr = "源线程类型：%d, 源线程id：%d, 源task id：%d； " +
            "目标线程类型：%d, 目标线程id：%d, 目标task id：%d, 消息类型：%d";

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

    @Override
    public String toString() {
        return String.format(formatStr, srcThreadType, srcThreadId, srcTaskId,
                desThreadType, desThreadId, desTaskId, msgType);
    }
}
