package com.nickyjovanus.atmakoreanbbq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nickyjovanus.atmakoreanbbq.API.DetailPesananAPI;
import com.nickyjovanus.atmakoreanbbq.API.ReservasiAPI;
import com.nickyjovanus.atmakoreanbbq.adapter.AdapterCheckout;
import com.nickyjovanus.atmakoreanbbq.adapter.AdapterDetail;
import com.nickyjovanus.atmakoreanbbq.database.Detail;
import com.nickyjovanus.atmakoreanbbq.database.Menu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.PUT;

public class CheckoutActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterCheckout adapter;
    private List<Detail> detailList;
    public List<Menu> menuList;
    private SharedPreferences sp;
    String noMeja, tanggalReservasi, sesiReservasi;
    TextView tvNoMeja, tvTanggalReservasi, tvSesiReservasi, tvIdPesanan, tvTotalMenu, tvTotalItem, tvTotalHarga;
    public int idReservasi, idPesanan, totalM = 0, totalI = 0, totalH = 0;
    Button btnCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        idReservasi = getIntent().getIntExtra("idReservasi", -1);
        noMeja = getIntent().getStringExtra("noMeja");
        tanggalReservasi = getIntent().getStringExtra("tanggalReservasi");
        sesiReservasi = getIntent().getStringExtra("sesiReservasi");
        idPesanan = getIntent().getIntExtra("idPesanan", -1);

        tvNoMeja = findViewById(R.id.noMeja);
        tvIdPesanan = findViewById(R.id.idPesanan);
        tvTanggalReservasi = findViewById(R.id.tanggalPesanan);
        tvSesiReservasi = findViewById(R.id.sesiReservasi);
        tvTotalMenu = findViewById(R.id.totalMenu);
        tvTotalItem = findViewById(R.id.totalItem);
        tvTotalHarga = findViewById(R.id.totalHarga);

        btnCheckout = findViewById(R.id.confirm_button);

        tvNoMeja.setText(noMeja);
        tvTanggalReservasi.setText(tanggalReservasi);
        tvSesiReservasi.setText(sesiReservasi);
        NumberFormat formatPesanan = new DecimalFormat("#######");
        String idP = formatPesanan.format(idPesanan);
        tvIdPesanan.setText(idP);

        detailList = new ArrayList<>();
        getDetail();

        loadAdapter(detailList);

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkout(idReservasi);
            }
        });
    }

    public void checkout(int idReservasi) {
        final ProgressDialog progressDialog = new ProgressDialog(CheckoutActivity.this);
        progressDialog.setMessage("Processing...");
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(CheckoutActivity.this);

        final JsonObjectRequest stringRequest = new JsonObjectRequest(PUT, ReservasiAPI.PAY + idReservasi
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                startActivity(new Intent(CheckoutActivity.this, CheckSplashActivity.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(CheckoutActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
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


    public void getDetail() {
        RequestQueue queue = Volley.newRequestQueue(CheckoutActivity.this);

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
                            totalM++;
                            totalI += jumlahItem;
                            totalH += hargaItem;
                        }
                    }
                    tvTotalMenu.setText("Total Menu: " + String.valueOf(totalM));
                    tvTotalItem.setText("x" + String.valueOf(totalI));
                    NumberFormat formatter = new DecimalFormat("#,###.00");
                    String hargaItem = formatter.format(totalH);
                    tvTotalHarga.setText("Rp. " + hargaItem);

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


    public void loadAdapter(List<Detail> details) {
        recyclerView = findViewById(R.id.rv_detail);
        adapter = new AdapterCheckout(this, details);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}