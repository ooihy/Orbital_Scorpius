package com.example.shanna.orbital2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class Profile extends AppCompatActivity {

    private static final int GET_FROM_GALLERY = 1;
    private ImageView mImageViewPic;
    private EditText mEditTextName;
    private EditText mEditTextLocation;
    private EditText mEditTextProfession;
    private EditText mEditTextDescription;
    private EditText mEditTextWebsite;
    private Button mBtnChoose;
    private Button mBtnUpload;
    private Button mBtnDone;
    private Uri filePath;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mImageViewPic = findViewById(R.id.imageView7);

        mEditTextName = findViewById(R.id.editText4);
        mEditTextLocation = findViewById(R.id.editText5);
        mEditTextProfession = findViewById(R.id.editText6);
        mEditTextDescription = findViewById(R.id.editText3);
        mEditTextWebsite = findViewById(R.id.editText);

        mBtnUpload = findViewById(R.id.button7);
        mBtnChoose = findViewById(R.id.button);
        mBtnDone = findViewById(R.id.btnDone);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        // choose image to upload
        mBtnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        // upload chosen image
        mBtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = mEditTextName.getText().toString().trim(); //get from textbox
                String location = mEditTextLocation.getText().toString().trim(); //get from textbox
                String profession = mEditTextProfession.getText().toString().trim(); //get from textbox
                String website = mEditTextWebsite.getText().toString().trim(); //get from textbox
                String description = mEditTextDescription.getText().toString().trim(); //get from textbox
                Uri image = null;

                // Check if full name is empty
                if (TextUtils.isEmpty(fullName)) {
                    Toast.makeText(Profile.this, "Enter full name", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if location is empty
                if (TextUtils.isEmpty(location)) {
                    Toast.makeText(Profile.this, "Enter location", Toast.LENGTH_SHORT).show();
                    return;
                }

                // check if profession is empty
                if (TextUtils.isEmpty(profession)) {
                    Toast.makeText(Profile.this, "Enter profession", Toast.LENGTH_SHORT).show();
                    return;
                }


                // check if description is empty
                if (TextUtils.isEmpty(description)) {
                    Toast.makeText(Profile.this, "Enter short description", Toast.LENGTH_SHORT).show();
                    return;
                }

                mDatabase.child("Name").setValue(fullName);
                mDatabase.child("Location").setValue(location);
                mDatabase.child("Profession").setValue(profession);
                mDatabase.child("Description").setValue(description);
                mDatabase.child("Website").setValue(website);

                startActivity(new Intent(Profile.this, MainActivity.class));
                finish();
            }

        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, GET_FROM_GALLERY);
    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_FROM_GALLERY && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            mImageViewPic.setImageURI(filePath);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                mImageViewPic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


