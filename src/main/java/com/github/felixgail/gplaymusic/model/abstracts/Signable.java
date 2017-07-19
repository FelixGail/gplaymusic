package com.github.felixgail.gplaymusic.model.abstracts;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;
import java.util.stream.IntStream;

public abstract class Signable {
    private final static byte[] s1 = Base64.getDecoder()
            .decode("VzeC4H4h+T2f0VI180nVX8x+Mb5HiTtGnKgH52Otj8" +
                    "ZCGDz9jRWyHb6QXK0JskSiOgzQfwTY5xgLLSdUSreaLVMsVVWfxfa8Rw==");
    private final static byte[] s2 = Base64.getDecoder()
            .decode("ZAPnhUkYwQ6y5DdQxWThbvhJHN8msQ1rqJw0ggKdufQjelrKuiG" +
                    "GJI30aswkgCWTDyHkTGK9ynlqTkJ5L4CiGGUabGeo8M6JTQ==");
    private final static byte[] key;

    static {
        int length = Math.min(s1.length, s2.length);
        char[] zipped = new char[length];
        IntStream.range(0, length).forEach(i -> zipped[i] = (char) (s1[i] ^ s2[i]));
        String helperString = new String(zipped);
        try {
            key = helperString.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract String getID();

    public abstract Signature getSignature();

    protected Signature createSignature(String id) {
        try {
            SecretKeySpec singingkey = new SecretKeySpec(key, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(singingkey);
            String slt = String.valueOf(System.currentTimeMillis() * 1000);
            byte[] value = (id + slt).getBytes("UTF-8");
            byte[] sigBytes = mac.doFinal(value);
            byte[] fullSig = Base64.getUrlEncoder().encode(sigBytes);
            byte[] shortened = Arrays.copyOf(fullSig, fullSig.length - 1);
            String sig = new String(shortened, "UTF-8");
            return new Signature(sig, slt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public class Signature {
        private String sig;
        private String slt;

        public Signature(String sig, String slt) {
            this.sig = sig;
            this.slt = slt;
        }

        public String getSignature() {
            return sig;
        }

        public String getSalt() {
            return slt;
        }
    }
}
