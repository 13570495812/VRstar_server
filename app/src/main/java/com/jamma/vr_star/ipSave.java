package com.jamma.vr_star;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class ipSave extends AppCompatActivity {
    private static final String TAG = "DualCamera";
    Preview[] mPreview;
    Camera[] mCamera;
    Activity act;
    //    Context ctx;
    private int mCameraNum;
    private Intent aInt;
    private TextView wifiname;
    private Button tv_oFile;
    private Button tvSettings;

    public static String SERVER_IP = "";

    private EditText ll_eTxtIP;
    private Read_coin_file coinipSave;
    private int downTimer;
    private Button ll_save;
    private Switch switchRad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ip);
        /*初始化自动*/
        Init();

    }

    private void Init() {
        ll_eTxtIP = findViewById(R.id.ll_set_eTxtIP);
        wifiname = (TextView) findViewById(R.id.tv_WIFI_name);
        tv_oFile = (Button) findViewById(R.id.tv_open_File);
        tvSettings = (Button) findViewById(R.id.tv_Settings);
        ll_save = findViewById(R.id.ll_set_save);
        switchRad = (Switch) findViewById(R.id.switch1);
        /*获取网线 ip地址*/
        SERVER_IP=getLocalIp();
        wifiname.setText(SERVER_IP);
        /*投币信息*/
        coinipSave = new Read_coin_file();
        /*读取时间*/
//        String a = coinipSave.Getminutes();
//         Log.e(TAG,"0000000m"+a);

//        Log.e(TAG,"1111100000001111111m"+coinipSave.minutes());


        if(coinipSave.minutes().equals("")){

            ll_eTxtIP.setText("10");
            coinipSave.changeTime("10");
//            coinipSave.Save_time();
        }
        if(!coinipSave.minutes().equals("")){
//            Log.e(TAG,"setText555555"+coinipSave.minutes());
            ll_eTxtIP.setText(coinipSave.minutes());
        }
        /*是否免费版 1收费 0是免费*/
//        Log.e(TAG,"77777777777"+coinipSave.ferr());
        if(coinipSave.ferr().equals("")){
            switchRad.setChecked(false);
            coinipSave.changeFerr("0");

        }
        if(coinipSave.ferr().equals("0")){
            switchRad.setChecked(false);
        }
        if(coinipSave.ferr().equals("1")){
//            Log.e(TAG,"k7777770"+coinipSave.ferr());
            switchRad.setChecked(true);
        }

//        Log.e(TAG,"kljk"+downTimer);
//        getDownTimer();
        /*初始化方法*/
        InitFunction();

    }
//    public int getDownTimer(){
//        return coinipSave.changeTimeMillisecond();
//    }
    private void InitFunction() {

        tvSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        });
        /*打开文件夹*/
        tv_oFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,1);
            }
        });
        /*保存投币时间*/
        ll_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                coinipSave.changeTime("10");
                coinipSave.changeTime(ll_eTxtIP.getText().toString());

//                finish();
//                int d = coinipSave.changeTimeMillisecond();
//                Log.e(TAG,"我是毫秒"+d);
            }
        });
        switchRad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    coinipSave.changeFerr("1");
                    Toast.makeText(getBaseContext(), "TRUE", Toast.LENGTH_SHORT).show();
                } else {
                    coinipSave.changeFerr("0");
                    Toast.makeText(getBaseContext(), "FALSE", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    /**
     * 得到有限网关的IP地址
     *
     * @return
     */
    private String getLocalIp() {
        try {
            // 获取本地设备的所有网络接口
            Enumeration<NetworkInterface> enumerationNi = NetworkInterface
                    .getNetworkInterfaces();
            while (enumerationNi.hasMoreElements()) {
                NetworkInterface networkInterface = enumerationNi.nextElement();
                String interfaceName = networkInterface.getDisplayName();
//                Log.i("tag", "网络名字m" + interfaceName);
                // 如果是有限网卡
                if (interfaceName.equals("eth0")) {
                    Enumeration<InetAddress> enumIpAddr = networkInterface
                            .getInetAddresses();
                    while (enumIpAddr.hasMoreElements()) {
                        // 返回枚举集合中的下一个IP地址信息
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        // 不是回环地址，并且是ipv4的地址
                        if (!inetAddress.isLoopbackAddress()
                                && inetAddress instanceof Inet4Address) {
//                            Log.i("tag并且是ipv4的地址", inetAddress.getHostAddress() + "   ");

                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }

        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";

    }
    @Override
    protected void onResume() {
        super.onResume();
    }
}
