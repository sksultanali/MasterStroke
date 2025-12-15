package com.developerali.masterstroke.Models;

import com.developerali.masterstroke.ApiModels.PhoneAddressModel;

import java.util.List;

public class VoterListResponse {
    private String status;
    private String total;
    private int offset;
    private int nextToken;
    private int limit;
    private Filters filters;
    private List<PhoneAddressModel.Item> data;

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getNextToken() {
        return nextToken;
    }

    public void setNextToken(int nextToken) {
        this.nextToken = nextToken;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Filters getFilters() {
        return filters;
    }

    public void setFilters(Filters filters) {
        this.filters = filters;
    }

    public List<PhoneAddressModel.Item> getData() {
        return data;
    }

    public void setData(List<PhoneAddressModel.Item> data) {
        this.data = data;
    }

    public static class Filters {
        private String interesetParty;

        public String getInteresetParty() {
            return interesetParty;
        }

        public void setInteresetParty(String interesetParty) {
            this.interesetParty = interesetParty;
        }
    }

}

