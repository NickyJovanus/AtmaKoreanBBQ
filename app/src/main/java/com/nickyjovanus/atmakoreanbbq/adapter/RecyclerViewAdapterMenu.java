package com.nickyjovanus.atmakoreanbbq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class RecyclerViewAdapterMenu extends RecyclerView.Adapter<RecyclerViewAdapterMenu.MenuViewHolder>  {
    private Context context;
    private List<Menu> result;

    public RecyclerViewAdapterMenu(List<Menu> result) {
        this.result = result;
    }

    public RecyclerViewAdapterMenu(Context context, List<Menu> result) {
        this.context   = context;
        this.result    = result;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
//        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
//        context = viewGroup.getContext();
//        view = layoutInflater.inflate(R.layout.item_menu, viewGroup, false);

        ItemMenuBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_menu, parent, false);
        return new MenuViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MenuViewHolder holder, int position) {
        final Menu menu = result.get(position);
        holder.bind(menu);

        NumberFormat formatter = new DecimalFormat("#,###");
        holder.txtNama.setText(menu.getNamaMenu());
        holder.txtHarga.setText(String.valueOf(menu.getHargaMenu()));
        holder.txtDeskripsi.setText(menu.getDeskripsiMenu());
        holder.txtKategori.setText(menu.getKategoriMenu());
        holder.txtUnit.setText(menu.getUnitMenu());

        Glide.with(context)
                .load(menu.getImgURL())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.ivFoto);
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder{
        private ItemMenuBinding itemMenuBinding;
        private TextView txtNama, txtHarga, txtKategori, txtUnit, txtDeskripsi;
        private ImageView ivFoto;

        public MenuViewHolder(ItemMenuBinding itemMenuBinding) {
            super(itemMenuBinding.getRoot());
            this.itemMenuBinding = itemMenuBinding;
        }

        public MenuViewHolder(View itemView){
            super(itemView);
            txtNama       = itemView.findViewById(R.id.namaMenu);
            txtHarga      = itemView.findViewById(R.id.hargaMenu);
            txtKategori   = itemView.findViewById(R.id.category);
            txtUnit       = itemView.findViewById(R.id.unit);
            txtDeskripsi  = itemView.findViewById(R.id.deskripsi);
            ivFoto        = itemView.findViewById(R.id.iv_fotoBarang);
        }

        public void bind(Object obj) {
            itemMenuBinding.setVariable(com.nickyjovanus.atmakoreanbbq.BR.menu, obj);
            itemMenuBinding.executePendingBindings();
            txtNama       = itemView.findViewById(R.id.namaMenu);
            txtHarga      = itemView.findViewById(R.id.hargaMenu);
            txtKategori   = itemView.findViewById(R.id.category);
            txtUnit       = itemView.findViewById(R.id.unit);
            txtDeskripsi  = itemView.findViewById(R.id.deskripsi);
            ivFoto        = itemView.findViewById(R.id.iv_fotoBarang);

        }
    }
}
