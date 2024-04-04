package com.jamma.vr_star;

import static com.jamma.vr_star.AppContext.instance;
import static com.jamma.vr_star.HdmiActivity.hdmiActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;

import com.jamma.vr_star.SocketMSG.SocketClient;
//import com.jamma.vr_star.hdmi.FloatBar;
//import com.jamma.vr_star.hdmi.HdmiPlayerView;
import com.jamma.vr_star.otg.UsbHost;

import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


//import com.google.android.exoplayer2.DefaultLoadControl;
//import com.google.android.exoplayer2.DefaultRenderersFactory;
//import com.google.android.exoplayer2.MediaItem;
//import com.google.android.exoplayer2.SimpleExoPlayer;
//import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
//import com.google.android.exoplayer2.source.MediaSource;
//import com.google.android.exoplayer2.source.ProgressiveMediaSource;
//import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
//import com.google.android.exoplayer2.ui.PlayerView;
//import com.google.android.exoplayer2.upstream.DataSource;
//import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
//import com.google.android.exoplayer2.util.Util;
import com.google.android.flexbox.FlexboxLayout;
import com.jamma.vr_star.util.ACache;
import com.jamma.vr_star.util.MachineSendType;
import com.jamma.vr_star.util.SystemHelper;
import com.jamma.vr_star.util.TgSystem;
import com.jamma.vr_star.HdmiActivity;
import com.jamma.vr_star.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Formatter;
import java.util.List;
import java.util.concurrent.TimeUnit;
//import io.vov.vitamio.LibsChecker;

//import io.vov.vitamio.LibsChecker;

public class MainActivity extends AppCompatActivity implements ServiceConnection {
//    private TextView service;
//    private TextView client;
//    private TextView usbBotton;

//    private PlayerView playerView;
//    private SimpleExoPlayer player;

    private ViewGroup mRootLayout;
    //    private HdmiPlayerView mHdmiPlayerView;
    private TextView mTextView;
    private ImageButton mBtnSetting;

//    private Handler mHandler;

    private int mPlayerErrorCount = -1;
    private boolean mProcessExit = false;



    public static final String TAG = "MainActivity";
    /*游戏报名*/
    public static  String PackName="";
    public static final String DEFAULT_SOCKETMODE = "OTG";
    /***  必须*/
    public static String SocketModeAPI = DEFAULT_SOCKETMODE;
    private Intent socketIntent;
    private ServiceConnection serviceConnection;
    //是否关机
    public static String IP = "192.168.15.86";
    //语音端口
    public static int Port = 9000;

    public static int GetCacheTime = 0;

    /**
     * 游戏倒计时时间
     */
    public static boolean isTime = false;
    public static int CountDownGameTimes = 0;

    public static String DownGameTimes = "00:00:00";
    private static DevicePolicyManager policyManager;
    private PowerManager.WakeLock wakeLock;
    private static PowerManager pm;
    public static int radioGroupsCheck = 0;
    public static boolean radioGroupsCheckClick = false;
    //是否游戏中
    public static boolean isGameing = false;

    public static boolean isStartTime = false;
    public static boolean isConnect = false;
    //投币是否随机播放
    public static boolean isRandom = false;
    /*刷新时间*/
    private int TIME = 1000;
    private static final int PERMISSION_REQUEST_MANAGE_EXTERNAL_STORAGE = 1;
    MyCountDownTimer myCountDownTimer;
    private VideoView videoView;
    private static final int MY_PERMISSIONS_REQUEST_READ_WRITE_EXTERNAL_STORAGE = 1;
    private RadioGroup radioGroups;
    private TextView re;
    private String path;
    //private HashMap<String, String> options;
    private VideoView mVideoView;
    private MediaPlayer mediaPlayer;
    MediaController mMediaController;
    //Activity
    public static MainActivity M_this;
    public static MainActivity mainActivity = null;
    private VideoView videoV;
    private Button startB;
    private Button stoneB;
    private ImageView detailChart;

    private LinearLayout p_setting;
    private TextView dialog_opne;
    private AlertDialog dialog;
    private int POWER_OFF_MSG_D = 11115;
    // 是否点击open
    public boolean isOpne = false;
    //是否关机
    public boolean isShutdown = false;
    //是否接收消息
    boolean isReceive = true;
    private boolean isStopSocket = false;
    private boolean isDialog=false;
    //剩余硬币
    private Socket mSocket;
    public static int Coin = 0;
    //单次硬币
    public static int CoinSum = 1;
    // 客户端是否在线
    public static boolean isServer = false;
    /*socket 数据*/
    private ServerSocket mServerSocket = null;
    /*投币器是否开启*/ boolean isCoin_box = false;
    /*投币器是否开启*/
    public static boolean isCoin_boxup = false;
    //传感器监听
//    SensorEventListener sensorEventListener;
    //OTG通讯
    private UsbHost mUsbOtg;
    // socket是否在线
    public static boolean isScoket = true;
    //游戏名
    public static String GameName = "";
    /*socket数据*/
    private String receivedData;
    //连接服务器
    public static SocketClient socketConnect = new SocketClient();

//    private MainActivity.SocketAcceptThread socketAcceptThread;
    private TextView xml_time;
    private ACache mCache; // 缓存
    private TextView l_top_onlines;
    private static HdmiActivity hdmiIn;
    private ServiceActivity server_socket;
    private ipSave coin;
    private Read_coin_file coinipSave;
    private LinearLayout lferr_mode;
    private LinearLayout ltime_liner;

//    private TextView testImer;
//    private Button but_sss;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        M_this = this;
        /*投币信息*/
        coinipSave = new Read_coin_file();
        initView();  //初始化

//        setListener();
        /**
         *-----------------------ubs界面融合代码开始--------------------
         */
    }

    /**
     * 倒计时
     */
    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            int totalSeconds = (int) (millisUntilFinished / 1000);
            //            int totalSeconds = (int) (millisUntilFinished/1000);
