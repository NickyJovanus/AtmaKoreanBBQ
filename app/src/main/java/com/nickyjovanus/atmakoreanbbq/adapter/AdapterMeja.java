package com.nickyjovanus.atmakoreanbbq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nickyjovanus.atmakoreanbbq.BR;
import com.nickyjovanus.atmakoreanbbq.R;
import com.nickyjovanus.atmakoreanbbq.database.Meja;
import com.nickyjovanus.atmakoreanbbq.databinding.ItemMejaBinding;

import java.util.List;

public class AdapterMeja extends RecyclerView.Adapter<AdapterMeja.MejaHolder> {
    private Context context;
    private List<Meja> result;

    public AdapterMeja(Context context, List<Meja> result) {
        this.context   = context;
        this.result    = result;
    }

    @NonNull
    @Override
    public AdapterMeja.MejaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        ItemMejaBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_meja, parent, false);
        return new AdapterMeja.MejaHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterMeja.MejaHolder holder, int position) {
        final Meja meja = result.get(position);
        holder.bind(meja);
    }


    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MejaHolder extends RecyclerView.ViewHolder{
        private ItemMejaBinding itemMejaBinding;

        public MejaHolder(ItemMejaBinding itemMejaBinding) {
            super(itemMejaBinding.getRoot());
            this.itemMejaBinding = itemMejaBinding;
        }

        public void bind(Object obj) {
            itemMejaBinding.setVariable(BR.res, obj);
            itemMejaBinding.executePendingBindings();
        }

        public ItemMejaBinding getItemMenuBinding() {
            return itemMejaBinding;
        }
    }
}
