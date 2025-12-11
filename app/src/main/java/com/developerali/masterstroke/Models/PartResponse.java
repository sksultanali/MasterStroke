package com.developerali.masterstroke.Models;

import java.util.List;

public class PartResponse {

    private List<Item> item;
    private String status;

    // Default constructor
    public PartResponse() {
    }

    // Parameterized constructor
    public PartResponse(List<Item> item, String status) {
        this.item = item;
        this.status = status;
    }

    // Getters and Setters
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

    // Inner class for "item" array objects
    public static class Item {
        private String part_no;

        // Default constructor
        public Item() {
        }

        public Item(String part_no) {
            this.part_no = part_no;
        }

        public String getPart_no() {
            return part_no;
        }

        public void setPart_no(String part_no) {
            this.part_no = part_no;
        }

        @Override
        public String toString() {
            return part_no;
        }
    }

    @Override
    public String toString() {
        return "PartResponse{" +
                "item=" + item +
                ", status='" + status + '\'' +
                '}';
    }
}
