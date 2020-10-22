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

public class UsersFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private UserAdapter mUserAdapter;
    private List<User> mUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_users, container, false);

        mRecyclerView = view.findViewById(R.id.MyRecyclerView);
//        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUser = new ArrayList<>();
        ReadUsers();
        return view;

    }

    private void ReadUsers() {

        final FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("Users");

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUser.clear();

                for(DataSnapshot mSnapshot: snapshot.getChildren()){
                  User user = mSnapshot.getValue(User.class);

//                    assert user != null;
//                    assert mFirebaseUser != null;
//                    if(!user.getUserID().equals(mFirebaseUser.getUid())){
//                    mUser.add(user);
//                  }

                    mUser.add(user);

                }

                mUserAdapter = new UserAdapter(getContext(), mUser);
                mRecyclerView.setAdapter(mUserAdapter);
            }




            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}
