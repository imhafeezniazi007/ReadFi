package com.example.readfi.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.example.readfi.databinding.ActivityProfileBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;
//    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        changePassword();
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signout();
            }
        });

//        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
//        if (acct != null) {
//            String personName = acct.getDisplayName();
//            String personGivenName = acct.getGivenName();
//            String personFamilyName = acct.getFamilyName();
//            String personEmail = acct.getEmail();
//            String personId = acct.getId();
//            Uri personPhoto = acct.getPhotoUrl();
//
//            binding.profileUsername.setText(personName);
        }




    private void changePassword(){

    binding.confirmButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        AlertDialog.Builder alert = new AlertDialog.Builder(ProfileActivity.this);

        alert.setTitle("Confirm your old password...");


        final EditText input = new EditText(ProfileActivity.this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = String.valueOf(input.getText());
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;
                AuthCredential credential = EmailAuthProvider
                        .getCredential(Objects.requireNonNull(user.getEmail()), value);

                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updatePassword(binding.profileChangepassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ProfileActivity.this, "Password changed...", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(ProfileActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(ProfileActivity.this, "User authentication failed...", Toast.LENGTH_SHORT).show();
                                    binding.profileChangepassword.setText("");
                                }
                            }
                        });



            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                binding.profileChangepassword.setText("");
            }
        });

        alert.show();

    }
});

    }


     private void signout() {

         FirebaseAuth.getInstance().signOut();
         startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
         finish();

//         if (mGoogleSignInClient != null) {
//             mGoogleSignInClient.signOut()
//                     .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                         @Override
//                         public void onComplete(@NonNull Task<Void> task) {
//                             startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
//                             finish();
//                         }
//                     });
//         }
     }
}