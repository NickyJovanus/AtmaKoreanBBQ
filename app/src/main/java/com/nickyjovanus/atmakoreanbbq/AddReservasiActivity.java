package com.nickyjovanus.atmakoreanbbq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ikovac.timepickerwithseconds.MyTimePickerDialog;
import com.nickyjovanus.atmakoreanbbq.adapter.AdapterMeja;
import com.nickyjovanus.atmakoreanbbq.adapter.RecyclerViewAdapterMenu;
import com.nickyjovanus.atmakoreanbbq.database.Meja;
import com.nickyjovanus.atmakoreanbbq.database.Menu;
import com.nickyjovanus.atmakoreanbbq.database.Reservasi;
import com.nickyjovanus.atmakoreanbbq.retrofit.ApiClient;
import com.nickyjovanus.atmakoreanbbq.retrofit.ApiInterface;
import com.nickyjovanus.atmakoreanbbq.retrofit.MejaResponse;
import com.nickyjovanus.atmakoreanbbq.retrofit.MenuResponse;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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

    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myTime = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reservasi);

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
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        tanggalReservasi.setText(sdf.format(myCalendar.getTime()));
    }


    public void getMeja() {
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
            }

            @Override
            public void onFailure(Call<MejaResponse> call, Throwable t) {
                Toast.makeText(AddReservasiActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void submit() {
        Reservasi reservasi = new Reservasi();
    }

    public void reservasiList(View view){
        startActivity(new Intent(AddReservasiActivity.this,ReservationListActivity.class));
    }

}