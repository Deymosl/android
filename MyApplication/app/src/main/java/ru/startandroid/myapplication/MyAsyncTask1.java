package ru.startandroid.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;

public class MyAsyncTask1 extends AsyncTask<Object, Void, Bitmap> {
    private WeakReference<ImageLoader.Listener> weakReference;

    void setListener(ImageLoader.Listener ao) {
        weakReference = new WeakReference<>(ao);
    }

    @Override
    protected Bitmap doInBackground(Object... objs) {
        ImageData data = (ImageData) objs[0];
        File dir = (File) objs[1];
        LruCache<Integer, Bitmap> cache = (LruCache<Integer, Bitmap>) objs[2];
        File f = new File(dir, Integer.toString(data.id) + ".jpg");
        Bitmap image = null;
        try {
            if (!f.exists()) {
                URLConnection url;
                url = (new URL(data.url)).openConnection();
                url.connect();
                image = BitmapFactory.decodeStream(url.getInputStream());
                FileOutputStream stream = new FileOutputStream(f);
                image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            } else {
                image = BitmapFactory.decodeStream(new FileInputStream(f));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        cache.put(data.id, image);
        return image;
    }

    @Override
    protected void onPostExecute(Bitmap a) {
        super.onPostExecute(a);
        weakReference.get().onSuccess(a);
    }
}

