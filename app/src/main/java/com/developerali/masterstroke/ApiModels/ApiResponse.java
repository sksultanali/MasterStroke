package com.developerali.masterstroke.ApiModels;

public class ApiResponse {
    private String status;
    private String message;
    private int ward_id;
    private int new_mobile_count;
    private int printed_count;
    private int edited_count;
    private int today_birthday_with_mobile;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public int getWard_id() {
        return ward_id;
    }

    public int getNew_mobile_count() {
        return new_mobile_count;
    }

    public int getPrinted_count() {
        return printed_count;
    }

    public int getEdited_count() {
        return edited_count;
    }

    public int getToday_birthday_with_mobile() {
        return today_birthday_with_mobile;
    }
}
