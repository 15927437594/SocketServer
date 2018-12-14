package cn.com.hwtc.socketserver;

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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.com.hwtc.socketserver.utils.Constants;
import cn.com.hwtc.socketserver.utils.Manager;

/**
 * user:Created by jid on 2018/12/4 17:42:51.
 * email:18571762595@163.com.
 */
public class SocketServer {
    private static final String TAG = Constants.TAG_BASE + SocketServer.class.getSimpleName();
    private static final int PORT = 8989;
    private List<Socket> mList = new ArrayList<>();
    private ServerSocket server;
    private Socket mClient;
    private ExecutorService mThreadPool;
    private OutputStream mOutputStream;
    private static SocketServer sInstance = null;
    private Handler mMainHandler = null;

    public static SocketServer getInstance() {
        if (null == sInstance) {
            synchronized (SocketServer.class) {
                if (null == sInstance) {
                    sInstance = new SocketServer();
                }
            }
        }
        return sInstance;
    }

    public SocketServer() {
        if (mThreadPool == null) {
            mThreadPool = Executors.newCachedThreadPool();
        }
        mMainHandler = Manager.getInstance().getMainHandler();
    }

//    public void start() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    if (null == server) {
//                        server = new ServerSocket(PORT);
//                    }
//                    ExecutorService mExecutorService = Executors.newCachedThreadPool();
//                    while (true) {
//                        Log.d(TAG, "SocketServer is start");
//                        mClient = server.accept();
//                        mList.add(mClient);
//                        mExecutorService.execute(new Service(mClient));
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }

    public void start() {
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (null == server) {
                        server = new ServerSocket(PORT);
                    }
                    while (true) {
                        Log.d(TAG, "SocketServer is start");
                        mClient = server.accept();
                        mList.add(mClient);
                        mThreadPool.execute(new Service(mClient));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void sendMsgToClient(final String sendMessage) {
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                if (null == mClient) return;
                if (TextUtils.isEmpty(sendMessage)) return;
                try {
                    mOutputStream = mClient.getOutputStream();
                    mOutputStream.write((sendMessage + "\n").getBytes("utf-8"));
                    //发送数据到服务端
                    mOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void close() {
        if (null != server) {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class Service implements Runnable {
        private Socket socket;
        private BufferedReader in = null;

        private Service(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            Log.d(TAG, "service run");
            try {
                while (true) {
                    String receiveMsg;
                    if ((receiveMsg = in.readLine()) != null) {
                        Log.d(TAG, "receiveMsg:" + receiveMsg);
                        updateReceiveMsg(receiveMsg);
                        if (receiveMsg.equals("0")) {
                            mList.remove(socket);
                            in.close();
                            socket.close();
                            break;
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