//            Log.d("tog","=================="+totalSeconds);
            long milliseconds = TimeUnit.SECONDS.toMillis(totalSeconds); // 30秒转换为毫秒
//            int intValue = (int) milliseconds;
//            Log.e(TAG,"我是分转换秒"+intValue);

            mCache.put("TimeCache", Long.toString(milliseconds));
            long seconds = totalSeconds % 60;
            long minutes = (totalSeconds / 60) % 60;
            long hours = totalSeconds / 3600;
            String a = new Formatter().format("%02d:%02d:%02d", hours, minutes, seconds).toString();
//            Log.d(TAG, "---------------时间倒计时---------" + a);
            DownGameTimes = new Formatter().format("%02d:%02d:%02d", hours, minutes, seconds).toString();
            xml_time.setText(DownGameTimes);


        }

        @Override
        public void onFinish() {
            isStartTime = false;
//            getJsonList();
            xml_time.setText("00:00:00");
            myCountDownTimer.cancel();
            CountDownGameTimes=0;
//            M_this.mCache.put("TimeCache", CountDownGameTimes);

//            TgSystem.setTopApp(MainActivity.thi
            if(isDialog){
                dialog.hide();
            }
            if(isOpne){
                hdmiIn.myMethod();
            }

            //startActivity(new Intent(hdmiActivity,MainActivity.class));
//            Intent intent = new Intent(HdmiActivity.this,MainActivity.class);


//            Intent launchIntentForPackage=getPackageManager().getLaunchIntentForPackage("com.jamma.vrstar"); // 启动第三方APP
//            startActivity(launchIntentForPackage);
//            finish();
        }
    }

    private void requestManageExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                Intent getpermission = new Intent();
                getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(getpermission);
            }
        }
    }

    /*获取本地头部列表JSON文件件*/
    public JSONArray getFileNavigation() {
        JSONArray jsonArray = null;
        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this, permission, 1);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_WRITE_EXTERNAL_STORAGE);
        }
