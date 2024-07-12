package com.developerali.masterstroke.ApiModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PhoneAddressModel {

    @SerializedName("item")
    private List<Item> item;
    @SerializedName("status")
    private String status;
    @SerializedName("nextToken")
    private String nextToken;

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

    public String getNextToken() {
        return nextToken;
    }

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }

    public static class Item{

        @SerializedName("con_phone_id")
        private String conPhoneId;
        @SerializedName("constitution_id")
        private String constitutionId;
        @SerializedName("part_no")
        private String partNo;
        @SerializedName("section")
        private String section;
        @SerializedName("name")
        private String name;
        @SerializedName("address")
        private String address;
        @SerializedName("religion")
        private String religion;
        @SerializedName("polling_station")
        private String pollingStation;
        @SerializedName("mobile")
        private String mobile;
        @SerializedName("sex")
        private String sex;
        @SerializedName("age")
        private String age;
        @SerializedName("house")
        private String house;
        @SerializedName("ward")
        private String ward;

        public String getConPhoneId() {
            return conPhoneId;
        }

        public void setConPhoneId(String conPhoneId) {
            this.conPhoneId = conPhoneId;
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

        public String getSection() {
            return section;
        }

        public void setSection(String section) {
            this.section = section;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getReligion() {
            return religion;
        }

        public void setReligion(String religion) {
            this.religion = religion;
        }

        public String getPollingStation() {
            return pollingStation;
        }

        public void setPollingStation(String pollingStation) {
            this.pollingStation = pollingStation;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getHouse() {
            return house;
        }

        public void setHouse(String house) {
            this.house = house;
        }

        public String getWard() {
            return ward;
        }

        public void setWard(String ward) {
            this.ward = ward;
        }
    }
}
