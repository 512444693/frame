package com.zm.frame.thread.thread;

import com.zm.frame.conf.Definition;
import com.zm.frame.thread.msg.ThreadMsg;
import com.zm.frame.thread.msg.ThreadMsgBody;
import com.zm.frame.thread.server.ThreadServer;
import com.zm.frame.thread.task.Task;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/2.
 */
public abstract class BasicThread extends Thread {

    private ThreadServer threadServer = ThreadServer.getInstance();
    protected int threadType;
    protected int threadId;
    protected Map<Integer, Task> tasks;
    private int taskIdNo = 0;

    public BasicThread(int threadType, int threadId) {
        this.threadType = threadType;
        this.threadId = threadId;
        tasks = new HashMap<>();
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

    //发送消息，发往task
    protected void sendThreadMsgTo
    (int msgType, ThreadMsgBody msgBody, int desThreadType, int desThreadId, int desTaskId) {
        ThreadMsg msg = new ThreadMsg(this.threadType, this.threadId, Definition.NONE,
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
        ThreadMsg replyMsg = new ThreadMsg(this.threadType, this.threadId, Definition.NONE,
                msg.srcThreadType, msg.srcThreadId, msg.srcTaskId, msgType, msgBody);
        threadServer.sendThreadMsgTo(replyMsg);
    }

    public int getThreadType() {
        return threadType;
    }

    public int getThreadId() {
        return threadId;
    }

    public void addTask(int taskType, String[] args) {
        int taskId = taskIdNo++;
        this.tasks.put(taskId, threadServer.getClassFactory().genTask(taskType, taskId, this, args));
    }

    public void removeTask(int taskId) {
        this.tasks.remove(taskId);
    }

    public int getTaskSize() {
        return tasks.size();
    }
}
