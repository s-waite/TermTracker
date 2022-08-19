package com.sam.termtracker.UI;

import android.content.Context;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<T> items;

    public abstract RecyclerView.ViewHolder setViewHolder(ViewGroup parent);

    public abstract void onBindData(RecyclerView.ViewHolder holder, T val);

    public RecyclerAdapter(Context context, ArrayList<T> items){
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = setViewHolder(parent);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        onBindData(holder,items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItems( ArrayList<T> savedCardItemz){
        items = savedCardItemz;
        this.notifyDataSetChanged();
    }

    public T getItem(int position){
        return items.get(position);
    }
}
