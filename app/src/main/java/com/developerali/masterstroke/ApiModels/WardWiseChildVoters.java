package com.developerali.masterstroke.ApiModels;

import com.google.gson.annotations.SerializedName;

public class WardWiseChildVoters {

    @SerializedName("thisId")
    private String thisId;

    @SerializedName("conPhoneId")
    private String conPhoneId;

    @SerializedName("constitutionId")
    private String constitutionId;

    @SerializedName("partNo")
    private String partNo;

    @SerializedName("section")
    private String section;

    @SerializedName("name")
    private String name;

    @SerializedName("address")
    private String address;

    @SerializedName("religion")
    private String religion;  // Optional field

    @SerializedName("pollingStation")
    private String pollingStation;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("sex")
    private String sex;

    @SerializedName("house")
    private String house;

    @SerializedName("ward")
    private String ward;

    @SerializedName("language")
    private String language;  // Optional field

    @SerializedName("lname")
    private String lname;

    @SerializedName("dob")
    private String dob;

    @SerializedName("type")
    private String type;

    @SerializedName("class")
    private String sClass;

    // Getters and Setters for all fields


    public WardWiseChildVoters() {
    }

    public String getThisId() {
        return thisId;
    }

    public void setThisId(String thisId) {
        this.thisId = thisId;
    }

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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getsClass() {
        return sClass;
    }

    public void setsClass(String sClass) {
        this.sClass = sClass;
    }
}
