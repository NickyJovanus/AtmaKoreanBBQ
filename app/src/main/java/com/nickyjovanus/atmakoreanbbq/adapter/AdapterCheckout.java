package com.nickyjovanus.atmakoreanbbq.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nickyjovanus.atmakoreanbbq.API.DetailPesananAPI;
import com.nickyjovanus.atmakoreanbbq.BR;
import com.nickyjovanus.atmakoreanbbq.OnRefreshViewListener;
import com.nickyjovanus.atmakoreanbbq.R;
import com.nickyjovanus.atmakoreanbbq.database.Detail;
import com.nickyjovanus.atmakoreanbbq.databinding.ItemDetailBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.DELETE;

public class AdapterCheckout extends RecyclerView.Adapter<AdapterCheckout.DetailHolder> {

    private Context context;
    private List<Detail> result;

    public AdapterCheckout(Context context, List<Detail> result) {
        this.context   = context;
        this.result    = result;
    }

    @NonNull
    @Override
    public AdapterCheckout.DetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        ItemDetailBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_detail, parent, false);
        return new AdapterCheckout.DetailHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterCheckout.DetailHolder holder, int position) {
        final Detail detail = result.get(position);
        holder.bind(detail);

        NumberFormat formatter = new DecimalFormat("#,###.00");
        String hargaItem = formatter.format(detail.getHargaItem());
        holder.harga.setText(hargaItem);
        holder.tvDelete.setVisibility(View.GONE);

    }


    @Override
    public int getItemCount() {
        return result.size();
    }

    public class DetailHolder extends RecyclerView.ViewHolder{
        private ItemDetailBinding itemDetailBinding;
        private TextView tvDelete, harga;

        public DetailHolder(ItemDetailBinding itemDetailBinding) {
            super(itemDetailBinding.getRoot());
            this.itemDetailBinding = itemDetailBinding;
        }

        public void bind(Object obj) {
            itemDetailBinding.setVariable(BR.detail, obj);
            itemDetailBinding.executePendingBindings();
            tvDelete = itemView.findViewById(R.id.tv_delete_detail);
            harga = itemView.findViewById(R.id.tv_hargaItem);
        }

        public ItemDetailBinding getItemMenuBinding() {
            return itemDetailBinding;
        }
    }
}
