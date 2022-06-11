package com.example.projetmobileapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements UserRVAdapter.UserClickInterface{

    // creating variables for fab, firebase database,
    // progress bar, list, adapter,firebase auth,
    // recycler view and relative layout.
    private FloatingActionButton addUserFAB;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private RecyclerView UserRV;
    private FirebaseAuth mAuth;
    private ProgressBar loadingPB;
    private ArrayList<UserRVModal> UserRVModalArrayList;
    private UserRVAdapter UserRVAdapter;
    private RelativeLayout homeRL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializing variables.
        UserRV = findViewById(R.id.idRVUsers);
        homeRL = findViewById(R.id.idRLBSheet);
        loadingPB = findViewById(R.id.idPBLoading);
        addUserFAB = findViewById(R.id.idFABAddUser);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        UserRVModalArrayList = new ArrayList<>();

        //getting database reference.
        databaseReference = firebaseDatabase.getReference("Users");

        //adding a click listener for floating action button.
        addUserFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opening a new activity for adding User.
                Intent i = new Intent(MainActivity.this, AddUserActivity.class);
                startActivity(i);
            }
        });
        //initializing adapter class.
        UserRVAdapter = new UserRVAdapter(UserRVModalArrayList, this, this::onUserClick);

        // setting layout malinger to recycler view.
        UserRV.setLayoutManager(new LinearLayoutManager(this));

        // setting adapter to recycler view.
        UserRV.setAdapter(UserRVAdapter);

        //calling a method to fetch all Users from database.
        getUsers();
    }

    private void getUsers() {
        //clearing the list.
        UserRVModalArrayList.clear();

        //calling add child event listener method to read the data.
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //hiding our progress bar.
                loadingPB.setVisibility(View.GONE);
                // adding snapshot to array list on below line.
                UserRVModalArrayList.add(snapshot.getValue(UserRVModal.class));
                // notifying adapter that data has changed.
                UserRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // this method is called when new child is added
                //notifying adapter and making progress bar visibility as gone.
                loadingPB.setVisibility(View.GONE);
                UserRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // notifying adapter when child is removed.
                UserRVAdapter.notifyDataSetChanged();
                loadingPB.setVisibility(View.GONE);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // notifying adapter when child is moved.
                UserRVAdapter.notifyDataSetChanged();
                loadingPB.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onUserClick(int position) {
        // calling method to display a bottom sheet.
        displayBottomSheet(UserRVModalArrayList.get(position));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // adding a click listener for option selected.
        int id = item.getItemId();
        switch (id) {
            case R.id.idLogOut:
                // displaying a message on user logged out inside on click.
                Toast.makeText(getApplicationContext(), "User Logged Out", Toast.LENGTH_LONG).show();

                //signing out user.
                mAuth.signOut();

                //opening login activity.
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflating menu file for displaying menu options.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressLint("SetTextI18n")
    private void displayBottomSheet(UserRVModal modal) {
        // creating bottom sheet dialog.
        final BottomSheetDialog bottomSheetTeachersDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);

        // inflating layout file for our bottom sheet.
        View layout = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_layout, homeRL);

        // setting content view for bottom sheet.
        bottomSheetTeachersDialog.setContentView(layout);

        //setting a cancelable
        bottomSheetTeachersDialog.setCancelable(false);
        bottomSheetTeachersDialog.setCanceledOnTouchOutside(true);

        // calling a method to display bottom sheet.
        bottomSheetTeachersDialog.show();

        //creating variables for text view and image view inside bottom sheet
        // and initialing them with their ids.
        TextView UserNameTV = layout.findViewById(R.id.idTVUserName);
        TextView UserDescTV = layout.findViewById(R.id.idTVUserDesc);
        TextView suitedForTV = layout.findViewById(R.id.idTVUserRole);
        TextView priceTV = layout.findViewById(R.id.idTVUserAge);
        ImageView UserIV = layout.findViewById(R.id.idIVUser);

        //setting data to different views.
        UserNameTV.setText(modal.getUserName());
        UserDescTV.setText(modal.getUserDescription());
        suitedForTV.setText("Role :  " + modal.getUserRole());
        priceTV.setText("Age : " + modal.getUserAge() + " years Old");
        Picasso.get().load(modal.getUserLink()).into(UserIV);
        Button viewBtn = layout.findViewById(R.id.idBtnVIewDetails);
        Button editBtn = layout.findViewById(R.id.idBtnEditUser);

        // adding on click listener for edit button.
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //opening our EditUserActivity.
                Intent i = new Intent(MainActivity.this, EditUserActivity.class);
                //passing User modal
                i.putExtra("User", modal);
                startActivity(i);
            }
        });
        // adding click listener for the view button.
        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navigating to browser for displaying User details from its url
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(modal.getUserLink()));
                startActivity(i);
            }
        });
    }
}