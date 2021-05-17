package com.nickyjovanus.atmakoreanbbq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nickyjovanus.atmakoreanbbq.BR;
import com.nickyjovanus.atmakoreanbbq.R;
import com.nickyjovanus.atmakoreanbbq.database.Reservasi;
import com.nickyjovanus.atmakoreanbbq.databinding.ItemReservasiBinding;

import java.util.List;

public class RecyclerViewAdapterReservasi extends RecyclerView.Adapter<RecyclerViewAdapterReservasi.ResViewHolder> {
    private Context context;
    private List<Reservasi> result;

    public RecyclerViewAdapterReservasi(Context context, List<Reservasi> result) {
        this.context   = context;
        this.result    = result;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterReservasi.ResViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        ItemReservasiBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_reservasi, parent, false);
        return new RecyclerViewAdapterReservasi.ResViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewAdapterReservasi.ResViewHolder holder, int position) {
        final Reservasi reservasi = result.get(position);
        holder.bind(reservasi);
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class ResViewHolder extends RecyclerView.ViewHolder{
        private ItemReservasiBinding itemResBinding;

        public ResViewHolder(ItemReservasiBinding itemResBinding) {
            super(itemResBinding.getRoot());
            this.itemResBinding = itemResBinding;
        }

        public void bind(Object obj) {
            itemResBinding.setVariable(BR.res, obj);
            itemResBinding.executePendingBindings();
        }

        public ItemReservasiBinding getItemMenuBinding() {
            return itemResBinding;
        }
    }

}
