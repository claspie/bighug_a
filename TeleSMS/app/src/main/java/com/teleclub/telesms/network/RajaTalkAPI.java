package com.teleclub.telesms.network;

import com.teleclub.telesms.network.result.BlockAccountResult;
import com.teleclub.telesms.network.result.CanTopupResult;
import com.teleclub.telesms.network.result.GeneralResult;
import com.teleclub.telesms.network.result.LoginResult;
import com.teleclub.telesms.network.result.BalanceResult;
import com.teleclub.telesms.network.result.SignUpResult;
import com.teleclub.telesms.network.result.TokenResult;

import org.json.JSONArray;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import rx.Observable;

import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RajaTalkAPI {
    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @POST("/api/guest/login")
    @FormUrlEncoded
    Observable<LoginResult> login(@Field("phone") String phone, @Field("password") String password, @Field("imei") String imei,
                                  @Field("sim") String sim, @Field("type") String type);

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @POST("/api/guest/signup")
    @FormUrlEncoded
    Observable<SignUpResult> signup(@Field("phone") String phone, @Field("imei") String imei,
                                    @Field("sim") String sim, @Field("type") String type);

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @POST("/api/account/balance")
    @FormUrlEncoded
    Observable<BalanceResult> getBalance(@Field("token") String token, @Field("imei") String imei, @Field("type") String type);

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @POST("/api/guest/blocking")
    @FormUrlEncoded
    Observable<BlockAccountResult> blockAccount(@Field("token") String token, @Field("phone") String phone,
                                                @Field("imei") String imei, @Field("type") String type);

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @POST("/api/guest/unblocking")
    @FormUrlEncoded
    Observable<BlockAccountResult> unblockAccount(@Field("token") String token, @Field("phone") String phone,
                                                  @Field("imei") String imei, @Field("type") String type);

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @POST("/api/account/getcantopup")
    @FormUrlEncoded
    Observable<CanTopupResult> getCanTopup(@Field("token") String token, @Field("imei") String imei,
                                           @Field("sim") String sim, @Field("type") String type);

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @POST("/api/account/setcantopup")
    @FormUrlEncoded
    Observable<GeneralResult> setCanTopup(@Field("token") String token, @Field("imei") String imei,
                                          @Field("sim") String sim, @Field("type") String type);

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @POST("/api/contact/setcontacts")
    @FormUrlEncoded
    Observable<GeneralResult> setContacts(@Field("token") String token, @Field("contact") JSONArray contacts);

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @POST("/api/guest/passrecover")
    @FormUrlEncoded
    Observable<GeneralResult> resetPassword(@Field("phone") String phone, @Field("imei") String imei, @Field("type") String type);

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @POST("/api/guest/validimei")
    @FormUrlEncoded
    Observable<GeneralResult> checkDevice(@Field("phone") String phone, @Field("imei") String imei, @Field("type") String type);

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @POST("/api/guest/deviceupdate")
    @FormUrlEncoded
    Observable<GeneralResult> resetDevice(@Field("phone") String phone, @Field("imei") String imei, @Field("type") String type);

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @POST("/api/guest/pushtoken")
    @FormUrlEncoded
    Observable<TokenResult> saveToken(@Field("phone") String phone, @Field("type") String type, @Field("imei") String imei, @Field("pushtoken") String token, @Field("telesms") String telesms);
}
