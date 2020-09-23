package cn.com.hwtc.socketserver.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import cn.com.hwtc.socketserver.R;
import cn.com.hwtc.socketserver.manager.HandlerManager;
import cn.com.hwtc.socketserver.manager.ServerManager;
import cn.com.hwtc.socketserver.utils.Constants;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG = Constants.TAG_BASE + MainActivity.class.getSimpleName();
    private TextView receiveMessage;
    private EditText mEdit;
    private Button sendMessage;
    private ServerManager mServerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    private void initEvent() {
        HandlerManager.getInstance().setMainHandler(mMainHandler);
        mServerManager = ServerManager.getInstance();
        sendMessage.setOnClickListener(this);
    }

    private void initView() {
        receiveMessage = findViewById(R.id.receive_message);
        mEdit = findViewById(R.id.edit);
        sendMessage = findViewById(R.id.send_message);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send_message) {
            mServerManager.sendMsg(mEdit.getText().toString());
        }
    }

    private Handler mMainHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == Constants.MSG_UPDATE_RECEIVE_MESSAGE) {
                receiveMessage.setText(msg.getData().getString(Constants.RECEIVE_MSG, ""));
            }
            return true;
        }
    });
}
