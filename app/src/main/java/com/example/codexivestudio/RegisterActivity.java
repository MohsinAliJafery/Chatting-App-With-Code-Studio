package com.example.codexivestudio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText Username, Email, Password;
    private Button Register;

    FirebaseAuth Auth;
    DatabaseReference DBaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        Username = findViewById(R.id.Username);
        Email = findViewById(R.id.Email);
        Password = findViewById(R.id.Password);
        Register = findViewById(R.id.Register);

        Auth = FirebaseAuth.getInstance();

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            String txt_username = Username.getText().toString();
            String txt_email = Email.getText().toString();
            String txt_password = Password.getText().toString();

            if(txt_username.isEmpty() || txt_email.isEmpty() || txt_password.isEmpty()){
                Toast.makeText(RegisterActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
            }else if(txt_password.length() < 6){
                Toast.makeText(RegisterActivity.this, "Password must be atleast 6 Characters", Toast.LENGTH_SHORT).show();
            }else {
                Register(txt_username, txt_email, txt_password);
            }



            }
        });





    }

    private void Register(final String Username, String Email, final String Password){
        Auth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser mFirebaseUser = Auth.getCurrentUser();
                            String UserID = mFirebaseUser.getUid();

                            DBaseReference = FirebaseDatabase.getInstance().getReference("Users").child(UserID);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("ID", UserID);
                            hashMap.put("Username", Username);
                            hashMap.put("ImageUrl", "Default");

                            DBaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(RegisterActivity.this, ChatActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });


                        }else {
                            Toast.makeText(getApplicationContext(), "You Cannot Register with this Email or Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
