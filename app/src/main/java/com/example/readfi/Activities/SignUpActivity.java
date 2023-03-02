package com.example.readfi.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.readfi.R;
import com.example.readfi.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    ProgressDialog progressBar;
    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar = new ProgressDialog(SignUpActivity.this);
                progressBar.setMessage("Creating Account...");
                progressBar.setCancelable(false);
                progressBar.show();
                String userName, email, password;
                userName = binding.edittextRegisterUsername.getText().toString();
                email = binding.edittextRegisterEmail.getText().toString();
                password = binding.edittextRegisterPassword.getText().toString();

                mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        Toast.makeText(SignUpActivity.this, "Account created...", Toast.LENGTH_SHORT).show();
                        DocumentReference documentReference = firebaseFirestore.collection("Users")
                                .document(firebaseUser.getUid());
                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("username", userName);
                        userInfo.put("email", email);
                        userInfo.put("Username", userName);

                        userInfo.put("isUser", "1");

                        documentReference.set(userInfo);
                        progressBar.dismiss();
                        startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.dismiss();
                        Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                        Log.e("msg",e.getMessage());
                    }
                });
            }
        });



        binding.btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
        }
    }
}
