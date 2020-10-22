package com.example.codexivestudio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_or_Register extends AppCompatActivity {

    private Button Login, Register;
    FirebaseUser mFirebaseUser;


    @Override
    protected void onStart() {
        super.onStart();

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mFirebaseUser != null){
            Intent mIntent = new Intent(Login_or_Register.this, ChatActivity.class);
            startActivity(mIntent);
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or__register);

        Login = findViewById(R.id.Login);
        Register = findViewById(R.id.Register);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_or_Register.this, LoginActivity.class));
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_or_Register.this, RegisterActivity.class));
            }
        });


    }
}
