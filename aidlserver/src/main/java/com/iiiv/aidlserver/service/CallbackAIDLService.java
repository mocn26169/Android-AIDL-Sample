package com.iiiv.aidlserver.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.viii.aidlclient.IService;
import com.viii.aidlclient.Info;
import com.viii.aidlclient.MessageCenter;

import java.util.ArrayList;
import java.util.List;


/**
 * 服务端的AIDLService.java
 */
public class CallbackAIDLService extends Service {

    public final String TAG = this.getClass().getSimpleName();



    private boolean quit = false;
    private int count;

    private Handler timeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i(getClass().getSimpleName(), "发送消息：" + msg.what);
            Info info = new Info();
            info.setContent("来自服务器的消息:"+msg.what);
            callBack(info);
            super.handleMessage(msg);
        }
    };


    private RemoteCallbackList<MessageCenter> mCallbacks = new RemoteCallbackList<MessageCenter>();
    private IService.Stub mBinder = new IService.Stub() {

        @Override
        public void unregisterCallback(MessageCenter cb){
            if(cb != null) {
                mCallbacks.unregister(cb);
            }
        }

        @Override
        public void registerCallback(MessageCenter cb){
            if(cb != null) {
                mCallbacks.register(cb);
            }
        }
    };

    private void callBack(Info info) {
        int N = mCallbacks.beginBroadcast();
        try {
            for (int i = 0; i < N; i++) {
                mCallbacks.getBroadcastItem(i).addInfo(info);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        }
        mCallbacks.finishBroadcast();
    }

    /**
     * 初始化一个定时器
     */
    private void initTimer() {
        new Thread() {
            @Override
            public void run() {
                while (!quit) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message = new Message();
                    message.what = count++;
                    timeHandler.sendMessage(message);
                }
            }
        }.start();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(getClass().getSimpleName(), String.format("on bind,intent = %s", intent.toString()));
        initTimer();
        return mBinder;
    }

    @Override
    public void onDestroy() {
        quit = true;
        mCallbacks.kill();
        super.onDestroy();

    }


}
