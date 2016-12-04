package com.example.smartbusinesscard2;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 현욱 on 2016-12-03.
 */
public class AdapterThree extends RecyclerView.Adapter<AdapterThree.ViewHolder> {
    private ArrayList<MyData2> mDataset;
    ItemClick itemClick;
    public interface ItemClick {
        public void onClick(View view,int position);
    }

    //아이템 클릭시 실행 함수 등록 함수
    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        CardView cardview;

        public ViewHolder(View view) {
            super(view);
            mTextView = (TextView)view.findViewById(R.id.textView23);
            cardview = (CardView)itemView.findViewById(R.id.cardview_pos);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterThree(ArrayList<MyData2> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterThree.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.company_position_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int Position = position;
        MyData2 item = mDataset.get(position);
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset.get(position).pos.toString());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.onClick(v, Position);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

class MyData2{
    public String pos;
    public MyData2(String pos){
        this.pos = pos;
    }
}

