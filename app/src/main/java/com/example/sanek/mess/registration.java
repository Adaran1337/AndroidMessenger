package com.example.sanek.mess;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.example.sanek.mess.Model.User;
import com.example.sanek.mess.Model.Users;
import com.example.sanek.mess.utils.NetworkConnectionUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class registration extends Activity {
EditText et_email,et_pass,et_pass_conf,et_nik;
Button btn_reg;
DatabaseReference myRefers;
TextView tv;
public DatabaseReference myRef;
    private FirebaseAuth mAuth;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        tv= findViewById(R.id.textView);
        et_email = findViewById(R.id.et_email_reg);
        et_nik= findViewById(R.id.et_nik);
        myRef= FirebaseDatabase.getInstance().getReference();
        et_pass = findViewById(R.id.et_pass_reg);
        et_pass_conf = findViewById(R.id.pass_conf);
        btn_reg = findViewById(R.id.btn_registration);
        mAuth = FirebaseAuth.getInstance();
        et_email.setText(getIntent().getStringExtra("email"));
            View.OnClickListener lst = new View.OnClickListener() {

                    @Override
                    public void onClick (View view){
                        if(NetworkConnectionUtil.isConnectedToInternet(getBaseContext())){

                        if(et_pass.getText().toString().equals(et_pass_conf.getText().toString())){
                            if(et_nik.getText().toString().length()<21) {
                                if (et_nik.getText().toString().length() > 1)
                                    registrationuser(et_email.getText().toString().trim(), et_pass.getText().toString());
                                else
                                    Snackbar.make(view, "Ваш ник слишком короткий! Минимум 2 символа!", Snackbar.LENGTH_LONG).show();
                            }else
                                Snackbar.make(view, "Ваш ник слишком длинный! Максимум 20 символов!", Snackbar.LENGTH_LONG).show();
                        }
                        else
                            Snackbar.make(view, "Пароли не совпадают!", Snackbar.LENGTH_LONG).show();

                    }
                    else
                        TSnackbar.make(findViewById(android.R.id.content),"Check your ethernet connection!",TSnackbar.LENGTH_LONG).show();
                    }
    };
            btn_reg.setOnClickListener(lst);
            }

    public void registrationuser(String email, String password) {
        final Intent intent = new Intent(registration.this,logpass.class);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    writeNewUser(task.getResult().getUser().getUid(), et_nik.getText().toString(),
                          task.getResult().getUser().getEmail());
                    Toast.makeText(registration.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                    startActivity(intent);

                } else
                    Toast.makeText(registration.this, "Регистрация провалена", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void writeNewUser(String userId, String name, String email) {
        class User{
            public String uid;
            public String email;
            public String nickname;

            public User(){
            }

            public User(String email,String nickname, String uid){
                this.email = email;
                this.nickname =nickname;
                this.uid = uid;

            }
        }
        User user = new User(email,name,userId);
        myRef.child("users").child(userId).setValue(user);
    }
}
