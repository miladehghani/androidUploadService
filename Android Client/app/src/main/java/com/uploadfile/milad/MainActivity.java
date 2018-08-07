package com.uploadfile.milad;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();

        Log.w("LOG", Environment.getDataDirectory().toString());
        Button upload = (Button) findViewById(R.id.upload);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("file/*");
                startActivityForResult(i, 0);
                /*final String path = "/storage/emulated/0/Download/pdf.pdf";
                upload(path);*/

            }
        });
    }

    public void upload(String path){

        Upload upload = new Upload(MainActivity.this, path);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        Log.w("LOG","file exist: " + upload.fileExist());

        upload.run(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.w("LOG","response: " + responseBody.toString() + " status code: " + statusCode);
                if(statusCode == 200){ //responseBody.toString().equals("success")) {
                    Toast.makeText(MainActivity.this, "Upload Suecced",Toast.LENGTH_LONG).show();
                    progressBar.setProgress(progressBar.getMax());
                }
                else {
                    Toast.makeText(MainActivity.this, "Upload Failed",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MainActivity.this, "Upload Failed: " + error.getMessage(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                double percentage = (double)bytesWritten/(double)totalSize * 100;
                progressBar.setProgress ((int)percentage);
                Log.w("LOG","Percentage: "  + percentage);
                super.onProgress(bytesWritten, totalSize);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && data != null) {
            if(resultCode == RESULT_OK) {
                final String path = data.getDataString().substring(7);
                Log.w("LOG","FILE PATH: " + path);
                upload(path);
            }
        }
    }

    public void checkPermissions(){
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

}
