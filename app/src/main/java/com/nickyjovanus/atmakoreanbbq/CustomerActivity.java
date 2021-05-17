package com.nickyjovanus.atmakoreanbbq;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.nickyjovanus.atmakoreanbbq.API.CustomerAPI;
import com.nickyjovanus.atmakoreanbbq.database.Customer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class CustomerActivity extends AppCompatActivity {

    private Button submit;
    private TextInputEditText namaCustomer, telponCustomer, emailCustomer;
    private TextInputLayout namaLayout, telponLayout, emailLayout;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        
        sp = new SharedPreferences(CustomerActivity.this);

        namaCustomer    = findViewById(R.id.tf_tanggalReservasi);
        telponCustomer  = findViewById(R.id.tf_telponCustomer);
        emailCustomer   = findViewById(R.id.tf_emailCustomer);

        namaLayout      = findViewById(R.id.ti_tanggalReservasi);
        telponLayout    = findViewById(R.id.ti_telponCustomer);
        emailLayout     = findViewById(R.id.ti_emailCustomer);

        submit          = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }

    public void submit() {
        Customer customer = new Customer();

        String nama = namaCustomer.getText().toString();
        String telpon = telponCustomer.getText().toString();
        String email = emailCustomer.getText().toString();

        if(nama.isEmpty() || telpon.isEmpty() || email.isEmpty()) {
            if (nama.isEmpty())
                namaLayout.setError("Please fill in the Nama Lengkap field.");
            else
                namaLayout.setError(null);
            if (telpon.isEmpty())
                telponLayout.setError("Please fill in the Nomor Telepon field.");
            else
                telponLayout.setError(null);
            if (email.isEmpty())
                emailLayout.setError("Please fill in the Alamat E-mail field.");
            else
                emailLayout.setError(null);
        } else {
            if (!email.contains("@")) {
                emailLayout.setError("Please fill in a valid email address.");
                Toast.makeText(this, "Please fill in a valid email address.", Toast.LENGTH_SHORT).show();
            } else {
                final ProgressDialog progressDialog = new ProgressDialog(CustomerActivity.this);
                progressDialog.setMessage("Processing...");
                progressDialog.show();

                RequestQueue queue = Volley.newRequestQueue(CustomerActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, CustomerAPI.ADD, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("message").equals("Success")) {
                                Toast.makeText(CustomerActivity.this, "Data submitted.", Toast.LENGTH_SHORT).show();
                            }
                            JSONObject data = obj.getJSONObject("data");

                            customer.setNamaCustomer(nama);
                            customer.setTelponCustomer(telpon);
                            customer.setEmailCustomer(email);
                            customer.setIdCustomer(Integer.parseInt(data.optString("id_customer")));
                            sp.createCustomer(customer);

                            Toast.makeText(CustomerActivity.this, "Data submitted.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CustomerActivity.this, ReservationListActivity.class));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject data = new JSONObject(responseBody);
                            JSONObject message = data.getJSONObject("message");
                            String namaCustomerJSON = message.optString("nama_customer");
                            String telponCustomerJSON = message.optString("telpon_customer");
                            String emailCustomerJSON = message.optString("email_customer");

                            progressDialog.dismiss();
                            if(!namaCustomerJSON.isEmpty())
                                Toast.makeText(CustomerActivity.this, namaCustomerJSON.substring(2, namaCustomerJSON.length() - 2), Toast.LENGTH_SHORT).show();
                            else if(!telponCustomerJSON.isEmpty())
                                Toast.makeText(CustomerActivity.this, telponCustomerJSON.substring(2, telponCustomerJSON.length() - 2), Toast.LENGTH_SHORT).show();
                            else if(!emailCustomerJSON.isEmpty())
                                Toast.makeText(CustomerActivity.this, emailCustomerJSON.substring(2, emailCustomerJSON.length() - 2), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("nama_customer", nama);
                        params.put("telpon_customer", telpon);
                        params.put("email_customer", email);

                        System.out.println(nama);
                        System.out.println(telpon);
                        System.out.println(email);

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
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(CustomerActivity.this,HomeActivity.class));
    }

    public void home(View view){
        startActivity(new Intent(CustomerActivity.this,HomeActivity.class));
    }
}