package com.example.sanek.mess;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.example.sanek.mess.utils.NetworkConnectionUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class logpass extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Button btn_sign, btn_reg;
    private EditText ETemail;
    private EditText ETpassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logpass);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            final Intent app = new Intent(logpass.this, MainMenu.class);
            startActivity(app);
        }
        ETemail = findViewById(R.id.et_email_reg);
        ETpassword = findViewById(R.id.et_pass_reg);
        final Intent intent = new Intent(logpass.this, registration.class);
        btn_sign = findViewById(R.id.btn_sign_in);
        btn_reg = findViewById(R.id.btn_registration);
        View.OnClickListener lst = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NetworkConnectionUtil.isConnectedToInternet(getBaseContext())){
                switch (view.getId()) {
                    case R.id.btn_sign_in:
                        if (!validateForm()) {
                            return;
                        }
                        signin(ETemail.getText().toString().trim(), ETpassword.getText().toString());
                        break;
                    case R.id.btn_registration:
                        intent.putExtra("email", ETemail.getText().toString());
                        startActivity(intent);
                        break;
                }
                }
                else
                    TSnackbar.make(findViewById(android.R.id.content),"Check your ethernet connection!",TSnackbar.LENGTH_LONG).show();
            }
        };
        btn_sign.setOnClickListener(lst);
        btn_reg.setOnClickListener(lst);

    }
    public void signin(String email, String password) {
        final Intent i = new Intent(logpass.this, MainMenu.class);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(logpass.this, "Aвторизация успешна", Toast.LENGTH_SHORT).show();
                    startActivity(i);
                } else
                    Toast.makeText(logpass.this, "Aвторизация провалена", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private boolean validateForm() {

        boolean result = true;

        if (TextUtils.isEmpty(ETemail.getText().toString())) {
            ETemail.setError("Required");
            result = false;
        } else {
            ETemail.setError(null);
            if (ETemail.getText().toString().contains("@"))

                ETemail.setError(null);

            else {

                ETemail.setError("isn't email");
                result=false;

            }
            return result;
        }

        if (TextUtils.isEmpty(ETpassword.getText().toString())) {
            ETpassword.setError("Required");
            result = false;
        } else {
            ETpassword.setError(null);
        }

        return result;
    }


}
