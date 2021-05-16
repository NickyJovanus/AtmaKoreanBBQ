package com.nickyjovanus.atmakoreanbbq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class QRScanActivity extends AppCompatActivity {
    //QR Scanner
    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = new SharedPreferences(this);
        setContentView(R.layout.activity_qrscan);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(result.getText().equals("https://atmakoreanbbq.web.app/barcode")) {
                            if(sp.getNamaCustomer().equals(""))
                                startActivity(new Intent(QRScanActivity.this,CustomerActivity.class));
                            else
                                startActivity(new Intent(QRScanActivity.this,ReservationListActivity.class));
                        }
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
    
    public void home(View view){
        startActivity(new Intent(QRScanActivity.this,HomeActivity.class));
    }

    public void customer(View view){
        startActivity(new Intent(QRScanActivity.this,CustomerActivity.class));
    }

}