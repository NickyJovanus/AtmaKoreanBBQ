package com.nickyjovanus.atmakoreanbbq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nickyjovanus.atmakoreanbbq.API.ReservasiAPI;
import com.nickyjovanus.atmakoreanbbq.adapter.RecyclerViewAdapterMenu;
import com.nickyjovanus.atmakoreanbbq.adapter.RecyclerViewAdapterReservasi;
import com.nickyjovanus.atmakoreanbbq.database.Reservasi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.GET;

public class ReservationListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapterReservasi adapter;
    private List<Reservasi> reservasiList;
    private SwipeRefreshLayout swipeRefresh;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_list);
        sp = new SharedPreferences(this);
        reservasiList = new ArrayList<>();
        getReservasi();

        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setRefreshing(true);

        loadAdapter(reservasiList);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getReservasi();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ReservationListActivity.this, HomeActivity.class));
    }

    public void addReservasi(View v) {
        startActivity(new Intent(ReservationListActivity.this, AddReservasiActivity.class));
    }

    public void loadAdapter(List<Reservasi> resList) {
        recyclerView = findViewById(R.id.rv_reservasi);
        adapter = new RecyclerViewAdapterReservasi(this, resList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void getReservasi() {
        RequestQueue queue = Volley.newRequestQueue(ReservationListActivity.this);

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, ReservasiAPI.SHOW + sp.getIdCustomer()
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                swipeRefresh.setRefreshing(false);
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    if(!reservasiList.isEmpty())
                        reservasiList.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        if(!jsonObject.optString("status_reservasi").equals("paid")) {

                            int idReservasi = jsonObject.optInt("id_reservasi");
                            String tanggalReservasi = jsonObject.optString("tanggal_reservasi");
                            String sesiReservasi = jsonObject.optString("sesi_reservasi");
                            int idPesanan = jsonObject.optInt("id_pesanan");
                            int idMeja = jsonObject.optInt("id_meja");
                            int noMeja = jsonObject.optInt("no_meja");
                            int totalMenu = jsonObject.optInt("total_menu");
                            int totalItem = jsonObject.optInt("total_item");
                            int idKaryawan = jsonObject.optInt("id_karyawan");
                            int idCustomer = sp.getIdCustomer();

                            Reservasi reservasi = new Reservasi(idReservasi, tanggalReservasi, sesiReservasi, idPesanan, idMeja, noMeja, totalMenu, totalItem, idKaryawan, idCustomer);
                            reservasiList.add(reservasi);
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
                try {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    JSONObject data = new JSONObject(responseBody);
                    String message = data.getString("message");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    Toast.makeText(ReservationListActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException errorr) {
                    Toast.makeText(ReservationListActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                }
                swipeRefresh.setRefreshing(false);
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
}