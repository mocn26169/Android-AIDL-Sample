package com.iiiv.aidlserver.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;


public class MessengerService extends Service {

    /**
     * 标识
     */
    private static final int MSG_FLAG = 0x110;

    private Messenger messenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //获取客户端传过来的对象
            Message clientMsg = Message.obtain(msg);
            switch (clientMsg.what) {
                case MSG_FLAG:
                    clientMsg.what = MSG_FLAG;
                    try {
                        Thread.sleep(2000);
                        //做处理
                        String string = clientMsg.arg1 + "+" + clientMsg.arg2 + "===>>>" + (clientMsg.arg1 + clientMsg.arg2);
                        Bundle bundle = new Bundle();
                        bundle.putString("msg", string);
                        clientMsg.setData(bundle);
                        //发送给客户端
                        clientMsg.replyTo.send(clientMsg);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    });


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

}
