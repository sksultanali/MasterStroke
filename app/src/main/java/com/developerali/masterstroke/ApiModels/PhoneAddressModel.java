package com.developerali.masterstroke.ApiModels;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PhoneAddressModel {

    @SerializedName("item")
    private List<Item> item;
    @SerializedName("status")
    private String status;
    @SerializedName("nextToken")
    private String nextToken;
    @SerializedName("totalItems")
    private String totalItems;

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

    public String getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(String totalItems) {
        this.totalItems = totalItems;
    }

    public static class Item implements Parcelable {

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
        @SerializedName("voter_id")
        private String voter_id;
        @SerializedName("sl_no")
        private String sl_no;
        @SerializedName("language")
        private String language;
        @SerializedName("status")
        private String status;
        @SerializedName("lname")
        private String lname;
        @SerializedName("dob")
        private String dob;
        @SerializedName("doa")
        private String doa;
        @SerializedName("new_mobile")
        private String new_mobile;
        @SerializedName("stat")
        private String stat;
        @SerializedName("note")
        private String note;
        @SerializedName("intereset_party")
        private String intereset_party;

        public Boolean isSelected = false;

        public Item() {
            this.isSelected = false; // Initialize to false in the default constructor
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

        public String getVoter_id() {
            return voter_id;
        }

        public void setVoter_id(String voter_id) {
            this.voter_id = voter_id;
        }

        public String getSl_no() {
            return sl_no;
        }

        public void setSl_no(String sl_no) {
            this.sl_no = sl_no;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getDoa() {
            return doa;
        }

        public void setDoa(String doa) {
            this.doa = doa;
        }

        public String getNew_mobile() {
            return new_mobile;
        }

        public void setNew_mobile(String new_mobile) {
            this.new_mobile = new_mobile;
        }

        public String getStat() {
            return stat;
        }

        public void setStat(String stat) {
            this.stat = stat;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getIntereset_party() {
            return intereset_party;
        }

        public void setIntereset_party(String intereset_party) {
            this.intereset_party = intereset_party;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLname() {
            return lname;
        }

        public void setLname(String lname) {
            this.lname = lname;
        }

        protected Item(Parcel in) {
            conPhoneId = in.readString();
            constitutionId = in.readString();
            partNo = in.readString();
            section = in.readString();
            name = in.readString();
            address = in.readString();
            religion = in.readString();
            pollingStation = in.readString();
            mobile = in.readString();
            sex = in.readString();
            age = in.readString();
            house = in.readString();
            ward = in.readString();
            voter_id = in.readString();
            sl_no = in.readString();
            language = in.readString();
            dob = in.readString();
            doa = in.readString();
            new_mobile = in.readString();
            stat = in.readString();
            note = in.readString();
            intereset_party = in.readString();
            status = in.readString();
            lname = in.readString();
            isSelected = in.readByte() != 0; // Handle boolean conversion
        }

        public static final Creator<Item> CREATOR = new Creator<Item>() {
            @Override
            public Item createFromParcel(Parcel in) {
                return new Item(in);
            }

            @Override
            public Item[] newArray(int size) {
                return new Item[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeString(conPhoneId);
            parcel.writeString(constitutionId);
            parcel.writeString(partNo);
            parcel.writeString(section);
            parcel.writeString(name);
            parcel.writeString(address);
            parcel.writeString(religion);
            parcel.writeString(pollingStation);
            parcel.writeString(mobile);
            parcel.writeString(sex);
            parcel.writeString(age);
            parcel.writeString(house);
            parcel.writeString(ward);
            parcel.writeString(voter_id);
            parcel.writeString(sl_no);
            parcel.writeString(language);
            parcel.writeString(dob);
            parcel.writeString(doa);
            parcel.writeString(new_mobile);
            parcel.writeString(stat);
            parcel.writeString(note);
            parcel.writeString(intereset_party);
            parcel.writeString(status);
            parcel.writeString(lname);
            parcel.writeByte((byte) (isSelected ? 1 : 0)); // Handle boolean conversion
        }
    }

}
