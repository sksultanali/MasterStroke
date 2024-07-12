package com.developerali.masterstroke;

import com.developerali.masterstroke.ApiModels.BoothReportModel;
import com.developerali.masterstroke.ApiModels.ConstitutionModel;
import com.developerali.masterstroke.ApiModels.PhoneAddressModel;
import com.developerali.masterstroke.ApiModels.UpdateModel;

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

    @GET("phoneAddSearch")
    Call<PhoneAddressModel> SearchVoters(
            @Query("token") String token,
            @Query("nextToken") int nextToken,
            @Query("consId") int consId,
            @Query("searchTerm") int searchTerm,
            @Query("searchOn") int searchOn
    );

    @GET("consTable")
    Call<ConstitutionModel> getConstitutions(
            @Query("token") String token
    );

    @GET("updatePhoneAdd")
    Call<UpdateModel> UpdateVoter(
            @Query("token") String token,
            @Query("con_phone_id") int con_phone_id,
            @Query("fieldName") int fieldName,
            @Query("newValue") int newValue
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

}
