package com.zm.frame.thread.server;

import com.zm.frame.thread.task.Task;
import com.zm.frame.thread.thread.BasicThread;

/**
 * Created by Administrator on 2016/11/2.
 */
public abstract class ClassFactory {
    public ClassFactory() {
        ThreadServer.getInstance().setClassFactory(this);
    }
    public abstract BasicThread genThread(int threadType, int threadId, String[] args);
    public abstract Task genTask(int taskType, int taskId, BasicThread thread, int time, String[] args);
}
