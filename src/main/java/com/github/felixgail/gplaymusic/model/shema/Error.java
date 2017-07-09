package com.github.felixgail.gplaymusic.model.shema;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Error implements Serializable{

    private class ErrorHelper implements Serializable{
        @Expose
        private int code;
        @Expose
        private String message;

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }

    @Expose
    @SerializedName("error")
    private ErrorHelper helper = new ErrorHelper();


    public int getCode() {
        return helper.getCode();
    }

    public String getMessage() {
        return helper.getMessage();
    }

    @Override
    public String toString() {
        return String.format("%d: %s", getCode(), getMessage());
    }
}
