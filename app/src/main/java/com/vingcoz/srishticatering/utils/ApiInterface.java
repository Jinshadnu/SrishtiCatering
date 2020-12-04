package com.vingcoz.srishticatering.utils;

import com.vingcoz.srishticatering.models.api.CommonResponse;
import com.vingcoz.srishticatering.models.api.ComonResponse;
import com.vingcoz.srishticatering.models.api.categ.CategoryResponse;
import com.vingcoz.srishticatering.models.api.login.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("add_user_details")
    Call<CommonResponse> getRegister(@Field("NAME") String strName,
                                     @Field("CONTACT") String strContact,
                                     @Field("PASSWORD") String strPassword,
                                     @Field("USER_EMAIL") String email);

    @FormUrlEncoded
    @POST("user_login")
    Call<LoginResponse> getLogin(@Field("CONTACT") String strContact,
                                 @Field("PASSWORD") String strPassword);


    @GET("category")
    Call<CategoryResponse> getCategories();

    @FormUrlEncoded
    @POST("order_insert")
    Call<CommonResponse> getOrderInsert(@Field("STAFF_ID") long lngStaff,
                                        @Field("CUSTOMER_NAME") String strName,
                                        @Field("CUSTOMER_PHONE") String strContact,
                                        @Field("CAT") int intCatID,
                                        @Field("D_DATE") String strDate,
                                        @Field("D_TIME") String strTime,
                                        @Field("DISHES") String strDishes,
                                        @Field("SECOND_PHONE") String strSecondPhone,
                                        @Field("D_PLACE") String strPlace,
                                        @Field("DELIVERY_DATE_TIME") String strDeliveryDateTime,
                                        @Field("D_ADDRESS") String strAddress);
    @FormUrlEncoded
    @POST("change_password")
    Call<ComonResponse> changePassword(@Field("user_id") String userId,
                                       @Field("old_password") String oldPassword,
                                       @Field("new_password")String newPassword);

    @FormUrlEncoded
    @POST("forgot_password")
    Call<ComonResponse> forgotPassword(@Field("email") String email);


}
