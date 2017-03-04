package com.viii.aidlclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import java.util.List;

public class CallBackActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText;
    private TextView tv_modify;

    //标志当前与服务端连接状况的布尔值，false为未连接，true为连接中
    private boolean mBound = false;

    private IService mService;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String str = (String) msg.obj;
            tv_modify.setText(str);
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_back);
        editText = (EditText) findViewById(R.id.editText);
        tv_modify = (TextView) findViewById(R.id.tv_modify);
        findViewById(R.id.button).setOnClickListener(this);
    }

    /**
     * 跨进程绑定服务
     */
    private void attemptToBindService() {
        Intent intent = new Intent();
        intent.setAction("com.vvvv.callbackaidl");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            try {
                mService.unregisterCallback(mCallback);
            } catch (RemoteException e) {
                Log.e(getClass().getSimpleName(), "", e);
            }
        }

    }

    /**
     * service的回调方法
     */
    private MessageCenter.Stub mCallback = new MessageCenter.Stub() {

        @Override
        public List<Info> getInfo() throws RemoteException {
            return null;
        }

        @Override
        public Info addInfo(Info info) throws RemoteException {
            Message message = new Message();
            message.obj = info.getContent();
            handler.sendMessage(message);
            return null;
        }
    };

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(getLocalClassName(), "完成绑定aidlserver的AIDLService服务");
            mService = IService.Stub.asInterface(service);
            try {
                mService.registerCallback(mCallback);
            } catch (RemoteException e) {
                Log.e(getClass().getSimpleName(), "", e);
            }

            mBound = true;

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
                break;
        }
    }
}