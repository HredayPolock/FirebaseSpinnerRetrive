package com.example.firebasespinnerretrive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;

public class MainActivity extends AppCompatActivity {

    private Spinner spinner;
    private EditText editText;
    private Button button;
    DatabaseReference databaseReference;

    ValueEventListener listener;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.spinnerid);
        editText = findViewById(R.id.edittextid);
        button = findViewById(R.id.button);

        arrayList = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arrayList);
        spinner.setAdapter(arrayAdapter);


        databaseReference = FirebaseDatabase.getInstance().getReference("Spinner");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertdata();
            }
        });

        fetchdata();

    }


    private void insertdata() {

        String data = editText.getText().toString().trim();
        databaseReference.push().setValue(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        editText.setText("");

                        arrayList.clear();

                        fetchdata();
                        arrayAdapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "Data inserted", Toast.LENGTH_SHORT).show();


                    }
                });
    }


    private void fetchdata() {

        listener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())

                    arrayList.add(dataSnapshot.getValue().toString());
                    arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}