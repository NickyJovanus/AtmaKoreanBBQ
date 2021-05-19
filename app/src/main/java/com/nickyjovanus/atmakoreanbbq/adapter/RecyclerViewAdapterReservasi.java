package com.nickyjovanus.atmakoreanbbq.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nickyjovanus.atmakoreanbbq.BR;
import com.nickyjovanus.atmakoreanbbq.R;
import com.nickyjovanus.atmakoreanbbq.ReservasiEditActivity;
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

        holder.positionId.setText(String.valueOf(position+1));

        holder.mParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReservasiEditActivity.class);
                intent.putExtra("idReservasi",Integer.valueOf(reservasi.idReservasi));
                intent.putExtra("noMeja",String.valueOf(reservasi.noMeja));
                intent.putExtra("tanggalReservasi",String.valueOf(reservasi.tanggalReservasi));
                intent.putExtra("sesiReservasi",String.valueOf(reservasi.sesiReservasi));
                intent.putExtra("idPesanan",Integer.valueOf(reservasi.idPesanan));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class ResViewHolder extends RecyclerView.ViewHolder{
        private ItemReservasiBinding itemResBinding;
        private ConstraintLayout mParent;
        private TextView positionId;

        public ResViewHolder(ItemReservasiBinding itemResBinding) {
            super(itemResBinding.getRoot());
            this.itemResBinding = itemResBinding;
        }

        public void bind(Object obj) {
            itemResBinding.setVariable(BR.res, obj);
            itemResBinding.executePendingBindings();
            mParent = itemView.findViewById(R.id.mParent);
            positionId = itemView.findViewById(R.id.position_id);
        }

        public ItemReservasiBinding getItemMenuBinding() {
            return itemResBinding;
        }
    }

}
