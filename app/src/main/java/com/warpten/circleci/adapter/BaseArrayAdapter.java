package com.warpten.circleci.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

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
        notifyItemInserted(list.indexOf(item));
    }

    public void addAll(List<T> items) {
        final int startIndex = list.size();
        list.addAll(items);
        notifyItemRangeInserted(startIndex, items.size());
    }

    public void remove(T item) {
        final int position = list.indexOf(item);
        list.remove(item);
        notifyItemRemoved(position);
    }

    public void clear() {
        final int size = list.size();
        list.clear();
        notifyDataSetChanged();
    }
}
