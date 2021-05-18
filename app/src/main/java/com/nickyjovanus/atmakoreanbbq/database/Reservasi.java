package com.nickyjovanus.atmakoreanbbq.database;

public class Reservasi {
    public int idReservasi;
    public String tanggalReservasi;
    public String sesiReservasi;
    public int idPesanan;
    public int idMeja;
    public int noMeja;
    public int totalMenu;
    public int totalItem;
    public int idKaryawan;
    public int idCustomer;

    public Reservasi() {}

    public Reservasi(int idReservasi, String tanggalReservasi, String sesiReservasi,
                     int idPesanan, int idMeja, int noMeja, int totalMenu, int totalItem,
                     int idKaryawan, int idCustomer) {
        this.idReservasi = idReservasi;
        this.tanggalReservasi = tanggalReservasi;
        this.sesiReservasi = sesiReservasi;
        this.idPesanan = idPesanan;
        this.idMeja = idMeja;
        this.noMeja = noMeja;
        this.totalMenu = totalMenu;
        this.totalItem = totalItem;
        this.idKaryawan = idKaryawan;
        this.idCustomer = idCustomer;
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

    public int getNoMeja() {
        return noMeja;
    }

    public void setNoMeja(int noMeja) {
        this.noMeja = noMeja;
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

    public int getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }
}
