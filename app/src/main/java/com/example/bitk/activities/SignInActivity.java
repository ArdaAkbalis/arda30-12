package com.example.bitk.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.bitk.R;
import com.example.bitk.databinding.ActivitySignInBinding;
import com.example.bitk.utilities.Constants;
import com.example.bitk.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;



public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private PreferenceManager preferenceManager;
    FirebaseAuth firebaseAuth;//9.01.2023

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        binding = ActivitySignInBinding.inflate(getLayoutInflater());


        setContentView(binding.getRoot());
        setListeners();

        firebaseAuth = FirebaseAuth.getInstance();//9.01.2023

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();//9.01.2023

        if (firebaseUser != null) {//9.01.2023

            Intent intent = new Intent(SignInActivity.this, MainActivity.class);//9.01.2023
            startActivity(intent);//9.01.2023
            finish();//9.01.2023

        }//9.01.2023
    }
    private void setListeners() {
        binding.textCreateNewAccount.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));
            binding.buttonSignIn.setOnClickListener(view -> {
                if(isValidSignInDetails()) {
                   signIn();

                }
            });
    }

   private void signIn() {
       /* loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL,binding.inputEmail.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWARD,binding.inputPassword.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null
                            && task.getResult().getDocuments().size() > 0) {
                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                    preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                    preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else {
                        loading(false);
                        showToast("Giriş yapılamadı");
                    }
                });*/


       String email = binding.inputEmail.getText().toString();//9.01.2023
       String password = binding.inputPassword.getText().toString();//9.01.2023


       firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {//9.01.2023
           @Override
           public void onSuccess(AuthResult authResult) {//9.01.2023

               FirebaseFirestore database = FirebaseFirestore.getInstance();
               database.collection(Constants.KEY_COLLECTION_USERS)
                       .whereEqualTo(Constants.KEY_EMAIL,binding.inputEmail.getText().toString())
                       .whereEqualTo(Constants.KEY_PASSWARD,binding.inputPassword.getText().toString())
                       .get()
                       .addOnCompleteListener(task -> {
                           if (task.isSuccessful() && task.getResult() != null
                                   && task.getResult().getDocuments().size() > 0) {
                               DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                               preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                               preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                               preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                               preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                               Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                               startActivity(intent);
                           }else {
                               loading(false);
                               showToast("Giriş yapılamadı");
                           }
                       });
           }
       }).addOnFailureListener(new OnFailureListener() {//9.01.2023
           @Override//9.01.2023
           public void onFailure(@NonNull Exception e) {//9.01.2023
               Toast.makeText(SignInActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();//9.01.2023
           }//9.01.2023
       });//9.01.2023



    }

    private void loading(Boolean isLoading) {
        if(isLoading) {
            binding.buttonSignIn.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonSignIn.setVisibility(View.VISIBLE);
        }
    }

    private void showToast(String message ) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidSignInDetails() {
        if(binding.inputEmail.getText().toString().trim().isEmpty()) {
            showToast("Email'i giriniz");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()){
            showToast("Geçerli Email'i giriniz");
            return false;
        }else if(binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Şifreyi giriniz");
            return false;
        }else {
            return true;
        }
    }
}