package com.zm.frame.thread;

import com.zm.frame.thread.msg.StringMsgBody;
import com.zm.frame.thread.msg.ThreadMsg;
import com.zm.frame.thread.server.ClassFactory;
import com.zm.frame.thread.server.Server;
import org.junit.Test;

/**
 * Created by Administrator on 2016/11/3.
 */
public class ThreadTest {

    public static void main(String[] args) {
        Server server = Server.getInstance();
        server.setClassFactory(new MyClassFactory());

        new ThreadGroup(MyDef.THREAD_TYPE_SEND, 1, null);
        new ThreadGroup(MyDef.THREAD_TYPE_REC, 2, null);

        server.start();
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
    }

    @Override
    protected void afterProcessMsg() {
        System.out.println("send thread running ...");
    }

    @Override
    protected void init() {
        System.out.println("send thread ready to send");
        sendThreadMsgTo(MyDef.MSG_TYPE_REQ, new StringMsgBody("req 1"),  MyDef.THREAD_TYPE_REC);
        sendThreadMsgTo(MyDef.MSG_TYPE_REQ, new StringMsgBody("req 2"),  MyDef.THREAD_TYPE_REC);
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

class MyClassFactory extends ClassFactory {

    @Override
    public BasicThread genThread(int threadType, int threadId, String[] args) {
        BasicThread ret = null;
        switch (threadType) {
            case MyDef.THREAD_TYPE_SEND :
                ret =  new MySendThread(threadType, threadId, 5000);
                break;
            case MyDef.THREAD_TYPE_REC :
                ret =  new MyRecThread(threadType, threadId);
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
}
