package com.example.readfi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.readfi.databinding.ActivitySignUpBinding;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username, email, password, confirmpassword;
                username = String.valueOf(binding.edittextRegisterUsername.getText());
                email = String.valueOf(binding.edittextRegisterEmail.getText());
                password = String.valueOf(binding.edittextRegisterPassword.getText());
                confirmpassword = String.valueOf(binding.edittextRegisterConfirmPassword.getText());


                    if (!username.equals("") && !email.equals("") && !password.equals("")) {

//                        progressBar.show();
//                        progressBar.setMessage("Creating your account...");
//                        progressBar.setCancelable(false);

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //Starting Write and Read data with URL
                                //Creating array for parameters
                                String[] field = new String[3];
                                field[0] = "username";
                                field[1] = "email";
                                field[2] = "password";
                                //Creating array for data
                                String[] data = new String[3];
                                data[0] = username;
                                data[1] = email;
                                data[2] = password;
                                //write your own url while uploading the app in place of 192.168.100.8
                                //this ip address is just for debugging purpose!!!
                                PutData putData = new PutData("http://192.168.100.8/ReadFi/signup.php", "POST", field, data);
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
//                                        progressBar.dismiss();
                                        String result = putData.getResult();
                                        if (result.equals("Sign Up Success"))
                                        {
                                            Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }
                                //End Write and Read data with URL
                            }
                        });
                    } else {
                        validateInput(username, password, confirmpassword, email);
                    }
                }

        });
    }


    private boolean validateInput(String username, String password, String confirmpassword, String email) {
        if (TextUtils.isEmpty(username)) {
            binding.edittextRegisterUsername.setError("Username cannot be empty");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            binding.edittextRegisterPassword.setError("Password cannot be empty");
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            binding.edittextRegisterEmail.setError("Email cannot be empty");
            return false;
        }

        if (TextUtils.isEmpty(confirmpassword)) {
            binding.edittextRegisterConfirmPassword.setError("Password cannot be empty");
            return false;
        }

        return true;
    }
}