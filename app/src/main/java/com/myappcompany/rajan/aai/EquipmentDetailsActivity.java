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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EquipmentDetailsActivity extends AppCompatActivity {

    private TextView mSerialNumber;
    private TextView mDOI;
    private TextView mCategory;
    private TextView mWarranty;
    private Button mHistory;
    private String data = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_details);

        data = getIntent().getExtras().getString("EQUIPMENT_UUID");
        mSerialNumber = (TextView)findViewById(R.id.serial_number);
        mDOI = (TextView)findViewById(R.id.doi);
        mCategory = (TextView)findViewById(R.id.category);
        mHistory = (Button)findViewById(R.id.history_button);
        mWarranty = (TextView)findViewById(R.id.warranty);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("equipment_data")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getId().equals(data)) {
                                    mSerialNumber.setText(document.getString("sn"));
                                    String instDate = document.getString("date_of_installation");
                                    Date currentDate = new Date();
                                    SimpleDateFormat
                                            sdfo
                                            = new SimpleDateFormat("yyyyMMdd");
                                    String currentDateString = sdfo.format(currentDate);
                                    int currentDateInt = Integer.parseInt(currentDateString);
                                    int instDateInt = Integer.parseInt(instDate.toString().replaceAll("/", ""));
                                    int temp = Integer.parseInt(document.getString("warranty_period"));
                                    instDateInt += temp;
                                    StringBuilder sb = new StringBuilder(String.valueOf(instDateInt));
                                    sb.reverse();
                                    int tillWarranty = Integer.parseInt(sb.toString());

                                    if(currentDateInt<=tillWarranty) {
                                        mWarranty.setText("Yes");
                                    }
                                    else {
                                        mWarranty.setText("No");
                                    }

                                    mDOI.setText(instDate);
                                    mCategory.setText(document.getString("category"));


                                }
                                Log.d("tag", document.getId() + " => " + document.getData());
                            }
                        }
                        else {
                            Log.d("tag", "Error!");
                        }
                    }
                });

        db.collection("/equipment_data/"+data+"/record")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("tag", document.getId() + " => " + document.getData());
                            }
                        }
                        else {
                            Log.d("tag", "Error!");
                        }
                    }
                });

        mHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EquipmentDetailsActivity.this, EquipmentRecordList.class);
                intent.putExtra("EQUIPMENT_UUID", data);
                startActivity(intent);
            }
        });

    }
}
