package com.nickyjovanus.atmakoreanbbq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ikovac.timepickerwithseconds.MyTimePickerDialog;
import com.nickyjovanus.atmakoreanbbq.API.CustomerAPI;
import com.nickyjovanus.atmakoreanbbq.API.ReservasiAPI;
import com.nickyjovanus.atmakoreanbbq.adapter.AdapterMeja;
import com.nickyjovanus.atmakoreanbbq.adapter.RecyclerViewAdapterMenu;
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
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddReservasiActivity extends AppCompatActivity {

    private Button submit;
    private TextInputEditText tanggalReservasi, sesiReservasi;
    private DatePicker dpTanggal;
    private TimePicker tpReservasi;
    private TextInputLayout tanggalLayout, sesiLayout, mejaLayout;
    Spinner spinner;

    private int hourOfDay;
    private int minute;
    private int seconds;
    private int timeInSeconds=-1;

    private SharedPreferences sp;

    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myTime = Calendar.getInstance();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reservasi);
        sp = new SharedPreferences(this);
        progressDialog = new ProgressDialog(AddReservasiActivity.this);

        tanggalReservasi = findViewById(R.id.tf_tanggalReservasi);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        tanggalReservasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddReservasiActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        sesiReservasi = findViewById(R.id.tf_sesiReservasi);

        sesiReservasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                MyTimePickerDialog mTimePicker = new MyTimePickerDialog(AddReservasiActivity.this, new MyTimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(com.ikovac.timepickerwithseconds.TimePicker view, int hourOfDay, int minute, int seconds) {
                        sesiReservasi.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute) + ":" + String.format("%02d", seconds));
                    }
                }, hourOfDay = c.get(Calendar.HOUR_OF_DAY), minute = c.get(Calendar.MINUTE), seconds = c.get(Calendar.SECOND), true);

                timeInSeconds = ((hourOfDay * 60 * 60) + (minute * 60) + seconds);
                mTimePicker.show();
            }
        });


        tanggalLayout = findViewById(R.id.ti_tanggalReservasi);
        sesiLayout = findViewById(R.id.ti_sesiReservasi);
        mejaLayout = findViewById(R.id.ti_noMeja);

        submit = findViewById(R.id.submit);

        spinner = (Spinner) findViewById(R.id.spinner_noMeja);
        getMeja();

        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        tanggalReservasi.setText(sdf.format(myCalendar.getTime()));
    }


    public void getMeja() {
        progressDialog.setMessage("Processing...");
        progressDialog.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MejaResponse> call = apiService.getAllMeja("data");
        call.enqueue(new Callback<MejaResponse>() {
            @Override
            public void onResponse(Call<MejaResponse> call, Response<MejaResponse> response) {
                if (response.isSuccessful()) {
                    List<Meja> mejaItem = response.body().getMeja();
                    List<String> listSpinner = new ArrayList<String>();
                    for (int i = 0; i < mejaItem.size(); i++){
                        listSpinner.add(String.valueOf(mejaItem.get(i).getNoMeja()));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddReservasiActivity.this,
                            android.R.layout.simple_spinner_item, listSpinner);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                } else {
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<MejaResponse> call, Throwable t) {
                Toast.makeText(AddReservasiActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
    
    public void submit() {
        Reservasi reservasi = new Reservasi();

        String tanggal = tanggalReservasi.getText().toString();
        String sesi = sesiReservasi.getText().toString();
        String nomeja = spinner.getSelectedItem().toString();

        if(tanggal.isEmpty() || sesi.isEmpty() || nomeja.isEmpty()) {
            if (tanggal.isEmpty())
                tanggalLayout.setError("Please fill in the Tanggal Pesanan field.");
            else
                tanggalLayout.setError(null);
            if (sesi.isEmpty())
                sesiLayout.setError("Please fill in the Sesi Pesanan field.");
            else
                sesiLayout.setError(null);
            if (nomeja.isEmpty())
                mejaLayout.setError("Please fill in the Nomor Meja field.");
            else
                mejaLayout.setError(null);
        } else {
            progressDialog.setMessage("Processing...");
            progressDialog.show();

            RequestQueue queue = Volley.newRequestQueue(AddReservasiActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, ReservasiAPI.ADD + sp.getIdCustomer(), new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    try {
                        JSONObject obj = new JSONObject(response);
                        JSONObject data = obj.getJSONObject("data");
                        JSONObject datapesanan = obj.getJSONObject("datapesanan");

                        reservasi.setTanggalReservasi(tanggal);
                        reservasi.setSesiReservasi(sesi);
                        reservasi.setNoMeja(Integer.parseInt(nomeja));
                        reservasi.setIdCustomer(sp.getIdCustomer());
                        reservasi.setIdPesanan(Integer.parseInt(datapesanan.optString("id_pesanan")));
                        reservasi.setIdKaryawan(Integer.parseInt(datapesanan.optString("id_karyawan")));
                        reservasi.setIdMeja(Integer.parseInt(data.optString("id_meja")));
                        reservasi.setTotalItem(Integer.parseInt(datapesanan.optString("total_item")));
                        reservasi.setTotalMenu(Integer.parseInt(datapesanan.optString("total_menu")));

                        Toast.makeText(AddReservasiActivity.this, "Pesanan Successfully Added.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddReservasiActivity.this, ReservationListActivity.class));
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
                        JSONObject message = data.getJSONObject("message");
                        String tanggalReservasiJSON = message.optString("tanggal_reservasi");
                        String sesiReservasiJSON = message.optString("sesi_reservasi");
                        String noMejaJSON = message.optString("no_meja");

                        if(!tanggalReservasiJSON.isEmpty())
                            Toast.makeText(AddReservasiActivity.this, tanggalReservasiJSON.substring(2, tanggalReservasiJSON.length() - 2), Toast.LENGTH_SHORT).show();
                        else if(!sesiReservasiJSON.isEmpty())
                            Toast.makeText(AddReservasiActivity.this, sesiReservasiJSON.substring(2, sesiReservasiJSON.length() - 2), Toast.LENGTH_SHORT).show();
                        else if(!noMejaJSON.isEmpty())
                            Toast.makeText(AddReservasiActivity.this, noMejaJSON.substring(2, noMejaJSON.length() - 2), Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(AddReservasiActivity.this, message.toString(), Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        String body;
                        try {
                            body = new String(error.networkResponse.data,"UTF-8");
                            JSONObject data = new JSONObject(body);
                            String message = data.optString("message");
                            Toast.makeText(AddReservasiActivity.this, message, Toast.LENGTH_SHORT).show();
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
                    params.put("tanggal_reservasi", tanggal);
                    params.put("sesi_reservasi", sesi);
                    params.put("no_meja", nomeja);

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();

                    headers.put("Content-Type", "application/x-www-form-urlencoded");
                    return headers;
                }
            };

            queue.add(stringRequest);
        }

    }

    public void reservasiList(View view){
        startActivity(new Intent(AddReservasiActivity.this,ReservationListActivity.class));
    }

}