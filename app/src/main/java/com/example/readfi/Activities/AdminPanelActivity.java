package com.example.readfi.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.readfi.Models.Post;
import com.example.readfi.databinding.ActivityAdminPanelBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;
import java.util.UUID;

public class AdminPanelActivity extends AppCompatActivity {


    private final int IMG_REQ_ID = 10;
    ActivityAdminPanelBinding binding;
    FirebaseAuth auth;
    StorageReference storageReference;
    final int PICK_IMAGE = 100;
    FirebaseStorage storage;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminPanelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        binding.btnAdminuploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromAlbum();
            }
        });

        binding.btnLogoutAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AdminPanelActivity.this, LoginActivity.class));
                finish();
            }
        });
        binding.btnAdminPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.edittextAdminTitle.getText() != null && binding.edittextAdminDescription.getText() != null && imageUri != null) {


                    String postID = UUID.randomUUID().toString();
                    String postText = binding.edittextAdminTitle.getText().toString();
                    String postDescription = binding.edittextAdminDescription.getText().toString();

                    auth = FirebaseAuth.getInstance();
                    storageReference = FirebaseStorage.getInstance().getReference().child(Objects.requireNonNull(auth.getUid()));
                    storageReference.putFile(imageUri).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                String image = uri.toString();

                                Post post = new Post(postID, postText, postDescription, image);
                                saveInFirebase();
                                uploadDetails(post);

                            });
                        }
                    });


                } else {
                    Toast.makeText(AdminPanelActivity.this, "Please enter data...", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }

    private void saveInFirebase() {
        if (imageUri!=null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Please wait...");
            progressDialog.show();

            StorageReference reference = storageReference.child("picture/" + UUID.randomUUID().toString());
            try {
                reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(AdminPanelActivity.this, "Uploaded...", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AdminPanelActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        progressDialog.setMessage("Saved " + (int) progress + " %");
                    }
                });
        }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadDetails(Post post) {

                FirebaseApp.initializeApp(new AdminPanelActivity());
                FirebaseFirestore.getInstance()
                        .collection("post")
                        .document(post.getPostID())
                        .set(post);
                binding.edittextAdminTitle.setText("");
                binding.edittextAdminDescription.setText("");
            }


            private void getImageFromAlbum() {
                try {
                    Intent gallery = new Intent(Intent.ACTION_PICK);
                    gallery.setType("image/*");
                    startActivityForResult(gallery, PICK_IMAGE);

                } catch (Exception exp) {
                    Log.i("Error", exp.toString());
                }
            }

            @Override
            protected void onActivityResult(int reqCode, int resultCode, Intent data) {
                super.onActivityResult(reqCode, resultCode, data);

                if (resultCode == RESULT_OK && data!=null && data.getData()!=null) {
                    imageUri = data.getData();
//                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
////                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
////                        image_view.setImageBitmap(selectedImage);
                }else {
                    Toast.makeText(AdminPanelActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
                }
            }
        }
