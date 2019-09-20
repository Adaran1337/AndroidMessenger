package com.example.sanek.mess.Friend;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.sanek.mess.Model.User;
import com.example.sanek.mess.Model.Users;
import com.example.sanek.mess.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFriends extends Fragment {
    private RecyclerView mRecyclerViewFriends;
    private AddFriendsAdapter mFriendsAdapter;
private DatabaseReference mDatabase;

    public AddFriends() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_addfriends, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mRecyclerViewFriends = v.findViewById(R.id.recycler_view_friends);
        LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
        View viewChatOther = layoutInflater.inflate(R.layout.friends, container, false);

        ValueEventListener friendsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshotChild : dataSnapshot.getChildren()) {
                    Users user = dataSnapshotChild.getValue(Users.class);
                    if (!TextUtils.equals(user.uid, User.uid)) {
                        onGetFriendsSuccess(user);
                    }
                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.child("users").addValueEventListener(friendsListener);
        return v;
    }
    public void onGetFriendsSuccess(Users users) {
        if (mFriendsAdapter == null) {
            mFriendsAdapter = new AddFriendsAdapter(new ArrayList<Users>());
            mRecyclerViewFriends.setAdapter(mFriendsAdapter);
        }
        mFriendsAdapter.add(users);
        mRecyclerViewFriends.smoothScrollToPosition(mFriendsAdapter.getItemCount() - 1);
    }
}
