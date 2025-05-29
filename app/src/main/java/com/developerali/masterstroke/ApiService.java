package com.developerali.masterstroke;

import com.developerali.masterstroke.ApiModels.ApiResponse;
import com.developerali.masterstroke.ApiModels.BoothReportModel;
import com.developerali.masterstroke.ApiModels.ConstitutionModel;
import com.developerali.masterstroke.ApiModels.FamilyCountResponse;
import com.developerali.masterstroke.ApiModels.LoginModel;
import com.developerali.masterstroke.ApiModels.PhoneAddressModel;
import com.developerali.masterstroke.ApiModels.UpdateModel;
import com.developerali.masterstroke.ApiModels.WardClass;
import com.developerali.masterstroke.ApiModels.WardStudentVoterModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("booth-report")
    Call<BoothReportModel> getBoothReport(
            @Query("token") String token,
            @Query("nextToken") int nextToken,
            @Query("consId") int consId
    );

    @GET("phoneAdd")
    Call<PhoneAddressModel> getVoters(
            @Query("token") String token,
            @Query("nextToken") int nextToken,
            @Query("consId") int consId
    );

    @GET("moderators-login")
    Call<LoginModel> getLoginCredentials(
            @Query("token") String token,
            @Query("username") String username
    );

    @GET("getAllCounts")
    Call<ApiResponse> getAllCounts(
            @Query("token") String token,
            @Query("ward") String ward
    );

    @GET("phoneAddSearch")
    Call<PhoneAddressModel> SearchVoters(
            @Query("token") String token,
            @Query("nextToken") int nextToken,
            @Query("consId") int consId,
            @Query("searchTerm") String searchTerm,
            @Query("searchOn") String searchOn
    );

    @GET("insertWorkRecord")
    Call<UpdateModel> insertWorkRecord(
            @Query("token") String token,
            @Query("username") String username,
            @Query("location") String location,
            @Query("date") String date,
            @Query("ward") String ward,
            @Query("note") String note,
            @Query("part_no") String part_no,
            @Query("sl_no") String sl_no,
            @Query("works") String works,
            @Query("actual_address") String actual_address,
            @Query("distance") String distance
    );

    @GET("insertWorkRecord")
    Call<UpdateModel> insertWorkRecord(
            @Query("token") String token,
            @Query("username") String username,
            @Query("location") String location,
            @Query("date") String date,
            @Query("ward") String ward,
            @Query("note") String note,
            @Query("part_no") String part_no,
            @Query("sl_no") String sl_no,
            @Query("actual_address") String actual_address,
            @Query("distance") String distance
    );

    @GET("phoneAddSearchDual")
    Call<PhoneAddressModel> SearchDualVoters(
            @Query("token") String token,
            @Query("nextToken") int nextToken,
            @Query("consId") int consId,
            @Query("searchTerm") String searchTerm,
            @Query("searchOn") String searchOn,
            @Query("partNo") int partNo
            );

    @GET("onlyMobileNo")
    Call<PhoneAddressModel> getOnlyMobileNo(
            @Query("token") String token,
            @Query("nextToken") int nextToken,
            @Query("consId") int consId
    );

    @GET("consTable")
    Call<ConstitutionModel> getConstitutions(
            @Query("token") String token
    );

    @GET("familyCount")
    Call<FamilyCountResponse> getFamilyCount(
            @Query("token") String token,
            @Query("consId") int consId
    );

    @GET("consDetails")
    Call<ConstitutionModel> getConstitutionDetails(
            @Query("token") String token,
            @Query("consId") int consId

    );

    @GET("uniqueWards")
    Call<WardClass> getUniqueWards(
            @Query("token") String token,
            @Query("consId") int consId,
            @Query("values") String values

    );

    @GET("uniqueWardsFF")
    Call<WardClass> getUniqueWardsFF(
            @Query("token") String token,
            @Query("consId") int consId,
            @Query("values") String values

    );

    @GET("uniquePartLang1")
    Call<WardClass> getUniquePartLan(
            @Query("token") String token,
            @Query("consId") int consId,
            @Query("field") String field,
            @Query("values") String values

    );

    @GET("uniquePartByAge")
    Call<WardClass> getUniquePartAge(
            @Query("token") String token,
            @Query("consId") int consId,
            @Query("minAge") String minAge,
            @Query("maxAge") String maxAge
    );

    @GET("uniqueWardsWithQuery2")
    Call<WardClass> getUniqueValuesWithQuery(
            @Query("token") String token,
            @Query("consId") int consId,
            @Query("values") String values,
            @Query("query") String query,
            @Query("check") String check

    );

//    @GET("uniquePartLang")
//    Call<WardClass> getUniquePartLan(
//            @Query("token") String token,
//            @Query("consId") int consId,
//            @Query("values") String values,
//            @Query("lan") String lan
//
//    );

    @GET("updatePhoneAdd")
    Call<UpdateModel> UpdateVoter(
            @Query("token") String token,
            @Query("con_phone_id") String con_phone_id,
            @Query("fieldName") String fieldName,
            @Query("newValue") String newValue,
            @Query("note") String note
    );

    @GET("updateConstitutionName")
    Call<UpdateModel> updateConstitution(
            @Query("token") String token,
            @Query("conId") int conId,
            @Query("newValue") String newValue
    );

    @GET("updateFamilyCount")
    Call<UpdateModel> updateFamilyCount(
            @Query("token") String token,
            @Query("conId") int conId,
            @Query("newValue") int newValue
    );

    @GET("updateBoothReportTable")
    Call<UpdateModel> UpdateBoothReport(
            @Query("token") String token,
            @Query("booth_report_id") int booth_report_id,
            @Query("fieldName") int fieldName,
            @Query("newValue") int newValue
    );

    @GET("insert-student-new-voter")  // The endpoint
    Call<UpdateModel> insertWardWiseChild(
            @Query("token") String token,
            @Query("con_phone_id") String conPhoneId,
            @Query("constitution_id") String constitutionId,
            @Query("part_no") String partNo,
            @Query("section") String section,
            @Query("name") String name,
            @Query("address") String address,
            @Query("religion") String religion,
            @Query("polling_station") String pollingStation,
            @Query("sex") String sex,
            @Query("house") String house,
            @Query("ward") String ward,
            @Query("language") String language,
            @Query("lname") String lname,
            @Query("dob") String dob,
            @Query("mobile") String mobile,
            @Query("type") String type,
            @Query("class") String sClass
    );

    @GET("getStudentNewVoter")
    Call<WardStudentVoterModel> getStudentNewVoter(
            @Query("token") String token,
            @Query("con_phone_id") String conPhoneId,
            @Query("type") String type
    );

    @GET("getAllStudentNewVoter")
    Call<WardStudentVoterModel> getAllStudentNewVoter(
            @Query("token") String token,
            @Query("type") String type
    );

    @GET("updateAllData")
    Call<UpdateModel> updateAllData(
            @Query("token") String token,
            @Query("queryField") String queryField,
            @Query("queryText") String queryText,
            @Query("editField") String editField,
            @Query("newValue") String newValue,
            @Query("constitution_id") int constitution_id
    );

}
