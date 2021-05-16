package com.nickyjovanus.atmakoreanbbq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nickyjovanus.atmakoreanbbq.API.MenuAPI;
import com.nickyjovanus.atmakoreanbbq.adapter.RecyclerViewAdapterMenu;
import com.nickyjovanus.atmakoreanbbq.database.Menu;
import com.nickyjovanus.atmakoreanbbq.databinding.ActivityMenuBinding;
import com.nickyjovanus.atmakoreanbbq.retrofit.ApiClient;
import com.nickyjovanus.atmakoreanbbq.retrofit.ApiInterface;
import com.nickyjovanus.atmakoreanbbq.retrofit.MenuResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.android.volley.Request.Method.GET;

public class MenuActivity extends AppCompatActivity {

    private RecyclerViewAdapterMenu adapter;
    private RecyclerView recyclerView;
    ArrayList<Menu> ListMenu;
    private View view;
    ActivityMenuBinding binding;

    private SwipeRefreshLayout swipeRefresh;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        searchView = findViewById(R.id.searchMenu);
        swipeRefresh = findViewById(R.id.swipeRefresh);
//        ListMenu = new ArrayList<Menu>();
        getMenu();
        swipeRefresh.setRefreshing(true);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMenu();
            }
        });
    }

//    public void getMenu() {
//        RequestQueue queue = Volley.newRequestQueue(MenuActivity.this);
//
//        final ProgressDialog progressDialog;
//        progressDialog = new ProgressDialog(MenuActivity.this);
//        progressDialog.setMessage("Loading....");
//        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
//        progressDialog.show();
//
//        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, MenuAPI.SHOW
//                , null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                progressDialog.dismiss();
//                swipeRefresh.setRefreshing(false);
//                try {
//                    JSONArray jsonArray = response.getJSONArray("data");
//                    if(!ListMenu.isEmpty())
//                        ListMenu.clear();
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
//
//                        int idMenu           = jsonObject.optInt("id_menu");
//                        String namaMenu      = jsonObject.optString("nama_menu");
//                        double hargaMenu     = jsonObject.optDouble("harga_menu");
//                        String unitMenu      = jsonObject.optString("unit_menu");
//                        String deskripsiMenu = jsonObject.optString("deskripsi_menu");
//                        int stokMenu         = jsonObject.optInt("stok_menu");
//                        String kategori      = jsonObject.optString("kategori_menu");
//                        String gambar        = jsonObject.optString("gambar_menu");
//
//                        Menu menu = new Menu(idMenu, namaMenu, hargaMenu, stokMenu,unitMenu, deskripsiMenu, kategori,gambar);
//                        ListMenu.add(menu);
//                    }
////                    adapter.notifyDataSetChanged();
//                    generateDataList(ListMenu);
//                }catch (JSONException e){
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //Disini bagian jika response jaringan terdapat ganguan/error
//                progressDialog.dismiss();
//                Toast.makeText(MenuActivity.this, error.getMessage(),
//                        Toast.LENGTH_SHORT).show();
//                swipeRefresh.setRefreshing(false);
//            }
//        }){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String,String> headers = new HashMap<>();
//
//                headers.put("Content-Type","application/x-www-form-urlencoded");
//                return headers;
//            }
//        };
//        // Disini proses penambahan request yang sudah kita buat ke request queue yang sudah dideklarasi
//        queue.add(stringRequest);
//    }

    private void getMenu() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MenuResponse> call = apiService.getAllMenu("data");
        call.enqueue(new Callback<MenuResponse>() {
            @Override
            public void onResponse(Call<MenuResponse> call, Response<MenuResponse> response) {
                generateDataList(response.body().getMenus());
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<MenuResponse> call, Throwable t) {
                Toast.makeText(MenuActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                swipeRefresh.setRefreshing(false);
            }
        });

    }

    private void generateDataList(List<Menu> menuList) {
        recyclerView = findViewById(R.id.rv_menu);
        adapter = new RecyclerViewAdapterMenu(this,menuList);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_menu);
        binding.rvMenu.setLayoutManager((new LinearLayoutManager(this)));
        binding.rvMenu.setItemAnimator(new DefaultItemAnimator());
        binding.setAdaptermenu(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i("search submit", query);
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("search change", newText);
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}