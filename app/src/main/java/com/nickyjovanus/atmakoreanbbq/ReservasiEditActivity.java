package com.nickyjovanus.atmakoreanbbq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nickyjovanus.atmakoreanbbq.API.DetailPesananAPI;
import com.nickyjovanus.atmakoreanbbq.API.ReservasiAPI;
import com.nickyjovanus.atmakoreanbbq.adapter.AdapterDetail;
import com.nickyjovanus.atmakoreanbbq.adapter.RecyclerViewAdapterReservasi;
import com.nickyjovanus.atmakoreanbbq.database.Detail;
import com.nickyjovanus.atmakoreanbbq.database.Reservasi;
import com.nickyjovanus.atmakoreanbbq.dialog.AddDetailDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.GET;

public class ReservasiEditActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterDetail adapter;
    private List<Detail> detailList;
    private SharedPreferences sp;
    String noMeja, tanggalReservasi, sesiReservasi;
    TextView tvNoMeja, tvTanggalReservasi, tvSesiReservasi;
    Button addButton;
    public int idPesanan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservasi_edit);
        sp = new SharedPreferences(ReservasiEditActivity.this);

        noMeja = getIntent().getStringExtra("gambar");
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

        addButton = findViewById(R.id.addDetailBtn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDetailDialog addDetailDialog = new AddDetailDialog(ReservasiEditActivity.this);
                addDetailDialog.show();

                addDetailDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        getDetail();
                    }
                });
            }
        });

        detailList = new ArrayList<>();
        getDetail();

        loadAdapter(detailList);
    }

    public void loadAdapter(List<Detail> details) {
        recyclerView = findViewById(R.id.rv_detail);
        adapter = new AdapterDetail(this, details);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
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