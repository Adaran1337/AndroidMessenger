package com.example.sanek.mess.Friend;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.example.sanek.mess.Model.User;
import com.example.sanek.mess.Model.Users;
import com.example.sanek.mess.R;
import com.example.sanek.mess.utils.NetworkConnectionUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class Friends extends Fragment {
    Button btnewfriends;
    TextView tvfriend;
    boolean friendscount;
    private RecyclerView mRecyclerViewFriends;
    private FriendsAdapter mFriendsAdapter;
    private DatabaseReference mDatabase;

    public Friends() {
        // Required empty public constructor
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_friends, container, false);
        btnewfriends = v.findViewById(R.id.button);
        tvfriend = v.findViewById(R.id.tvfriends);
        tvfriend.setVisibility(View.INVISIBLE);
        friendscount = true;
        mRecyclerViewFriends = v.findViewById(R.id.recycler_view_friends);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(NetworkConnectionUtil.isConnectedToInternet(getContext())){
        ValueEventListener friendsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                try {
                    dataSnapshot.getValue().toString();
                } catch (NullPointerException e) {
                    tvfriend.setVisibility(View.VISIBLE);
                    friendscount = false;
                }
                if (friendscount) {
                        String user = dataSnapshot.getValue().toString();
                if (user != null) {
                    for (DataSnapshot dataSnapshotChild : dataSnapshot.getChildren()) {
                        Users users = new Users();
                        users.nickname = dataSnapshotChild.getKey();
                        users.uid = dataSnapshotChild.getValue().toString();
                        Log.d(TAG, "uid = " + users.uid + " , name = " + users.nickname);
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

        View.OnClickListener lst = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkConnectionUtil.isConnectedToInternet(getContext())) {
                    AddFriends addFriends = new AddFriends();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment, addFriends);
                    //transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Check your ethernet connection!", TSnackbar.LENGTH_LONG).show();
                }
            }
        };
        btnewfriends.setOnClickListener(lst);
        return v;
    }

    public void onGetFriendsSuccess(Users users) {
        if (mFriendsAdapter == null) {
            mFriendsAdapter = new FriendsAdapter(new ArrayList<Users>());
            mRecyclerViewFriends.setAdapter(mFriendsAdapter);
        }
        mFriendsAdapter.add(users);
        mRecyclerViewFriends.smoothScrollToPosition(mFriendsAdapter.getItemCount() - 1);
    }
}