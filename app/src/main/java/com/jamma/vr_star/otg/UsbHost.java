package com.jamma.vr_star.otg;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.jamma.vr_star.MainActivity;
import com.jamma.vr_star.R;
import com.jamma.vr_star.otg.driver.UsbSerialDriver;
import com.jamma.vr_star.otg.driver.UsbSerialProber;



import com.jamma.vr_star.otg.util.HexDump;
import com.jamma.vr_star.otg.util.SerialInputOutputManager;
import com.jamma.vr_star.util.Util;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class DataListener implements SerialInputOutputManager.Listener {
    private Activity activity;
    private String TAG="UsbHost";
    private TextView mSend;
    private TextView mRe;
    private TextView mcomm;
    private static boolean isFirst=true;
    public DataListener(Activity act){
        activity=act;
//        mSend=act.findViewById(R.id.textView5);
//        mRe=act.findViewById(R.id.textView6);
//        mcomm=act.findViewById(R.id.textView8);
    }

    /***
     *
     * 接收到USB设备发送来的数据时触发,如果data.length<0表示usb设备已经发送但是主设备接收失败，反之接收成功，如果需要在UI线程处理数据，需要调用runOnUiThread防止崩溃
     * @param data 接收数据
     */
    @Override
    public void onGetData(final byte[] data) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String info=data.length>=0?"接收成功   "+HexDump.toHexString(data):"接收失败";
//                mRe.setText(info);

                if(data.length>=0){
                    byte[] b=data;
                    try{
                        if(data.length<3){
                            b=new byte[3];
                            System.arraycopy(data, 0, b,0, data.length);
                        }

                        if (b[1] != (byte) 0x4F && b[2] != (byte) 0x4B) {
                            if (!MainActivity.M_this.isGameing) {
                                if (!MainActivity.M_this.Receive(b[0])) {
                                    MainActivity.M_this.ReceiveAction(b[0]);//统一行动
                                }
                            }
                        }
                    }catch (Exception e){
//                        mRe.setText(e.getMessage());
                    }
                }
            }
        });
    }

    /***
     *
     * 检测通信过程中出现的错误时触发，方便查错
     * @param e 错误信息
     */
    @Override
    public void onRunError(Exception e) {
        Log.i(TAG, "Runner stopped.");
//        mcomm.setText(e.getMessage());
        mcomm.refreshDrawableState();
    }

    /***
     *
     * 主设备向usb设备发送数据时触发，如果#@param writeLen <0 表示发送失败，如果需要在UI线程处理数据，需要调用runOnUiThread防止崩溃
     * @param data 发送数据
     * @param writeLen 数据长度
     */
    @Override
    public void onWriteData(final byte[] data,final int writeLen) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String info=writeLen>=0?"发送成功   "+HexDump.toHexString(data,0,writeLen):"发送失败";
//                mSend.setText(info);
            }
        });
    }


    /***
     * 轮询
     * @param msg
     */
    @Override
    public void onUpdate(final String msg) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 98;
//                JAMMA_USB.jamma_start.handler.sendMessage(msg);
            }
        });
    }
}

/***
 * UsbHost，实现Usb host方式通信
 */
public class UsbHost  extends BroadcastReceiver{
    private Context mContext;
    private String TAG="UsbHost";
    private static final String ACTION_USB_PERMISSION = "com.android.usb.USB_PERMISSION";
    private UsbManager mUsbManager;
    private UsbSerialDriver mDriver;
    private UsbDeviceConnection mConn;
    public boolean isConnect=false;


    /***
     *
     * 监听线程，用于监听数据的发送和接收
     *
     */
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();


    /***
     *数据流控制
     *
     */
    private SerialInputOutputManager mSerialIoManager;

    /***
     * 监听接口
     */
    private SerialInputOutputManager.Listener mListener;

    private TextView mDevices;

    private TextView mConnt;
    private TextView mp;
    private static boolean isFirst=true;

    /***
     * 初始化
     * @param context
     */
    public UsbHost(Context context) {
        Log.d(TAG, "SocketModeAPI--------------------------: " +context);
        mContext=context;
        Activity act=(Activity)mContext;
//        mDevices=act.findViewById(R.id.textView2);
//        mConnt=act.findViewById(R.id.tv_USB_name);
//        mp=act.findViewById(R.id.textView7);
        init();
    }

