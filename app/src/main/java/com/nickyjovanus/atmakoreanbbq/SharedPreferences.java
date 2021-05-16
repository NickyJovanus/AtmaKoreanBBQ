package com.nickyjovanus.atmakoreanbbq;

import android.app.Activity;
import android.content.Context;

import com.nickyjovanus.atmakoreanbbq.database.Customer;

public class SharedPreferences {
    private android.content.SharedPreferences preferences;
    private android.content.SharedPreferences.Editor editor;
    private static final String PREFERENCE_NAME = "JSMPref";

    private static final String idCustomer     = "id";
    private static final String namaCustomer   = "nama";
    private static final String telponCustomer = "telpon";
    private static final String emailCustomer  = "email";

    public SharedPreferences(Context context){
        preferences = context.getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void createCustomer(Customer customer){
        editor.putInt(idCustomer, customer.getIdCustomer());
        editor.putString(namaCustomer,customer.getNamaCustomer());
        editor.putString(telponCustomer, customer.getTelponCustomer());
        editor.putString(emailCustomer,customer.getEmailCustomer());
        editor.commit();
    }

    public Customer getCustomer() {
        Customer customer = new Customer();
        customer.setIdCustomer(preferences.getInt(idCustomer,0));
        customer.setNamaCustomer(preferences.getString(namaCustomer,""));
        customer.setTelponCustomer(preferences.getString(telponCustomer,""));
        customer.setEmailCustomer(preferences.getString(emailCustomer,""));
        return customer;
    }

    public String getNamaCustomer() {return preferences.getString(namaCustomer,"");}
    public int getIdCustomer() {return preferences.getInt(idCustomer,0);}
}

