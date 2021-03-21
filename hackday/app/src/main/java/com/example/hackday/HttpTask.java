package com.example.hackday;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import okhttp3.Headers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

class HttpTask extends AsyncTask<String, Void, String> {

    String responseBody;
    @Override
    protected String doInBackground(String... params) {
        String url = "https://bef2c44061654479868834a873039292.apigw.ntruss.com/custom/v1/464/2580fcb43bc90fe421f10c7206a0f8ae6fd43667eaeda7c4d679e21910b0b1e9/general";
        MediaType media = MediaType.parse("multipart/form-data");
        String loadedText = "";
        try {
            File file = new File(params[0]);
            String FileName = file.getName();
            String boundary = String.valueOf(System.currentTimeMillis());

            RequestBody imagebinary = RequestBody.create(media, file);

            String filePath = params[0];
//                File imageFile = new File(filePath);
//                Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
//                byte[] image1 = stream.toByteArray();
//                String img_str = Base64.encodeToString(image1, 0);

            JSONObject data = new JSONObject();
            JSONArray image = new JSONArray();
            try {
                JSONObject imageData = new JSONObject();
                imageData.put("format", "jpg");
                imageData.put("name", FileName);
                image.put(imageData);

                data.put("version", "V2");
                data.put("requestId", UUID.randomUUID().toString());
                data.put("timestamp", 0);
                data.put("lang", "ja");
                data.put("images", image);
            } catch (Exception e) {

            }


            RequestBody requestBody = new MultipartBody.Builder(boundary).setType(MultipartBody.FORM)
                    .addFormDataPart("file", FileName, RequestBody.create(media, file))
                    .addFormDataPart("message", data.toString())
                    .build();

            //boundary = "----" + UUID.randomUUID().toString().replaceAll("-", "");
            Request request = new Request.Builder()
                    .addHeader("X-OCR-SECRET","ZEphenlNYkFSeEhCbUdyc1phbmFEaEhQR2dKU1ZYYVU=")
                    .addHeader("Content-Type","multipart/form-data")
                    .url(url)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            responseBody = response.body().string();

            StringBuilder resultTexts = new StringBuilder();

            JSONArray resultImages = null;
            try {
                JSONObject jsonObj = new JSONObject(responseBody);
                resultImages = jsonObj.getJSONArray("images");
                for(int i = 0; i < resultImages.length(); i++){
                    JSONObject imageJson = resultImages.getJSONObject(i);
                    JSONArray fields = imageJson.getJSONArray("fields");
                    for(int j=0;j < fields.length(); j++){
                        String inferText = fields.getJSONObject(j).getString("inferText");
                        resultTexts.append(inferText);
                    }
                }
            } catch (JSONException ioe) {
                Log.e("ioe", ioe.getMessage());
            }
            loadedText = resultTexts.toString();

            Log.v("data", data.toString());
            Log.v("response", loadedText);

            return loadedText;


        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadedText;
    }

    @Override
    protected void onPostExecute(String result) {
        new ConvertToCurrent().execute(result);
    }

}