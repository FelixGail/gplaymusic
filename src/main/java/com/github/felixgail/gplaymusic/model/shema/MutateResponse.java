package com.github.felixgail.gplaymusic.model.shema;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class MutateResponse implements Serializable {

    @Expose
    private List<Item> mutate_response = new LinkedList<>();

    public List<Item> getItems() {
        return mutate_response;
    }

    public boolean checkSuccess() {
        for (Item item : mutate_response) {
            if (!item.getResponse_code().matches("^(OK|CONFLICT)$")) {
                return false;
            }
        }
        return true;
    }


    public class Item implements Serializable{
        @Expose
        private String id;
        @Expose
        @SerializedName("client_id")
        private String clientID;
        @Expose
        private String response_code;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getClientID() {
            return clientID;
        }

        public void setClientID(String clientID) {
            this.clientID = clientID;
        }

        public String getResponse_code() {
            return response_code;
        }

        public void setResponse_code(String response_code) {
            this.response_code = response_code;
        }
    }
}
