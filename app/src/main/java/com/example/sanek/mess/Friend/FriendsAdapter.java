package com.example.sanek.mess.Friend;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.sanek.mess.Model.User;
import com.example.sanek.mess.Model.Users;
import com.example.sanek.mess.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static android.content.ContentValues.TAG;

public class FriendsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Users> mFriends;
    private DatabaseReference mDatabase;

    public FriendsAdapter(List<Users> users){
        mFriends=users;
    }
    public void add(Users users) {
        mFriends.add(users);
        notifyItemInserted(mFriends.size()-1);
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View viewChatOther = layoutInflater.inflate(R.layout.friends, parent, false);
        return new FriendsViewHolder(viewChatOther);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        configureMyChatViewHolder((FriendsViewHolder) holder, position);
    }

    private void configureMyChatViewHolder(final FriendsViewHolder friendsViewHolder, final int position) {
        final Users users = mFriends.get(position);
        friendsViewHolder.UserName.setText(users.nickname);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("friends");
        View.OnClickListener lst = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child(User.uid).child(users.nickname).removeValue();
                mDatabase.child(users.uid).child(User.nickname).removeValue();
                Log.d(TAG , "Users nickname is "+users.nickname);
                removeat(position);
            }
        };
        friendsViewHolder.cancelfriend.setOnClickListener(lst);
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
        private TextView UserName;
        private ImageButton addfriend,cancelfriend;


        public FriendsViewHolder(View itemView) {
            super(itemView);
            UserName = itemView.findViewById(R.id.TvNickName);
            addfriend = itemView.findViewById(R.id.imageButtonAdd);
            cancelfriend = itemView.findViewById(R.id.imageButtonCancel);
            addfriend.setVisibility(View.GONE);

        }
    }
    private void removeat(int position){
        mFriends.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,mFriends.size());
    }
}

