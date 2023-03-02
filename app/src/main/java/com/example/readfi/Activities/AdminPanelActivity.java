package com.example.readfi.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.example.readfi.Models.AdminModel;
import com.example.readfi.databinding.ActivityAdminPanelBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class AdminPanelActivity extends AppCompatActivity {


    ActivityAdminPanelBinding binding;
    final int PICK_IMAGE = 100;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminPanelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.btnAdminuploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromAlbum();
            }
        });

        binding.btnAdminPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String postID = UUID.randomUUID().toString();
                String postText = binding.edittextAdminTitle.getText().toString();
                String postDescription = binding.edittextAdminDescription.getText().toString();

                AdminModel adminModel = new AdminModel(postID, postText, postDescription, imageUri.toString());
                uploadDetails(adminModel);
            }
        });

    }

    private void uploadDetails(AdminModel adminModel) {
        FirebaseApp.initializeApp(new AdminPanelActivity());
                FirebaseFirestore.getInstance()
                        .collection("post")
                        .document(adminModel.getPostID())
                        .set(adminModel);

    }

    private void getImageFromAlbum() {
        try{
            Intent gallery = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, PICK_IMAGE);
        }catch(Exception exp){
            Log.i("Error",exp.toString());
        }
    }
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK && reqCode == 100){
            imageUri = data.getData();
        }
    }

}