package com.nickyjovanus.atmakoreanbbq.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nickyjovanus.atmakoreanbbq.database.Meja;

import java.util.List;

public class MejaResponse {
    @SerializedName("data")
    @Expose
    private List<Meja> meja = null;

    @SerializedName("message")
    @Expose
    private String message;

    public List<Meja> getMeja()
    {
        return meja;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMeja(List<Meja> meja)
    {
        this.meja = meja;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
