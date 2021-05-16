package com.nickyjovanus.atmakoreanbbq.database;

public class Customer {

    private int idCustomer;
    private String namaCustomer,
                   telponCustomer,
                   emailCustomer;

    public String getNamaCustomer() {
        return namaCustomer;
    }

    public void setNamaCustomer(String namaCustomer) {
        this.namaCustomer = namaCustomer;
    }

    public String getTelponCustomer() {
        return telponCustomer;
    }

    public void setTelponCustomer(String telponCustomer) {
        this.telponCustomer = telponCustomer;
    }

    public String getEmailCustomer() {
        return emailCustomer;
    }

    public void setEmailCustomer(String emailCustomer) {
        this.emailCustomer = emailCustomer;
    }

    public int getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }

    public Customer(int idCustomer, String namaCustomer, String telponCustomer, String emailCustomer) {
        this.idCustomer     = idCustomer;
        this.namaCustomer   = namaCustomer;
        this.telponCustomer = telponCustomer;
        this.emailCustomer  = emailCustomer;
    }

    public Customer(){}
}
