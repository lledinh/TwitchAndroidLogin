package com.ledinh.twitch_login.rest;

import com.ledinh.twitch_login.rest.RestResponseObjects.TopObjects.UserObject;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Header;

/**
 * Created by Lam on 17/01/2016.
 */
public class Twitch {
    private static final String DEBUG_TAG = Twitch.class.getSimpleName();

    public static final String API_URL = "https://api.twitch.tv";
    public static TwitchRestService api;

    private static final String RESPONSE_TYPE = "token";
    private static final String DEV_ACCESS_TOKEN = "j5wciy6vg3ze76cv5wzmzj5uqf1gm4p";
    private static final String REDIRECT_URL = "http://localhost";
    private static final String SCOPE = "chat_login user_follows_edit user_read";

    public static final String LOGIN_URL = "https://api.twitch.tv/kraken/oauth2/authorize"
            + "?response_type=" + RESPONSE_TYPE
            + "&client_id=" + DEV_ACCESS_TOKEN
            + "&redirect_uri=" + REDIRECT_URL
            + "&scope=" + SCOPE;

    public interface TwitchRestService {
        @GET("/kraken/user")
        Call<UserObject> getUser(@Header("Authorization") String accessToken);
    }


    static {
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request newRequest;

                newRequest = request.newBuilder()
                        .addHeader("Accept", "application/vnd.twitchtv.v3+json")
                        .build();

                return chain.proceed(newRequest);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        api = retrofit.create(TwitchRestService.class);
    }
}
