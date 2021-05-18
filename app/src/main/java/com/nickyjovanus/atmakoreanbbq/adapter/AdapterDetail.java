package com.nickyjovanus.atmakoreanbbq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nickyjovanus.atmakoreanbbq.BR;
import com.nickyjovanus.atmakoreanbbq.R;
import com.nickyjovanus.atmakoreanbbq.database.Detail;
import com.nickyjovanus.atmakoreanbbq.databinding.ItemDetailBinding;

import java.util.List;

public class AdapterDetail extends RecyclerView.Adapter<AdapterDetail.DetailHolder> {
    private Context context;
    private List<Detail> result;

    public AdapterDetail(Context context, List<Detail> result) {
        this.context   = context;
        this.result    = result;
    }

    @NonNull
    @Override
    public DetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        ItemDetailBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_meja, parent, false);
        return new DetailHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final DetailHolder holder, int position) {
        final Detail detail = result.get(position);
        holder.bind(detail);
    }


    @Override
    public int getItemCount() {
        return result.size();
    }

    public class DetailHolder extends RecyclerView.ViewHolder{
        private ItemDetailBinding itemDetailBinding;

        public DetailHolder(ItemDetailBinding itemDetailBinding) {
            super(itemDetailBinding.getRoot());
            this.itemDetailBinding = itemDetailBinding;
        }

        public void bind(Object obj) {
            itemDetailBinding.setVariable(BR.res, obj);
            itemDetailBinding.executePendingBindings();
        }

        public ItemDetailBinding getItemMenuBinding() {
            return itemDetailBinding;
        }
    }
}

