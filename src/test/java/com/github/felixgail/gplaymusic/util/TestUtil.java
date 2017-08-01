package com.github.felixgail.gplaymusic.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestUtil {
    public static final Properties PROPS;
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
    }
}
