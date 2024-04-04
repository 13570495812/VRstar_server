package com.jamma.vr_star;

import android.content.Intent;
import android.hardware.Camera;
import android.app.Activity;
import android.content.Context;
//import android.graphics.Camera;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class HdmiActivity extends AppCompatActivity {
    private static final String TAG = "DualCamera";
    MainActivity mainActivity=new MainActivity();
    public static HdmiActivity hdmiActivity;
    Preview[] mPreview;
    Camera[] mCamera;
    Activity act;
//    Context ctx;
    private int mCameraNum;
    private Intent aInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("mmmmm","12121421212121");

//        ctx = this;
//        act = this;
        aInt = new Intent();
hdmiActivity = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_hdmi);
        mCameraNum = Camera.getNumberOfCameras();
        SurfaceView[] surfaViews = new SurfaceView[mCameraNum];
        mPreview = new Preview[mCameraNum];
        mCamera = new Camera[mCameraNum];
        for (int i = 0; i < mCameraNum; i++) {
            Log.e("mmmmm",""+i);
            surfaViews[i] = new SurfaceView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
            lp.setMargins(10, 10, 10, 10);
            surfaViews[i].setLayoutParams(lp);
            ((LinearLayout) findViewById(R.id.layout1)).addView(surfaViews[i]);

            mPreview[i] = new Preview(this, surfaViews[i]);
            mPreview[i].setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));
            ((FrameLayout) findViewById(R.id.layout)).addView(mPreview[i]);
        }
    }

    public static Handler handler= new Handler(){
        @Override
        public  void handleMessage(Message msg){
            switch (msg.what){
                case 0://返回主页面
                    hdmiActivity.startActivity(new Intent(hdmiActivity,MainActivity.class));;
                    break;

            }
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        for (int i = 0; i < mCameraNum; i++) {
            try{
                mCamera[i] = Camera.open(i);
                mPreview[i].setCamera(mCamera[i]);
            } catch (Throwable e) {
                Log.e(TAG, "Camera[" + i + "] exception: " + e.getMessage(), e);
                Toast.makeText(this, "Camera[" + i + "] exception: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    protected void onPause() {
        for (int i = 0; i < mCameraNum; i++) {
            if (mCamera[i] != null) {
                mCamera[i].stopPreview();
                mPreview[i].setCamera(null);
                mCamera[i].release();
                mCamera[i] = null;
            }
        }
        super.onPause();
    }
public void onReceive(Context context, Intent intent) {
    // 获取Context对象
}
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void myMethod() {
        Message message = new Message(); //发送消息给线程，用来返回
        message.what =0;
        handler.sendMessage(message);
        mainActivity.isOpne=false;

    }

}
