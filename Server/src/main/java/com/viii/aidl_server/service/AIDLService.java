package com.viii.aidl_server.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import aidl_client.Message;
import aidl_client.MessageCenter;

/**
 * 服务端的AIDLService.java
 * <p/>
 * Created by lypeer on 2016/7/17.
 */
public class AIDLService extends Service {

    public final String TAG = this.getClass().getSimpleName();

    //包含Book对象的list
    private List<Message> messages = new ArrayList<>();

    //由AIDL文件生成的BookManager
    private final MessageCenter.Stub messageCenter = new MessageCenter.Stub() {
        @Override
        public List<Message> getMessage() throws RemoteException {
            synchronized (this) {
                Log.e(TAG, "getBooks invoking getBooks() method , now the list is : " + messages.toString());
                if (messages != null) {
                    return messages;
                }
                return new ArrayList<>();
            }
        }


        @Override
        public void addMessage(Message message) throws RemoteException {
            synchronized (this) {
                if (messages == null) {
                    messages = new ArrayList<>();
                }
                if (message == null) {
                    Log.e(TAG, "Book is null in In");
                    message = new Message();
                }
                //尝试修改book的参数，主要是为了观察其到客户端的反馈
                message.setContent("dididi");
                if (!messages.contains(message)) {
                    messages.add(message);
                }
                //打印mBooks列表，观察客户端传过来的值
                Log.e(TAG, "客户传来了数据" + messages.toString());

//                //打开一个程序的后台服务！
//                Intent serviceIntent = new Intent();
//                //设置一个组件名称  同组件名来启动所需要启动Service
//                serviceIntent.setComponent(new ComponentName("com.yoursender.driversingle", "com.yoursender.driversingle.service.LocationService"));
//                startService(serviceIntent);

                //打开一个程序!
//                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq");
//                startActivity(launchIntent);
            }
        }
    };

    @Override
    public void onCreate() {

        Message message = new Message();
        message.setContent("消息");
        messages.add(message);
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(getClass().getSimpleName(), String.format("on bind,intent = %s", intent.toString()));
        return messageCenter;
    }
}
