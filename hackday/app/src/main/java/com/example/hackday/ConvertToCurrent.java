package com.example.hackday;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConvertToCurrent extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        String responseBody = "";
        MediaType media = MediaType.parse("application/json; charset=utf-8");
        String oldText = (String)params[0];
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(2000, TimeUnit.SECONDS)
                .build();
        RequestBody requestBody = RequestBody.create(media, "body=" + oldText);

        Request request = new Request.Builder()
                .url("http://192.168.11.7")
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            responseBody = response.body().string();
        }catch(Exception e){
            Log.e("err", e.getMessage());
        }finally {
            return responseBody;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("result", result);
    }
}
