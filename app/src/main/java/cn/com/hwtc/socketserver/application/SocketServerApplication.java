package cn.com.hwtc.socketserver.application;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import cn.com.hwtc.socketserver.service.SocketService;
import cn.com.hwtc.socketserver.utils.Constants;

/**
 * user:Created by jid on 2018/12/5 15:32:02.
 * email:18571762595@163.com.
 */
public class SocketServerApplication extends Application {
    private static final String TAG = Constants.TAG_BASE + SocketServerApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        startService(new Intent(this, SocketService.class));
    }
}
