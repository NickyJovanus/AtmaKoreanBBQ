package com.nickyjovanus.atmakoreanbbq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
    private List<Menu> ListMenu = new ArrayList<>();
    private View view;

    private SwipeRefreshLayout swipeRefresh;
    private SearchView searchView;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        searchView   = findViewById(R.id.sv_menu);
        searchView.setQueryHint("Search Here");

        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setRefreshing(true);
        getMenu();
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMenu();
            }
        });
    }

    private void getMenu() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MenuResponse> call = apiService.getAllMenu("data");
        call.enqueue(new Callback<MenuResponse>() {
            @Override
            public void onResponse(Call<MenuResponse> call, Response<MenuResponse> response) {
                generateDataList(response.body().getMenus());
                swipeRefresh.setRefreshing(false);
                searchView.setOnQueryTextListener(getQueryTextListener());
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
        adapter = new RecyclerViewAdapterMenu(this, menuList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        searchView.setQueryHint("Search Menu");
        searchView.setOnQueryTextListener(getQueryTextListener());
    }

    @Override
    public void onResume() {
        super.onResume();
        searchView.setOnQueryTextListener(getQueryTextListener());
    }


    public SearchView.OnQueryTextListener getQueryTextListener(){
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(adapter != null)
                    adapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(adapter != null)
                    adapter.getFilter().filter(s);
                return false;
            }
        };
    }


    public void home(View view){
        startActivity(new Intent(MenuActivity.this,HomeActivity.class));
    }

}