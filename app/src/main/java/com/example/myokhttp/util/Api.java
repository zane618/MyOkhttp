package com.example.myokhttp.util;

public class Api {
    public static final String BASE_URL = "https://test-microservice.log56.com/luge/gateway.do";
    //图片服务器地址
    public static final String BASE_IMG_URL = "https://test-microservice.log56.com/luge/upload.do";
    //aid
    public static final String AID = "QDD201809061356001";
    public static final String TOKEN = "wPzp4ZA7ZPllXPvq39bCBo+1rF/hwmii2G/6EPiZfLruo01xnzckWXBykanIrZRq4bn8XyI1sIGQmoyqOy6lhLmpLAkt4Za5YEoyLmBhoXvfquuXhaTAN/6mCOtLDvLoWSAyXWD10oYBcxBDWehtUQ==";


    public interface SID {
        /**
         * 上传图片
         */
        String POST_IMG_SID = "5008";
    }
}
