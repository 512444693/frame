package com.zm.frame.thread.server;

import com.zm.frame.thread.BasicThread;

/**
 * Created by Administrator on 2016/11/2.
 */
public abstract class ClassFactory {
    public abstract BasicThread genThread(int threadType, int threadId, String[] args);
}
