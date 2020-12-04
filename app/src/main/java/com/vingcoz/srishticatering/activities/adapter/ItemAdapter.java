package com.vingcoz.srishticatering.activities.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vingcoz.srishticatering.R;
import com.vingcoz.srishticatering.activities.Items;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    public Context context;
    public List<Items> itemsList;
    Onclick onclick;

    public ItemAdapter(Context context, List<Items> itemsList,Onclick onclick) {
        this.context = context;
        this.itemsList = itemsList;
        this.onclick=onclick;
    }



    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_items,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        final Items data=itemsList.get(position);
        if (data.getItem_name() !=null) {
            holder.editText_time.setText(data.getItem_name());
        }
        holder.removeImg.setOnClickListener(v -> {
            itemsList.remove(position);
            notifyDataSetChanged();
        });


        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclick.onEvent(data,position);
            }
        });
    }

    public interface Onclick {
        void onEvent(Items model,int pos);
    }
    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView editText_time;
        ImageView removeImg;
        LinearLayout llItem;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            editText_time=(TextView)itemView.findViewById(R.id.text_slot);
            removeImg=itemView.findViewById(R.id.img_remove);
            llItem = itemView.findViewById(R.id.ll_item);
        }
    }
}
