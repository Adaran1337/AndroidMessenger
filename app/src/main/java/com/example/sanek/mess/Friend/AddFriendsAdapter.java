package com.example.sanek.mess.Friend;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.sanek.mess.Model.User;
import com.example.sanek.mess.Model.Users;
import com.example.sanek.mess.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static android.content.ContentValues.TAG;

public class AddFriendsAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<Users> mFriends;
    private DatabaseReference mDatabase;

    public AddFriendsAdapter(List<Users> users){
        mFriends=users;
    }
    public void add(Users users) {
        mFriends.add(users);
        notifyItemInserted(mFriends.size() - 1);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View viewChatOther = layoutInflater.inflate(R.layout.friends, parent, false);
        return new FriendsViewHolder(viewChatOther);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    configureMyChatViewHolder((FriendsViewHolder) holder, position);

    }

    private void configureMyChatViewHolder(final FriendsViewHolder friendsViewHolder, final int position) {
        final Users users = mFriends.get(position);
        friendsViewHolder.UserName.setText(users.nickname);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        ValueEventListener friendsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshotChild : dataSnapshot.getChildren()) {
                    String user = dataSnapshotChild.getValue().toString();
                    Log.d(TAG, "uid = "+user);
                    if(user.equals(users.uid))
                        friendsViewHolder.addfriend.setVisibility(View.GONE);
                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.child("friends").child(User.uid).addValueEventListener(friendsListener);
        View.OnClickListener lst = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("friends").child(User.uid).child(users.nickname).setValue(users.uid);
                mDatabase.child("friends").child(users.uid).child(User.nickname).setValue(User.uid);

            }
        };
        friendsViewHolder.addfriend.setOnClickListener(lst);
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
            cancelfriend.setVisibility(View.GONE);

        }
    }

}
