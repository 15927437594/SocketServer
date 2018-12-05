package cn.com.hwtc.socketserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * user:Created by jid on 2018/12/5 15:31:23.
 * email:18571762595@163.com.
 */
public class SocketService extends Service {

    private static final String TAG = "SocketServer " + SocketService.class.getSimpleName();
    private SocketServer mSocketServer;

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mSocketServer = SocketServer.getInstance();
        startSocketServer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    private void startSocketServer() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (null != mSocketServer)
                    mSocketServer.start();
            }
        }.start();
    }

}