//        String path4=this.getExternalFilesDir("").getAbsolutePath()+"/jamma";
//        File file4=new File(path4);
//        Log.e(TAG,"========file4file4file4file4========"+file4);
        String folderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/jamma/";
        File file = new File(folderPath, "navigation.json");

        try {
            FileInputStream inputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            inputStream.close();
            String jsonString = stringBuilder.toString();
            jsonArray = new JSONArray(jsonString);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    /*获取本地JSON文件件*/
    public JSONArray getFilePermission() {
        JSONArray jsonArrayList = null;
        JSONArray jsonArray = new JSONArray();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_WRITE_EXTERNAL_STORAGE);
        }
        String folderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/jamma/";
        File file = new File(folderPath, "newdata.json");
        try {
            FileInputStream inputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            inputStream.close();
            String jsonString = stringBuilder.toString();
            jsonArrayList = new JSONArray(jsonString);
            for (int i = 0; i < jsonArrayList.length(); i++) {
                JSONObject item = jsonArrayList.getJSONObject(i);
                int type = item.getInt("type");
                if (radioGroupsCheck == 0) {
                    jsonArray.put(item);
                } else if (type == radioGroupsCheck) {
                    jsonArray.put(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    /*JSON文件读取游戏列表数据*/
    public void getNavList() {
        RadioGroup radioGroup = findViewById(R.id.ll_home_radioGroup);
        radioGroup.removeAllViews(); // 清空 RadioGroup 中的所有子 View
        JSONArray jsonArray = getFileNavigation();
        if (jsonArray != null) {
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    String Ename = item.getString("Ename");
                    String CHname = item.getString("CHname");
                    int PackageName = item.getInt("PackageName");
//                            int age = item.getInt("age");
//                    Log.d(TAG, "Ename: ");
//                    Log.d(TAG, "CHname: " + Ename + ", PackageName: " +PackageName);
// 创建 RadioButton 对象
                    RadioButton radioButton = new RadioButton(this);
//                   radioButton.setButtonDrawable(null);
                    radioButton.setButtonDrawable(android.R.color.transparent);
//                    radioButton.setBackgroundResource(R.drawable.selector_main_rb_text);
                    radioButton.setTextColor(getResources().getColorStateList(R.drawable.button_txt_color));
//
                    radioButton.setText(Ename);
//                    }
                    RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(10, 0, 10, 13);
                    radioButton.setLayoutParams(params);
                    //设置不要单选按钮前面的小圆圈
                    Bitmap a = null;
                    radioButton.setButtonDrawable(new BitmapDrawable(a));

                    radioButton.setPadding(13, 0, 13, 0);
                    radioButton.setTextSize(20);
//                    radioButton.setButtonDrawable(R.drawable.selector_main_rb_text);
                    radioButton.setId(PackageName);
                    radioGroups.addView(radioButton);
                    /* 名字*/
//                    View itemLis = flexboxLayout.getChildAt(i);
//                    TextView textView = itemLis.findViewById(R.id.ll_home_name);
//                    textView.setText(name);
                    /*图片*/

                    /*点击事件*/
//                    TextView openStart = itemLis.findViewById(R.id.openStart);
//                    openStart.setTag(PackageName); // 设置 tag 属性为 item 的 index
                    radioGroups.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                            if (radioGroupsCheckClick) {
                                radioGroupsCheck = checkedId;
                                getJsonList();
                            }
                            // 处理 RadioButton 的选中状态
//                            switch (checkedId) {
//                                case 0:
//                                    radioGroupsCheck=checkedId;
//                                    if(radioGroupsCheckClick){
//                                        getJsonList();
//                                    }
//                                    // 选中了 Option 1
//                                    break;
//                                case 1:
//                                    radioGroupsCheck=checkedId;
//                                    if(radioGroupsCheckClick){
//                                        getJsonList();
//                                    }
//
//                                    Log.e(TAG,"setOnCheckedChangeListenersetOnCheckedChangeListener5555555555555555555");
//                                    // 选中了 Option 1
//                                    break;
//                                case 2:
//                                    radioGroupsCheck=checkedId;
//                                    if(radioGroupsCheckClick){
//                                        getJsonList();
//                                    }
//
//                                    // 选中了 Option 2
//                                    break;
//                                case 3:
//                                    radioGroupsCheck=checkedId;
//                                    if(radioGroupsCheckClick){
//                                        getJsonList();
//                                    }
//                                    Log.e(TAG,"setOnCheckedChangeListenersetOnCheckedChangeListener"+checkedId);
////                                    radioGroupsCheck=3;
//                                    // 选中了 Option 3
//                                    break;
//                            }
                        }
                    });
                }
                radioGroups.check(radioGroupsCheck);
                radioGroupsCheckClick = true;
                getJsonList();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }

    }

    /*JSON文件读取游戏列表数据*/
    public void getJsonList() {
        FlexboxLayout flexboxLayout = (FlexboxLayout) findViewById(R.id.flexbox_layout); // 大布局
        flexboxLayout.removeAllViews(); // 清空flexbox
        JSONArray jsonArray = getFilePermission();
        if (jsonArray != null) {
            try {
                int a = jsonArray.length();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    String CHname = item.getString("CHname");
                    String name = item.getString("name");
                    int type = item.getInt("type");
                    String img = item.getString("image");
//                    Log.e(TAG,"12121212puth"+img);
                    String PackageName = item.getString("PackageName");
                    View newItemView = LayoutInflater.from(this).inflate(R.layout.item_layout, null);
                    /** 获取item.xml */
                    FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
//                        if((3 * a) - 1!=a){
//                            lp.setFlexGrow(1.0f); // 设置item的flex grow属性，控制item在空间不足时的扩展比例
//                        }
                    lp.setMargins(13, 0, 13, 0); // 设置item的margin

                    flexboxLayout.addView(newItemView, lp);

                    flexboxLayout.requestLayout();
                    /* 名字*/
                    View itemLis = flexboxLayout.getChildAt(i);
                    TextView textView = itemLis.findViewById(R.id.ll_home_name);
//                    if(langa.equals("1")){
//                        textView.setText(CHname);
//                    }else {
                    textView.setText(name);
//                    }
                    /*图片*/
                    ImageView imageView = itemLis.findViewById(R.id.ll_home_imageView);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeFile(img, options);
                    imageView.setImageBitmap(bitmap);
                    /*点击事件*/
                    TextView openStart = itemLis.findViewById(R.id.openStart);
                    ImageView ll_home_imageView = itemLis.findViewById(R.id.ll_home_imageView);
//                    ll_home_imageView
                    openStart.setTag(PackageName); // 设置 tag 属性为 item 的 index
                    ll_home_imageView.setTag(PackageName);

                    ll_home_imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            String Packags = (String) v.getTag();
                            PackName=(String) v.getTag();
//                            Log.e(TAG,"PackagsPackags"+Packags);
                            VideoDetails(PackName);
                        }
                    });
                    openStart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            String Packag = (String) v.getTag();
                            PackName = (String) v.getTag();
                            Log.e(TAG, PackName);
                            if (PackName.endsWith("mp4")) {
                                //启动视频播放器播放视频
//                                fragmentCallback.onVideoClicksendMsgToActivity(Packag);
                            } else {
                                String GetTime = mCache.getAsString("TimeCache");
                                Log.e("12313132131313",GetTime);

                                /*是否免费版 1收费 0是免费*/
                                if(coinipSave.ferr().equals("")){
                                    String coins = ";5;" + PackName + ";" + GetTime;
                                    updsteReader(coins);
                                    Intent intent = new Intent(MainActivity.this, HdmiActivity.class);
                                    startActivity(intent);
                                    isOpne=true;
                                }
                                if(coinipSave.ferr().equals("0")){
                                    String coins = ";5;" + PackName + ";" + GetTime;
                                    updsteReader(coins);
                                    Intent intent = new Intent(MainActivity.this, HdmiActivity.class);
                                    startActivity(intent);
                                    isOpne=true;
                                }
                                if(coinipSave.ferr().equals("1")){
                                    if (!isTime) {
                                        showNormalDialogPass();
                                    } else {
                                        String coins = ";5;" + PackName + ";" + GetTime;
                                        updsteReader(coins);
                                        Intent intent = new Intent(MainActivity.this, HdmiActivity.class);
                                        startActivity(intent);
                                        isOpne=true;
                                    }
                                }





//                                Intent launchIntentForPackage=getActivity().getPackageManager().getLaunchIntentForPackage(Packag); // 启动第三方APP
//                                getActivity().startActivity(launchIntentForPackage);
                            }
                        }
                    });
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }

    }

    /*没有投币弹出框*/
    private void showNormalDialogPass() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(MainActivity.this);
