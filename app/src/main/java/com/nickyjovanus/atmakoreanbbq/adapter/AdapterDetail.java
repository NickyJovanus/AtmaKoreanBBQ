package com.nickyjovanus.atmakoreanbbq.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nickyjovanus.atmakoreanbbq.API.DetailPesananAPI;
import com.nickyjovanus.atmakoreanbbq.AddReservasiActivity;
import com.nickyjovanus.atmakoreanbbq.BR;
import com.nickyjovanus.atmakoreanbbq.OnRefreshViewListener;
import com.nickyjovanus.atmakoreanbbq.R;
import com.nickyjovanus.atmakoreanbbq.ReservasiEditActivity;
import com.nickyjovanus.atmakoreanbbq.ReservationListActivity;
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

public class AdapterDetail extends RecyclerView.Adapter<AdapterDetail.DetailHolder> {
    private Context context;
    private List<Detail> result;
    private OnRefreshViewListener mRefreshListener;

    public AdapterDetail(Context context, List<Detail> result) {
        this.context   = context;
        this.result    = result;
        mRefreshListener = (OnRefreshViewListener)context;
    }

    @NonNull
    @Override
    public DetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        ItemDetailBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_detail, parent, false);
        return new DetailHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final DetailHolder holder, int position) {
        final Detail detail = result.get(position);
        holder.bind(detail);

        NumberFormat formatter = new DecimalFormat("#,###.00");
        String hargaItem = formatter.format(detail.getHargaItem());
        holder.harga.setText(hargaItem);

        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.app_name);
                builder.setMessage("Do you want to delete this menu?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final ProgressDialog progressDialog = new ProgressDialog(context);
                        progressDialog.setMessage("Deleting...");
                        progressDialog.show();
                        RequestQueue queue = Volley.newRequestQueue(context);
                        StringRequest stringRequest = new StringRequest(DELETE, DetailPesananAPI.DELETE + detail.getIdDetail(),
                                new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    JSONObject data = obj.getJSONObject("data");
                                    Toast.makeText(context, "Menu Successfully Deleted.", Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                progressDialog.dismiss();
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                try {
                                    String responseBody = new String(error.networkResponse.data, "utf-8");
                                    JSONObject data = new JSONObject(responseBody);
                                    String errmessage = data.optString("message");
                                    Toast.makeText(context, "Menu Successfully Deleted.", Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                    mRefreshListener.refreshView();

                                } catch (JSONException e) {
                                    String body;
                                    try {
                                        body = new String(error.networkResponse.data,"UTF-8");
                                        JSONObject data = new JSONObject(body);
                                        String message = data.optString("message");
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                        notifyDataSetChanged();
                                        e.printStackTrace();
                                    } catch (UnsupportedEncodingException | JSONException unsupportedEncodingException) {
                                        unsupportedEncodingException.printStackTrace();
                                    }
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                progressDialog.dismiss();
                            }
                        }) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                HashMap<String, String> headers = new HashMap<>();
                                headers.put("Content-Type", "application/x-www-form-urlencoded");
                                return headers;
                            }
                        };
                        queue.add(stringRequest);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
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

