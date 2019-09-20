package com.example.sanek.mess.PrivateMessages;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.sanek.mess.Friend.AddFriends;
import com.example.sanek.mess.Model.Users;
import com.example.sanek.mess.R;

import java.util.List;

import static android.content.ContentValues.TAG;

public class AddChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Users> mFriends;
    public AddChatAdapter(List<Users> users){
        mFriends=users;
    }
    public void add(Users users) {
        mFriends.add(users);
        notifyItemInserted(mFriends.size() - 1);
    }

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View viewChatOther = layoutInflater.inflate(R.layout.friends, parent, false);

        return new FriendsViewHolder(viewChatOther);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final Users users = mFriends.get(position);
        ((FriendsViewHolder) holder).UserName.setText(users.nickname);
        ((FriendsViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent i = new Intent(((FriendsViewHolder) holder).itemView.getContext(),PrivateMessagesChatActivity.class);
                i.putExtra("uid",users.uid);
                i.putExtra("name",users.nickname);
                ((FriendsViewHolder) holder).itemView.getContext().startActivity(i);
                Log.d(TAG,"Was pressed User{ uid: "+users.nickname+", nickname: "+users.nickname+" }");
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mFriends != null) {
            return mFriends.size();
        } else
            return 0;
    }

    private static class FriendsViewHolder extends RecyclerView.ViewHolder {
        private TextView UserName;
        private ImageButton addfriend, cancelfriend;

private RecyclerView recyclerView;
        public FriendsViewHolder(View itemView) {
            super(itemView);
            UserName = itemView.findViewById(R.id.TvNickName);
            addfriend = itemView.findViewById(R.id.imageButtonAdd);
            cancelfriend = itemView.findViewById(R.id.imageButtonCancel);
            recyclerView = itemView.findViewById(R.id.recycler_view_chat);
            addfriend.setVisibility(View.GONE);
            cancelfriend.setVisibility(View.GONE);

        }
    }

}