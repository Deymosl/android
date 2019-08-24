package ru.startandroid.t7.model;

import android.app.Application;
import android.util.Log;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static ru.startandroid.t7.model.Utils.LOG_TAG;

public class App extends Application {
    public static DBHelper helper;
    public static NetworkInterface vkApi;

    @Override
    public void onCreate() {
        super.onCreate();
       helper = new DBHelper(this);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.vk.com").addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())).addConverterFactory(JacksonConverterFactory.create()).build();
        vkApi = retrofit.create(NetworkInterface.class);
    }
}
