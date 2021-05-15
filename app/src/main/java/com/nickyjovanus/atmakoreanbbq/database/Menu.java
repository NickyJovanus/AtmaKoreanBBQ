package com.nickyjovanus.atmakoreanbbq.database;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

import java.io.Serializable;

public class Menu implements Serializable {
    public int getIdMenu() {
        return idMenu;
    }

    public void setidMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    private int idMenu;
    public String namaMenu;
    public double hargaMenu;
    public String unitMenu;
    public String deskripsiMenu;
    public int stokMenu;
    public String kategoriMenu;
    public String imgURL;

    public Menu(String namaMenu, double hargaMenu, int stokMenu, String unitMenu, String deskripsiMenu, String kategoriMenu, String imgURL) {
        this.namaMenu = namaMenu;
        this.hargaMenu = hargaMenu;
        this.unitMenu = unitMenu;
        this.deskripsiMenu = deskripsiMenu;
        this.stokMenu = stokMenu;
        this.kategoriMenu = kategoriMenu;
        this.imgURL = imgURL;
    }

    public Menu(int idMenu, String namaMenu, double hargaMenu, int stokMenu, String unitMenu, String deskripsiMenu, String kategoriMenu, String imgURL) {
        this.idMenu = idMenu;
        this.namaMenu = namaMenu;
        this.hargaMenu = hargaMenu;
        this.unitMenu = unitMenu;
        this.deskripsiMenu = deskripsiMenu;
        this.stokMenu = stokMenu;
        this.kategoriMenu = kategoriMenu;
        this.imgURL = imgURL;
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

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    @BindingAdapter("pImage")
    public static void loadImage(ImageView view, String imgURL) {
        Glide.with(view.getContext())
                .load(imgURL)
                .into(view);
    }
}
