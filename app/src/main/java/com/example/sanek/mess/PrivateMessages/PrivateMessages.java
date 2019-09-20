package com.example.sanek.mess.PrivateMessages;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.sanek.mess.Model.Chat;
import com.example.sanek.mess.Model.User;
import com.example.sanek.mess.R;
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
public class PrivateMessages extends Fragment {
    private RecyclerView mRecyclerViewFriends;
    private PrivateMessagesAdapter mMessagesAdapter;
    private Button buttonnewmessage;
    private DatabaseReference mDatabase;
    private String childName,reciveruid;
    private TextView tvchat;
    private int count;
    public PrivateMessages() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_private_messages, container, false);
        mRecyclerViewFriends = v.findViewById(R.id.recycler_view_private_messages);
        buttonnewmessage = v.findViewById(R.id.buttonnewmessage);
        tvchat=v.findViewById(R.id.tvchat);
        tvchat.setVisibility(View.GONE);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        View.OnClickListener lst = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddChat addChat = new AddChat();
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment, addChat);
                //transaction.addToBackStack(null);
                transaction.commit();
            }
        };
        buttonnewmessage.setOnClickListener(lst);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count=0;
                for (DataSnapshot dataSnapshotChild : dataSnapshot.getChildren()) {
                    if (dataSnapshotChild.getKey().contains(User.uid)){
                        count++;
                        childName = dataSnapshotChild.getKey();
                        if(childName.substring(0,28).equals(User.uid))
                            reciveruid=childName.substring(29,57);
                        else if(childName.substring(29,57).equals(User.uid))
                            reciveruid=childName.substring(0,28);
                        Log.d(TAG,"childname is "+childName+" reciveruid is "+reciveruid);
                        if(childName!=null && reciveruid!=null){
                            Log.d(TAG,"List uid: "+childName);
                            newAdapter(childName,reciveruid);}
                    }
                    else{
                        Log.d(TAG,"No such room aviable!");
                    }
                    Log.d(TAG,"COUNT: "+count);
                    if(count==0)
                        tvchat.setVisibility(View.VISIBLE);
                    else
                        tvchat.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.child("private_messages").addValueEventListener(eventListener);


        return v;
    }
    public void newAdapter(String childName,String reciveruid) {
        tvchat.setVisibility(View.GONE);
        if (mMessagesAdapter == null) {
            mMessagesAdapter = new PrivateMessagesAdapter(new ArrayList<Chat>());
            mRecyclerViewFriends.setAdapter(mMessagesAdapter);
        }
        mMessagesAdapter.add(childName,reciveruid);
       mRecyclerViewFriends.smoothScrollToPosition(mMessagesAdapter.getItemCount() - 1);
    }

}















