package com.developerali.masterstroke.ApiModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginModel {
    @SerializedName("item")
    private List<Item> item;
    @SerializedName("status")
    private String status;

    public List<Item> getItem() {
        return item;
    }

    public void setItem(List<Item> item) {
        this.item = item;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public class Item {
        @SerializedName("userId")
        private String userId;
        @SerializedName("name")
        private String name;
        @SerializedName("phone")
        private String phone;
        @SerializedName("username")
        private String username;
        @SerializedName("password")
        private String password;
        @SerializedName("ward_id")
        private String ward_id;
        @SerializedName("splash_link")
        private String splash_link;
        @SerializedName("home_link")
        private String home_link;
        @SerializedName("party_suggest")
        private String party_suggest;
        @SerializedName("candidate_name")
        private String candidate_name;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getWard_id() {
            return ward_id;
        }

        public void setWard_id(String ward_id) {
            this.ward_id = ward_id;
        }

        public String getSplash_link() {
            return splash_link;
        }

        public void setSplash_link(String splash_link) {
            this.splash_link = splash_link;
        }

        public String getCandidate_name() {
            return candidate_name;
        }

        public void setCandidate_name(String candidate_name) {
            this.candidate_name = candidate_name;
        }

        public String getHome_link() {
            return home_link;
        }

        public String getParty_suggest() {
            return party_suggest;
        }

        public void setParty_suggest(String party_suggest) {
            this.party_suggest = party_suggest;
        }

        public void setHome_link(String home_link) {
            this.home_link = home_link;
        }
    }
}
