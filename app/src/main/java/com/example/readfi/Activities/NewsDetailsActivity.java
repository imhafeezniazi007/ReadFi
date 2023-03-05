package com.example.readfi.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.readfi.R;
import com.example.readfi.databinding.ActivityHomeBinding;
import com.example.readfi.databinding.ActivityNewsDetailsBinding;

public class NewsDetailsActivity extends AppCompatActivity {

    ActivityNewsDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        String postText = getIntent().getStringExtra("postText");
        String postDescription = getIntent().getStringExtra("postDescription");
        String img = getIntent().getStringExtra("img");

        binding.newsDetailsHeader.setText(postText);
        binding.newsDetailsText.setText(postDescription);

        Glide.with(this)
                .load(img)
                .placeholder(R.drawable.placeholder)
                .into(binding.img);
    }
}