package ru.startandroid.t7.viewmodel;

import android.os.Bundle;

import ru.startandroid.t7.R;
import ru.startandroid.t7.view.ItemDetailActivity;
import ru.startandroid.t7.view.ItemDetailFragment;

import static ru.startandroid.t7.viewmodel.ItemDetailFragmentViewModel.ITEM_ID;

public class ItemDetailActivityViewModel {
    public static void onCreate(ItemDetailActivity act, Bundle savedInstanceState){
        act.setContentView(R.layout.activity_item_detail);
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(ITEM_ID,
                    act.getIntent().getParcelableExtra(ITEM_ID));
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            act.getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit();
        }
    }
}
