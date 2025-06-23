package com.developerali.masterstroke.ApiModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WorksModel {

    @SerializedName("status")
    private String status;
    @SerializedName("nextToken")
    private String nextToken;
    @SerializedName("totalItems")
    private String totalItems;
    @SerializedName("message")
    private String message;
    @SerializedName("items")
    private List<Item> items;

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

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getNextToken() {
        return nextToken;
    }

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }

    public String getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(String totalItems) {
        this.totalItems = totalItems;
    }

    public class Item {
        private String ward_id;
        private String username;
        private String note;
        private String works;
        private String date;
        private String location;
        private String ward;
        private String part_no;
        private String sl_no;
        private String distance;
        private String actual_address;

        // Default constructor
        public Item() {
        }

        // Parameterized constructor
        public Item(String ward_id, String username, String note, String works, String date,
                    String location, String ward, String part_no, String sl_no, String distance,
                    String actual_address) {
            this.ward_id = ward_id;
            this.username = username;
            this.note = note;
            this.works = works;
            this.date = date;
            this.location = location;
            this.ward = ward;
            this.part_no = part_no;
            this.sl_no = sl_no;
            this.distance = distance;
            this.actual_address = actual_address;
        }

        // Getters and Setters
        public String getWardId() {
            return ward_id;
        }

        public void setWardId(String wardId) {
            this.ward_id = wardId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getWorks() {
            return works;
        }

        public void setWorks(String works) {
            this.works = works;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getWard() {
            return ward;
        }

        public void setWard(String ward) {
            this.ward = ward;
        }

        public String getPartNo() {
            return part_no;
        }

        public void setPartNo(String partNo) {
            this.part_no = partNo;
        }

        public String getSlNo() {
            return sl_no;
        }

        public void setSlNo(String slNo) {
            this.sl_no = slNo;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getActualAddress() {
            return actual_address;
        }

        public void setActualAddress(String actualAddress) {
            this.actual_address = actualAddress;
        }

        // toString method for debugging
        @Override
        public String toString() {
            return note + '\n' +
                    "By " + username + "\n" +
                    "PartNo - "+ part_no + " SlNo - " + part_no;
        }
    }

}