//        normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle("WARNING");
        normalDialog.setMessage("Please Insert Coins");
//        normalDialog.setPositiveButton("确定",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //...To-do
//                    }
//                });
        normalDialog.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //...To-do
            }
        });
        // 显示
        normalDialog.show();
    }

    /*游戏详情弹出框*/
    private void DiyDialog2() {

        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性 final
         */
        AlertDialog.Builder normalDialog = new AlertDialog.Builder(MainActivity.this);
//        normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle("游戏名字");
        normalDialog.setMessage("wrong password, Please contact the JAMMA administrator");
//        normalDialog.setPositiveButton("确定",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //...To-do
//                    }
//                });
        normalDialog.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //...To-do
            }
        });
        // 显示
        normalDialog.show();
    }

    private void VideoDetails(String Packags) {
        isDialog=true;
//        Log.e(TAG,a);
        AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(MainActivity.this, R.style.MyDialog);
//
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_custom, null);
        alterDiaglog.setView(dialogView);//加载进去

//        findViewById(R.id.vvmovie);

        mediaPlayer.reset();
        videoV = (VideoView) dialogView.findViewById(R.id.vvmovie);

        Bundle extras = getIntent().getExtras();

        String VideoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/jamma/list/" + Packags + "/video/1.mp4";

        /*图片*/
        String ImgPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/jamma/list/" + Packags + "/img/1.jpg";
        detailChart = (ImageView) dialogView.findViewById(R.id.Detail_chart);
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(ImgPath, options);
        detailChart.setImageBitmap(bitmap);
//        videoV.setVideoPath(path);
//        videoV.start();
        try {
            File file = new File(VideoPath);
            if (!file.exists()) {
                // 文件不存在
                Log.e(TAG, "文件目录找不到");
            } else {
                videoV.setVideoPath(VideoPath);
                videoV.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.setLooping(true);
                        mp.start();
                    }
                });
            }
        } catch (Exception e) {

        }
//     Log.e(TAG,"-9-9-9-9-9-"+getPackageName());
//
//                String uri = "android.resource://" +getPackageName() + "/" + R.raw.as;
//                Log.e(TAG,"1231321321"+uri);
//                videoView.setVideoURI(Uri.parse(uri));
//                videoView.start();

        //显示
        dialog = alterDiaglog.create();
        dialog.show();
        //自定义的东西
    }

    public void videoStup() {
        videoView.pause();
//        Log.d(TAG,"1212  暂停暂停");
    }

    /*开始播放视频*/
    public void videoViewStart() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/jamma/1.mp4";
        videoView.setVideoURI(Uri.parse(path));
        videoView.start();
    }

    ;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除服务绑定和停止服务

    }

    private void initView() {
        mainActivity = this;
        radioGroups = findViewById(R.id.ll_home_radioGroup);
        p_setting = findViewById(R.id.Page_settings);
        re = findViewById(R.id.home_refresh);
        l_top_onlines = findViewById(R.id.l_top_Onlines);

        // Free Mode arrangement
        lferr_mode = findViewById(R.id.ferr_liner);

        // time show arrangement
        ltime_liner = findViewById(R.id.time_liner);



//        testImer = findViewById(R.id.testImer);
//        but_sss = findViewById(R.id.butsss);

//        coinipSave.changeTimeMillisecond();
        coin = new ipSave();

        /*是否免费版 1收费 0是免费*/
        if(coinipSave.ferr().equals("")){
            lferr_mode.setVisibility(View.VISIBLE);
            ltime_liner.setVisibility(View.GONE);

        }
        if(coinipSave.ferr().equals("0")){
            lferr_mode.setVisibility(View.VISIBLE);
            ltime_liner.setVisibility(View.GONE);
        }
        if(coinipSave.ferr().equals("1")){
            lferr_mode.setVisibility(View.GONE);
            ltime_liner.setVisibility(View.VISIBLE);
        }

        dialog_opne = findViewById(R.id.dialog_opne);
        xml_time = findViewById(R.id.time_update);
        xml_time.setText(DownGameTimes);
//        myCountDownTimer = new MyCountDownTimer(TIME, 1000);
//        myCountDownTimer.start();
        hdmiIn = new HdmiActivity();
        server_socket = new ServiceActivity();
        mediaPlayer = new MediaPlayer();
        server_socket.NewServerSocket();
        p_setting.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                Log.e(TAG, "我是长按按钮");
                ClickLongButton();
                return false;
            }
        });
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e(TAG,"ljlj");
//               Send1(MachineSendType.msgStart);
//                Intent launchIntentForPackage=getPackageManager().getLaunchIntentForPackage("com.lztek.hdmiin"); // 启动第三方APP
//                                    startActivity(launchIntentForPackage);

