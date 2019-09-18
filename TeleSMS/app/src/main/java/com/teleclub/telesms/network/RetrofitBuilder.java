package com.teleclub.telesms.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teleclub.telesms.util.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {
    public static <T> T createRetrofitService(final Class<T> clazz) {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        final Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(createOkHttpClient())
                .build();

        return restAdapter.create(clazz);
    }

    private static okhttp3.OkHttpClient createOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okhttp3.OkHttpClient.Builder b = new okhttp3.OkHttpClient.Builder();
        b.readTimeout(30, TimeUnit.SECONDS);
        b.connectTimeout(30, TimeUnit.SECONDS);
        b.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                return chain.proceed(chain.request());
            }
        });
        b.interceptors().add(interceptor);
        return b.build();
    }
}
