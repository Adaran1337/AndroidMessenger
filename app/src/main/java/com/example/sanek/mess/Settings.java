package com.example.sanek.mess;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sanek.mess.Model.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment {
    TextView email, nik, uid;
    public Settings() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        email = v.findViewById(R.id.Dataemail);
        nik = v.findViewById(R.id.Datanickname);
        uid = v.findViewById(R.id.Datatoken);
                email.setText(User.email);
                uid.setText(User.uid);
                nik.setText(User.nickname);

        return v;
    }
}
