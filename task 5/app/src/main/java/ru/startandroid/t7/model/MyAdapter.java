package ru.startandroid.t7.model;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.startandroid.t7.R;
import ru.startandroid.t7.view.ItemDetailActivity;
import ru.startandroid.t7.view.ItemDetailFragment;
import ru.startandroid.t7.view.ItemListActivity;

import static ru.startandroid.t7.viewmodel.ItemDetailFragmentViewModel.ITEM_ID;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private final ItemListActivity mParentActivity;
    private final ImageData[] mValues;
    private final boolean mTwoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ImageData item = (ImageData) view.getTag();
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putParcelable(ITEM_ID, item);
                ItemDetailFragment fragment = new ItemDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, ItemDetailActivity.class);
                intent.putExtra(ITEM_ID, item);

                context.startActivity(intent);
            }
        }
    };

    public MyAdapter(ItemListActivity parent, ImageData[] data, boolean twoPane) {
        mValues = data;
        mParentActivity = parent;
        mTwoPane = twoPane;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_content, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.mIdView.setText(Integer.toString(position));
        holder.mContentView.setText(mValues[position].description);

        holder.itemView.setTag(mValues[position]);
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mValues.length;
    }
}