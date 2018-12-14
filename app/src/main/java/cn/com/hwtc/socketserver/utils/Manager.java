package cn.com.hwtc.socketserver.utils;


import android.os.Handler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * user:Created by jid on 2018/12/14 17:01:03.
 * email:18571762595@163.com.
 */
public class Manager {
    private static Manager sInstance = null;
    private Handler mMainHandler = null;
    private ExecutorService mThreadPool;

    public static Manager getInstance() {
        if (null == sInstance) {
            synchronized (Manager.class) {
                if (null == sInstance) {
                    sInstance = new Manager();
                }
            }
        }
        return sInstance;
    }

    public Handler getMainHandler() {
        return mMainHandler;
    }

    public void setMainHandler(Handler handler) {
        this.mMainHandler = handler;
    }

    public ExecutorService createThreadPool() {
        int NUM_OF_CORES = Runtime.getRuntime().availableProcessors();
        int KEEP_ALIVE_TIME = 1;
        TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
        BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();
//        mThreadPool = Executors.newCachedThreadPool();
        if (mThreadPool == null) {
            mThreadPool = new ThreadPoolExecutor(NUM_OF_CORES, NUM_OF_CORES * 2, KEEP_ALIVE_TIME,
                    KEEP_ALIVE_TIME_UNIT, taskQueue);
        }
        return mThreadPool;
    }
}
