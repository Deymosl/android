package ru.startandroid.myapplication;

import android.os.AsyncTask;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MyAsyncTask extends AsyncTask<Void, Void, ImageData[]> {
    private WeakReference<Listener> listener;

    interface Listener {
        void onSuccess(ImageData[] data);
    }

    void setListener(Listener listener) {
        this.listener = new WeakReference<>(listener);
    }

    @Override
    protected ImageData[] doInBackground(Void... voids) {
        ObjectMapper mapper = new ObjectMapper();
        int n = 50;
        ImageData[] data = new ImageData[n];
        try {
            URL url = new URL("https://api.vk.com/method/photos.search?q=папич&count=50&v=5.92&access_token=169a4327169a4327169a43272716f26e1b1169a169a43274adf06531ceabf9ce993e0d7");
            URLConnection connection = url.openConnection();
            connection.connect();
            JsonNode items = mapper.readTree(connection.getInputStream()).path("response").path("items");
            for (int i = 0; i < n; i++) {
                JsonNode pepega = items.path(i).path("sizes").path(0);
                data[i] = new ImageData(i,
                        pepega.path("url").asText(),
                        items.path(i).path("text").asText().substring(0, Math.min(40, items.path(i).path("text").asText().length())));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(ImageData[] data) {
        listener.get().onSuccess(data);
    }
}