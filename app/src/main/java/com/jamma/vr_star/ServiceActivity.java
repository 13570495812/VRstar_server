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

    private ServerSocket mServerSocket;
    private Socket mSocket;
    //    MainActivity mainActivity = new MainActivity();
    private StringBuffer sb = new StringBuffer();

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler(Looper.myLooper()) {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Message message = new Message();
            switch (msg.what) {
                case 1:
//                    Bundle data = msg.getData();
                        message = new Message();
                        message.what = 1;
                        MainActivity.handler.sendMessage(message);
//                    split2(String.valueOf(data));
                    break;
                case 2:
                    try {
                        startReader(mSocket);
                        Thread.sleep(2000);
//                        Log.d("info", "run: ==============2222222");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 3:
                    startReader(mSocket);
                    try {
                        Thread.sleep(7000);
                        Log.d("info", "run: ==============333333");
                        if (is_socket) {
                            on_Close();
                            NewServerSocket();
                            is_socket = false;

                             message = new Message();
                            message.what = 13;
                            MainActivity.handler.sendMessage(message);

//                            AppContext.restartApp();
                            Log.d("info", "run: ==============第一次执行");
                        }


                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
            }
        }
    };

    protected void NewServerSocket() {
//        initView();
//        setListener();

        try {
            mServerSocket = new ServerSocket(3217);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //启动服务线程
        SocketAcceptThread socketAcceptThread = new SocketAcceptThread();
        socketAcceptThread.start();
    }

    private void initView() {

    }

    private void setListener() {

    }

    class SocketAcceptThread extends Thread {
        @Override
        public void run() {
            try {
                //等待客户端的连接，Accept会阻塞，直到建立连接，
                //所以需要放在子线程中运行。
                mSocket = mServerSocket.accept();

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("info", "run: ==============" + "accept error");
//                on_Close();
                return;
            }
            Log.e("info", "accept success==================");
            //启动消息接收线程
            startReader(mSocket);
        }
    }

    /**
     * 从参数的Socket里获取最新的消息
     */
    private void startReader(final Socket socket) {

        new Thread() {
            @Override
            public void run() {
                DataInputStream reader;
                try {
                    System.out.println("*等待客户端输入*");
                    MainActivity.isCoin_boxup = false;
                    MainActivity.isConnect = true;
//                    if (!MainActivity.isServer) {
//                       SendServer("1;1;true|");
//                    }
                    // 获取读取流
                    InputStream inputStream = socket.getInputStream();
                    // 创建一个缓冲区来读取客户端发来的数据
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    // 读取客户端发送的数据
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        is_socket = true; // 判断是否断开
                        MainActivity.isServer = true;
                        String receivedData = new String(buffer, 0, bytesRead);
                        if(receivedData.equals(null) && receivedData.equals("")){
                            split2(receivedData);
                        }
                        // 处理接收到的数据

                        //告知客户端消息收到
                        DataOutputStream writer = new DataOutputStream(mSocket.getOutputStream());
                        if(!is_socketThree){
                            SendServer(";1;1;true;");
                        }
                        if(is_socketThree){
                            SendServer(";1;1;true;");
                        }
//                        SendServer(";1;1;true;");
//                        writer.writeUTF("1;con.condlfjdlj.cn;true"); // 写一个

                        //发消息更新UI
                        Message message = new Message();
                        message.what = 1;
//                        Bundle bundle = new Bundle();
//                        bundle.putString("msg", receivedData);
//                        message.setData(bundle);
//                        message.obj = receivedData;

                        handler.sendMessage(message);
                        // ...
                    }
//                    is_socket = false; // 判断是否断开
                    Message message = new Message();
                    message.what = 3;
                    handler.sendMessageDelayed(message, 0);

                }  catch (IOException e) {
                    Log.e("woooooo", "我已经断开了 ");
//                    AppContext.restartApp();
//                    on_Close();
//                    NewServerSocket();
//                    is_socket = false;
                    Message message = new Message();
                    message.what = 13;
                    MainActivity.handler.sendMessage(message);

                    e.printStackTrace();
                }
            }

        }.start();
    }
    /*发送客户端信息*/
    public void SendServer(String mst) {
        try {
//            writer = new DataOutputStream(mSocket.getOutputStream());
//            writer.writeUTF(mst); // 客户回回复复我在线
//            if (mServerSocket == null) {
//                Create_server();
//                mSocket = mServerSocket.accept();
//            }
            DataOutputStream writer = new DataOutputStream(mSocket.getOutputStream());
            writer.writeUTF(mst); // 写一个
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void on_Close() {
        if (mServerSocket != null) {
            try {
                mServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**/
    //str = "1"
    public static void split2(String str) {
        String[] splits = str.split(";");
        List<String> list = new ArrayList<>();
//        Array b = [];
//        System.out.println("数组长度：" + toString(splits));
        for (String s : splits) {
            list.add(s.trim());
//            System.out.println("->" + split);
        }
        for (int i = 0; i < list.size(); i++) {
            Log.i("iiiiii","" + i);
            Log.i("1212121","list: " + list.get(i));
            if(i==0){
                is_socketOne= Integer.parseInt(list.get(i));
            }
            if(i==1){
                is_socketTow= Integer.parseInt(list.get(i));
            }
            if(i==2){
                is_socketThree= Boolean.parseBoolean(list.get(i));
            }
        }
        System.out.println(list);
//        System.out.println("数组长度：" + list.length);
//        Log.i("1212121","list: " + list.get(1));
//        System.out.println("数组长度：" + list(0));
    }




}
