package com.example.hackday;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.graphics.ImageDecoder;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.io.BufferedInputStream;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;
import static java.sql.DriverManager.println;

public class FirstFragment extends Fragment {

    static final int REQUEST_GET_IMAGE = 100;
    private static final int REQUEST_GALLERY = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    ImageView imageView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = (ImageView)view.findViewById(R.id.imageView);

        view.findViewById(R.id.covert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                println("l");
            }
        });

        view.findViewById(R.id.select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("1","aaaaaaaaaa");
                Intent pickPhotoIntent = new Intent(
                        Intent.ACTION_GET_CONTENT);
                pickPhotoIntent.setType("image/*");

                Intent takePhotoIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);

                Intent chooserIntent = Intent.createChooser(
                        pickPhotoIntent, "Picture...");
                chooserIntent.putExtra(
                        Intent.EXTRA_INITIAL_INTENTS,
                        new Intent[]{takePhotoIntent});

                startActivityForResult(
                        chooserIntent,
                        REQUEST_GET_IMAGE);

                return;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
//        Log.d("1","bbbbbbbbbbbbbb");

//       if ((requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) ||
//                (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)) {
//        if (requestCode == 100 || requestCode == REQUEST_IMAGE_CAPTURE){
//            Bitmap image = data.getParcelableExtra("data");
//            imageView.setImageBitmap(image    );
//            try {
//                Uri uri = data.getData();
//                InputStream stream = this.getActivity().getContentResolver().openInputStream(uri);
////                imageView.setImageBitmap(bitmap);
//                PostRequestToClova.main(this.getActivity(), uri);
//            } catch (Exception e) {
//                Log.e("error", e.getStackTrace().toString());
//            }

//        }
        super.onActivityResult(requestCode,resultCode,resultData);

        if (requestCode == REQUEST_GET_IMAGE && resultCode == Activity.RESULT_OK) {

            if (resultData != null) {
                Uri uri = resultData.getData();
                String path = getPathUri(this.getActivity(),uri);
                new HttpTask().execute(path);
            }
        }
    }

    public String getPathUri(final Context context,  Uri uri){
        String path = "";
        int apiVersion;
        try {
            apiVersion = Build.VERSION.SDK_INT;
        } catch(NumberFormatException e) {
            apiVersion = 3;
        }

        if (apiVersion >= 4.4) {
        /// Android4.4以降の場合...
            String wholeId = DocumentsContract.getDocumentId(uri);
            String id = wholeId.split(":")[1];
            ContentResolver cr = context.getContentResolver();
            String[] columns = {
                    MediaStore.Images.Media.DATA
            };
            String selection = MediaStore.Images.Media._ID + "=?";
            Cursor cursor = cr.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    columns, selection, new String[]{id}, null);
            cursor.moveToFirst();
            path = cursor.getString(
                    cursor.getColumnIndexOrThrow(columns[0]));
            cursor.close();
        } else {
        /// それ以前の古いバージョンの場合...
            ContentResolver cr = context.getContentResolver();
            String[] columns = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = cr.query(uri, columns, null, null, null);
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndexOrThrow(
                    MediaStore.Images.Media.DATA
            ));
            cursor.close();
        }
        return path;
    }

    public String getPathFromUri(final Context context, final Uri uri) {
        boolean isAfterKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isAfterKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if ("com.android.externalstorage.documents".equals(
                    uri.getAuthority())) {// ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }else {
                    return "/stroage/" + type +  "/" + split[1];
                }
            }else if ("com.android.providers.downloads.documents".equals(
                    uri.getAuthority())) {// DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }else if ("com.android.providers.media.documents".equals(
                    uri.getAuthority())) {// MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                contentUri = MediaStore.Files.getContentUri("external");
                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())) {//MediaStore
            return getDataColumn(context, uri, null, null);
        }else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String[] projection = {
                MediaStore.Files.FileColumns.DATA
        };
        try {
            cursor = context.getContentResolver().query(
                    uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int cindex = cursor.getColumnIndexOrThrow(projection[0]);
                return cursor.getString(cindex);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
}