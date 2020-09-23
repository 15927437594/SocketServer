package cn.com.hwtc.socketserver.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import cn.com.hwtc.socketserver.manager.ServerManager;
import cn.com.hwtc.socketserver.utils.Constants;

/**
 * user:Created by jid on 2018/12/5 15:31:23.
 * email:18571762595@163.com.
 */
public class SocketService extends Service {
    private static final String TAG = Constants.TAG_BASE + SocketService.class.getSimpleName();

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        ServerManager.getInstance().createServerSocket();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }
}