//                Intent intent = new Intent();
//                intent.setComponent(new ComponentName("com.jamma.vr_star", "com.lztek.hdmiin"));
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);

            }
        });
        l_top_onlines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ipSave.class);
                startActivity(intent);

            }
        });

//        but_sss.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("wertyuiop","234567890-"+coinipSave.changeTimeMillisecond());
////                testImer.setText(coinipSave.changeTimeMillisecond());
//            }
//        });
//
        /**
         * *********************************USB开始*********************************
         */
        M_this = this;

        /*创建socket服务器*/

        Create_server();

        /**
         * 获取USB信息
         */
        mUsbOtg = new UsbHost(this);
        mUsbOtg.connect();
        // 获取设备管理服务
        policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        new Thread(new MainActivity.ThreadShow()).start();//检测进程，验证消息
        if (!isCharging() && !isGameing) {
            MainActivity.M_this.TimeOutShutdown();
        }
        /** 获取缓存时间 */
        mCache = ACache.get(this);
        mCache.put("TimeCache", Integer.toString(CountDownGameTimes));
        String GetTime = mCache.getAsString("TimeCache");
        if (GetTime.equals("0") || GetTime == null) {
//            myCountDownTimer.cancel();
        } else {
//            myCountDownTimer = new MyCountDownTimer(Integer.parseInt(GetTime), 1000);
//            myCountDownTimer.start();
        }
        /**
         * 更新UI数据
         */
        Message message = new Message();
        message.what = 9;
        MainActivity.handler.sendMessage(message);
    }

    /*关闭dialog*/
    public void onButtonClickDialogHide(View view) {
        dialog.hide();
    }
    /*点击打开游戏dialog*/
    public void onButtonClickOpenGames(View view) {
        String GetTime = mCache.getAsString("TimeCache");
        Log.e(TAG,GetTime);

        /*是否免费版 1收费 0是免费*/
        if(coinipSave.ferr().equals("")){
            String coins = ";5;" + PackName + ";" + GetTime;
            updsteReader(coins);
            Intent intent = new Intent(MainActivity.this, HdmiActivity.class);
            startActivity(intent);
            isOpne=true;
        }
        if(coinipSave.ferr().equals("0")){
            String coins = ";5;" + PackName + ";" + GetTime;
            updsteReader(coins);
            Intent intent = new Intent(MainActivity.this, HdmiActivity.class);
            startActivity(intent);
            isOpne=true;
        }
        if(coinipSave.ferr().equals("1")){
            if (!isTime) {
                showNormalDialogPass();
            } else {
                String coins = ";5;" + PackName + ";" + GetTime;
                updsteReader(coins);
                Intent intent = new Intent(MainActivity.this, HdmiActivity.class);
                startActivity(intent);
                isOpne=true;
            }
        }
    }
    /*跳转设置页面*/
    private void ClickLongButton() {
        Intent intent = new Intent(MainActivity.this, ipSave.class);
        startActivity(intent);
    }
    /*传数据*/
//    protected void TransmitData(){
//        Intent intent = new Intent(MainActivity.this,JAMMA_USB.class);
//        intent.putExtra("handlerStart","1");
//        startActivity(intent);
//    }

    /**
     * 服务连接 获取当前服务对象
     */
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            Message message = new Message();
            message.what = 1;
            MainActivity.handler.sendMessage(message);
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            Message message = new Message();
            message.what = 2;
            MainActivity.handler.sendMessage(message);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /*创建服务器和启动服务端*/
    private void Create_server() {

//        try {
//            mServerSocket = new ServerSocket(3217);
//
////            Socket socket = mServerSocket.accept();
////            // 判断客户端是否已经断开连接
////            if (socket.isClosed()) {
////                System.out.println("客户端已经断开连接");
////            }
//            /*启动socket服务器线程*/
////            socketStartUp();
//
//        } catch (IOException e) {
//            Log.e(TAG,"我是端口我是端口我是断开");
//            e.printStackTrace();
//        }
    }

    /**
     * 根据不同连接方式发送
     */
    public void SocketSend(byte msg) {
        switch (SocketModeAPI) {
            case "TCP":
//                socketConnect.SendToServer(msg);
//                break;
            case "OTG":
                keepAlive();
                mUsbOtg.sendMSG(msg);
                break;
        }
    }

    private boolean isInWakeLock() {
        return null != wakeLock && wakeLock.isHeld();
    }

    private void keepAlive() {
        final int cmd = PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE;
        keepScreen(cmd, false, 15000);
    }

    private void keepScreen(final int cmd, boolean isForce, int timeOut) {
//        if (pm.isScreenOn() || (isInWakeLock() && !isForce)) {
//            return;
//        }
//        wakeLock = pm.newWakeLock(cmd, "keepScreen");
//        wakeLock.acquire(timeOut);
    }

    // 创建handler
    public static Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 11://投币
                       isTime=true; // 是否可以打开游戏
                        CountDownGameTimes = Integer.parseInt(M_this.mCache.getAsString("TimeCache"));

                        int AddTime = CountDownGameTimes + M_this.coinipSave.changeTimeMillisecond();

                        M_this.mCache.put("TimeCache", Integer.toString(AddTime));
                        String coins = ";3;3;" + AddTime;
                        M_this.updsteReader(coins);
                        if (!isStartTime) {
                            M_this.startTime();
                            M_this.isStartTime = true;
                        } else {
                            M_this.TimeCancles();
                            M_this.startTime();
                        }
