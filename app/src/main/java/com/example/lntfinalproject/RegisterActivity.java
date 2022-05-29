package com.example.lntfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {
    private EditText editId, editEmail, editName, editPassword, editPasswordConf;
    private Button btnRegister, btnLogin;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editId = findViewById(R.id.id_bimbel);
        editEmail = findViewById(R.id.email);
        editName = findViewById(R.id.name);
        editPassword = findViewById(R.id.password);
        editPasswordConf = findViewById(R.id.password_conf);
        btnRegister = findViewById(R.id.btn_Register);
        btnLogin = findViewById(R.id.btn_Login);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Silahkan Tunggu");
        progressDialog.setCancelable(false);

        btnLogin.setOnClickListener(v -> {
            finish();
        });

        btnRegister.setOnClickListener(v->{
            if(editName.getText().length()>0 && editId.getText().length()>0 && editEmail.getText().length()>0 && editPassword.getText().length()>6 && editPasswordConf.getText().length()>6){
                if(editPassword.getText().toString().equals(editPasswordConf.getText().toString())){
                    register(editId.getText().toString(), editName.getText().toString(), editEmail.getText().toString(), editPassword.getText().toString());
                }else{
                    Toast.makeText(getApplicationContext(), "Silahkan Masukkan password yang sama", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "Silahkan Isi Data anda Terlebih dahulu", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void register(String Id,String name, String email, String password){
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                 if(task.isSuccessful() && task.getResult()!=null){
                     FirebaseUser firebaseUser = task.getResult().getUser();
                     if(firebaseUser!=null) {

                         UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                         firebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                             @Override
                             public void onComplete(@NonNull Task<Void> task) {
                                 reload();
                             }
                         });
                     }else{
                         Toast.makeText(getApplicationContext(), "Register Gagal", Toast.LENGTH_SHORT).show();
                     }
                 }else{
                     Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                 }
            }
        });
    }

    private void reload(){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

}