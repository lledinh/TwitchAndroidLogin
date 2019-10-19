package com.ledinh.twitch_login.rest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by Lam on 17/01/2016.
 */
public class Twitch {
    private static final String DEBUG_TAG = Twitch.class.getSimpleName();

    public static final String API_URL = "https://api.twitch.tv";
    public static TwitchRestService api;

    private static final String RESPONSE_TYPE = "token";
    private static final String DEV_ACCESS_TOKEN = "114fefeis8yf6cww3xo0fv2k7hykjtq";
    private static final String REDIRECT_URL = "http://localhost";
    private static final String SCOPE = "user_read chat:read chat:edit channel:moderate whispers:read whispers:edit";

    public static final String LOGIN_URL = "https://api.twitch.tv/kraken/oauth2/authorize"
            + "?response_type=" + RESPONSE_TYPE
            + "&client_id=" + DEV_ACCESS_TOKEN
            + "&redirect_uri=" + REDIRECT_URL
            + "&scope=" + SCOPE;

    public interface TwitchRestService {
        @GET("/helix/users")
        Call<UserJSON> getUser(@Header("Authorization") String accessToken);
        @GET("/helix/users")
        Call<ResponseBody> getUser2(@Header("Authorization") String accessToken);

        @GET("/kraken/games/top")
        Call<ResponseBody> getFeaturedGamesList(@Query("limit") int limit, @Query("offset") int offset);
    }


    static {
//        OkHttpClient client = new OkHttpClient();
//        client.interceptors().add(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request();
//                Request newRequest;
//
//                newRequest = request.newBuilder()
////                        .addHeader("Accept", "application/vnd.twitchtv.v5+json")
//                        .build();
//
//                return chain.proceed(newRequest);
//            }
//        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
//                .client(client)
                .build();
        api = retrofit.create(TwitchRestService.class);
    }
}
