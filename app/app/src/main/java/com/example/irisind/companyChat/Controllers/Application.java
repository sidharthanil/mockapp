package com.example.irisind.companyChat.Controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.http.RealResponseBody;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by irisind on 2/5/17.
 */

public class Application extends android.app.Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.


    private static final String COOKIE_PREF = "cookie_cache";
    private static final String TAG = "Application";
    private static Object object;
    private PersistentCookieJar cookieJar;
    private Retrofit retrofit;
    private APIStructure service;
    private Session session;


    public static void transfer(Object object) {
        Application.object = object;
    }

    public static Object receive() {
        Object ret = object;
        object = null;
        return ret;
    }

    public static SharedPreferences getSharedPrefrence(Context context) {

        SharedPreferences pref = context.getSharedPreferences("CompanyChat", Context.MODE_PRIVATE);

        return pref;
    }

    public static void saveToSharedPreferences(Context context, String phone, String userType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("CompanyChat", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phoneNumber", phone);
        editor.putString("userType", userType);
        editor.apply();
    }

    public void clearSharedPreferences(Context context) {
        SharedPreferences preferences = getSharedPrefrence(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().apply();

    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        Log.e(TAG, "Application created");
        cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(this));
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)

                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        String reqstring = request.body() == null ? "null" : request.body().contentType().toString() + "hhh" + request.body().contentLength();
                        Log.e("request", reqstring);
                        Response response = chain.proceed(chain.request()); // Do anything with response here
                        String string = response.body().string();
                        Headers headers = response.headers();
                        InputStream is = new ByteArrayInputStream(string.getBytes());
                        Buffer buffer = new Buffer();
                        buffer.readFrom(is);
                        Log.e("response10", string);
                        return response.newBuilder().body(new RealResponseBody(headers, buffer)).build();
                    }
                })
                .build();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.160:3000")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        service = retrofit.create(APIStructure.class);


        session = Session.createInstance(Application.this);


    }

    public APIStructure getApi() {
//        loadConstants();
        return service;
    }

    public void clearCookies() {
        cookieJar.clear();
        cookieJar.clearSession();
    }
}
