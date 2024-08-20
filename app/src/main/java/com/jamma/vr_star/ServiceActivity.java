package com.jamma.vr_star;



import android.annotation.SuppressLint;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jamma.vr_star.util.MachineSendType;
import com.jamma.vr_star.util.NetWorkUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ServiceActivity {

    //    private TextView tv_clear;
//    private TextView tv_showIP;
//    private TextView tv_ip;
//    private TextView tv_msg;
    private static int is_socketOne =0 ;
    private static int is_socketTow =0 ;
    private static boolean is_socketThree = false;
    private static boolean is_socket = true;
    private static boolean is_TBC = false;


    private boolean suspendFlag = false;// 控制线程的执行
    // private int i = 0;
    private String TAG = "socekt服务端";
    private String[] temp;
    private ServerSocket mServerSocket;
    private Socket mSocket;

    MainActivity mainActivity = new MainActivity();
    private StringBuffer sb = new StringBuffer();
  Read_coin_file read_coin_file = new Read_coin_file();
    @SuppressLint("HandlerLeak")
//    public Handler handler = new Handler(Looper.myLooper()) {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            Message message = new Message();
//            switch (msg.what) {
//                case 1:
////                    Bundle data = msg.getData();
//
//                        message = new Message();
//                        message.what = 1;
//                        MainActivity.handler.sendMessage(message);
////                    split2(String.valueOf(data));
//                    break;
//                case 2:
//                    try {
////                        startReader(mSocket);
//                        Thread.sleep(2000);
////                        Log.d("info", "run: ==============2222222");
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                    break;
//                case 3:
//
////                    startReader(mSocket);
//                    Log.e("socket","我掉线");
//                    on_Close();
//
////                    try {
////
////                        Thread.sleep(7000);
////
////                        Log.d("info", "run: ==============333333");
////                        if (is_socket) {
//////                            on_Close();
//////                            NewServerSocket();
//////                            is_socket = false;
////
////                             message = new Message();
////                            message.what = 13;
////                            MainActivity.handler.sendMessage(message);
////
//////                            AppContext.restartApp();
////                            Log.d("info", "run: ==============第一次执行");
////                        }
////
////
////                    } catch (InterruptedException e) {
////                        throw new RuntimeException(e);
////                    }
//                    break;
//            }
//        }
//    };



    private void initView() {

    }

    private void setListener() {

    }


    public static int byteToInt(byte b) {
        //Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值
        return b & 0xFF;
    }
    /**
     * 从参数的Socket里获取最新的消息
     */
    public void NewServerSocket() {

        new Thread() {
            @Override
            public void run() {
                DataInputStream reader;
                try {
                    //所以需要放在子线程中运行。
                    mServerSocket = new ServerSocket(3217);
                    mSocket = mServerSocket.accept();
                    System.out.println("*等待客户端输入*");
                    MainActivity.isCoin_boxup = false;
                    MainActivity.isConnect = true;
                    /*是否免费模式*/
                    if(read_coin_file.ferr().equals("") ||read_coin_file.ferr().equals("0")) {

                        mainActivity.Free_Not = "01";
                    }
                    if(read_coin_file.ferr().equals("1")) {

                        mainActivity.Free_Not = "02";
                    }
//                    if (!MainActivity.isServer) {
//                   byte[] data = new byte[]{0x01,0x01,0x5c,0x23};
                    String data= ":"+mainActivity.DATA_HOT+";"+mainActivity.Free_Not+";"+mainActivity.In_Game+";"+
                            mainActivity.DATA_TIME+";"+mainActivity.SK_CONNECTED+";"+mainActivity.PackName+";"+mainActivity.END_CODE+":";
                       SendServer(data);   //  发送数据函数
//                    }
                    // 获取读取流
                    InputStream inputStream = mSocket.getInputStream();


                    //接收
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//                    String strRecive = reader.readLine();
                    // 创建一个缓冲区来读取客户端发来的数据
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    // 读取客户端发送的数据
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
//                        is_socket = true; // 判断是否断开
//

                        String receivedData = new String(buffer, 0, bytesRead);
//                        byte strRxBuf[] = new byte[bytesRead];
//                        String resa = new String(receivedData,"UTF-8");
//                        int len = inputStream.read(strRxBuf, 0, bytesRead);
//                        System.out.println(bytesToHexString(strRxBuf)); //  16进制数据
//                        System.out.println(receivedData);
//                        Log.e("rrrrrrrr",receivedData);
//                        if(receivedData.equals("")){
//                            String[] msgArray = receivedData.split(";");
                            Log.e("服务器",""+receivedData);

                        mainActivity.socket_data_processing(receivedData);

//
//                            for (int i = 0; i < msgArray.length; i += 1) {
//
//                                if(msgArray[1].equals("01")){
////                System.out.println( "我是免费模式" );
//
//                                }else{
////                System.out.println( "我是收费模式" );
//
//                                }
//                                if(msgArray[2].equals("01")){
////                System.out.println( "我是没有打开游戏" );
//
//
//                                }else{
////                System.out.println( "我是打开游戏" );
//                                }
//
//                                if(msgArray[3].equals("0")){
//                                    System.out.println( "我是0" );
//                                }else{
//                                    System.out.println(msgArray[3]);
//                                }
//                                if(msgArray[4].equals("01")){
//                                    System.out.println( "第一次连接成功" );
////                                   mainActivity.SK_CONNECTED="02";
////
//                                }else{
//                                    System.out.println( "心疼和其他数据" );
//                                    mainActivity.SK_CONNECTED="02";
//                                    mainActivity.isCoin_boxup=true;
////                                    mainActivity.Send1(MachineSendType.msgStart);
////
//                                }
//                            }
//                                String dataw= mainActivity.DATA_HOT+";"+mainActivity.Free_Not+";"+mainActivity.In_Game+";"+
//                                        mainActivity.DATA_TIME+";"+mainActivity.SK_CONNECTED+";"+mainActivity.END_CODE;
//                                SendServer(dataw);   //  发送数据函数
//                            Log.e("resultresultresultresult",""+result);

//                            temp = [];
//                            temp = receivedData.split(";");
//
//                            String res = new String(strRxBuf);
//
//
//
//                            String resa = new String(strRxBuf,"UTF-8");

//                            for (int i = 0; i < str.length(); i++) {
//                                chars[i] = str.charAt(i);
//                            }
//                            System.out.println("十进制resa "+resa.length());

//                            System.out.println("十进制resa "+resa[4]);
//                            System.out.println(resa);
//                            Log.e("123456789", resa);

//                            SendServer("sfd:dfljdl:dfjlsjdl:fjldsj:");
//                            StringBuilder sb = new StringBuilder();

//                            for (byte b : strRxBuf) {
//                                sb.append((char) b);
//                            }
//                            String str = sb.toString();
//                            Log.e("strstrstrstr", str);

//                            for (int i = 0; i < strRxBuf.length; i++) {
//                                Log.d("Fruit", ""+strRxBuf[i]);
////                                int a = byteToInt(strRxBuf[3]);
////                                Log.e("uoouoiuouo",""+a);
////                                if(strRxBuf[0]==0x01){
////                                    Log.d("Fruit", "我是开头");
////                                }
////                                if(strRxBuf[1]==0x00){
////                                    Log.d("Fruit", "我是二");
////                                }
////                                if(strRxBuf[2]==0x01){
////                                    Log.d("Fruit", "我是三");
////                                    Log.d("Fruit", ""+strRxBuf[3]);
////                                }
////                                if(strRxBuf[2]==0x01){
////                                    Log.d("Fruit", "我是三");
////                                    Log.d("Fruit", ""+strRxBuf[3]);
////                                }
////                                if(strRxBuf[2]==0x01){
////                                    Log.d("Fruit", "我是三");
////                                    Log.d("Fruit", ""+strRxBuf[3]);
////                                }
//                                Log.e("strRxBuf[strRxBuf.length]-1",""+strRxBuf[strRxBuf.length-1]);
////                                if(strRxBuf[strRxBuf.length]-1 ==11){
////                                    Log.d("Fruit", "我是四");
////                                    Log.d("Fruit", ""+strRxBuf[3]);
////                                }
//
//
//                            }
//                        }



//                        Log.e(TAG, "数量"+temp.length);


//                        if (temp.length ==3){
//                            for(int i = 1;i<temp.length;i++){
////                                Log.e(TAG, temp[i]);
//                                if(temp[2].equals("true")){
//                                    SendServer(";1;1;true;");
//                                    if(mainActivity.isStartTime){
//                                        int a= 0;
//                                       String forTiem = mainActivity.M_this.coinipSave.ReturForTime();
//                                        if(forTiem =="" || forTiem ==null){
//                                            a=0;
//                                        } else {
//                                            a=Integer.parseInt(forTiem) *1000;
//                                        }
//
////                                         = Integer.parseInt(mainActivity.M_this.mCache.getAsString("TimeCache"));
////                                       int addTime = a + mainActivity.M_this.coinipSave.changeTimeMillisecond();
////                                        Log.e("时间",""+a);
//                                        SendServer(";3;3;" + a);
//                                    }
//                                    if(!is_TBC){
//                                        mainActivity.M_this.tb_sercer_open();
//                                        is_TBC=true;
//                                    }
//
//
//                                }
//                                if(temp[2].equals("false|")){
//                                    is_TBC=false;
//                                    SendServer(";1;1;true;");
//
//                                }
//
//                                if(temp[2].equals("true")){
//
//                                }
//                            }
//                        }


//                        Log.e(TAG, temp[1]);
//                        Log.e(TAG,temp[2]);


//                        Log.e(TAG,temp[3]);

//                        if(receivedData.equals(null) && receivedData.equals("")){
//                            split2(receivedData);
//                        }
                        // 处理接收到的数据
                        //告知客户端消息收到
//                        DataOutputStream writer = new DataOutputStream(mSocket.getOutputStream());

//

//                        SendServer(";1;1;true;");
//                        writer.writeUTF("1;con.condlfjdlj.cn;true"); // 写一个

                        //发消息更新UI
//                        Message message = new Message();
//                        message.what = 1;
//                        Bundle bundle = new Bundle();
//                        bundle.putString("msg", receivedData);
//                        message.setData(bundle);
//                        message.obj = receivedData;

//                        handler.sendMessage(message);

                        // ...

//                        if(){
//
//                        }


                    }
//                    is_socket = false; // 判断是否断开
//                    Message message = new Message();
//                    message.what = 3;
//                    handler.sendMessageDelayed(message, 0);
                    on_Close();

                    mainActivity.M_this.tb_sercer_of();
//
                }  catch (IOException e) {
//                    Log.e("woooooo", "我已经断开了 ");
                    on_Close();
//                    AppContext.restartApp();
//                    on_Close();
//                    NewServerSocket();
//                    is_socket = false;

//                    Message message = new Message();
//                    message.what = 13;
//                    MainActivity.handler.sendMessage(message);

                    e.printStackTrace();
                }
            }

        }.start();
    }
    /**
     * byte[]数组转换为16进制的字符串
     *
     * @param bytes 要转换的字节数组
     * @return 转换后的结果
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
    /**
     * Hex字符串转byte
     * @param inHex 待转换的Hex字符串
     * @return  转换后的byte
     */
    public static byte hexToByte(String inHex){
        return (byte)Integer.parseInt(inHex,16);
    }

    /*发送客户端信息*/
    public void SendServer(String mst) {
        try {
            Log.e("SendServer",mst);
//            writer = new DataOutputStream(mSocket.getOutputStream());
//            writer.writeUTF(mst); // 客户回回复复我在线
//            if (mServerSocket == null) {
//                Create_server();
//                mSocket = mServerSocket.accept();
//            }
            DataOutputStream writer = new DataOutputStream(mSocket.getOutputStream());
//            writer.writeUTF(mst); // 写一个
            writer.write(mst.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void on_Close() {
//        Log.e("on_cler","1111111111111");
//        Log.e("on_cler",""+mServerSocket);
        if (mServerSocket != null) {
            try {
                mServerSocket.close();
                mServerSocket = null;
                NewServerSocket();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
 public void main_send_heand(){
     Message message = new Message();
     message.what = 13;
     MainActivity.handler.sendMessage(message);
 }
    /**/
    //str = "1"
//    public static void split2(String str) {
//        String[] splits = str.split(";");
//        List<String> list = new ArrayList<>();
////        Array b = [];
////        System.out.println("数组长度：" + toString(splits));
//        for (String s : splits) {
//            list.add(s.trim());
////            System.out.println("->" + split);
//        }
//        for (int i = 0; i < list.size(); i++) {
////            Log.i("iiiiii","" + i);
////            Log.i("1212121","list: " + list.get(i));
//            if(i==0){
//                is_socketOne= Integer.parseInt(list.get(i));
//            }
//            if(i==1){
//                is_socketTow= Integer.parseInt(list.get(i));
//            }
//            if(i==2){
//                is_socketThree= Boolean.parseBoolean(list.get(i));
//            }
//        }
//        System.out.println(list);
////        System.out.println("数组长度：" + list.length);
////        Log.i("1212121","list: " + list.get(1));
////        System.out.println("数组长度：" + list(0));
//    }

}
