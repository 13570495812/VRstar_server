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

    private Button openall;
    private Button open1;
    private Button open2;
    private Button open3;

    private Button closeall;
    private Button cloos1;
    private Button cloos2;
    private Button cloos3;

    private Button tb_open;
    private Button tb_close;

    private UdpTool udpTool;
    private MainActivity mainActivity;
    private  static final String O_="OPENALL";
    private  static final String O1_="OPEN1";
    private  static final String O2_="OPEN2";
    private  static final String O3_="OPEN3";

    private  static final String C_="CLOSEALL";
    private  static final String C1_="CLOSE1";
    private  static final String C2_="CLOSE2";
    private  static final String C3_="CLOSE3";
    private  static final String UDPIP_="192.168.188.255";

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
        /*投币*/
//        tb_open = findViewById(R.id.tb_open);
//        tb_close = findViewById(R.id.tb_close);
        /*特效按钮*/
//        openall = findViewById(R.id.openALL);
//        open1 = findViewById(R.id.open1);
//        open2 = findViewById(R.id.open2);
//        open3 = findViewById(R.id.open3);
//
//        closeall = findViewById(R.id.closeALL);
//        cloos1 = findViewById(R.id.close1);
//        cloos2 = findViewById(R.id.close2);
//        cloos3 = findViewById(R.id.close3);
        /*udp 发送数据*/
        mainActivity = new MainActivity();
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
//        openall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                mainActivity.M_this.udpTool.sendMessage(O_,UDPIP_);
//
////                mainActivity.sendMes
//            }
//        });
//        open1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                mainActivity.M_this.udpTool.sendMessage(O1_,UDPIP_);
//            }
//        });
//        open2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                mainActivity.M_this.udpTool.sendMessage(O2_,UDPIP_);
//            }
//        });
//        open3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                mainActivity.M_this.udpTool.sendMessage(O3_,UDPIP_);
//            }
//        });

//        closeall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                mainActivity.M_this.udpTool.sendMessage(C_,UDPIP_);
//            }
//        });
//        cloos1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                mainActivity.M_this.udpTool.sendMessage(C1_,UDPIP_);
//            }
//        });
//        cloos2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                mainActivity.M_this.udpTool.sendMessage(C2_,UDPIP_);
//            }
//        });
//        cloos3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                mainActivity.M_this.udpTool.sendMessage(C3_,UDPIP_);
//            }
//        });
        // 打开投币
//        tb_open.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mainActivity.M_this.tb_open();
//            }
//        });
//        tb_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mainActivity.M_this.tb_close();
//            }
//        });

    }
    /*打开*/
//    private void UDPstartC1(String str,String ip) {
//        if (ip!=null){
//            udpTool.sendMessage(str,ip);
//        }else {
////            Toast.makeText(MainActivity.this, "请输入对方ip和端口", Toast.LENGTH_SHORT).show();
//        }
//    }
//    private void UDPstartC2(String  str,String ip) {
//        if (ip!=null){
//            udpTool.sendMessage(str,ip);
//        }else {
////            Toast.makeText(MainActivity.this, "请输入对方ip和端口", Toast.LENGTH_SHORT).show();
//        }
//    }    private void UDPstartC3(String  str,String ip) {
//        if (ip!=null){
//            udpTool.sendMessage(str,ip);
//        }else {
////            Toast.makeText(MainActivity.this, "请输入对方ip和端口", Toast.LENGTH_SHORT).show();
//        }
//    }
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
    protected void onStart() {
        super.onStart();

//        Log.e("ip","我是onStart");
//        initializePlayer();
    }
    @Override
    protected void onResume() {
        super.onResume();
//        Log.e("ip","我是onResume");
    }
    @Override
    protected void onStop() {
        super.onStop();
//        releasePlayer();
//        Log.e("11","我在退出");
//        Log.e("ip","我是onStop");
//        udpTool.closeSocket();
    }



//    @Override
//    protected void OnRestart(){
//        super.onResume();
//        Log.e("11","我在退出");
//    }
}
