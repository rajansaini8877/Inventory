package com.myappcompany.rajan.aai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1beta1.WriteResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquipmentRecordList extends AppCompatActivity {

    private FirebaseFirestore db;

    private RecyclerView mRecordRecyclerView;
    private RecordAdapter mAdapter;
    private Button mAddButton;
    //private EditText mDateEditText;
    private EditText mPartEditText;
    private EditText mEnggNameEditText;
    private EditText mEnggContactEditText;

    private boolean isShown = false;

    private String data = new String();
    List<Record> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_record_list);

        db = FirebaseFirestore.getInstance();

        //mDateEditText = (EditText)findViewById(R.id.date_edit_text);
        mPartEditText = (EditText)findViewById(R.id.part_edit_text);
        mEnggNameEditText = (EditText)findViewById(R.id.engg_name_edit_text);
        mEnggContactEditText = (EditText)findViewById(R.id.engg_contact_edit_text);

        mAddButton = (Button)findViewById(R.id.add);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isShown) {
                    //mDateEditText.setVisibility(View.VISIBLE);
                    mPartEditText.setVisibility(View.VISIBLE);
                    mEnggNameEditText.setVisibility(View.VISIBLE);
                    mEnggContactEditText.setVisibility(View.VISIBLE);

                    mAddButton.setText("Done");
                    isShown = true;
                }
                else {
                    Date date = new Date();
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    String currentDate = df.format(date);
                    Map<String, Object> values = new HashMap<>();
                    values.put("date", currentDate.replaceAll("/", ""));
                    values.put("part_repaired", mPartEditText.getText().toString());
                    values.put("serviced_by", mEnggNameEditText.getText().toString());
                    values.put("contact", mEnggContactEditText.getText().toString());

                    Log.d("date", values.get("date").toString());

                    db.collection("/equipment_data/"+data+"/record")
                            .document(values.get("date").toString())
                            .set(values)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(EquipmentRecordList.this, "Success!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("TAG", "Error adding document", e);
                                }
                            });

                    //mDateEditText.setVisibility(View.GONE);
                    mPartEditText.setVisibility(View.GONE);
                    mEnggNameEditText.setVisibility(View.GONE);
                    mEnggContactEditText.setVisibility(View.GONE);
                    mAddButton.setText("add");
                    fetchData(db);
                }
            }
        });
        mRecordRecyclerView = findViewById(R.id.recycler_view);
        mRecordRecyclerView.setLayoutManager(new LinearLayoutManager(EquipmentRecordList.this));

        list = new ArrayList<>();

        data = getIntent().getExtras().getString("EQUIPMENT_UUID");

        fetchData(db);

    }

    private void fetchData(FirebaseFirestore db) {
        db.collection("/equipment_data/"+data+"/record")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            list.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String date = document.getString("date");
                                String part = document.getString("part_repaired");
                                String name = document.getString("serviced_by");
                                String contact = document.getString("contact");
                                Record record = new Record(date, part, name, contact);
                                list.add(record);
                                Log.d("tag", document.getId() + " => " + document.getData());
                            }
                            updateUi();
                        }
                        else {
                            Log.d("tag", "Error!");
                        }
                    }
                });
    }

    private void updateUi() {
        Log.d("size", list.size()+" ");

        if(mAdapter==null) {
            mAdapter = new RecordAdapter(list);
            mRecordRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setRecords(list);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class RecordHolder extends RecyclerView.ViewHolder {

        private TextView mDate;
        private TextView mPart;
        private TextView mName;
        private TextView mContact;
        private Record mRecord;

        public RecordHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_record_details, parent, false));

            mDate = (TextView)itemView.findViewById(R.id.date);
            mPart = (TextView)itemView.findViewById(R.id.part_repaired);
            mName = (TextView)itemView.findViewById(R.id.engg_name);
            mContact = (TextView)itemView.findViewById(R.id.engg_contact);
        }

        public void bind(Record record) {
            mRecord = record;
            mDate.setText("Date: "+mRecord.getDate());
            mPart.setText("Part Serviced: "+mRecord.getPart());
            mName.setText("Name of Engineer: "+mRecord.getName());
            mContact.setText("Contact of Engineer "+mRecord.getContact());
        }
    }

    private class RecordAdapter extends RecyclerView.Adapter<RecordHolder> {

        private List<Record> mRecords;

        public RecordAdapter(List<Record> records) {
            mRecords = records;
        }

        @NonNull
        @Override
        public RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(EquipmentRecordList.this);
            return new RecordHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull RecordHolder holder, int position) {
            Record record = mRecords.get(position);
            holder.bind(record);
        }

        @Override
        public int getItemCount() {
            return mRecords.size();
        }

        public void setRecords(List<Record> records) {
            mRecords = records;
        }
    }

}
