package ru.startandroid.t7.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

import ru.startandroid.t7.model.ImageData;
import ru.startandroid.t7.viewmodel.ItemListActivityViewModel;

import io.reactivex.disposables.CompositeDisposable;

public class ItemListActivity extends AppCompatActivity {

    public Button searchButton;
    public Button showFavButton;
    public EditText searchText;
    public RecyclerView recyclerView;
    public boolean mTwoPane;
    public ImageData[] data;
    public CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ItemListActivityViewModel.onCreate(this, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ItemListActivityViewModel.onSaveInstanceState(this, outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
