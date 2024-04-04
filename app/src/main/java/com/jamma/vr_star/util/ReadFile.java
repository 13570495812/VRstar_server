package com.jamma.vr_star.util;

import android.media.MediaPlayer;
import android.os.Message;
import android.util.Log;

import com.jamma.vr_star.MainActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ReadFile {
    //文件是否在
    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    //根据路径获取文件
    public static List<File> GetFilePath(String fileDir) {
        List<File> fileList = new ArrayList<>();
        File file = new File(fileDir);
        File[] files = file.listFiles();// 获取目录下的所有文件或文件夹
        if (files == null) {// 如果目录为空，直接退出
            return null;
        }
        // 遍历，目录下的所有文件
        for (File f : files) {
            if (f.isFile()) {
                fileList.add(f);
            }
        }
        //图片排序
        if(fileList.size()>=2){
            Collections.sort(fileList, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (o1.isDirectory() && o2.isFile())
                        return -1;
                    if (o1.isFile() && o2.isDirectory())
                        return 1;
                    return o1.getName().compareTo(o2.getName());
                }
            });
        }
        return fileList;
    }

    //获取影片时长并设置定时任务
    public static void SetVideoPlayerTime(final String GameName) {
        new Thread() {
            @Override
            public void run() {
                MediaPlayer player = new MediaPlayer();
                try {
                    player.setDataSource("/storage/emulated/0/JAMMA/Video/360/" + GameName);  //recordingFilePath（）为音频文件的路径
                    player.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                long duration = player.getDuration();//获取音频的时间
                player.release();//记得释放资源
                Message message = new Message();
                message.what = 4;
                message.obj = "StopNow";
                MainActivity.handler.sendMessageDelayed(message, duration + 5000);
            }
        }.start();
    }

    /*读取txt文本*/
    public static String ReadTxtFile(String strFilePath) {
        String txtContent = "";
        //打开文件
        File file = new File(strFilePath);
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory()) {
        } else {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null) {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    txtContent = buffreader.readLine();
                    instream.close();
                }
            } catch (java.io.FileNotFoundException e) {
                Log.d("TestFile", "The File doesn't not exist.");
            } catch (IOException e) {
                Log.d("TestFile", e.getMessage());
            }
        }
        return txtContent;
    }
}
