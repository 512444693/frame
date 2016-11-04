package com.zm.frame.thread;

import com.zm.frame.thread.msg.StringMsgBody;
import com.zm.frame.thread.msg.ThreadMsg;
import com.zm.frame.thread.server.ClassFactory;
import com.zm.frame.thread.server.Server;
import com.zm.frame.thread.task.Task;
import com.zm.frame.thread.thread.BasicThread;
import com.zm.frame.thread.thread.BlockingThread;
import com.zm.frame.thread.thread.NoBlockingThread;
import com.zm.frame.thread.thread.ThreadGroup;


/**
 * Created by Administrator on 2016/11/3.
 */
public class ThreadTest {

    public static void main(String[] args) {
        new MyClassFactory();

        new ThreadGroup(MyDef.THREAD_TYPE_SEND, 1, null);
        new ThreadGroup(MyDef.THREAD_TYPE_REC, 2, null);

        Server.getInstance().startThreads();
    }
}

class MySendThread extends NoBlockingThread {

    public MySendThread(int threadType, int threadId, int delayTime) {
        super(threadType, threadId, delayTime);
    }

    @Override
    protected void processMsg(ThreadMsg msg) {
        String req = ((StringMsgBody) msg.msgBody).getMsgBody();
        System.out.println(msg.toString() + ", body :" + req);
        super.processMsg(msg);
    }

    @Override
    protected void afterProcessMsg() {
        System.out.println("send thread running ...");
        System.out.println("Task size is " + this.getTaskSize());
    }

    @Override
    protected void init() {
        addTask(MyDef.TASK_TYPE_TEST, null);
        addTask(MyDef.TASK_TYPE_TEST, null);
    }
}

class MyRecThread extends BlockingThread {

    public MyRecThread(int threadType, int threadId) {
        super(threadType, threadId);
    }

    @Override
    protected void processMsg(ThreadMsg msg) {
        String req = ((StringMsgBody) msg.msgBody).getMsgBody();
        System.out.println(msg.toString() + ", body :" + req);
        replayThreadMsg(msg, MyDef.MSG_TYPE_REP, new StringMsgBody("this is reply from " + req));
    }

    @Override
    protected void init() {

    }
}

class MyTask extends Task {
    private final static int INIT = 0;
    private final static int FIRST_REC = 1;
    private final static int SECOND_REC = 2;
    private int status = INIT;

    public MyTask(int taskId, BasicThread thread) {
        super(taskId, thread);
        sendThreadMsgTo(MyDef.MSG_TYPE_REQ, new StringMsgBody("req 1"),  MyDef.THREAD_TYPE_REC);
        sendThreadMsgTo(MyDef.MSG_TYPE_REQ, new StringMsgBody("req 2"),  MyDef.THREAD_TYPE_REC);
    }

    @Override
    public void processMsg(ThreadMsg msg) {
        if(status == INIT) {
            status = FIRST_REC;
            System.out.println("FINISH FIRST REC !!!");
        } else if (status == FIRST_REC) {
            status = SECOND_REC;
            System.out.println("FINISH SECOND REC !!!");
            this.remove();
            System.out.println("DELETE ITSELF !!!");
        }
    }
}

class MyClassFactory extends ClassFactory {

    @Override
    public BasicThread genThread(int threadType, int threadId, String[] args) {
        BasicThread ret = null;
        switch (threadType) {
            case MyDef.THREAD_TYPE_SEND :
                ret =  new MySendThread(threadType, threadId, MyDef.NO_BLOCKING_THREAD_SLEEP);
                break;
            case MyDef.THREAD_TYPE_REC :
                ret =  new MyRecThread(threadType, threadId);
                break;
        }
        return ret;
    }

    @Override
    public Task genTask(int taskType, int taskId, BasicThread thread, String[] args) {
        Task ret = null;
        switch (taskType) {
            case MyDef.TASK_TYPE_TEST :
                ret = new MyTask(taskId, thread);
                break;
        }
        return ret;
    }
}

class MyDef {

    //线程类型
    public static final int THREAD_TYPE_SEND = 3;
    public static final int THREAD_TYPE_REC = 4;

    //消息类型
    public static final int MSG_TYPE_REQ = 8;
    public static final int MSG_TYPE_REP = 9;

    //task类型
    public static final int TASK_TYPE_TEST = 1001;

    public static final int NO_BLOCKING_THREAD_SLEEP = 1000;
}
