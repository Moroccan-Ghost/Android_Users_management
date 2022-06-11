package com.example.projetmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddUserActivity extends AppCompatActivity {

    // creating variables for our button, edit text,
    // firebase database, database reference, progress bar.
    private Button addUserBtn;
    private TextInputEditText userNameEdt, userDescEdt, ageEdt, roleEdt, LinkEdt;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private ProgressBar loadingPB;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        // initializing variables.
        addUserBtn = findViewById(R.id.idBtnAddUser);
        userNameEdt = findViewById(R.id.idEdtUserName);
        userDescEdt = findViewById(R.id.idEdtDescription);
        ageEdt = findViewById(R.id.idEdtAge);
        roleEdt = findViewById(R.id.idEdtRole);
        LinkEdt = findViewById(R.id.idEdtLink);
        loadingPB = findViewById(R.id.idPBLoading);
        firebaseDatabase = FirebaseDatabase.getInstance();

        //  creating our database reference.
        databaseReference = firebaseDatabase.getReference("Users");

        // adding click listener for add user button.
        addUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPB.setVisibility(View.VISIBLE);
                // getting data from edit text.
                String userName = userNameEdt.getText().toString();
                String userDesc = userDescEdt.getText().toString();
                String userAge = ageEdt.getText().toString();
                String userRole = roleEdt.getText().toString();
                String userLink = LinkEdt.getText().toString();
                userID = userName;
                // passing all data to modal class.
                UserRVModal userRVModal = new UserRVModal(userID, userName, userDesc, userAge, userRole, userLink);


                // calling a add value event to pass data to firebase database.
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //setting data in our firebase database.
                        databaseReference.child(userID).setValue(userRVModal);
                        // displaying a  message.
                        Toast.makeText(AddUserActivity.this, "User Added..", Toast.LENGTH_SHORT).show();
                        // starting a main activity.
                        startActivity(new Intent(AddUserActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // displaying a failure message.
                        Toast.makeText(AddUserActivity.this, "Fail to add User..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}