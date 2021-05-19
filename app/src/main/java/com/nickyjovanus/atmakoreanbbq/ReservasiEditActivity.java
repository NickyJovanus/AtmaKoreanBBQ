package com.nickyjovanus.atmakoreanbbq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nickyjovanus.atmakoreanbbq.API.DetailPesananAPI;
import com.nickyjovanus.atmakoreanbbq.API.ReservasiAPI;
import com.nickyjovanus.atmakoreanbbq.adapter.AdapterDetail;
import com.nickyjovanus.atmakoreanbbq.adapter.RecyclerViewAdapterReservasi;
import com.nickyjovanus.atmakoreanbbq.database.Detail;
import com.nickyjovanus.atmakoreanbbq.database.Menu;
import com.nickyjovanus.atmakoreanbbq.database.Reservasi;
import com.nickyjovanus.atmakoreanbbq.dialog.AddDetailDialog;
import com.nickyjovanus.atmakoreanbbq.retrofit.ApiClient;
import com.nickyjovanus.atmakoreanbbq.retrofit.ApiInterface;
import com.nickyjovanus.atmakoreanbbq.retrofit.MenuResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.GET;

public class ReservasiEditActivity extends AppCompatActivity implements OnRefreshViewListener {

    private RecyclerView recyclerView;
    private AdapterDetail adapter;
    private List<Detail> detailList;
    public List<Menu> menuList;
    private SharedPreferences sp;
    String noMeja, tanggalReservasi, sesiReservasi;
    TextView tvNoMeja, tvTanggalReservasi, tvSesiReservasi;
    Button addButton, deleteButton, checkoutButton;
    public int idReservasi, idPesanan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservasi_edit);
        sp = new SharedPreferences(ReservasiEditActivity.this);

        idReservasi = getIntent().getIntExtra("idReservasi", -1);
        noMeja = getIntent().getStringExtra("noMeja");
        tanggalReservasi = getIntent().getStringExtra("tanggalReservasi");
        sesiReservasi = getIntent().getStringExtra("sesiReservasi");
        idPesanan = getIntent().getIntExtra("idPesanan", -1);
        sp.setIdPesanan(idPesanan);

        tvNoMeja = findViewById(R.id.noMeja);
        tvTanggalReservasi = findViewById(R.id.tanggalPesanan);
        tvSesiReservasi = findViewById(R.id.sesiReservasi);

        tvNoMeja.setText(noMeja);
        tvTanggalReservasi.setText(tanggalReservasi);
        tvSesiReservasi.setText(sesiReservasi);

        checkoutButton = findViewById(R.id.checkoutBtn);

        addButton = findViewById(R.id.addDetailBtn);

        deleteButton = findViewById(R.id.deleteBtn);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ReservasiEditActivity.this);
                builder.setTitle("Delete Confirmation");
                builder.setMessage("Do you want to delete this order?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteData(idReservasi);
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

        detailList = new ArrayList<>();
        menuList = new ArrayList<>();
        getDetail();
        getMenu();

        loadAdapter(detailList);
    }

    public void deleteData(int idDelete) {
        final ProgressDialog progressDialog = new ProgressDialog(ReservasiEditActivity.this);
        progressDialog.setMessage("Deleting...");
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(ReservasiEditActivity.this);
        StringRequest stringRequest = new StringRequest(DELETE, ReservasiAPI.DELETE + idDelete,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject data = obj.getJSONObject("data");
                            Toast.makeText(ReservasiEditActivity.this, "Data Successfully Deleted.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ReservasiEditActivity.this,ReservationListActivity.class));
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
                    Toast.makeText(ReservasiEditActivity.this, "Data Successfully Deleted.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ReservasiEditActivity.this,ReservationListActivity.class));

                } catch (JSONException e) {
                    String body;
                    try {
                        body = new String(error.networkResponse.data,"UTF-8");
                        JSONObject data = new JSONObject(body);
                        String message = data.optString("message");
                        Toast.makeText(ReservasiEditActivity.this, message, Toast.LENGTH_SHORT).show();
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

    }

    @Override
    public void refreshView(){
        getDetail();
    }

    public void loadAdapter(List<Detail> details) {
        recyclerView = findViewById(R.id.rv_detail);
        adapter = new AdapterDetail(this, details);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void getMenu() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MenuResponse> call = apiService.getAllMenu("data");
        call.enqueue(new Callback<MenuResponse>() {
            @Override
            public void onResponse(Call<MenuResponse> call, retrofit2.Response<MenuResponse> response) {
                if (response.isSuccessful()) {
                    menuList = response.body().getMenus();
                    addButton.setClickable(true);
                    addButton.setBackgroundTintList(ContextCompat.getColorStateList(ReservasiEditActivity.this, R.color.red));
                    addButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AddDetailDialog addDetailDialog = new AddDetailDialog(ReservasiEditActivity.this, menuList);
                            addDetailDialog.show();

                            addDetailDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    getDetail();
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    });
                } else {
                }
            }

            @Override
            public void onFailure(Call<MenuResponse> call, Throwable t) {
                Toast.makeText(ReservasiEditActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getDetail() {
        RequestQueue queue = Volley.newRequestQueue(ReservasiEditActivity.this);

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, DetailPesananAPI.SHOW
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    if(!detailList.isEmpty())
                        detailList.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        if(jsonObject.optString("id_pesanan").equals(String.valueOf(idPesanan))) {

                            int idDetail = jsonObject.optInt("id_detail_pesanan");
                            int jumlahItem = jsonObject.optInt("jumlah_item");
                            double hargaItem = jsonObject.optDouble("harga_item");
                            String namaMenu = jsonObject.optString("nama_menu");

                            Detail detail = new Detail(idDetail, namaMenu, jumlahItem, hargaItem);
                            detailList.add(detail);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                }
                checkoutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ReservasiEditActivity.this,CheckoutActivity.class);
                        intent.putExtra("idReservasi",Integer.valueOf(idReservasi));
                        intent.putExtra("noMeja",String.valueOf(noMeja));
                        intent.putExtra("tanggalReservasi",String.valueOf(tanggalReservasi));
                        intent.putExtra("sesiReservasi",String.valueOf(sesiReservasi));
                        intent.putExtra("idPesanan",Integer.valueOf(idPesanan));
                        startActivity(intent);
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<>();

                headers.put("Content-Type","application/x-www-form-urlencoded");
                return headers;
            }
        };
        queue.add(stringRequest);
    }


    public void reservasiList(View view){
        startActivity(new Intent(ReservasiEditActivity.this,ReservationListActivity.class));
    }
}