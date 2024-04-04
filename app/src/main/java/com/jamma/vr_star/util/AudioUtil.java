package com.jamma.vr_star.util;

import android.content.Context;
import android.media.AudioManager;

/**
 * Created by Zain-PC on 2018/7/6 0006.
 */

public class AudioUtil {
    AudioManager mAudioManager;

    public AudioUtil(Context context) {
//初始化音频管理器
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
//获取系统最大音量
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    /**
     * 设置多媒体音量
     */
    public void setMediaVolume(int volume) {
        try {
            mAudioManager.setStreamVolume(AudioManager.STREAM_RING, //音量类型
                    volume,
                    AudioManager.FLAG_PLAY_SOUND
                            | AudioManager.FLAG_SHOW_UI);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    //设置通话音量
    public void setCallVolume(int volume) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                volume,
                AudioManager.STREAM_VOICE_CALL);
    }

    //最大音量
    public void setMaxVolume() {
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
        mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, max, AudioManager.STREAM_VOICE_CALL);
    }

    // 关闭/打开扬声器播放
    public void setSpeakerStatus(boolean on) {
        if (on) { //扬声器
            mAudioManager.setSpeakerphoneOn(true);
            mAudioManager.setMode(AudioManager.MODE_NORMAL);
        } else {
            // 设置最大音量
            int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
            mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, max, AudioManager.STREAM_VOICE_CALL);
            // 设置成听筒模式
            mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            mAudioManager.setSpeakerphoneOn(false);// 关闭扬声器
            mAudioManager.setRouting(AudioManager.MODE_NORMAL, AudioManager.ROUTE_EARPIECE, AudioManager.ROUTE_ALL);
        }
    }


}
