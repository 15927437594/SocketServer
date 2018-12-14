package cn.com.hwtc.socketserver;


import android.os.Handler;

/**
 * user:Created by jid on 2018/12/14 17:01:03.
 * email:18571762595@163.com.
 */
public class Manager {
    private static Manager sInstance = null;
    private Handler mMainHandler = null;

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
}
