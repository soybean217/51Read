package com.wanpg.bookread.logic;

import android.os.Handler;
import android.os.Message;

public abstract class BackTask {

    private Handler handler;

    private final int TASK_BEFORE_THREAD_NOTIFY = 0;
    private final int TASK_AFTER_THREAD_NOTIFY = 1;

    public BackTask() {
        // TODO Auto-generated constructor stub

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                switch (msg.what) {
                    case TASK_BEFORE_THREAD_NOTIFY:
                        doBeforeThread();
                        break;
                    case TASK_AFTER_THREAD_NOTIFY:
                        String result = null;
                        if (msg.obj != null) {
                            result = msg.obj.toString();
                        }
                        doAfterThread(result);
                        break;
                    default:
                        break;
                }
            }
        };

    }

    public void submit() {
        // TODO Auto-generated method stub
        BackThread backThread = new BackThread();

        //doBeforeThread();

        backThread.start();

    }

    private class BackThread extends Thread {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            handler.sendEmptyMessage(TASK_BEFORE_THREAD_NOTIFY);
            String result = doInThread();
            Message msg = handler.obtainMessage(TASK_AFTER_THREAD_NOTIFY, result);
            handler.sendMessage(msg);
        }

    }

    public abstract void doBeforeThread();

    public abstract void doAfterThread(String result);

    public abstract void doInThreadWhenCalling(String info);

    public abstract String doInThread();

}
