package com.developerali.masterstroke.ApiModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BoothReportModel {

    @SerializedName("item")
    private List<Item> item;
    @SerializedName("status")
    private String status;
    @SerializedName("nextToken")
    private String nextToken;

    public List<Item> getItems() {
        return item;
    }

    public void setItems(List<Item> items) {
        this.item = items;
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

    public class Item {
        @SerializedName("booth_report_id")
        private String boothReportId;
        @SerializedName("constitution_id")
        private String constitutionId;
        @SerializedName("part_no")
        private String partNo;
        @SerializedName("pooling_station")
        private String poolingStation;
        @SerializedName("polling_section")
        private String pollingSection;
        @SerializedName("pooling_address")
        private String poolingAddress;
        @SerializedName("polling_ward_no")
        private String pollingWardNo;
        @SerializedName("mondal_name")
        private String mondalName;
        @SerializedName("president_name")
        private String presidentName;
        @SerializedName("booth_incharge")
        private String boothIncharge;
        @SerializedName("party1_vote")
        private String party1Vote;
        @SerializedName("party2_vote")
        private String party2Vote;
        @SerializedName("party3_vote")
        private String party3Vote;
        @SerializedName("party4_vote")
        private String party4Vote;
        @SerializedName("party5_vote")
        private String party5Vote;
        @SerializedName("total_voter")
        private String totalVoter;
        @SerializedName("male")
        private String male;
        @SerializedName("female")
        private String female;
        @SerializedName("hindu")
        private String hindu;
        @SerializedName("muslim")
        private String muslim;
        @SerializedName("other_community")
        private String otherCommunity;

        public String getBoothReportId() {
            return boothReportId;
        }

        public void setBoothReportId(String boothReportId) {
            this.boothReportId = boothReportId;
        }

        public String getConstitutionId() {
            return constitutionId;
        }

        public void setConstitutionId(String constitutionId) {
            this.constitutionId = constitutionId;
        }

        public String getPartNo() {
            return partNo;
        }

        public void setPartNo(String partNo) {
            this.partNo = partNo;
        }

        public String getPoolingStation() {
            return poolingStation;
        }

        public void setPoolingStation(String poolingStation) {
            this.poolingStation = poolingStation;
        }

        public String getPollingSection() {
            return pollingSection;
        }

        public void setPollingSection(String pollingSection) {
            this.pollingSection = pollingSection;
        }

        public String getPoolingAddress() {
            return poolingAddress;
        }

        public void setPoolingAddress(String poolingAddress) {
            this.poolingAddress = poolingAddress;
        }

        public String getPollingWardNo() {
            return pollingWardNo;
        }

        public void setPollingWardNo(String pollingWardNo) {
            this.pollingWardNo = pollingWardNo;
        }

        public String getMondalName() {
            return mondalName;
        }

        public void setMondalName(String mondalName) {
            this.mondalName = mondalName;
        }

        public String getPresidentName() {
            return presidentName;
        }

        public void setPresidentName(String presidentName) {
            this.presidentName = presidentName;
        }

        public String getBoothIncharge() {
            return boothIncharge;
        }

        public void setBoothIncharge(String boothIncharge) {
            this.boothIncharge = boothIncharge;
        }

        public String getParty1Vote() {
            return party1Vote;
        }

        public void setParty1Vote(String party1Vote) {
            this.party1Vote = party1Vote;
        }

        public String getParty2Vote() {
            return party2Vote;
        }

        public void setParty2Vote(String party2Vote) {
            this.party2Vote = party2Vote;
        }

        public String getParty3Vote() {
            return party3Vote;
        }

        public void setParty3Vote(String party3Vote) {
            this.party3Vote = party3Vote;
        }

        public String getParty4Vote() {
            return party4Vote;
        }

        public void setParty4Vote(String party4Vote) {
            this.party4Vote = party4Vote;
        }

        public String getParty5Vote() {
            return party5Vote;
        }

        public void setParty5Vote(String party5Vote) {
            this.party5Vote = party5Vote;
        }

        public String getTotalVoter() {
            return totalVoter;
        }

        public void setTotalVoter(String totalVoter) {
            this.totalVoter = totalVoter;
        }

        public String getMale() {
            return male;
        }

        public void setMale(String male) {
            this.male = male;
        }

        public String getFemale() {
            return female;
        }

        public void setFemale(String female) {
            this.female = female;
        }

        public String getHindu() {
            return hindu;
        }

        public void setHindu(String hindu) {
            this.hindu = hindu;
        }

        public String getMuslim() {
            return muslim;
        }

        public void setMuslim(String muslim) {
            this.muslim = muslim;
        }

        public String getOtherCommunity() {
            return otherCommunity;
        }

        public void setOtherCommunity(String otherCommunity) {
            this.otherCommunity = otherCommunity;
        }
    }
}
