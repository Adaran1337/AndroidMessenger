package com.example.sanek.mess.PrivateMessages;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sanek.mess.Model.Chat;
import com.example.sanek.mess.Model.Users;
import com.example.sanek.mess.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PrivateMessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Chat> mFriends;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<String> reciveruid = new ArrayList<>();

    public PrivateMessagesAdapter(List<Chat> users){
        mFriends=users;
    }
    public void add(String childName,String reciver) {
        if(!arrayList.contains(childName)){
            arrayList.add(childName);
        mFriends.add(null);
        reciveruid.add(reciver);
        notifyItemInserted(mFriends.size() - 1);}
    }

    public void update(){

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View viewChatOther = layoutInflater.inflate(R.layout.chat_item, parent, false);
        return new FriendsViewHolder(viewChatOther);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        configureMyChatViewHolder((FriendsViewHolder) holder, position);
    }

    private void configureMyChatViewHolder(final FriendsViewHolder friendsViewHolder, final int position) {
        ValueEventListener listner = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Users users = dataSnapshot.getValue(Users.class);
                friendsViewHolder.UserName.setText(users.nickname);
                friendsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        Intent i = new Intent(friendsViewHolder.itemView.getContext(),PrivateMessagesChatActivity.class);
                        i.putExtra("uid",users.uid);
                        i.putExtra("name",users.nickname);
                        friendsViewHolder.itemView.getContext().startActivity(i);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        FriendsViewHolder.mDatabase.child("users").child(reciveruid.get(position)).addValueEventListener(listner);


            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshotChild : dataSnapshot.getChildren()) {
                        Chat chat = dataSnapshotChild.getValue(Chat.class);
                        friendsViewHolder.lastmessage.setText(chat.message);
                        SimpleDateFormat date = new SimpleDateFormat("HH:mm");
                        friendsViewHolder.timestamp.setText(date.format(chat.timestamp));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            FriendsViewHolder.mDatabase.child("private_messages").child(arrayList.get(position)).orderByKey().limitToLast(1).addValueEventListener(eventListener);


        //final Chat users = mFriends.get(position);


    }
    @Override
    public int getItemCount() {
        if (mFriends != null) {
            return mFriends.size();
        }
        else
            return 0;
    }


    private static class FriendsViewHolder extends RecyclerView.ViewHolder {
        private TextView UserName,lastmessage,timestamp;
private static DatabaseReference mDatabase;
        public FriendsViewHolder(View itemView) {
            super(itemView);
            UserName = itemView.findViewById(R.id.nameTextView);
            lastmessage = itemView.findViewById(R.id.messageTextView);
            timestamp = itemView.findViewById(R.id.timeTextView);
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }
    }
    public void removeat(int position){
        mFriends.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,mFriends.size());
    }
}
