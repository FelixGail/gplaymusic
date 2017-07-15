package com.github.felixgail.gplaymusic.model.shema;

import com.github.felixgail.gplaymusic.model.shema.snippets.ArtRef;
import com.github.felixgail.gplaymusic.model.search.ResultType;
import com.google.gson.annotations.Expose;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.IntStream;

public class Track implements Result, Serializable {
    public final static ResultType RESULT_TYPE = ResultType.TRACK;

    @Expose
    private String title;
    @Expose
    private String artist;
    @Expose
    private String composer;
    @Expose
    private String album;
    @Expose
    private String albumArtist;
    @Expose
    private int year;
    @Expose
    private int trackNumber;
    @Expose
    private String genre;
    @Expose
    private String durationMillis;
    @Expose
    private List<ArtRef> albumArtRef;
    @Expose
    private int discNumber;
    @Expose
    private String estimatedSize;
    @Expose
    private String trackType;
    @Expose
    private String storeId;
    @Expose
    private String albumId;
    @Expose
    private List<String> artistId;
    @Expose
    private String nid;
    @Expose
    private boolean trackAvailableForSubscription;
    @Expose
    private boolean trackAvailableForPurchase;
    @Expose
    private boolean albumAvailableForPurchase;
    @Expose
    private String explicitType;

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


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbumArtist() {
        return albumArtist;
    }

    public void setAlbumArtist(String albumArtist) {
        this.albumArtist = albumArtist;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDurationMillis() {
        return durationMillis;
    }

    public void setDurationMillis(String durationMillis) {
        this.durationMillis = durationMillis;
    }

    public List<ArtRef> getAlbumArtRef() {
        return albumArtRef;
    }

    public void setAlbumArtRef(List<ArtRef> albumArtRef) {
        this.albumArtRef = albumArtRef;
    }

    public int getDiscNumber() {
        return discNumber;
    }

    public void setDiscNumber(int discNumber) {
        this.discNumber = discNumber;
    }

    public String getEstimatedSize() {
        return estimatedSize;
    }

    public void setEstimatedSize(String estimatedSize) {
        this.estimatedSize = estimatedSize;
    }

    public String getTrackType() {
        return trackType;
    }

    public void setTrackType(String trackType) {
        this.trackType = trackType;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public List<String> getArtistId() {
        return artistId;
    }

    public void setArtistId(List<String> artistId) {
        this.artistId = artistId;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public boolean isTrackAvailableForSubscription() {
        return trackAvailableForSubscription;
    }

    public void setTrackAvailableForSubscription(boolean trackAvailableForSubscription) {
        this.trackAvailableForSubscription = trackAvailableForSubscription;
    }

    public boolean isTrackAvailableForPurchase() {
        return trackAvailableForPurchase;
    }

    public void setTrackAvailableForPurchase(boolean trackAvailableForPurchase) {
        this.trackAvailableForPurchase = trackAvailableForPurchase;
    }

    public boolean isAlbumAvailableForPurchase() {
        return albumAvailableForPurchase;
    }

    public void setAlbumAvailableForPurchase(boolean albumAvailableForPurchase) {
        this.albumAvailableForPurchase = albumAvailableForPurchase;
    }

    public String getExplicitType() {
        return explicitType;
    }

    public void setExplicitType(String explicitType) {
        this.explicitType = explicitType;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Track) && ((Track) o).getStoreId().equals(this.getStoreId());
    }

    public Signature createSignature(){
        try {
            SecretKeySpec singingkey = new SecretKeySpec(key, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(singingkey);
            String slt = String.valueOf(System.currentTimeMillis() * 1000);
            byte[] value = (getStoreId() + slt).getBytes("UTF-8");
            byte[] sigBytes = mac.doFinal(value);
            byte[] fullSig = Base64.getUrlEncoder().encode(sigBytes);
            byte[] shortened = Arrays.copyOf(fullSig, fullSig.length-1);
            String sig = new String(shortened, "UTF-8");
            return new Signature(sig, slt);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public class Signature{
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

    private void printBytes(byte[] bytes, String title) {
        StringBuilder sb = new StringBuilder();
        sb.append(title).append(": ");
        for (byte b : bytes) {
            sb.append(b).append(" ");
        }
        System.out.println(sb.toString());
    }
}
