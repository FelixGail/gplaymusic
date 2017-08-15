package com.github.felixgail.gplaymusic.util;

import org.junit.Assume;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

public class TestUtil {
    public static final String USERNAME_KEY = "auth.username";
    public static final String PASSWORD_KEY = "auth.password";
    public static final String ANDROID_ID_KEY = "auth.android_id";
    public static final String TOKEN_KEY = "auth.token";
    public static final Property USERNAME;
    public static final Property PASSWORD;
    public static final Property ANDROID_ID;
    public static final Property TOKEN;
    private static final Properties PROPS;
    private static final String RESOURCE = "gplaymusic.properties";

    static {
        PROPS = new Properties();
        InputStream in = TestUtil.class
                .getClassLoader()
                .getResourceAsStream(RESOURCE);

        try {
            PROPS.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        USERNAME = new Property(USERNAME_KEY);
        PASSWORD = new Property(PASSWORD_KEY);
        ANDROID_ID = new Property(ANDROID_ID_KEY);
        TOKEN = new Property(TOKEN_KEY);
    }

    public static Property get(String s) {
        return new Property(s);
    }

    public static void set(String key, String value) throws IOException {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(TestUtil.class.getClassLoader().getResource(RESOURCE).getFile());
            PROPS.setProperty(key, value);
            PROPS.store(writer, null);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public static void assume(Property... properties) {
        for (Property property : properties) {
            Assume.assumeTrue(
                    String.format("Test has been skipped. Required value \"%s\" is missing.", property.getKey()),
                    property.isValid());
        }
    }

    public static void assumeFilled(Property... properties) {
        assume(properties);
        for (Property property : properties) {
            Assume.assumeTrue(
                    String.format("Test has been skipped. \"%s\" is not allowed to be an empty String.",
                            property.getKey()),
                    !property.get().isEmpty());
        }
    }

    public static void assume(Object... objects) {
        for (Object object : objects) {
            Assume.assumeNotNull(
                    String.format("Test has been skipped. Required object \"%s\" is null.", object),
                    object);
        }
    }

    public static class Property {
        private String key;

        public Property(String k) {
            this.key = k;
        }

        public String getKey() {
            return key;
        }

        public String get() {
            return TestUtil.PROPS.getProperty(key);
        }

        @Override
        public String toString() {
            return get();
        }

        public boolean isValid() {
            return (get() != null);
        }
    }
}
