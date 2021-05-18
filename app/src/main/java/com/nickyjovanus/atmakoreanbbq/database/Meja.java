package com.nickyjovanus.atmakoreanbbq.database;

import com.google.gson.annotations.SerializedName;

public class Meja {

    @SerializedName("id_meja")
    private int idMeja;

    @SerializedName("no_meja")
    private int noMeja;

    @SerializedName("status_meja")
    private String statusMeja;

    public Meja() {}

    public Meja(int idMeja, int noMeja, String statusMeja) {
        this.idMeja = idMeja;
        this.noMeja = noMeja;
        this.statusMeja = statusMeja;
    }

    public int getIdMeja() {
        return idMeja;
    }

    public void setIdMeja(int idMeja) {
        this.idMeja = idMeja;
    }

    public int getNoMeja() {
        return noMeja;
    }

    public void setNoMeja(int noMeja) {
        this.noMeja = noMeja;
    }

    public String getStatusMeja() {
        return statusMeja;
    }

    public void setStatusMeja(String statusMeja) {
        this.statusMeja = statusMeja;
    }
}
