package com.nickyjovanus.atmakoreanbbq.database;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Menu implements Serializable {
    public int getIdMenu() {
        return idMenu;
    }

    public void setidMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    @SerializedName("id_menu")
    private int idMenu;

    @SerializedName("nama_menu")
    public String namaMenu;

    @SerializedName("harga_menu")
    public double hargaMenu;

    @SerializedName("unit_menu")
    public String unitMenu;

    @SerializedName("deskripsi_menu")
    public String deskripsiMenu;

    @SerializedName("stok_menu")
    public int stokMenu;

    @SerializedName("kategori_menu")
    public String kategoriMenu;

    @SerializedName("gambar_menu")
    public String gambarMenu;

    public Menu(String namaMenu, double hargaMenu, int stokMenu, String unitMenu, String deskripsiMenu, String kategoriMenu, String gambarMenu) {
        this.namaMenu = namaMenu;
        this.hargaMenu = hargaMenu;
        this.unitMenu = unitMenu;
        this.deskripsiMenu = deskripsiMenu;
        this.stokMenu = stokMenu;
        this.kategoriMenu = kategoriMenu;
        this.gambarMenu = gambarMenu;
    }

    public Menu(int idMenu, String namaMenu, double hargaMenu, int stokMenu, String unitMenu, String deskripsiMenu, String kategoriMenu, String gambarMenu) {
        this.idMenu = idMenu;
        this.namaMenu = namaMenu;
        this.hargaMenu = hargaMenu;
        this.unitMenu = unitMenu;
        this.deskripsiMenu = deskripsiMenu;
        this.stokMenu = stokMenu;
        this.kategoriMenu = kategoriMenu;
        this.gambarMenu = gambarMenu;
    }

    public String getNamaMenu() {
        return namaMenu;
    }

    public void setNamaMenu(String namaMenu) {
        this.namaMenu = namaMenu;
    }

    public double getHargaMenu() {
        return hargaMenu;
    }

    public void setHargaMenu(double hargaMenu) {
        this.hargaMenu = hargaMenu;
    }

    public int getStokMenu() {
        return stokMenu;
    }

    public void setStokMenu(int stokMenu) {
        this.stokMenu = stokMenu;
    }

    public String getDeskripsiMenu() {
        return deskripsiMenu;
    }

    public void setDeskripsiMenu(String deskripsiMenu) {
        this.deskripsiMenu = deskripsiMenu;
    }

    public String getUnitMenu() {
        return unitMenu;
    }

    public void setUnitMenu(String unitMenu) {
        this.unitMenu = unitMenu;
    }

    public String getKategoriMenu() {
        return kategoriMenu;
    }

    public void setKategoriMenu(String kategoriMenu) {
        this.kategoriMenu = kategoriMenu;
    }

    public String getGambarMenu() {
        return gambarMenu;
    }

    public void setGambarMenu(String gambarMenu) {
        this.gambarMenu = gambarMenu;
    }

    @BindingAdapter("pImage")
    public static void loadImage(ImageView view, String imgURL) {
        Glide.with(view.getContext())
                .load(imgURL)
                .into(view);
    }
}
