package com.developerali.masterstroke.ApiModels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class WardClass {
    @SerializedName("items")
    private List<Item> item;
    @SerializedName("status")
    private String status;
    @SerializedName("grossTotal")
    private String grossTotal;
    @SerializedName("message")
    private String message;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGrossTotal() {
        return grossTotal;
    }

    public void setGrossTotal(String grossTotal) {
        this.grossTotal = grossTotal;
    }

    public static class Item{
        @SerializedName("txt")
        private String txt;
        @SerializedName("total")
        private String total;
        @SerializedName("hof_name")
        private String hof_name;
        @SerializedName("subnames")
        private ArrayList<String> subnames;;
        @SerializedName("total_count")
        private String total_count;
        public Boolean isSelected = false;

        public Item() {
            this.isSelected = false; // Initialize to false in the default constructor
        }

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public ArrayList<String> getSubnames() {
            return subnames;
        }

        public void setSubnames(ArrayList<String> subnames) {
            this.subnames = subnames;
        }

        public String getTotal_count() {
            return total_count;
        }

        public void setTotal_count(String total_count) {
            this.total_count = total_count;
        }

        public String getHof_name() {
            return hof_name;
        }

        public void setHof_name(String hof_name) {
            this.hof_name = hof_name;
        }
    }

}
