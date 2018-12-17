package cn.com.hwtc.socketserver.manager;


import android.os.Handler;

/**
 * user:Created by jid on 2018/12/14 17:01:03.
 * email:18571762595@163.com.
 */
public class HandlerManager {
    private static HandlerManager sInstance = null;
    private Handler mMainHandler = null;

    public static HandlerManager getInstance() {
        if (null == sInstance) {
            synchronized (HandlerManager.class) {
                if (null == sInstance) {
                    sInstance = new HandlerManager();
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
