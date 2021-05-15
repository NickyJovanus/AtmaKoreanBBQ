package com.nickyjovanus.atmakoreanbbq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nickyjovanus.atmakoreanbbq.API.MenuAPI;
import com.nickyjovanus.atmakoreanbbq.adapter.RecyclerViewAdapterMenu;
import com.nickyjovanus.atmakoreanbbq.database.Menu;
import com.nickyjovanus.atmakoreanbbq.databinding.ActivityMenuBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.GET;

public class MenuActivity extends AppCompatActivity {

    private RecyclerViewAdapterMenu adapter;
    private RecyclerView recyclerView;
    ArrayList<Menu> ListMenu;
    private View view;
    ActivityMenuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ListMenu = new ArrayList<Menu>();
        getMenu();

//        binding = DataBindingUtil.setContentView(MenuActivity.this,R.layout.activity_menu);
//        adapter = new RecyclerViewAdapterMenu(MenuActivity.this, ListMenu);
//        binding.rvMenu.setAdapter(adapter);
//        binding.rvMenu.setLayoutManager((new LinearLayoutManager(this)));


        binding= DataBindingUtil.setContentView(this,R.layout.activity_menu);
        binding.rvMenu.setLayoutManager((new LinearLayoutManager(this)));
        adapter = new RecyclerViewAdapterMenu(MenuActivity.this,ListMenu);
        binding.setAdaptermenu(adapter);
    }

    public void getMenu() {
        RequestQueue queue = Volley.newRequestQueue(MenuActivity.this);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(MenuActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, MenuAPI.SHOW
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengambil data response json object yang berupa data mahasiswa
                    JSONArray jsonArray = response.getJSONArray("data");
                    if(!ListMenu.isEmpty())
                        ListMenu.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        int idMenu           = jsonObject.optInt("id_menu");
                        String namaMenu      = jsonObject.optString("nama_menu");
                        double hargaMenu     = jsonObject.optDouble("harga_menu");
                        String unitMenu      = jsonObject.optString("unit_menu");
                        String deskripsiMenu = jsonObject.optString("deskripsi_menu");
                        int stokMenu         = jsonObject.optInt("stok_menu");
                        String kategori      = jsonObject.optString("kategori_menu");
                        String gambar        = jsonObject.optString("gambar_menu");

                        Menu menu = new Menu(idMenu, namaMenu, hargaMenu, stokMenu,unitMenu, deskripsiMenu, kategori,gambar);
                    }
                    adapter.notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                Toast.makeText(MenuActivity.this, error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<>();

                headers.put("Content-Type","application/x-www-form-urlencoded");
//                headers.put("Authorization","Bearer " + token);
                return headers;
            }
        };
        // Disini proses penambahan request yang sudah kita buat ke request queue yang sudah dideklarasi
        queue.add(stringRequest);
    }
}