//                    }
                    break;
                case 0:
//                    if(M_this.mServerSocket.isClosed()) {
//                        Log.e("连接断开了", "0");
//                    }else {
//                        Log.e("连接了", "1"+M_this.mServerSocket.isClosed());
//                        M_this.isCoin_box=false;
//                    }
//                    if(!M_this.isCoin_box){
//                        /*投币器开机发送命令*/
//                        M_this.Send1(MachineSendType.msgStop);
//                        M_this.isCoin_box=true;
//                    }
                    break;
                case 1:
                    /*开启投币器*/
//                    M_this.Send1(MachineSendType.msgStart);
//                    Log.e(TAG,"jlj"+M_this.isCoin_boxup);

                    if (!M_this.isCoin_boxup) {
                        if(M_this.coinipSave.ferr().equals("1")){
                            M_this.Send1(MachineSendType.msgStart);
                        }
                    }

//                    jamma_start.serverStatusFucn();
                    break; //+
                case 2:
                    break;//-
                case 3://播放
//                    if (!isGameing) {
//                        if (jamma_start.Coin >= jamma_start.CoinSum && !GameName.equals("")) {
//                            jamma_start.PlayVideo(".mp4");
//                            jamma_start.Coin = jamma_start.Coin - jamma_start.CoinSum;
//                            jamma_start.RestCoin();
//                        }
//                    }
                    break;
                case 4:
                    isScoket = false;
                    if (M_this.isStopSocket) {
                        M_this.serverStatusFucnOFF();
//                        jamma_start.StopSocket();
                    }
//                  Log.e(TAG,"444444444444444444444444444");
                    break;//socket 结束
                case 5:
                    Util.restartApp(MainActivity.M_this);
                    break;
                case 6:
                    if (!isServer) {

//                        jamma_start.ServerRestart();

                        Message message = new Message();
                        message.what = 6;
                        handler.sendMessageDelayed(message, 10 * 1000);
                    }
//                    Message receive_MSG = new Message();
//                    receive_MSG.what = 101;
//                    JAMMA_USB.handler.sendMessageDelayed(receive_MSG, 15000);
                    break;
                case 8:
