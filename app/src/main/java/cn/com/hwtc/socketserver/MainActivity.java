package cn.com.hwtc.socketserver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SocketServer.OnUpdateReceiveMsgCallback, View.OnClickListener {

    private static final String TAG = "SocketServer " + MainActivity.class.getSimpleName();
    private TextView receiveMessage;
    private EditText mEdit;
    private Button sendMessage;
    private SocketServer mSocketServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    private void initEvent() {
        mSocketServer = SocketServer.getInstance();
        mSocketServer.setOnUpdateReceiveMsgCallback(this);
        sendMessage.setOnClickListener(this);
    }

    private void initView() {
        receiveMessage = findViewById(R.id.receive_message);
        mEdit = findViewById(R.id.edit);
        sendMessage = findViewById(R.id.send_message);
    }

    @Override
    public void onUpdateReceiveMsg(final String receiveMsg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                receiveMessage.setText(receiveMsg);
            }
        });
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
        switch (v.getId()) {
            case R.id.send_message:
                mSocketServer.sendMsgToClient(mEdit.getText().toString());
                break;
            default:
                break;
        }
    }
}
