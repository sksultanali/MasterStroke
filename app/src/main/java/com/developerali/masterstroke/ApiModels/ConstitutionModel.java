package com.developerali.masterstroke.ApiModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConstitutionModel {

    @SerializedName("item")
    private List<Item> item;
    @SerializedName("status")
    private String status;

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

    public class Item {
        @SerializedName("constitution_id")
        private String constitution_id;
        @SerializedName("constitution_name")
        private String constitution_name;

        public String getConstitution_id() {
            return constitution_id;
        }

        public void setConstitution_id(String constitution_id) {
            this.constitution_id = constitution_id;
        }

        public String getConstitution_name() {
            return constitution_name;
        }

        public void setConstitution_name(String constitution_name) {
            this.constitution_name = constitution_name;
        }
    }
}
