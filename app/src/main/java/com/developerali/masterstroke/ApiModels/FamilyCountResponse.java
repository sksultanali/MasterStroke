package com.developerali.masterstroke.ApiModels;

public class FamilyCountResponse {
    private int count;
    private String status;

    // Constructor
    public FamilyCountResponse(int count, String status) {
        this.count = count;
        this.status = status;
    }

    // Getter for count
    public int getCount() {
        return count;
    }

    // Setter for count
    public void setCount(int count) {
        this.count = count;
    }

    // Getter for status
    public String getStatus() {
        return status;
    }

    // Setter for status
    public void setStatus(String status) {
        this.status = status;
    }

    // toString method for printing object details
    @Override
    public String toString() {
        return "FamilyCountResponse{" +
                "count=" + count +
                ", status='" + status + '\'' +
                '}';
    }
}

