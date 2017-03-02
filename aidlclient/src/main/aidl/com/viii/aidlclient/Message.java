package com.viii.aidlclient;

import android.os.Parcel;
import android.os.Parcelable;

public class Message implements Parcelable {

    public Message() {
    }

    String content;

    protected Message(Parcel in) {
        content = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
    }

    /**
     * 参数是一个Parcel,用它来存储与传输数据
     *
     * @param dest
     */
    public void readFromParcel(Parcel dest) {
        //注意，此处的读值顺序应当是和writeToParcel()方法中一致的
        content = dest.readString();
    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                '}';
    }
}
