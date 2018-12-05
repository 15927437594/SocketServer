package cn.com.hwtc.socketserver;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

/**
 * user:Created by jid on 2018/12/5 15:32:02.
 * email:18571762595@163.com.
 */
public class SocketApplication extends Application {

    private static final String TAG = "SocketServer " + SocketApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        startService(new Intent(this, SocketService.class));
    }
}
