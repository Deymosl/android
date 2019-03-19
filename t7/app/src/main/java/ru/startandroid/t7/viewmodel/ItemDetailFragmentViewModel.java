package ru.startandroid.t7.viewmodel;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ru.startandroid.t7.R;
import ru.startandroid.t7.model.App;
import ru.startandroid.t7.model.DBHelper;
import ru.startandroid.t7.view.ItemDetailFragment;
import com.squareup.picasso.Picasso;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

import static ru.startandroid.t7.model.Utils.LOG_TAG;

public class ItemDetailFragmentViewModel {
    public static final String ITEM_ID = "item_id";
    private static DBHelper helper;

    public static void onCreate(ItemDetailFragment act) {
        act.compositeDisposable = new CompositeDisposable();
        assert act.getArguments() != null;
        if (act.getArguments().containsKey(ITEM_ID)) {
            act.item = act.getArguments().getParcelable(ITEM_ID);
        }
    }

    public static View onCreateView(ItemDetailFragment act, @NonNull LayoutInflater inflater, ViewGroup container) {
        act.rootView = inflater.inflate(R.layout.item_detail, container, false);

        ((TextView) act.rootView.findViewById(R.id.item_detail)).setText(act.item.description);

        return act.rootView;
    }

    public static void onActivitiyCreated(final ItemDetailFragment act) {
        helper = App.helper;
        act.favButton = act.rootView.findViewById(R.id.favButton);
        act.compositeDisposable.add(helper.check(act.item.description, act.item.URL).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean checkres) {
                if (!checkres) {
                    act.favButton.setText("Add");
                    act.added = false;
                } else {
                    act.favButton.setText("Delete");
                    act.added = true;
                }
            }
        }));

        act.favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (act.added) {
                    act.favButton.setText("Add");
                    helper.delete(act.item.description, act.item.URL);
                } else {
                    act.favButton.setText("Delete");
                    helper.add(act.item.description, act.item.URL);
                }
                act.added = !act.added;
            }
        });
        Picasso.get().load(act.item.URL).into((ImageView) act.rootView.findViewById(R.id.itemDetailImageView));
    }
}