//                    if (!isGameing) {
//                        new PlayerSoundPool(3).start();
//                    }
                    Message receiveMSG = new Message();
                    receiveMSG.what = 101;
                    MainActivity.handler.sendMessageDelayed(receiveMSG, 2000);
                    break;
                case 9:
                    /*延迟更新UI页面*/
                    M_this.requestManageExternalStoragePermission();
                    M_this.getNavList();
                    M_this.Send1(MachineSendType.msgStop);
                    break;
                case 13:
                    /*socekt 掉线*/
                    M_this.Send1(MachineSendType.msgStop);

                    M_this.restartApp();

                    break;
                case 97:
                    if (!isGameing) {
//                        M_this.SetStateColor(Color.RED);
                    }
                    break;
                case 98:
                    if (!isGameing) {
//                        M_this.SetStateColor(Color.GREEN);
                    }
                    break;
                case 99:
                    if (M_this.isShutdown == false) {
//                        M_this.androidShutDown();
                    }
                    break;
                case 101:
                    M_this.isReceive = true;
                    break;
            }
        }
    };

    private void TimeCancles() {
        myCountDownTimer.cancel();
    }
    public  void restartApp() {
//        Intent intent = new Intent(MainActivity.this, MainActivity.class);
//        startActivity(intent);
//        System.exit(0);
//        finish();
        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
//杀掉以前进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    private void startTime() {
//        Log.e(TAG,"12346796421313");
        String GetTime = mCache.getAsString("TimeCache");
//        int a =Integer.parseInt(GetTime);
        myCountDownTimer = new MyCountDownTimer(Integer.parseInt(GetTime), 1000);
        myCountDownTimer.start();
//       isStartTime = true;
    }
    private Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 11111) {
//                updateVideoView();
            } else if (msg.what == POWER_OFF_MSG_D) {
                Log.d("powerOffTest", "handleMessage:isCharging " + isCharging());
//                powerOnOrNot.setText("是否通电：是");
                if (!isCharging()) {
                    try {
                        Method method = pm.getClass().getDeclaredMethod("shutdown", boolean.class, boolean.class);
                        method.invoke(pm, false, true);
                        isShutdown = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    /***是否充电*/
    private boolean isCharging() {
        Intent batteryBroadcast = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        if (batteryBroadcast == null) {
            return false;
        }
        boolean isCharging = batteryBroadcast.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) != 0;
//        Log.d("charge state", "isCharging = " + isCharging);
        if (isCharging) {
//            usnES.setText("online");
//            usnES.setTextColor(Color.parseColor("#90EE90"));
        } else {
//            usnES.setText("off");
//            usnES.setTextColor(Color.parseColor("#FF0000"));
        }
        return isCharging;
    }

    private void serverStatusFucnOFF() {
        /*投币器开机发送命令*/
//        Send1(MachineSendType.msgStart);
        /*投币器开机和关机更新状态*/
//        tvCoinBox.setText("off");
//        tvCoinBox.setTextColor(Color.parseColor("#FF0000"));
        /*服务器在线状态更新UI*/
//        serverStatus.setText("off");
//        serverStatus.setTextColor(Color.parseColor("#FF0000"));

    }

    /**
     * 线程类
     */
    class ThreadShow implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (true) {
                try {
                    updateUI();
                    if (!isGameing) {
                        if (MainActivity.Coin < MainActivity.CoinSum) {
                            //Send1(MachineSendType.msgStop);//FB GUANBI投币器
                            //Send(MachineSendType.msgStop);//F1关闭上下
                        } else if (isRandom && Coin >= CoinSum) {
//                            jamma_start.VideosPicRandom();
                        }
                    } else {
//                         Send(MachineSendType.msgStart);
                    }
                    Message msgOtg = new Message();
//                    msgOtg.what=5;
                    handler.sendMessage(msgOtg);
                    Thread.sleep(3000);
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * 更新 服务器UI
     */
    private void updsteReader(String msg) {
        new Thread() {
            @Override
            public void run() {
                server_socket.SendServer(msg);
            }
        }.start();
    }

    /*发送客户端信息*/
//    public void SendServer(String mst) {
//        try {
////            writer = new DataOutputStream(mSocket.getOutputStream());
////            writer.writeUTF(mst); // 客户回复我在线
////            if (mServerSocket == null) {
////                Create_server();
////                mSocket = mServerSocket.accept();
////            }
//            DataOutputStream writer = new DataOutputStream(mSocket.getOutputStream());
//            writer.writeUTF(mst); // 写一个
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 发送数据
     */
    public void Send1(MachineSendType mst) {
//        Log.e(TAG,"==========MMMMM======="+mst );
        byte msg = 0;
        byte msg1 = 0;
        if (mst == MachineSendType.msgStart) {
            isCoin_boxup=true;
            msg = (byte) 0xB1;
        } else if (mst == MachineSendType.msgStop) {
            isCoin_boxup=false;
            msg = (byte) 0xFB;
        }
        if (mst == MachineSendType.msgStart) {
            SocketSend(msg);
//            tvCoinBox.setText("online");
//            tvCoinBox.setTextColor(Color.parseColor("#90EE90"));
        } else if (mst == MachineSendType.msgStop) {
//            tvCoinBox.setText("off");
//            tvCoinBox.setTextColor(Color.parseColor("#FF0000"));
            for (int i = 0; i < 2; i++) {
                SocketSend(msg);
            }
        }
    }


//    class SocketAcceptThread extends Thread {
//        @Override
//        public void run() {
//            Log.d("info", "accept success=========SocketAcceptThreadSocketAcceptThreadSocketAcceptThread=========");
//            try {
////                Log.d("info", "accept success=========SocketAcceptThreadSocketAcceptThreadSocketAcceptThread=========");
//                //等待客户端的连接，Accept会阻塞，直到建立连接，
//                //所以需要放在子线程中运行。
////                if (mServerSocket == null) {
////                    Create_server();
////                    mSocket = mServerSocket.accept();
////                }
//                mServerSocket.setSoTimeout(7000); // 设置超时时间为1秒
//                mSocket = mServerSocket.accept();
//
////                try {
////                    // 处理客户端连接...
////                } catch (SocketTimeoutException e) {
////                    // 连接已经断开
////                    e.printStackTrace();
////                }
//
////                if (mSocket.isClosed()) {
////                    System.out.println("客户端已经断开连接");
////                }
//                startReader(mSocket);
//            } catch (IOException e) {
//                System.out.println("客户端已经断开连接");
//                e.printStackTrace();
////                Log.e("info", "run: ==============" + "accept error");
//                return;
//            }
////            Log.e("info", "accept success========SocketAcceptThreadSocketAcceptThreadSocketAcceptThread==========");
//
//            //启动消息接收线程
//
//        }
//
//    }


    /**
     * 从参数的Socket里获取最新的消息
     */
//    private void startReader(final Socket socket) {
//        new Thread() {
//            @Override
//            public void run() {
//                DataInputStream reader;
//                try {
//                    System.out.println("*等待客户端输入*");
//                    if (!isServer) {
//                        SendServer("1;1;true|");
//                    }
//                    // 获取读取流
//                    InputStream inputStream = socket.getInputStream();
//                    // 创建一个缓冲区来读取客户端发来的数据
//                    byte[] buffer = new byte[1024];
//                    int bytesRead;
//
//                    // 读取客户端发送的数据
//                    while ((bytesRead = inputStream.read(buffer)) != -1) {
//                        isServer = true;
//                        isScoket = true;
//                        isStopSocket = false;
//                        receivedData = new String(buffer, 0, bytesRead);
////                        Log.d("info", "run: =============="+ receivedData);
//                        // 处理接收到的数据
//                        //告知客户端消息收到
////                        String b="3;3;6000;";
////                        SendServer(b);
//                        //发消息更新
//                        Message message = new Message();
//                        message.what = 1;
//                        handler.sendMessage(message);
//                    }
//
////                    Message message = new Message();
////                    message.what = 3;
////                    handler.sendMessageDelayed(message,0);
//
//                } catch (IOException e) {
//                    Log.e(TAG,"我断了 我端口了");
//                    e.printStackTrace();
//                }
//            }
//
//        }.start();
//    }

    /*启动服务线程*/
//    private void socketStartUp() {
//
//        socketAcceptThread = new MainActivity.SocketAcceptThread();
//        socketAcceptThread.start();
//    }

    /**
     * 定时关机
     */
    public void TimeOutShutdown() {
//        Message message = new Message();
//        message.what = 99;
//        handler.sendMessageDelayed(message, 2 * 60 * 1000);
    }

    /*发送消息执行对应操作 重要重要mr*/
    public void ReceiveAction(byte type) {
        Message message = new Message();
        message.what = 999;
        Log.d("powerTest", "ReceiveAction: removeMessages POWER_OFF_MSG ");
        mHandler.removeCallbacksAndMessages(null);
        Log.d("powerTest", "type" + type);
        switch (type) {
            case (byte) 11:
                message.what = 11;
                handler.sendMessage(message);

                //new PlayerSoundPool(4).start();
                break;
            case (byte) 0x1B:
                message.what = 12;
                handler.sendMessage(message);
                //new PlayerSoundPool(4).start();
                break;
            case (byte) 0x01:
                message.what = 1;
                break;
            case (byte) 0x02:
                message.what = 2;
                break;
            case (byte) 0x05:
                message.what = 3;
                break;
            case (byte) 0x06:
                if (isReceive) {
                    message.what = 6;
                    isReceive = false;
                } else {
                    message.what = 999;
                }
                break;
            case (byte) 0x07:
                message.what = 4;
                message.obj = "StopNow";
                break;
            case (byte) 0x08:
                if (isReceive) {
                    message.what = 8;
                    isReceive = false;
                } else {
                    message.what = 999;
                }
                break;
        }
        if (message.what != 999) {
            handler.sendMessage(message);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        registerScreenListener();

//        registerBoradcastReceiver();
//
////        registerSensorListener();
//        if (mUsbOtg != null) {
//            mUsbOtg.onResume();
//        }
//        Log.d("testLog", "onCreate end>>>>>>>>: ");
    }

    /**
     * 注册广播
     */
    private void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
    }

    /**
     * 十六进制字符串装十进制
     */
    public static int hexStringToAlgorism(String hex) {
        hex = hex.toUpperCase();
        int max = hex.length();
        int result = 0;
        for (int i = max; i > 0; i--) {
            char c = hex.charAt(i - 1);
            int algorism = 0;
            if (c >= '0' && c <= '9') {
                algorism = c - '0';
            } else {
                algorism = c - 55;
            }
            result += Math.pow(16, max - i) * algorism;
        }
        return result;
    }

    /**
     * byte转str
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            if (src[i] != 0) {
                int v = src[i] & 0xFF;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    stringBuilder.append(0);
                }
                stringBuilder.append(hv.toUpperCase());
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 接收数据
     */
    public boolean Receive(byte msg) {
        Log.e(TAG,"我是USB信息收到投币");
        boolean isCoin = false;
        if (msg == (byte) 0x1B) {
            isCoin = true;
        }
        if (isCoin) {
            //new PlayerSoundPool(4).start();

            Message message = new Message();
            message.what = 11;
            handler.sendMessage(message);
        }
        return isCoin;
    }

    private void updateUI() {


    }


    /**
     * -----------------------ubs界面融合代码结束--------------------
     */

    private void startLocalApp(String packageNameTarget) {
        if (SystemHelper.appIsExist(MainActivity.this, packageNameTarget)) {
            PackageManager packageManager = getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(packageNameTarget);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NEW_TASK);

            /**android.intent.action.MAIN：打开另一程序
             */
            intent.setAction("android.intent.action.MAIN");
            /**
             * FLAG_ACTIVITY_SINGLE_TOP:
             * 如果当前栈顶的activity就是要启动的activity,则不会再启动一个新的activity
             */
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "被启动的 APP 未安装", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        initializePlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        releasePlayer();
    }


//    private void releasePlayer() {
//        playerView.setPlayer(null);
//        player.release();
//        player = null;
//    }


}