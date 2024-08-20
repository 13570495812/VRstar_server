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
     * 时间
     */
    String FORTIMEFILE = "FORTIME.txt";  // 投币时间
    String FILENAME = "JAMMA.txt";  // 投币时间
    String IsItFree = "Free.txt";  // 是否免费
    /*是否免费模式*/
    String is_ferr = "";
    // 投币保存时间
    String minutes_number= "";
    // 循环时间

    String forTime= "";

    /*投币游戏时间*/
   ;
    /*当天的投币数量*/
    /*一个月的投币数量*/
/***
 * 保存循环时间
 */
public String ReturForTime() {
    if(GETFORTIME().equals(null) || GETFORTIME().equals("")){
        return "0";
    }else {
        return GETFORTIME();
    }

}
    /**
     *
     * @return 保存投币时间开始
     */
    // 返回投标时间
    public String minutes() {
        if(Getminutes().equals(null) || Getminutes().equals("")){
            return "0";
        }else {
            return Getminutes();
        }
    }

//    是否免费模式
    public String ferr() {
        return GetFerr();
    }

    /*返回是否免费状态*/
//    public String Return_to_statu_ferr(){
//        return GetFerr();
//    }
    /**
     * 点击保存修改时间含税
     */
    public void changeTime(String time){
        minutes_number=time;
        Save_time();
    }
    /**
     * 点击修改投币时间
     */
    public void changeFerr(String ferr){
        is_ferr=ferr;
        Save_Free();
    }

    /**
     * 投币修改时间
     */
    public void changeForTime(String forT){
        forTime= forT;
        Save_FORtime();
    }
    /**
     * 保存循环时间
     */
    public void Save_FORtime() {
        try {
            FileOutputStream fileCoun =M_this.openFileOutput(FORTIMEFILE, Context.MODE_PRIVATE);
            fileCoun.write(forTime.getBytes());
            fileCoun.close();

//            Toast.makeText(M_this, "保存成功", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存时间
     */
    public void Save_time() {
        try {
            FileOutputStream fileCoun =M_this.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fileCoun.write(minutes_number.getBytes());
            fileCoun.close();
//            Toast.makeText(M_this, "保存成功", Toast.LENGTH_LONG).show();
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
     * 读取时间文件夹
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
//            Log.e("我是时间",minutes_number);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return minutes_number;
    }

    // 是否免费模式读取
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
    // 倒计时时间读取
    public String GETFORTIME() {
        try {
            FileInputStream inStream = M_this.openFileInput(FORTIMEFILE);
            int len = 0;
            byte[] buf = new byte[1024];
            StringBuilder StringBuilder = new StringBuilder();
            while ((len = inStream.read(buf)) != -1) {
                StringBuilder.append(new String(buf, 0, len));
            }
            inStream.close();
            forTime= StringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return forTime;
    }
}
