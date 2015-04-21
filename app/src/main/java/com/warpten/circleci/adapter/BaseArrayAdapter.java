package com.warpten.circleci.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by bhaskar on 15-04-20.
 */
public abstract class BaseArrayAdapter<T extends Object, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {

    private ArrayList<T> list = new ArrayList<>();

    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void add(T item) {
        list.add(item);
        notifyItemInserted(list.size());
    }

    public void remove(T item) {
        int position = list.indexOf(item);
        list.remove(item);
        notifyItemRemoved(position);
    }

    public void clear() {
        int size = list.size();
        list.clear();
        notifyItemRangeRemoved(0, size);
    }
}
