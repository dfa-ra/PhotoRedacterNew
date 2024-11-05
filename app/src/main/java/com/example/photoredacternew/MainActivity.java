package com.example.photoredacternew;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.photoredacternew.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final String url = "https://kinopoiskapiunofficial.tech/images/posters/kp_small/301.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(view -> {

            PhotoDialog dialog = PhotoDialog.getInstance(MainActivity.this);
            dialog.open();

            Picasso.get().load(url).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Log.i("aa99", "onBitmapLoaded");
                    dialog.drawPhoto(new BitmapDrawable(getResources(), bitmap));
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    Log.e("aa99", "onBitmapFailed: " + e.toString());
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    Log.d("aa99", "onPrepareLoad");
                }
            });
        });
    }
}