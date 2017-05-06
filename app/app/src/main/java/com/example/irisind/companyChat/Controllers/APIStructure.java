package com.example.irisind.companyChat.Controllers;

import android.support.annotation.NonNull;

import com.example.irisind.companyChat.Models.Company;
import com.example.irisind.companyChat.Models.User;

import java.io.File;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by irisind on 2/5/17.
 */

public interface APIStructure {

    @GET("/api/companies")
    Call<List<Company>> getCompanyList();

    @GET("/api/persons/findOne")
    Call<User> getUser(@NonNull @Query("filter") String filter);

    @GET("/api/companies/findOne")
    Call<Company> getCompany(@NonNull @Query("filter") String filter);

    @FormUrlEncoded
    @POST("/api/persons")
    Call<User> signupUser(@Field("fullname") String fullname,
                      @Field("age") String age,
                      @Field("sex") String sex,
                      @Field("dateOfBirth") String dob,
                      @Field("phoneNumber") String phone,
                      @Field("place") String place,
                      @Field("city") String city,
                      @Field("profilePic") String pic);

    @FormUrlEncoded
    @POST("/api/companies")
    Call<Company> signupCompany(@Field("companyName") String name,
                          @Field("subAccountName") String subname,
                          @Field("phoneNumber") String phone,
                          @Field("place") String place,
                          @Field("city") String city,
                          @Field("profilePic") String pic);




}
