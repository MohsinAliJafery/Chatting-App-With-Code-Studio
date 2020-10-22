package com.example.codexivestudio;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codexivestudio.Adapter.MessageAdapter;
import com.example.codexivestudio.Model.Chat;
import com.example.codexivestudio.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView ProfileImage;
    TextView Username;

    Intent mIntent;

    Button SendMessage;
    EditText TypeAMessage;

    MessageAdapter mMessageAdapter;
    List<Chat> mChat;

    RecyclerView mRecyclerView;

    FirebaseUser mFirebaseUser;
    DatabaseReference mDatabaseReference;

    @SuppressLint("WrongViewCast")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar mToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });





        SendMessage = findViewById(R.id.send_message);
        TypeAMessage = findViewById(R.id.type_a_message);

        ProfileImage = findViewById(R.id.ProfileImage);
        Username = findViewById(R.id.Username);

        mIntent = getIntent();
        final String UserId = mIntent.getStringExtra("UserId");

        mRecyclerView = findViewById(R.id.recyclerview_message_activity);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLinearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Log.d("Tag", UserId+";");

        assert UserId != null;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(UserId);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                assert user != null;
                Username.setText(user.getUsername());
                if(user.getImageUrl().equals("defaults")){
                    ProfileImage.setImageResource(R.mipmap.ic_launcher);
                }else{
                    //Glide.with(MessageActivity.this).load(user.getImageUrl()).into(ProfileImage);
                }

                readMessage(mFirebaseUser.getUid(), UserId, user.getImageUrl());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        SendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typeAMessage = TypeAMessage.getText().toString();
                if(!typeAMessage.equals("")){
                    sendMessage(mFirebaseUser.getUid(), UserId, typeAMessage);
                }else{
                    Toast.makeText(MessageActivity.this, "You can't send empty messages", Toast.LENGTH_SHORT).show();
                }
                TypeAMessage.setText("");
            }
        });


    }


    private void sendMessage(String Sender, String Receiver, String Message){

        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> mHashmap = new HashMap<>();
        mHashmap.put("sender", Sender);
        mHashmap.put("receiver", Receiver);
        mHashmap.put("message", Message);

        mReference.child("Chats").push().setValue(mHashmap);
    }


    private void readMessage(final String MyID, final String UserID, final String ImageUrl){

        mChat = new ArrayList<>();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChat.clear();
                for(DataSnapshot mSnapshot: snapshot.getChildren()){
                    Chat chat = mSnapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(MyID) && chat.getSender().equals(UserID)
                    || chat.getReceiver().equals(UserID) && chat.getSender().equals(MyID)){

                        mChat.add(chat);

                    }

                    mMessageAdapter = new MessageAdapter(MessageActivity.this, mChat, ImageUrl);
                    mRecyclerView.setAdapter(mMessageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
