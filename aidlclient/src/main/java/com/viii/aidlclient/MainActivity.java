package com.viii.aidlclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText;
    private TextView tv_modify;
    private TextView tv_realtime;

    //由AIDL文件生成的Java类
    private MessageCenter messageCenter = null;

    //标志当前与服务端连接状况的布尔值，false为未连接，true为连接中
    private boolean mBound = false;

    //包含Book对象的list
    private List<Info> mInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        tv_realtime = (TextView) findViewById(R.id.tv_realtime);
        tv_modify = (TextView) findViewById(R.id.tv_modify);
        findViewById(R.id.button).setOnClickListener(this);

    }

    /**
     * 调用服务端的addInfo方法
     */
    public void addMessage(String content) {
        //如果与服务端的连接处于未连接状态，则尝试连接
        if (!mBound) {
            attemptToBindService();
            Toast.makeText(this, "当前与服务端处于未连接状态，正在尝试重连，请稍后再试", Toast.LENGTH_SHORT).show();
            return;
        }
        if (messageCenter == null) return;

        Info info = new Info();
        info.setContent(content);
        try {
            info =messageCenter.addInfo(info);
            tv_modify.setText(info.getContent());
            Log.e(getLocalClassName(), "客户端发送：" + info.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 跨进程绑定服务
     */
    private void attemptToBindService() {
        Intent intent = new Intent();
        intent.setAction("com.vvvv.aidl");
        intent.setPackage("com.iiiv.aidlserver");
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mBound) {
            attemptToBindService();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mServiceConnection);
            mBound = false;
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(getLocalClassName(), "完成绑定aidlserver的AIDLService服务");
            messageCenter = MessageCenter.Stub.asInterface(service);
            mBound = true;

            if (messageCenter != null) {
                try {
                    mInfoList = messageCenter.getInfo();
                    Log.e(getLocalClassName(), mInfoList.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(getLocalClassName(), "无法绑定aidlserver的AIDLService服务");
            mBound = false;
        }
    };

    @Override
    public void onClick(View view) {
        String content;
        switch (view.getId()) {
            case R.id.button:
                content = editText.getText().toString();
//                Log.i(getLocalClassName(), content);
                addMessage(content);
                break;

    }}
}
