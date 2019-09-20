package com.example.sanek.mess.PrivateMessages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sanek.mess.ChatAll.MessageRecyclerAdapter;
import com.example.sanek.mess.MainMenu;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class PrivateMessagesChatActivity extends AppCompatActivity {
    private RecyclerView mRecyclerViewChat;
    public TextView tvchat;
    private EditText mETxtMessage;
    private ImageButton mButton;
    private String reciveruid,recivername,childName;

    private MessageRecyclerAdapter mChatRecyclerAdapter;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mMessageDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_messages_chat);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            recivername= extras.getString("name");
            setTitle(recivername);
            reciveruid = extras.getString("uid");
        }
        tvchat = findViewById(R.id.tvchat);
        tvchat.setVisibility(View.GONE);
        mMessageDatabaseReference = FirebaseDatabase.getInstance().getReference();
        ValueEventListener friendsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(User.uid + "_" + reciveruid).exists())
                    childName = User.uid + "_" + reciveruid;
                else if (dataSnapshot.child(reciveruid + "_" + User.uid).exists())
                    childName = reciveruid + "_" + User.uid;
                else {
                    tvchat.setVisibility(View.VISIBLE);
                    childName = User.uid + "_" + reciveruid;
                }
                getMessages();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mMessageDatabaseReference.child("private_messages").addListenerForSingleValueEvent(friendsListener);
        mRecyclerViewChat = findViewById(R.id.recycler_view_private_chat);
        mETxtMessage = findViewById(R.id.messageToSend);

        mButton = findViewById(R.id.sendButton);
        mButton.setVisibility(View.INVISIBLE);
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


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkConnectionUtil.isConnectedToInternet(getBaseContext())) {
                    Chat chat = new Chat(mETxtMessage.getText().toString(),User.nickname,User.uid,ServerValue.TIMESTAMP);
                    if (chat.senderUid != null) {

                        mMessageDatabaseReference.child("private_messages").child(childName).push().setValue(chat);
                        mETxtMessage.setText("");
                    }
                } else
                    Toast.makeText(getBaseContext(), "Check your ethernet connection!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getMessages() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    Chat chat = dataSnapshot.getValue(Chat.class);
                    onGetMessagesSuccess(chat);
                            tvchat.setVisibility(View.GONE);

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
            mMessageDatabaseReference.child("private_messages").child(childName)
                    .addChildEventListener(mChildEventListener);
        }
    }
@Override
public void onBackPressed(){
        Intent i= new Intent(PrivateMessagesChatActivity.this, MainMenu.class);
        startActivity(i);
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
