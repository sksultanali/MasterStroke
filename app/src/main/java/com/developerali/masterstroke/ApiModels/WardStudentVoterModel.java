package com.developerali.masterstroke.ApiModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WardStudentVoterModel {

    @SerializedName("item")
    private List<WardWiseChildVoters> item;
    @SerializedName("status")
    private String status;
    @SerializedName("nextToken")
    private String nextToken;
    @SerializedName("totalItems")
    private String totalItems;

    public List<WardWiseChildVoters> getItem() {
        return item;
    }

    public void setItem(List<WardWiseChildVoters> item) {
        this.item = item;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
