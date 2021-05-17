package com.nickyjovanus.atmakoreanbbq.database;

public class Reservasi {
    private int idReservasi;
    private String tanggalReservasi;
    private String sesiReservasi;
    private int idPesanan;
    private int idMeja;
    private int totalMenu;
    private int totalItem;
    private int idKaryawan;

    public Reservasi() {}

    public Reservasi(int idReservasi, String tanggalReservasi, String sesiReservasi,
                     int idPesanan, int idMeja, int totalMenu, int totalItem,
                     int idKaryawan) {
        this.idReservasi = idReservasi;
        this.tanggalReservasi = tanggalReservasi;
        this.sesiReservasi = sesiReservasi;
        this.idPesanan = idPesanan;
        this.idMeja = idMeja;
        this.totalMenu = totalMenu;
        this.totalItem = totalItem;
        this.idKaryawan = idKaryawan;
    }

    public int getIdReservasi() {
        return idReservasi;
    }

    public void setIdReservasi(int idReservasi) {
        this.idReservasi = idReservasi;
    }

    public String getTanggalReservasi() {
        return tanggalReservasi;
    }

    public void setTanggalReservasi(String tanggalReservasi) {
        this.tanggalReservasi = tanggalReservasi;
    }

    public String getSesiReservasi() {
        return sesiReservasi;
    }

    public void setSesiReservasi(String sesiReservasi) {
        this.sesiReservasi = sesiReservasi;
    }

    public int getIdPesanan() {
        return idPesanan;
    }

    public void setIdPesanan(int idPesanan) {
        this.idPesanan = idPesanan;
    }

    public int getIdMeja() {
        return idMeja;
    }

    public void setIdMeja(int idMeja) {
        this.idMeja = idMeja;
    }

    public int getTotalMenu() {
        return totalMenu;
    }

    public void setTotalMenu(int totalMenu) {
        this.totalMenu = totalMenu;
    }

    public int getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }

    public int getIdKaryawan() {
        return idKaryawan;
    }

    public void setIdKaryawan(int idKaryawan) {
        this.idKaryawan = idKaryawan;
    }
}
