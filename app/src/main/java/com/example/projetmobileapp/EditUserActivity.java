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

import java.util.HashMap;
import java.util.Map;

public class EditUserActivity extends AppCompatActivity {

    // creating variables for our edit text, firebase database,
    // database reference, user rv modal,progress bar.
    private TextInputEditText userNameEdt, userDescEdt, userAgeEdt, userRoleEdt,  userLinkEdt;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    UserRVModal userRVModal;
    private ProgressBar loadingPB;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        // initializing variables.
        Button adduserBtn = findViewById(R.id.idBtnAddUser);
        userNameEdt = findViewById(R.id.idEdtUserName);
        userDescEdt = findViewById(R.id.idEdtDescription);
        userAgeEdt = findViewById(R.id.idEdtAge);
        userRoleEdt = findViewById(R.id.idEdtRole);
        userLinkEdt = findViewById(R.id.idEdtLink);
        loadingPB = findViewById(R.id.idPBLoading);
        firebaseDatabase = FirebaseDatabase.getInstance();

        //getting modal class
        userRVModal = getIntent().getParcelableExtra("user");
        Button deleteUserBtn = findViewById(R.id.idBtnDeleteUser);

        if (userRVModal != null) {
            //setting data to edit text from modal class.
            userNameEdt.setText(userRVModal.getUserName());
            userAgeEdt.setText(userRVModal.getUserAge());
            userRoleEdt.setText(userRVModal.getUserRole());
            userLinkEdt.setText(userRVModal.getUserLink());
            userDescEdt.setText(userRVModal.getUserDescription());
            userID = userRVModal.getUserId();
        }

        //initialing database reference and adding a child as user id.
        databaseReference = firebaseDatabase.getReference("Users").child(userID);

        // adding click listener for add user button.
        adduserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // making progress bar visible.
                loadingPB.setVisibility(View.VISIBLE);

                //getting data from our edit text.
                String userName = userNameEdt.getText().toString();
                String userDesc = userDescEdt.getText().toString();
                String userAge = userAgeEdt.getText().toString();
                String userRole = userRoleEdt.getText().toString();
                String userLink = userLinkEdt.getText().toString();

                //creating a map for passing a data using key : value.
                Map<String, Object> map = new HashMap<>();
                map.put("userName", userName);
                map.put("userDescription", userDesc);
                map.put("userAge", userAge);
                map.put("userRole", userRole);
                map.put("userLink", userLink);
                map.put("userId", userID);

                //calling a database reference on add value event listener and on data change method
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // making progress bar visibility as gone.
                        loadingPB.setVisibility(View.GONE);

                        // adding a map to database.
                        databaseReference.updateChildren(map);

                        // displaying a message.
                        Toast.makeText(EditUserActivity.this, "User Updated..", Toast.LENGTH_SHORT).show();
                        // opening a new activity after updating user.
                        startActivity(new Intent(EditUserActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // displaying a failure message.
                        Toast.makeText(EditUserActivity.this, "Fail to update User..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // adding a click listener for delete user button.
        deleteUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //method to delete user.
                deleteUser();
            }
        });

    }

    private void deleteUser() {
        // calling a method to delete the user.
        databaseReference.removeValue();

        // displaying a message.
        Toast.makeText(this, "User Deleted..", Toast.LENGTH_SHORT).show();

        // opening the main activity .
        startActivity(new Intent(EditUserActivity.this, MainActivity.class));
    }
}