    /***
     * 初始化监听接口
     */
    public void init(){
        mUsbManager=(UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        mListener=new DataListener((Activity)mContext);
        registerUsbAction();
        start();
    }

    /***
     * 动态获取权限@detach
     */
    private void registerUsbAction(){
        IntentFilter mUsbDeviceFilter = new IntentFilter();
        mUsbDeviceFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        mUsbDeviceFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        mUsbDeviceFilter.addAction(ACTION_USB_PERMISSION);
        mContext.registerReceiver(this,mUsbDeviceFilter);
    }

    /***
     * 初始化usb设备
     */
    private  void initUsbDevice(){
        String devices="no devices";

        final List<UsbSerialDriver> drivers = UsbSerialProber.getDefaultProber().findAllDrivers(mUsbManager);
        if(null != drivers && drivers.size() != 0){
            UsbSerialDriver driver=drivers.get(0);
            devices="    vendorId: "+driver.getDevice().getVendorId()+", productId: "+driver.getDevice().getProductId()+"\n";

            mDriver = driver;
        }
//        mDevices.setText(devices);
    }


    /***
     * 主设备和usb设备监理连接，在此之前需连接设备，并获取权限，否者连接失败
     */
    public void connect(){
        String info="";

        if(mDriver!=null&&!isConnect){
            try {
                mConn=mUsbManager.openDevice(mDriver.getDevice());
                if (mConn == null) {
//                    mConnt.setText("Opening device failed");
                    return;
                }
                mDriver.getPort().open(mConn);
                Log.e(TAG,"建立usb_otg通道成功");
//                info="建立usb_otg通道成功";
                info = "online";

//                mConnt.setText(info);
//                mConnt.setTextColor(Color.parseColor("#90EE90"));

                isConnect=true;
            }catch (Exception e){
                e.printStackTrace();
                info =e.getMessage();
                Toast.makeText(mContext,"建立usb_otg通道失败",Toast.LENGTH_SHORT).show();
//                mConnt.setText(info);
                try {
                    if(mConn!=null){
                        mDriver.getPort().close();
                        mDriver=null;
                    }
                    isConnect=false;
                    mConn=null;
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }
        }
    }

    /***
     * 权限请求@detach
     * @param device
     */
    private void procPermission(UsbDevice device){
        //startIoManager();
        if (!mUsbManager.hasPermission(device) && isFirst){
//            Log.i(TAG,"request permission");
            Toast.makeText(mContext,"没有usb读写权限，正在尝试请求...",Toast.LENGTH_SHORT).show();
//            mp.setText("没有usb读写权限，正在尝试请求...");
            PendingIntent pendingIntent =PendingIntent.getBroadcast(mContext,0,new Intent(ACTION_USB_PERMISSION),0);
            mUsbManager.requestPermission(device,pendingIntent);
            isFirst=false;
        }
    }

    /***
     * 检测目标设备
     * @param device
     * @return 是否目标设备
     */
    private boolean isTarget(UsbSerialDriver device){
        return device!=null && device.getDevice().getVendorId() == MyUsbDeviceParam.mVendorId && device.getDevice().getProductId() == MyUsbDeviceParam.mProductId;

    }

    /***
     * 向usb设备发送字节流数据
     * @param bData 数据流
     */
    public void sendMSG(final byte[] bData){
        if(mDriver!=null){
            if(!isConnect){
                connect();
            }
            mSerialIoManager.writeAsync(bData);
        }
    }

    /***
     * 发送单字节数据
     * @param bData 数据流
     */
    public void sendMSG(final byte bData){
        byte[] data={bData};
        sendMSG(data);
    }


    /***
     * 销毁
     */
    public void destory(){
        mContext.unregisterReceiver(this);
        clean();
    }

    /***
     * 广播监听，在此处用于usb设备权限监听，usb设备插入拔出监听
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {
            /*case ACTION_USB_PERMISSION:
                UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                UsbSerialDriver driver=UsbSerialProber.getDefaultProber().probeDevice(usbDevice);
                if (isTarget(driver) && intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                    mp.setText("请求usb读写权限成功");

                }else if(isTarget(driver)){
                    mp.setText("请求usb读写权限失败，无法进行usb-otg通信");
                    Toast.makeText(mContext,"请求usb读写权限失败，无法进行usb-otg通信",Toast.LENGTH_SHORT).show();
                }
                break;*/

                case UsbManager.ACTION_USB_DEVICE_ATTACHED:
                UsbDevice device_add = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                UsbSerialDriver driver_add=UsbSerialProber.getDefaultProber().probeDevice(device_add);
                if (isTarget(driver_add) && mDriver==null) {
                    /*isConnect=false;
                    mUsbManager=(UsbManager) mContext.getSystemService(Context.USB_SERVICE);
                    mp.setText("目标设备中途加入！");
                    Toast.makeText(mContext,"found device",Toast.LENGTH_SHORT).show();
                    mDriver=driver_add;*/
                    start();
                    //connect();
                    //procPermission(device_add);
                }
                break;
            case UsbManager.ACTION_USB_DEVICE_DETACHED:
                Log.d(TAG, "onReceive ACTION_USB_DEVICE_DETACHED >>>>>>>>>>>>: ");
                UsbDevice device_remove = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                UsbSerialDriver driver_rm=UsbSerialProber.getDefaultProber().probeDevice(device_remove);
                if (isTarget(driver_rm)) {
//                    mp.setText("目标设备中途退出！");
                    clean();
                }
                Util.forceStopPackage(context);
                break;
        }
    }

    /***
     * activity的onResume调用
     */
    public void onResume() {
        /*if(isFirst){
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    start();
                }
            };
            timer.schedule(timerTask, 7000);
            isFirst=false;
        }else{
            start();
        }*/
        //start();
    }

    private void start(){
        mUsbManager= (UsbManager)mContext.getSystemService(Context.USB_SERVICE);
        initUsbDevice();
        connect();
        onDeviceStateChange();
    }


    /***
     *状态变更
     */
    public void onDeviceStateChange(){
        stopIoManager();
        startIoManager();
    }

    /***
     * 暂停
     */
    public void onPause() {
        clean();
    }

    /***
     * 停止监听数据的接收和发送，停止后不在发送和接收数据
     */
    private void stopIoManager() {
        if (mSerialIoManager != null) {
            Log.i(TAG, "Stopping io manager ..");
            mSerialIoManager.stop();
            mListener=new DataListener((Activity)mContext);
            mSerialIoManager = null;
        }
    }

    /***
     * 开始监听数据的接收和发送
     */
    private void startIoManager() {
        if (mDriver!=null && mDriver.getPort() != null) {
            Log.i(TAG, "Starting io manager ..");
            mListener= new DataListener((Activity)mContext);
            mSerialIoManager = new SerialInputOutputManager(mDriver.getPort(), mListener);
            mExecutor.submit(mSerialIoManager);
        }
    }


    public void clean(){
        isConnect=false;
        mConn=null;
        mDriver=null;
        stopIoManager();
    }
}
