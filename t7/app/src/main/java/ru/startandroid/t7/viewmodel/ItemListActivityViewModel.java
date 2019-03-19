package ru.startandroid.t7.viewmodel;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import ru.startandroid.t7.R;
import ru.startandroid.t7.model.App;
import ru.startandroid.t7.model.DBHelper;
import ru.startandroid.t7.model.ImageData;
import ru.startandroid.t7.model.MyAdapter;
import ru.startandroid.t7.view.ItemListActivity;
import com.fasterxml.jackson.databind.JsonNode;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static ru.startandroid.t7.model.Utils.LOG_TAG;
import static java.lang.Math.min;

public class ItemListActivityViewModel {
    private static String dataKey = "ru.startandroid.t7.dataKey";
    private static DBHelper helper;

    public static void onCreate(final ItemListActivity act, Bundle savedInstanceState) {
        act.setContentView(R.layout.activity_item_list);
        if (act.findViewById(R.id.item_detail_container) != null) {
            act.mTwoPane = true;
        }
        act.compositeDisposable = new CompositeDisposable();
        helper = App.helper;
        act.showFavButton = act.findViewById(R.id.showFavButton);
        act.showFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                act.compositeDisposable.add(helper.getData().subscribe(new Consumer<ImageData[]>() {
                    @Override
                    public void accept(ImageData[] imageData) {
                        setupRecyclerView(act, imageData);
                    }
                }));
            }
        });
        act.searchButton = act.findViewById(R.id.searchButton);
        act.searchText = act.findViewById(R.id.searchText);
        act.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                act.compositeDisposable.add(App.vkApi.getData(act.searchText.getText().toString(), 100, "5.92", "169a4327169a4327169a43272716f26e1b1169a169a43274adf06531ceabf9ce993e0d7")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<JsonNode>() {
                                       @Override
                                       public void accept(JsonNode root) {
                                           JsonNode items = root.path("response").path("items");
                                           act.data = new ImageData[items.size()];
                                           for (int i = 0; i < items.size(); i++) {
                                               JsonNode images = items.path(i).path("sizes");
                                               JsonNode best = items.path(i).path("sizes").path(0);
                                               for (JsonNode id : images)
                                                   if (id.path("width").asInt() * id.path("height").asInt() >
                                                           best.path("width").asInt() * best.path("height").asInt())
                                                       best = id;
                                               act.data[i] = new ImageData(i,
                                                       best.path("url").asText(),
                                                       items.path(i).path("text").asText().substring(0, min(40, items.path(i).path("text").asText().length())));
                                           }
                                           setupRecyclerView(act, act.data);
                                       }
                                   }
                        ));
            }
        });
        if (savedInstanceState != null && savedInstanceState.getParcelableArray(dataKey) != null) {
            setupRecyclerView(act, (ImageData[]) savedInstanceState.getParcelableArray(dataKey));
        }
    }

    public static void onSaveInstanceState(final ItemListActivity act, Bundle outState) {
        if (act.data != null) {
            outState.putParcelableArray(dataKey, act.data);
        }
    }

    private static void setupRecyclerView(ItemListActivity act, ImageData[] _data) {
        act.data = _data;
        act.recyclerView = act.findViewById(R.id.item_list);
        act.recyclerView.setAdapter(new MyAdapter(act, act.data, act.mTwoPane));
    }
}
