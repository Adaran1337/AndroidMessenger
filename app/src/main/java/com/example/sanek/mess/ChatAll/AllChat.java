package com.example.sanek.mess.ChatAll;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sanek.mess.Model.Chat;
import com.example.sanek.mess.Model.User;
import com.example.sanek.mess.R;
import com.example.sanek.mess.utils.NetworkConnectionUtil;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllChat extends Fragment {
    private RecyclerView mRecyclerViewChat;
    private EditText mETxtMessage;
    private ImageButton mButton;


    private MessageRecyclerAdapter mChatRecyclerAdapter;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mMessageDatabaseReference;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_all_chat, container, false);
        bindViews(fragmentView);
        mETxtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0)
                    mButton.setVisibility(View.VISIBLE);
                else mButton.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return fragmentView;

    }

    private void bindViews(View view) {
        mRecyclerViewChat = (RecyclerView) view.findViewById(R.id.recycler_view_chat);
        mETxtMessage = (EditText) view.findViewById(R.id.messageToSend);
        mButton = (ImageButton) view.findViewById(R.id.sendButton);
        mButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {

        mMessageDatabaseReference = FirebaseDatabase.getInstance().getReference();

        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    Chat chat = dataSnapshot.getValue(Chat.class);
                    onGetMessagesSuccess(chat);


                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mMessageDatabaseReference.child("messages").addChildEventListener(mChildEventListener);
        }
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NetworkConnectionUtil.isConnectedToInternet(getContext())){
                Chat chat = new Chat(mETxtMessage.getText().toString(), User.nickname, User.uid, ServerValue.TIMESTAMP);
                if(chat.senderUid != null){
                mMessageDatabaseReference.child("messages").push().setValue(chat);
                mETxtMessage.setText("");
                }
                }
                else
                    Toast.makeText(getContext(), "Check your ethernet connection!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onGetMessagesSuccess(Chat chat) {
        if (mChatRecyclerAdapter == null) {
            mChatRecyclerAdapter = new MessageRecyclerAdapter(new ArrayList<Chat>());
            mRecyclerViewChat.setAdapter(mChatRecyclerAdapter);
        }
        mChatRecyclerAdapter.add(chat);
        mRecyclerViewChat.smoothScrollToPosition(mChatRecyclerAdapter.getItemCount() - 1);
    }
}
