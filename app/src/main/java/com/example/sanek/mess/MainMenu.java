package com.example.sanek.mess;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sanek.mess.ChatAll.AllChat;
import com.example.sanek.mess.Friend.Friends;
import com.example.sanek.mess.Model.User;
import com.example.sanek.mess.PrivateMessages.PrivateMessagesChatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.sanek.mess.PrivateMessages.PrivateMessages;

public class MainMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    TextView tvnick,tvemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Сообщения");
        setContentView(R.layout.activity_main_menu);
        com.google.firebase.database.DatabaseReference myRefers = FirebaseDatabase.getInstance().getReference();
        myRefers.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User.email=dataSnapshot.child("email").getValue().toString();
                User.uid=dataSnapshot.child("uid").getValue().toString();
                User.nickname=dataSnapshot.child("nickname").getValue().toString();
                loaded_info();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        tvnick = header.findViewById(R.id.NikName);
        tvemail = header.findViewById(R.id.tvemail);

    }
private void loaded_info(){
    setTitle("Сообщеиня");
    tvnick.setText(User.nickname);
    tvemail.setText(User.email);
    PrivateMessages messenge = new PrivateMessages();
    FragmentManager fragmentManage=getSupportFragmentManager();
    fragmentManage.beginTransaction().replace(R.id.fragment,messenge).commit();
}
    private static long back_pressed;

    @Override
    public void onBackPressed()
    {   if (back_pressed + 2000 > System.currentTimeMillis()){
        moveTaskToBack(true);
        finish();
        System.exit(0);}
    else Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }
    public void onDestroy() {

        super.onDestroy();
        System.exit(0);
    }

    private void openQuitDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                MainMenu.this);
        quitDialog.setTitle("Вы уверены что хотите сменить аккаунт?");
        final Intent i = new Intent(MainMenu.this,logpass.class);
        quitDialog.setPositiveButton("Да!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                startActivity(i);
            }
        });

        quitDialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        quitDialog.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case(R.id.nav_all_chat):
                setTitle("Общий чат");
                AllChat chat = new AllChat();
                FragmentManager fragmentManager=getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment,chat).commit();
                break;
            case(R.id.nav_messenge):
                setTitle("Сообщеиня");
                PrivateMessages messenge = new PrivateMessages();
                FragmentManager fragmentManage=getSupportFragmentManager();
                fragmentManage.beginTransaction().replace(R.id.fragment,messenge).commit();
                break;
            case(R.id.nav_friend):
                setTitle("Друзья");
                Friends friend = new Friends();
                FragmentManager fragmentManag=getSupportFragmentManager();
                fragmentManag.beginTransaction().replace(R.id.fragment,friend).commit();
                break;
            case(R.id.nav_settings):
                setTitle("Настройки");
                Settings settings = new Settings();
                FragmentManager fragmentMana=getSupportFragmentManager();
                fragmentMana.beginTransaction().replace(R.id.fragment,settings).commit();
                break;
            case(R.id.nav_reg):
                openQuitDialog();
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class DatabaseReference {
    }

}
