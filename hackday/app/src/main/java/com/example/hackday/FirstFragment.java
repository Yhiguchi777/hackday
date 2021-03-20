package com.example.hackday;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.io.BufferedInputStream;

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("1","bbbbbbbbbbbbbb");

       if ((requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) ||
                (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)) {
//            Bitmap image = data.getParcelableExtra("data");
//            imageView.setImageBitmap(image    );
            try {
                Uri uri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {

            }

        }
    }
}