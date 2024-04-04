package com.jamma.vr_star;

import static com.jamma.vr_star.MainActivity.M_this;
import static com.jamma.vr_star.MainActivity.TAG;
import static com.jamma.vr_star.util.Util.getSerialNumber;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.logging.Level;

public class Read_coin_file {
    /**
     * ip地址文件名
     */
    String FILENAME = "JAMMA.txt";

    String IsItFree = "Free.txt";  // 是否免费
    /*是否免费模式*/
    String is_ferr = "";
    String minutes_number= "";


    /*投币游戏时间*/
   ;
    /*当天的投币数量*/
    /*一个月的投币数量*/

    public String minutes() {
        return Getminutes();
    }
    public String ferr() {
        return GetFerr();
    }

    /*返回是否免费状态*/
//    public String Return_to_statu_ferr(){
//        return GetFerr();
//    }
    /**
     * 修改时间
     */
    public void changeTime(String time){
//        Log.e(TAG,"121212122121MM"+time);
        minutes_number=time;
        Save_time();
    }
    /**
     * 修改时间
     */
    public void changeFerr(String ferr){
        is_ferr=ferr;
        Save_Free();
    }
    /**
     * 转换返回毫秒
     *
     * @return
     */
    public int changeTimeMillisecond(){
        return Integer.parseInt(Getminutes()) * 60000;
    }

    /**
     * 保存时间
     */
    public void Save_time() {
//        Toast.makeText(M_this, getSerialNumber(), Toast.LENGTH_LONG).show();
//        Log.e(TAG,"787878MMMMMMMMM"+minutes_number);

        try {
            FileOutputStream fileCoun =M_this.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fileCoun.write(minutes_number.getBytes());
            fileCoun.close();
            Toast.makeText(M_this, "保存成功", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*保存免费模式*/
    public void Save_Free() {
//        Toast.makeText(M_this, getSerialNumber(), Toast.LENGTH_LONG).show();
        try {
            FileOutputStream fileCoun = M_this.openFileOutput(IsItFree, Context.MODE_PRIVATE);
            fileCoun.write(is_ferr.getBytes());
            fileCoun.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        /*保存IP提醒框*/

    /**
     * 时间获取获取
     *
     * @return
     */
    public String Getminutes() {
        try {
            FileInputStream inStream = M_this.openFileInput(FILENAME);
            int len = 0;
            byte[] buf = new byte[1024];
            StringBuilder StringBuilder = new StringBuilder();
            while ((len = inStream.read(buf)) != -1) {
//                Log.e("Getminutes","=》"+len);
                StringBuilder.append(new String(buf, 0, len));
            }
            inStream.close();
            minutes_number = StringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return minutes_number;
    }

    public String GetFerr() {
        try {
            FileInputStream inStream = M_this.openFileInput(IsItFree);
            int len = 0;
            byte[] buf = new byte[1024];
            StringBuilder StringBuilder = new StringBuilder();
            while ((len = inStream.read(buf)) != -1) {
                StringBuilder.append(new String(buf, 0, len));
            }
            inStream.close();
            is_ferr = StringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return is_ferr;
    }

}
