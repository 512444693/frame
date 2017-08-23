package com.zm.frame.thread.thread;

import com.zm.frame.conf.Definition;
import com.zm.frame.thread.msg.ThreadMsg;
import com.zm.frame.thread.msg.ThreadMsgBody;
import com.zm.frame.thread.server.ThreadServer;
import com.zm.frame.thread.task.Task;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.zm.frame.conf.Definition.MSG_TYPE_CHECK_TASK_TIMEOUT;
import static com.zm.frame.log.Log.log;

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
        log.error("线程" + threadType + ":" + threadId + "异常退出!!!");
    }

    protected abstract void init();

    protected abstract void process();

    protected void processMsg(ThreadMsg msg) {
        if (msg.desTaskId == Definition.NONE) {
            threadProcessMsg(msg);
        } else {
            taskProcessMsg(msg);
        }
    }

    protected void threadProcessMsg(ThreadMsg msg) {
        switch (msg.msgType) {
            case MSG_TYPE_CHECK_TASK_TIMEOUT:
                checkTaskTimeout();
                break;
            default:
                log.error("消息类型错误：" + msg.msgType);
        }
    }

    protected void taskProcessMsg(ThreadMsg msg) {
        Task task = tasks.get(msg.desTaskId);
        if(task != null) {
            task.processMsg(msg);
        } else {
            log.error("处理线程消息失败, 找不到task id为" + msg.desTaskId + "的task");
        }
    }

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

    public Task addTask(int taskType,int time, Object arg) {
        int taskId = taskIdNo++;
        Task task = threadServer.getClassFactory().genTask(taskType, taskId, this, time, arg);
        this.tasks.put(taskId, task);
        task.init();
        return task;
    }

    public void removeTask(Task task) {
        task.destroy();
        this.tasks.remove(task.getTaskId());
    }

    public void checkTaskTimeout() {
        Iterator<Map.Entry<Integer, Task>> iterator = tasks.entrySet().iterator();
        Map.Entry<Integer, Task> entry;
        Task task;
        while(iterator.hasNext()) {
            entry = iterator.next();
            task = entry.getValue();
            if(task.isTimeout()) {
                iterator.remove();
            }
        }
    }

    public int getTaskSize() {
        return tasks.size();
    }
    //TODO : 写一个定时线程，给所有线程定时发消息检查任务是否超时
}
