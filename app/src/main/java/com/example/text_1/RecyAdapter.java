package com.example.text_1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by ${李昊男} on 2019/11/12.
 */

public class RecyAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<Bean.DataBean.DatasBean> list;

    public RecyAdapter(Context context, ArrayList <Bean.DataBean.DatasBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recy_item, parent, false);
        return new Item(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Item item= (Item) holder;
        item.tv.setText(list.get(position).getTitle());
        Glide.with(context).load(list.get(position).getEnvelopePic()).into(item.iv);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Item extends RecyclerView.ViewHolder{
        private ImageView iv;
        private TextView tv;
        public Item(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.item_image);
            tv = itemView.findViewById(R.id.item_tv);
        }
    }

}
