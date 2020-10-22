package com.example.codexivestudio.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codexivestudio.Adapter.UserAdapter;
import com.example.codexivestudio.Model.Chat;
import com.example.codexivestudio.Model.User;
import com.example.codexivestudio.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {

    private RecyclerView mRecyclerView;

    private UserAdapter mUserAdapter;
    private List<User> mUsers;

    FirebaseUser mFirebaseUser;
    DatabaseReference mDatabaseReference;

    private List<String> mUsersList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mUsers = new ArrayList<>();
        mUsersList = new ArrayList<>();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Chats");

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();

                for(DataSnapshot mSnapshot: snapshot.getChildren()){
                    Chat chat = mSnapshot.getValue(Chat.class);

                    if(chat.getSender().equals(mFirebaseUser.getUid())){
                        mUsersList.add(chat.getReceiver());
                    }
                    if(chat.getReceiver().equals(mFirebaseUser.getUid())){
                        mUsersList.add(chat.getSender());
                    }

                }

                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void readChats() {
        mUsers = new ArrayList<>();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                for(DataSnapshot mSnapshot: snapshot.getChildren()){
                    User user = mSnapshot.getValue(User.class);

                    // Display 1 user from chats
                    for(String ID: mUsersList){
                        if(user.getID().equals(ID)){
                            if(mUsers.size() != 0){
                                for(User mUser: mUsers){
                                    if(!user.getID().equals(mUser.getID())){
                                        mUsers.add(mUser);
                                    }
                                }
                            } else{
                                mUsers.add(user);
                            }
                        }
                    }
                }

                mUserAdapter = new UserAdapter(getContext(), mUsers);
                mRecyclerView.setAdapter(mUserAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
