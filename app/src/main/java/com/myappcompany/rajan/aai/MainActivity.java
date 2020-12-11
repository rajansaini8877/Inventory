package com.myappcompany.rajan.aai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button mUserLogin;
    private Button mEngineerLogin;

    //private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        //mAuth = FirebaseAuth.getInstance();

        mUserLogin = (Button)findViewById(R.id.user_login_button);
        mUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mEngineerLogin = (Button)findViewById(R.id.engineer_login_button);
        mEngineerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null) {
//            Toast.makeText(MainActivity.this, currentUser.getEmail().toString(), Toast.LENGTH_SHORT).show();
//        }
//        else {
//            Toast.makeText(MainActivity.this, "=======", Toast.LENGTH_SHORT).show();
//        }
//    }
}
