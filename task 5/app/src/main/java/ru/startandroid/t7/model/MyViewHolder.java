package ru.startandroid.t7.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ru.startandroid.t7.R;

class MyViewHolder extends RecyclerView.ViewHolder {
    final TextView mIdView;
    final TextView mContentView;

    MyViewHolder(View view) {
        super(view);
        mIdView = view.findViewById(R.id.id_text);
        mContentView = view.findViewById(R.id.content);
    }
}