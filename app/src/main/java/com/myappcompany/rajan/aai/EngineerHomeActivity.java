package com.myappcompany.rajan.aai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class EngineerHomeActivity extends AppCompatActivity {

    private TextView mName;
    private TextView mCategory;
    private Button mScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engineer_home);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        mName = (TextView)findViewById(R.id.name);
        mCategory = (TextView)findViewById(R.id.category);
        mScan = (Button)findViewById(R.id.scan_code);

        db.collection("engineer_data")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getId().equals(currentUser.getEmail().toString())) {
                                    mName.setText(document.getString("Name"));
                                    mCategory.setText(document.getString("Category"));
                                }
                                //Log.d("tag", document.getId() + " => " + document.getData());
                            }
                        }
                        else {
                            Log.d("tag", "Error!");
                        }
                    }
                });

        mScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EngineerHomeActivity.this,ScannerBarcodeActivity.class));
            }
        });

    }
}
