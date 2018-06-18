package com.example.shanna.orbital2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.lang.System.load;


public class Profile extends AppCompatActivity {

    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;

    //Android layout
    private CircleImageView mDisplayImage;
    private EditText mEditTextName;
    private EditText mEditTextLocation;
    private EditText mEditTextProfession;
    private EditText mEditTextDescription;
    private EditText mEditTextWebsite;
    private EditText mEditTextPhoneNum;
    private Button mButtonDone;
    private Button mButtonUpload;

    //Image Upload
    private static final int GALLERY_PICK = 1;
    String download_url;

    //Create storage reference in firebase
    private StorageReference mStorageRef;

    //Progress button
    private ProgressDialog mProgress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mDisplayImage = (CircleImageView)findViewById(R.id.Avatar);
        mEditTextName = (EditText) findViewById(R.id.fullName);
        mEditTextLocation = (EditText) findViewById(R.id.Location);
        mEditTextProfession = (EditText) findViewById(R.id.Profession);
        mEditTextWebsite = (EditText) findViewById(R.id.Website);
        mEditTextPhoneNum = (EditText) findViewById(R.id.PhoneNumber);
        mEditTextDescription = (EditText) findViewById(R.id.AboutMe);
        mButtonDone = (Button)findViewById(R.id.btnDone);
        mButtonUpload = (Button)findViewById(R.id.btnUpload);

        //Image -> Reference to Firebase storage root
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //User data -> Reference to Firebase database root
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        //first set the value inside the text boxes to contain information that was set previously
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //For image, we need to use picasso library

                String image = dataSnapshot.child("Image").getValue().toString();
                Picasso.get().load(image).into(mDisplayImage);

                mEditTextName.setText(dataSnapshot.child("Full Name").getValue().toString());
                mEditTextLocation.setText(dataSnapshot.child("Location").getValue().toString());
                mEditTextProfession.setText(dataSnapshot.child("Profession").getValue().toString());
                mEditTextWebsite.setText(dataSnapshot.child("Website").getValue().toString());
                mEditTextPhoneNum.setText(dataSnapshot.child("Phone number").getValue().toString());
                mEditTextDescription.setText(dataSnapshot.child("Description").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //To update firebase when the done button is clicked. Supposed to lead user to profile view
        //but currently profile view is not working
        mButtonDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                //Progress
                mProgress = new ProgressDialog(Profile.this);
                mProgress.setTitle("Saving Changes");
                mProgress.setMessage("Please wait while the changes are being saved");
                mProgress.show();

                mUserDatabase.child("Full Name").setValue(mEditTextName.getText().toString());
                mUserDatabase.child("Location").setValue(mEditTextLocation.getText().toString());
                mUserDatabase.child("Profession").setValue(mEditTextProfession.getText().toString());
                mUserDatabase.child("Website").setValue(mEditTextWebsite.getText().toString());
                mUserDatabase.child("Phone number").setValue(mEditTextPhoneNum.getText().toString());
                mUserDatabase.child("Description").setValue(mEditTextDescription.getText().toString());

                mProgress.setTitle("Done");
                mProgress.dismiss();

                // if update information is successful, go to view Profile
                startActivity(new Intent(Profile.this, ViewProfile.class));
                // End the activity
                finish();

            }

        });


        //This is for when user wants to upload their photos
        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                // This is for choosing image from google drive i think


                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*"); //define the type->Here we only want to pick images
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGE" ), GALLERY_PICK);

                /*   The 3 code of lines below also works. It can replace the 4 lines above and still
                    work.
                // start picker to get image for cropping and then use the image in cropping activity
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(Profile.this);

                */
            }
        });

    }


    //Enable user to crop the image -> Do this by adding a crop library
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        //Check request code
        if(requestCode == GALLERY_PICK && resultCode==RESULT_OK){

            Uri imageUri = data.getData();

            // start cropping activity for pre-acquired image saved on the device
            CropImage.activity(imageUri)
                     .setAspectRatio(1,1)
                     .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                //Progress dialogue
                mProgress = new ProgressDialog(Profile.this);
                mProgress.setTitle("Uploading image...");
                mProgress.setMessage("Please wait while we upload and process the image");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.show();


                Uri resultUri = result.getUri(); //uri of cropped image

                //change the icon directly on the update information page
                mDisplayImage.setImageURI(resultUri);

                //Now we should store that image in Firebase storage
                //Below is where we are going to store the file
                StorageReference filepath = mStorageRef.child("Avatar").child(mCurrentUser.getUid()+ ".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(Profile.this, "Successful in uploading to storage", Toast.LENGTH_LONG).show();

                            // to get the download_url
                            mStorageRef.child("Avatar").child(mCurrentUser.getUid() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    download_url = uri.toString();
                                    //to update the database image value
                                    mUserDatabase.child("Image").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                mProgress.dismiss(); //To remove the progress update or else, it would be up there forever
                                                Toast.makeText(Profile.this, "Successful in updating database", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                            });

                        }else{
                            Toast.makeText(Profile.this, "Error", Toast.LENGTH_LONG).show();
                            mProgress.dismiss();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}

