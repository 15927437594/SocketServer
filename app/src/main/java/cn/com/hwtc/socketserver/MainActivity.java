package cn.com.hwtc.socketserver;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
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
        Manager.getInstance().setMainHandler(mMainHandler);
        mSocketServer = SocketServer.getInstance();
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
        switch (v.getId()) {
            case R.id.send_message:
                mSocketServer.sendMsgToClient(mEdit.getText().toString());
                break;
            default:
                break;
        }
    }

    private Handler mMainHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MSG_UPDATE_RECEIVE_MESSAGE:
                    receiveMessage.setText(msg.getData().getString(Constants.RECEIVE_MSG, ""));
                    break;
                default:
                    break;
            }
            return true;
        }
    });
}
