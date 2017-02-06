package com.example.ibanez_xiphos.itrextest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ibanez_xiphos.itrextest.R;
import com.loopj.android.image.SmartImageView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    private List<RecyclerItem> listItems;
    private Context mContext;

    public MyAdapter(List<RecyclerItem> listItems, Context mContext) {
        this.listItems = listItems;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        final RecyclerItem itemList = listItems.get(position);
        holder.image.setImageUrl(itemList.getImage());
        holder.txtName.setText(itemList.getName());
        holder.txtNameEng.setText(itemList.getNameEng());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public long getItemId(int position) {
        return listItems.get(position).getId();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public SmartImageView image;
        public TextView txtName;
        public TextView txtNameEng;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (SmartImageView) itemView.findViewById(R.id.image);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtNameEng = (TextView) itemView.findViewById(R.id.txtNameEng);
        }
    }
}
