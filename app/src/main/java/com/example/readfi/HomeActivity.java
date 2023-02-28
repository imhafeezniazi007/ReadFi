package com.example.readfi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.readfi.databinding.ActivityHomeBinding;
import com.example.readfi.databinding.ActivityProfileBinding;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}