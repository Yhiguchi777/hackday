package com.example.hackday;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_GET_IMAGE = 100;
    private static final int REQUEST_GALLERY = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView imageView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar toolbar = findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_set_image) {
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
        if(REQUEST_GET_IMAGE == requestCode &&
        resultCode == Activity.RESULT_OK &&
        data != null){

            try {
                if(data.getExtras() != null &&
                data.getExtras().get("data")!= null){
                    Bitmap capturedImage
                            = (Bitmap) data.getExtras().get("data");
                    imageView1.setImageBitmap(capturedImage);
                }
        else{
                    InputStream stream
                            = getContentResolver().openInputStream(
                            data.getData());
                    Bitmap bitmap =
                            BitmapFactory.decodeStream(stream);
                    stream.close();
                    imageView1.setImageBitmap(bitmap);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        */
    }
}