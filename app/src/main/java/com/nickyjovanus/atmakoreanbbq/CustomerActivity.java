package com.nickyjovanus.atmakoreanbbq;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

        namaCustomer = findViewById(R.id.tf_namaCustomer);
        telponCustomer = findViewById(R.id.tf_telponCustomer);
        emailCustomer = findViewById(R.id.tf_emailCustomer);
        sp = new SharedPreferences(CustomerActivity.this);

        submit = findViewById(R.id.submit);
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
            if (telpon.isEmpty())
                telponLayout.setError("Please fill in the Nomor Telepon field.");
            if (email.isEmpty())
                emailLayout.setError("Please fill in the Alamat E-mail field.");
            else if(!email.contains("@")) {
                emailLayout.setError("Please fill in a valid email address.");
                Toast.makeText(this, "Please fill in a valid email address.", Toast.LENGTH_SHORT).show();
            }
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
                        if(obj.getString("message").equals("Success")) {
                            Toast.makeText(CustomerActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(CustomerActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Disini bagian jika response jaringan terdapat ganguan/error
                    progressDialog.dismiss();
                    Toast.makeText(CustomerActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("nama_customer", nama);
                    params.put("telpon_customer", telpon);
                    params.put("email_customer", email);

                    System.out.println(nama);
                    System.out.println(telpon);
                    System.out.println(email);

                    customer.setNamaCustomer(nama);
                    customer.setTelponCustomer(telpon);
                    customer.setEmailCustomer(email);

                    sp.createCustomer(customer);

                    return params;
                }
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

    public void home(View view){
        startActivity(new Intent(CustomerActivity.this,HomeActivity.class));
    }
}