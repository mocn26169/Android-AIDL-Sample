package com.viii.aidlclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MessengerActivity extends AppCompatActivity {

    private TextView textView;
    private String TAG;
    private static final int MSG_FLAG = 0x110;
    private Messenger serviceMessenger;
    private Boolean isConnSucessful;
    private int count;

    private Messenger clientMessanger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FLAG:
                    //获取Service传来的消息并显示
                    String text = textView.getText().toString() + "\n" + msg.getData().getString("msg");
                    textView.setText(text);
                    break;
            }
            super.handleMessage(msg);
        }
    });

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            serviceMessenger = new Messenger(service);
            Toast.makeText(MessengerActivity.this, "连接成功！", Toast.LENGTH_LONG).show();
            isConnSucessful = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(MessengerActivity.this, "连接失败！", Toast.LENGTH_LONG).show();
            isConnSucessful = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        TAG = getClass().getSimpleName();
        textView = (TextView) findViewById(R.id.textView);
        bindServiceInvoke();
    }

    /**
     * 发送消息
     */
    public void sendMessage(View view) {
        int a = count++;
        int b = (int) (Math.random() * 1000);
        Message client = Message.obtain(null, MSG_FLAG, a, b);
        //设置回调
        client.replyTo = clientMessanger;
        if (isConnSucessful) {
            try {
                //发送消息
                serviceMessenger.send(client);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 绑定服务
     */
    private void bindServiceInvoke() {
        Intent intent = new Intent();
        intent.setAction("com.vvvv.message");
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        //解绑服务
        unbindService(conn);
        super.onDestroy();
    }
}
