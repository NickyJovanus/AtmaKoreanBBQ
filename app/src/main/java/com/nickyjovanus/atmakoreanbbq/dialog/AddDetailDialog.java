package com.nickyjovanus.atmakoreanbbq.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.nickyjovanus.atmakoreanbbq.API.DetailPesananAPI;
import com.nickyjovanus.atmakoreanbbq.API.ReservasiAPI;
import com.nickyjovanus.atmakoreanbbq.AddReservasiActivity;
import com.nickyjovanus.atmakoreanbbq.CustomerActivity;
import com.nickyjovanus.atmakoreanbbq.R;
import com.nickyjovanus.atmakoreanbbq.ReservasiEditActivity;
import com.nickyjovanus.atmakoreanbbq.ReservationListActivity;
import com.nickyjovanus.atmakoreanbbq.SharedPreferences;
import com.nickyjovanus.atmakoreanbbq.database.Detail;
import com.nickyjovanus.atmakoreanbbq.database.Meja;
import com.nickyjovanus.atmakoreanbbq.database.Menu;
import com.nickyjovanus.atmakoreanbbq.database.Reservasi;
import com.nickyjovanus.atmakoreanbbq.retrofit.ApiClient;
import com.nickyjovanus.atmakoreanbbq.retrofit.ApiInterface;
import com.nickyjovanus.atmakoreanbbq.retrofit.MejaResponse;
import com.nickyjovanus.atmakoreanbbq.retrofit.MenuResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDetailDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity activity;
    public Dialog d;
    public Button yes, no;
    public TextInputEditText jumlahItem;
    public Spinner spinnerMenu;
    public List<Menu> menuList;
    private int idMenu = -1;
    private int[] idMenus = new int[255];
    SharedPreferences sp;
    boolean clicked = false;

    public AddDetailDialog(Activity a, List<Menu> menuList) {
        super(a);
        this.activity = a;
        this.menuList = menuList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_dialog);

        sp = new SharedPreferences(activity);

        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);

        jumlahItem = findViewById(R.id.tf_jumlahItem);
        spinnerMenu = findViewById(R.id.spinner_NamaMenu);
        spinnerMenu.setSelection(-1);

//        getNamaMenu();

        List<Menu> mejaItem = menuList;
        List<String> listSpinner = new ArrayList<String>();
        for (int i = 0; i < mejaItem.size(); i++){
            listSpinner.add(String.valueOf(mejaItem.get(i).getNamaMenu()));
            idMenus[i] = mejaItem.get(i).getIdMenu();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_item, listSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMenu.setAdapter(adapter);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!clicked) {
                    clicked = true;
                    Detail detail = new Detail();
                    String jml = jumlahItem.getText().toString();

                    final ProgressDialog progressDialog = new ProgressDialog(activity);
                    progressDialog.setMessage("Processing...");
                    progressDialog.show();

                    int positionMenu = spinnerMenu.getSelectedItemPosition();
                    for(int x = 0; x< idMenus.length; x++) {
                        if(positionMenu == x)
                            idMenu = idMenus[x];
                    }
                    dismiss();

                    String idPesanan = String.valueOf(sp.getIdPesanan());
                    Log.i("jmlItem", jml);
                    Log.i("idMenu", String.valueOf(idMenu));

                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, DetailPesananAPI.ADD, new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONObject data = obj.getJSONObject("data");
                                Toast.makeText(activity, "Pesanan Successfully Added.", Toast.LENGTH_SHORT).show();
                                activity.finish();
                                activity.startActivity(activity.getIntent());
                                progressDialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject data = new JSONObject(responseBody);
                                String errmessage = data.optString("message");
                                JSONObject message = data.getJSONObject("message");
                                String jumlahItemJSON = message.optString("jumlah_item");

                                if(!jumlahItemJSON.isEmpty())
                                    Toast.makeText(activity, jumlahItemJSON.substring(2, jumlahItemJSON.length() - 2), Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(activity, errmessage, Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
                                String body;
                                try {
                                    body = new String(error.networkResponse.data,"UTF-8");
                                    JSONObject data = new JSONObject(body);
                                    String message = data.optString("message");
                                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
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
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("jumlah_item", String.valueOf(jml));
                            params.put("id_menu", String.valueOf(idMenu));
                            params.put("id_pesanan", idPesanan);

                            return params;
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<>();

                            headers.put("Content-Type", "application/x-www-form-urlencoded");
                            return headers;
                        }
                    };
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            0,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    queue.add(stringRequest);
                }
            }
        });

        no.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    public void getNamaMenu() {
    }
}
