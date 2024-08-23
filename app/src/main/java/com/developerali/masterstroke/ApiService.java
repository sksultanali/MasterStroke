package com.developerali.masterstroke;

import com.developerali.masterstroke.ApiModels.BoothReportModel;
import com.developerali.masterstroke.ApiModels.ConstitutionModel;
import com.developerali.masterstroke.ApiModels.LoginModel;
import com.developerali.masterstroke.ApiModels.PhoneAddressModel;
import com.developerali.masterstroke.ApiModels.UpdateModel;
import com.developerali.masterstroke.ApiModels.WardClass;
import com.developerali.masterstroke.ApiModels.WardWiseChildVoters;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    @GET("phoneAddSearch")
    Call<PhoneAddressModel> SearchVoters(
            @Query("token") String token,
            @Query("nextToken") int nextToken,
            @Query("consId") int consId,
            @Query("searchTerm") String searchTerm,
            @Query("searchOn") String searchOn
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

    @GET("updatePhoneAdd")
    Call<UpdateModel> UpdateVoter(
            @Query("token") String token,
            @Query("con_phone_id") String con_phone_id,
            @Query("fieldName") String fieldName,
            @Query("newValue") String newValue,
            @Query("note") String note
    );

    @GET("updateConstitutionName")
    Call<UpdateModel> UpdateConstitution(
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
    Call<UpdateModel> getStudentNewVoter(
            @Query("token") String token,
            @Query("con_phone_id") String conPhoneId,
            @Query("type") String type
    );

}
