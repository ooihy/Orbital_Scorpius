package com.example.shanna.orbital2;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
//alt+enter to import automatically

public class SignupActivity extends AppCompatActivity {

    private EditText mEditTextPw;
    private EditText mEditTextEmail;
    private EditText mEditTextName;
    private EditText mEditTextPhone;
    private EditText mEditTextUsername;
    private EditText mEditTextIC;
    private Button mBtnNext;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup); //set layout [activity_signup] for this activity

        //Initialise widgets
        mEditTextEmail = findViewById(R.id.emailEditText);
        mEditTextPw = findViewById(R.id.passwordEditText);
        mEditTextName = findViewById(R.id.nameEditText);
        mEditTextUsername = findViewById(R.id.usernameEditText);
        mEditTextPhone = findViewById(R.id.phoneEditText);
        mEditTextIC = findViewById(R.id.icEditText);
        mBtnNext = (Button) findViewById(R.id.mBtnNext);

        // Firebase Auth Instance
        auth = FirebaseAuth.getInstance();


        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = mEditTextName.getText().toString().trim(); //get from textbox
                String username = mEditTextUsername.getText().toString().trim(); //get from textbox
                String phoneNum = mEditTextPhone.getText().toString().trim(); //get from textbox
                String email = mEditTextEmail.getText().toString().trim(); //get from textbox
                String password = mEditTextPw.getText().toString().trim(); //get from textbox
                String ic = mEditTextIC.getText().toString().trim(); //get from textbox

                // Check if email is empty
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignupActivity.this, "Enter email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if ic is empty
                if (TextUtils.isEmpty(ic)) {
                    Toast.makeText(SignupActivity.this, "Enter IC/Passport number", Toast.LENGTH_SHORT).show();
                    return;
                }

                // check if username is empty
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(SignupActivity.this, "Enter username", Toast.LENGTH_SHORT).show();
                    return;
                }

                // check if full name is empty
                if (TextUtils.isEmpty(fullName)) {
                    Toast.makeText(SignupActivity.this, "Enter full name", Toast.LENGTH_SHORT).show();
                    return;
                }

                // check if phone number is empty
                if (TextUtils.isEmpty(phoneNum)) {
                    Toast.makeText(SignupActivity.this, "Enter phone number", Toast.LENGTH_SHORT).show();
                    return;
                }

                // check if phone number is of valid length
                if (phoneNum.length() != 8) {
                    Toast.makeText(SignupActivity.this, "Enter valid phone number", Toast.LENGTH_SHORT).show();
                    return;
                }

                // check if password is empty
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(SignupActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                //check if password is alphanumeric
                if(!password.matches("^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z])[a-zA-Z0-9]+$")){
                    Toast.makeText(SignupActivity.this, "Password must contain one upper and lower case letter and one number", Toast.LENGTH_LONG).show();
                    return;
                }

                // Create a new user
                auth.createUserWithEmailAndPassword(email, password).
                        addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Signup successful, got to main activity
                                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                    // End the activity
                                    finish();
                                }
                            }
                        });
            }
        });

    }
}
