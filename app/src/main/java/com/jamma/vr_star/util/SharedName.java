package com.jamma.vr_star.util;

public enum SharedName {
    coin_IP("coin_IP"),
    coin_CoinSum("coin_Sum"),
    coin_Coin("coin_Coin"),
    coin_isRandom("coin_isRandom"),
    coin_isRandom_Music("coin_isRandom_Music"),
    coin_SocketModeAPI("SocketModeAPI"),
    coin_Port("coin_Port");


    private final String value;

    //构造方法必须是private或者默认
    private SharedName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


}
