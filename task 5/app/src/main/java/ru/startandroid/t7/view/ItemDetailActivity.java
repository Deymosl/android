package ru.startandroid.t7.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ru.startandroid.t7.viewmodel.ItemDetailActivityViewModel;

public class ItemDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ItemDetailActivityViewModel.onCreate(this, savedInstanceState);
    }
}
