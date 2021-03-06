package com.nickyjovanus.atmakoreanbbq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nickyjovanus.atmakoreanbbq.BR;
import com.nickyjovanus.atmakoreanbbq.database.Menu;
import com.nickyjovanus.atmakoreanbbq.R;
import com.nickyjovanus.atmakoreanbbq.databinding.ItemMenuBinding;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.stream.Collectors;

public class RecyclerViewAdapterMenu extends RecyclerView.Adapter<RecyclerViewAdapterMenu.MenuViewHolder> implements Filterable {
    private Context context;
    private List<Menu> result;
    private List<Menu> filteredDataList;

    public RecyclerViewAdapterMenu(Context context, List<Menu> result) {
        this.context   = context;
        this.result    = result;
        this.filteredDataList = result;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        ItemMenuBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_menu, parent, false);
        return new MenuViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MenuViewHolder holder, int position) {
        final Menu menu = filteredDataList.get(position);
        holder.bind(menu);
        NumberFormat formatter = new DecimalFormat("#,###.##");
        String hargaMenu = formatter.format(menu.getHargaMenu());
        holder.harga.setText(hargaMenu);
        if(menu.getHargaMenu() == 0) {
            holder.harga.setText("Free");
            holder.hargaPrefix.setText("");
        }

        if (!menu.getGambarMenu().equals("")) {
            Glide.with(context)
                    .load(menu.getGambarMenu())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .into(holder.ivFoto);
        } else {
            holder.ivFoto.setImageResource(R.drawable.logo);
        }
    }

    @Override
    public int getItemCount() {
        return filteredDataList.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder{
        private ItemMenuBinding itemMenuBinding;
        private TextView harga, hargaPrefix;
        private ImageView ivFoto;

        public MenuViewHolder(ItemMenuBinding itemMenuBinding) {
            super(itemMenuBinding.getRoot());
            this.itemMenuBinding = itemMenuBinding;
        }

        public void bind(Object obj) {
            itemMenuBinding.setVariable(BR.menu, obj);
            itemMenuBinding.executePendingBindings();
            ivFoto = itemView.findViewById(R.id.iv_fotoBarang);
            harga = itemView.findViewById(R.id.totalItem);
            hargaPrefix = itemView.findViewById(R.id.totalItemPrefix);
        }

        public ItemMenuBinding getItemMenuBinding() {
            return itemMenuBinding;
        }
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(final CharSequence charSequence) {

                filteredDataList = charSequence == null ? result :
                        result.stream().filter(data -> data.getNamaMenu().toLowerCase().contains(charSequence.toString().toLowerCase())).collect(Collectors.toList());

                FilterResults results = new FilterResults();
                results.values = filteredDataList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredDataList = (List<Menu>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
