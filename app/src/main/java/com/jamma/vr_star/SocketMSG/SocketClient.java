package com.jamma.vr_star.SocketMSG;

public class SocketClient {
//    private Socket socket;
//    private String sendMsg = "";
//    public boolean isConnect = false;
//    private byte HeadMsg[];
//    Handler handler = new Handler();
//    boolean isConnected = false;
//    //利用HandlerThread处理异步消息，Activity销毁的时候回收Ï
//    private Handler mHandler;
//
//    public SocketClient() {
//        HandlerThread thread = new HandlerThread("MyHandlerThread");
//        thread.start();
//        mHandler = new Handler(thread.getLooper());
//    }
//
//    //实现耗时操作的线程
//    Runnable mBackgroundRunnable = new Runnable() {
//
//        @Override
//        public void run() {
//            //----------模拟耗时的操作，开始---------------
//            while (true) {
//                try {
//                    Log.d(MainActivity.TAG, "SOCKET run: ");
//                    socket = new Socket();
//                    socket = new Socket(MainActivity.IP, MainActivity.Port);
//                    InputStream inputStream = socket.getInputStream();
//                    DataInputStream input = new DataInputStream(inputStream);
//                    isConnected = isConnect = true;
//                    Message msg = new Message();
//                    msg.what = 98;
//                    MainActivity.M_this.handler.sendMessage(msg);
//
//                    byte[] b = new byte[1024];
//
//                    while (isConnect) {
//                        try {
//                            int length = input.read(b);
//                            Log.d(MainActivity.TAG, "TCP 读取数据的长度: " +length);
//                            if (b[1] != (byte) 0x4F && b[2] != (byte) 0x4B) {
//                                if (!MainActivity.M_this.isGameing) {
//                                    //机器号区分接受信息
//                                    if (!MainActivity.M_this.Receive(b[0])) {
//                                        MainActivity.M_this.ReceiveAction(b[0]);//统一行动
//                                    }
//                                }
//                            }
//                            b = new byte[1024];
//                        } catch (IOException e) {
//                            isConnect = false;
//                        }
//                    }
//                } catch (IOException e) {
//                    isConnect = false;
//                }
//                //----------模拟耗时的操作，结束---------------
//            }
//        }
//    };
//
//    /**
//     * 建立服务端连接
//     */
//    public void conn() {
//        mHandler.post(mBackgroundRunnable);//将线程post到Handler中
//        /*new Thread() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        socket = new Socket();
//                        socket = new Socket(JAMMA_Videos.UDP_IP, JAMMA_Videos.UDP_Port);
//                        InputStream inputStream = socket.getInputStream();
//                        DataInputStream input = new DataInputStream(inputStream);
//                        isConnected = isConnect = true;
//                        Message msg = new Message();
//                        msg.what = 98;
//                        JAMMA_Videos.jamma_videos.handler.sendMessage(msg);
//
//                        byte[] b = new byte[1024];
//
//                        while (isConnect) {
//                            try {
//                                int length = input.read(b);
//                                if (b[1] != (byte) 0x4F && b[2] != (byte) 0x4B) {
//                                    if (!JAMMA_Videos.jamma_videos.isGameing) {
//                                        //机器号区分接受信息
//                                        if (!JAMMA_Videos.jamma_videos.Receive(b[0])) {
//                                            JAMMA_Videos.jamma_videos.ReceiveAction(b[0]);//统一行动
//                                        }
//                                    }
//                                }
//                                b = new byte[1024];
//                            } catch (IOException e) {
//                                isConnect = false;
//                            }
//                        }
//                    } catch (IOException e) {
//                        isConnect = false;
//                    }
//                }
//            }
//        }.start();*/
//    }
//
//
//    /**
//     * 发送消息 单个数据
//     */
//    public void SendToServer(byte MSG) {
//        if (socket.isConnected()) {
//            this.HeadMsg = new byte[]{MSG};
//            new Thread() {
//                @Override
//                public void run() {
//                    try {
//                        DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
//                        writer.write(HeadMsg);
//                    } catch (IOException e) {
//                        isConnect = false;
//                    }
//                }
//            }.start();
//        }
//    }
//
//    /**
//     * 发送消息 数据数组
//     */
//    public void SendToServer(byte[] MSG) {
//        if (socket.isConnected()) {
//            this.HeadMsg = MSG;
//            new Thread() {
//                @Override
//                public void run() {
//                    try {
//                        DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
//                        writer.write(HeadMsg);
//                    } catch (IOException e) {
//                    }
//                }
//            }.start();
//        }
//    }
//
//    public boolean ConnectState() {
//        return socket.isConnected();
//    }
//
//    public void Uart_SendCMD(int CMD, int feedback, int dat) {
//        //设置数据
//        byte[] Send_buf = new byte[8];
//        Send_buf[0] = (byte) 0xff;    //保留字节
//        Send_buf[1] = (byte) 0x06;    //长度
//        Send_buf[2] = (byte) CMD;     //控制指令
//        Send_buf[3] = (byte) feedback;//是否需要反馈
//        Send_buf[4] = (byte) (dat >> 8);//datah
//        Send_buf[5] = (byte) (dat);     //datal
//
//        //计算校验
//        int xorsum = 0;
//        for (int i = 0; i < 6; i++) {
//            xorsum = xorsum + Send_buf[i];
//        }
//        xorsum = 0 - xorsum;
//        (Send_buf[6]) = (byte) (xorsum >> 8);
//        (Send_buf[7]) = (byte) (xorsum & 0x00ff);
//
//        //拼接数据头、尾 发送数据
//        byte[] Send_Data = new byte[10];
//        Send_Data[0] = (byte) 0x7E;
//        Send_Data[9] = (byte) 0xEF;
//        for (int i = 0; i < Send_buf.length; i++) {
//            Send_Data[i + 1] = Send_buf[i];
//        }
//        SendToServer(Send_Data);
//    }
//
//    public void onDestroy(){
//        Log.d(MainActivity.TAG, "SocketClient onDestroy: ");
//        if(mHandler!=null){
//            mHandler.removeCallbacks(mBackgroundRunnable);
//            mHandler.removeCallbacksAndMessages("");
//        }
//    }
}
