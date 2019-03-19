package ru.startandroid.t7.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.startandroid.t7.model.ImageData;
import ru.startandroid.t7.viewmodel.ItemDetailFragmentViewModel;

import io.reactivex.disposables.CompositeDisposable;

public class ItemDetailFragment extends Fragment {
    public View rootView;
    public Button favButton;
    public boolean added = false;
    public ImageData item;
    public CompositeDisposable compositeDisposable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ItemDetailFragmentViewModel.onCreate(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return ItemDetailFragmentViewModel.onCreateView(this, inflater, container);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ItemDetailFragmentViewModel.onActivitiyCreated(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
