package com.nickyjovanus.atmakoreanbbq.database;

public class Detail {
    private int idDetail;
    private String namaMenu;
    private int jumlahItem;
    private double hargaItem;

    public Detail() {
    }

    public Detail(int idDetail, String namaMenu, int jumlahItem, double hargaItem) {
        this.idDetail = idDetail;
        this.namaMenu = namaMenu;
        this.jumlahItem = jumlahItem;
        this.hargaItem = hargaItem;
    }

    public int getIdDetail() {
        return idDetail;
    }

    public void setIdDetail(int idDetail) {
        this.idDetail = idDetail;
    }

    public String getNamaMenu() {
        return namaMenu;
    }

    public void setNamaMenu(String namaMenu) {
        this.namaMenu = namaMenu;
    }

    public int getJumlahItem() {
        return jumlahItem;
    }

    public void setJumlahItem(int jumlahItem) {
        this.jumlahItem = jumlahItem;
    }

    public double getHargaItem() {
        return hargaItem;
    }

    public void setHargaItem(double hargaItem) {
        this.hargaItem = hargaItem;
    }
}
