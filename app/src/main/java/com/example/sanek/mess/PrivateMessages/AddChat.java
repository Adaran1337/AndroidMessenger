package com.example.sanek.mess.PrivateMessages;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.example.sanek.mess.Friend.FriendsAdapter;
import com.example.sanek.mess.Model.Chat;
import com.example.sanek.mess.Model.User;
import com.example.sanek.mess.Model.Users;
import com.example.sanek.mess.R;
import com.example.sanek.mess.utils.NetworkConnectionUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddChat extends Fragment {
DatabaseReference mDatabase;
RecyclerView mRecyclerViewFriends;
TextView tvchat;
    boolean friendscount;
    AddChatAdapter mChatAdapter;

    public AddChat() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_chat, container, false);

        mRecyclerViewFriends = v.findViewById(R.id.recycler_view_chat);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        friendscount = true;
        tvchat = v.findViewById(R.id.tvchat);
        tvchat.setVisibility(View.GONE);

        if(NetworkConnectionUtil.isConnectedToInternet(getContext())){
            ValueEventListener friendsListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    try {
                        dataSnapshot.getValue().toString();
                    } catch (NullPointerException e) {
                        tvchat.setVisibility(View.VISIBLE);
                        friendscount = false;
                    }
                    if (friendscount) {
                        String user = dataSnapshot.getValue().toString();
                        if (user != null) {
                            for (DataSnapshot dataSnapshotChild : dataSnapshot.getChildren()) {
                                Users users = new Users();
                                users.nickname = dataSnapshotChild.getKey();
                                users.uid = dataSnapshotChild.getValue().toString();
                                Log.d(TAG, "uid = " + users.uid + "name = " + users.nickname);
                                onGetFriendsSuccess(users);
                            }
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                }
            };
            mDatabase.child("friends").child(User.uid).addListenerForSingleValueEvent(friendsListener);
        }
        else
            Snackbar.make(getActivity().findViewById(android.R.id.content), "Check your ethernet connection!", TSnackbar.LENGTH_LONG).show();
        return v;
    }

    public void onGetFriendsSuccess(Users users) {
        if (mChatAdapter == null) {
            mChatAdapter = new AddChatAdapter(new ArrayList<Users>());
            mRecyclerViewFriends.setAdapter(mChatAdapter);
        }
        mChatAdapter.add(users);
        mRecyclerViewFriends.smoothScrollToPosition(mChatAdapter.getItemCount() - 1);
    }
}
