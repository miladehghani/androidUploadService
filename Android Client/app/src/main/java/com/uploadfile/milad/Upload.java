package com.uploadfile.milad;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.location.Address;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.client.ResponseHandler;

/**
 * Created by Milad on 11/24/2017.
 */

public class Upload {

    private static String URL = "http://smart-wallpaper.ir/uploadService/upload.php", KEY = "uploaded_file";
    private String fileAddress;
    private File file;

    private AsyncHttpClient httpClient;
    private Context context;
    private RequestParams params;

    public Upload(Context context, String FileAddress){
        this.fileAddress = FileAddress;
        this.context = context;
        httpClient = new AsyncHttpClient();
        params = new RequestParams();
        file  = new File(FileAddress);
    }

    public boolean fileExist(){
        if(file.exists())
            return true;
        else
            return false;
    }
    /*returns
       true if file is exist
       false if file not exist
    * */
    public boolean run(ResponseHandlerInterface handler){
        if(putFileParam(file)) {
            httpClient.post(context, URL, params, handler);
            return true;
        }
        else {
            return false;
        }
    }
    /*returns
       true if file is exist
       false if file not exist
    * */
    private boolean putFileParam(File file)
    {
        try {
            params.put(KEY, file);
            return true;
        }
        catch(Exception e) {
            Log.w("LOG_UPLOAD", e.toString());
            return false;
        }
    }
}
