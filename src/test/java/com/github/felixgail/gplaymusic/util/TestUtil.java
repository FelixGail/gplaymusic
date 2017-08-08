package com.github.felixgail.gplaymusic.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestUtil {
    public static final Properties PROPS;
    public static final String USERNAME;
    public static final String PASSWORD;
    public static final String TOKEN;
    public static final String ANDROID_ID;
    static {
        PROPS = new Properties();
        InputStream in = TestUtil.class
                .getClassLoader()
                .getResourceAsStream("gplaymusic.properties");
        try {
            PROPS.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        USERNAME = PROPS.getProperty("auth.username");
        PASSWORD = PROPS.getProperty("auth.password");
        ANDROID_ID = PROPS.getProperty("auth.android_id");
        TOKEN = PROPS.getProperty("auth.token");
    }
}
