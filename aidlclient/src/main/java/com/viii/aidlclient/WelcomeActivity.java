package com.viii.aidlclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class WelcomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

    }

    public void toMainActivity(View view) {
        Log.i(getClass().getSimpleName(), "toMainActivity");
        toActivity(this, MainActivity.class);
    }

    public void toLocalService(View view) {
        Log.i(getClass().getSimpleName(), "toLocalService");
        toActivity(this, LocalAIDLActivity.class);
    }

    public void toCallbackService(View view) {
        Log.i(getClass().getSimpleName(), "toCallbackService");
        toActivity(this, CallBackActivity.class);
    }
    public void toMessengerService(View view) {
        Log.i(getClass().getSimpleName(), "toCallbackService");
        toActivity(this, MessengerActivity.class);
    }

    private void toActivity(Context _context, Class<? extends Activity> _class) {
        Intent intent = new Intent(_context, _class);
        startActivity(intent);
    }

}
