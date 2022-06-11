package com.example.projetmobileapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText userNameEdt, passwordEdt, confirmPwdEdt;
    private TextView loginTV;
    private Button registerBtn;
    private FirebaseAuth mAuth;
    private ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // initializing variables.
        userNameEdt = findViewById(R.id.idEdtUserName);
        passwordEdt = findViewById(R.id.idEdtPassword);
        loadingPB = findViewById(R.id.idPBLoading);
        confirmPwdEdt = findViewById(R.id.idEdtConfirmPassword);
        loginTV = findViewById(R.id.idTVLoginUser);
        registerBtn = findViewById(R.id.idBtnRegister);
        mAuth = FirebaseAuth.getInstance();

        // adding on click for login.
        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opening a login activity on clicking login text.
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        // adding click listener for register button.
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hide progress bar.
                loadingPB.setVisibility(View.VISIBLE);

                // getting data from edit text.
                String userName = userNameEdt.getText().toString();
                String pwd = passwordEdt.getText().toString();
                String cnfPwd = confirmPwdEdt.getText().toString();

                // checking if the password and confirm password is equal or not.
                if (!pwd.equals(cnfPwd)) {
                    Toast.makeText(RegisterActivity.this, "Passwords are not equals ...", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(userName) && TextUtils.isEmpty(pwd) && TextUtils.isEmpty(cnfPwd)) {

                    // checking if the text fields are empty or not.
                    Toast.makeText(RegisterActivity.this, "Please enter your informations..", Toast.LENGTH_SHORT).show();
                } else {

                    // on below line we are creating a new user by passing email and password.
                    mAuth.createUserWithEmailAndPassword(userName, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //if the task is success or not.
                            if (task.isSuccessful()) {

                                //hiding progress bar and opening a login activity.
                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(RegisterActivity.this, "User Registered..", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(i);
                                finish();
                            } else {

                                //displaying a failure message.
                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(RegisterActivity.this, "Fail to register new user..", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}