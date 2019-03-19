package ru.startandroid.myapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";
    ImageLoader.ImageLoaderBinder binder = null;
    ServiceConnection serviceConnection = null;
    View imageDir = null;
    private ImageData item;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binder = (ImageLoader.ImageLoaderBinder) service;
                binder.setCallback(new ImageLoader.Listener() {
                    @Override
                    public void onSuccess(Bitmap img) {
                        ((ImageView) imageDir.findViewById(R.id.item_image)).setImageBitmap(img);
                    }
                });
                binder.setItem(item);
                binder.start();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            item = getArguments().getParcelable(ARG_ITEM_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        imageDir = inflater.inflate(R.layout.item_detail, container, false);

        ((TextView) imageDir.findViewById(R.id.item_detail)).setText(item.description);
        return imageDir;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent ImageLoaderIntent = new Intent(getActivity(), ImageLoader.class);
        ImageLoaderIntent.putExtra(ARG_ITEM_ID, item);
        getActivity().bindService(ImageLoaderIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        getActivity().startService(ImageLoaderIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(serviceConnection);
    }
}
