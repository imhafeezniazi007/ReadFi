package com.example.readfi.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.readfi.Adapters.AdminPostAdapter;
import com.example.readfi.Adapters.PostAdapter;
import com.example.readfi.Models.Post;
import com.example.readfi.R;
import com.example.readfi.databinding.ActivityAdminPanelBinding;
import com.example.readfi.databinding.ActivityAdminPostsBinding;
import com.example.readfi.databinding.ActivityHomeBinding;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdminPostsActivity extends AppCompatActivity {

    ActivityAdminPostsBinding binding;
    AdminPostAdapter adminPostAdapter;
    ArrayList<Post> posts;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminPostsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ImageView myImageView= binding.btc;
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        myImageView.startAnimation(myFadeInAnimation);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        binding.postsList.setHasFixedSize(true);
        binding.postsList.setLayoutManager(new LinearLayoutManager(this));
        firebaseFirestore = FirebaseFirestore.getInstance();
        posts = new ArrayList<Post>();
        adminPostAdapter = new AdminPostAdapter(AdminPostsActivity.this, posts);

        binding.postsList.setAdapter(adminPostAdapter);

        getPosts();

    }

    private void getPosts() {
        firebaseFirestore
                .collection("post")
                .orderBy("postID", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null)
                        {
                            progressDialog.dismiss();

                            Toast.makeText(AdminPostsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            //progressDialog.dismiss();
                        }
                        else{

                            for (DocumentChange documentChange:
                                    value.getDocumentChanges()) {
                                if (documentChange.getType() == DocumentChange.Type.ADDED)
                                {
                                    posts.add(documentChange
                                            .getDocument()
                                            .toObject(Post.class));
                                }

                                adminPostAdapter.notifyDataSetChanged();
                                progressDialog.dismiss();


                            }
                        }
                    }
                });

    }
}