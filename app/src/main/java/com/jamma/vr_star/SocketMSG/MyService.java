package com.jamma.vr_star.SocketMSG;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
//import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;

import com.jamma.vr_star.MainActivity;
import com.jamma.vr_star.R;

public class MyService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        //创建通知实例
        Notification notification=new Notification();
        notification.contentView=new RemoteViews(getPackageName(), R.layout.activity_main);
        notification.contentIntent= PendingIntent.getActivity(this,0,new Intent(this,MainActivity.class),0);
        startForeground(1,notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
