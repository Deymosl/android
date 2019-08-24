package ru.startandroid.myapplication;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.util.LruCache;

import java.io.File;

public class ImageLoader extends Service {
    public Listener call;
    ImageData imageData;
    MyAsyncTask1 myAsyncTask1;
    File dir = null;
    int cacheSize = 1024 * 1024 * 8;
    LruCache<Integer, Bitmap> cache = new LruCache<>(cacheSize);
    interface Listener {
        void onSuccess(Bitmap img);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dir = getDir("internalStorage", MODE_PRIVATE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    void start() {
        Bitmap img = cache.get(imageData.id);
        if (img == null) {
            myAsyncTask1.execute(imageData, dir, cache, call);
        } else {
            call.onSuccess(img);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ImageLoaderBinder(this);
    }

    class ImageLoaderBinder extends Binder {
        ImageLoader service;

        ImageLoaderBinder(Service service) {
            this.service = (ImageLoader) service;
        }

        void setCallback(Listener back) {
            service.myAsyncTask1 = new MyAsyncTask1();
            service.myAsyncTask1.setListener(back);
            service.call = back;
        }

        void setItem(ImageData item) {
            service.imageData = item;
        }

        void start() {
            service.start();
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        call = null;
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
