package cn.com.hwtc.socketserver.manager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import cn.com.hwtc.socketserver.utils.Constants;

/**
 * user:Created by jid on 2018/12/4 17:42:51.
 * email:18571762595@163.com.
 */
public class ServerManager {
    private static final String TAG = Constants.TAG_BASE + ServerManager.class.getSimpleName();
    private static final int PORT = 8989;
    private ServerSocket mServerSocket;
    private Socket mClient;
    private ExecutorService mThreadPool;
    private OutputStream mOutputStream;
    private static ServerManager sInstance = null;
    private Handler mMainHandler;

    public static ServerManager getInstance() {
        if (null == sInstance) {
            synchronized (ServerManager.class) {
                if (null == sInstance) {
                    sInstance = new ServerManager();
                }
            }
        }
        return sInstance;
    }

    private ServerManager() {
        mThreadPool = Executors.newFixedThreadPool(10);
        mMainHandler = HandlerManager.getInstance().getMainHandler();
        mThreadPool.shutdownNow();
    }

    public void createServerSocket() {
        mThreadPool.execute(() -> {
            try {
                if (mServerSocket == null) {
                    mServerSocket = new ServerSocket(PORT);
                }
                while (!mIsServerSocketInterrupted.get()) {
                    mClient = mServerSocket.accept();
                    Log.d(TAG, "accept and add client");
                    mThreadPool.execute(new ClientService(mClient));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void sendMsg(final String sendMsg) {
        mThreadPool.execute(() -> {
            if (TextUtils.isEmpty(sendMsg) || mClient == null) {
                return;
            }
            try {
                //发送数据到客户端
                mOutputStream = mClient.getOutputStream();
                mOutputStream.write((sendMsg + "\n").getBytes(StandardCharsets.UTF_8));
                mOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void close() {
        try {
            if (mServerSocket != null) {
                mServerSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private AtomicBoolean mIsServerSocketInterrupted = new AtomicBoolean(false);
    private AtomicBoolean mIsClientInterrupted = new AtomicBoolean(false);

    private class ClientService implements Runnable {
        private Socket socket;
        private BufferedReader bufferedReader = null;

        private ClientService(Socket socket) {
            this.socket = socket;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            Log.d(TAG, "service run");
            try {
                while (!mIsClientInterrupted.get()) {
                    String receiveMsg;
                    if ((receiveMsg = bufferedReader.readLine()) != null) {
                        Log.d(TAG, "receiveMsg:" + receiveMsg);
                        updateReceiveMsg(receiveMsg);
                        if (receiveMsg.equals("0")) {
                            bufferedReader.close();
                            socket.close();
                            mIsClientInterrupted.set(true);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateReceiveMsg(String receiveMsg) {
        Message msg = mMainHandler.obtainMessage();
        msg.what = Constants.MSG_UPDATE_RECEIVE_MESSAGE;
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RECEIVE_MSG, receiveMsg);
        msg.setData(bundle);
        mMainHandler.sendMessage(msg);
    }
}
