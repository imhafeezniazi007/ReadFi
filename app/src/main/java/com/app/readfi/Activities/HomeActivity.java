package com.app.readfi.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.app.readfi.Adapters.PostAdapter;
import com.app.readfi.Models.Post;
import com.app.readfi.R;
import com.app.readfi.databinding.ActivityHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    PostAdapter postAdapter;
    ArrayList<Post> posts;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomNavigationView = binding.bottomNavigationView;

        ImageView myImageView= binding.btc;
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        myImageView.startAnimation(myFadeInAnimation);

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                        break;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        break;

                    default:
                }
                return true;
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        binding.postsList.setHasFixedSize(true);
        binding.postsList.setLayoutManager(new LinearLayoutManager(this));
        firebaseFirestore = FirebaseFirestore.getInstance();
        posts = new ArrayList<Post>();
        postAdapter = new PostAdapter(HomeActivity.this, posts);

        binding.postsList.setAdapter(postAdapter);

        getPosts();
        progressDialog.dismiss();
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

//                            Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                            //progressDialog.dismiss();
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

                                postAdapter.notifyDataSetChanged();
                                progressDialog.dismiss();

                            }
                        }
                    }
                });

    }

}