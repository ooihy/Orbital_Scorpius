package com.example.shanna.orbital2;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProfile extends AppCompatActivity {

    private DatabaseReference mDatabase;

    //Android layout
    private CircleImageView mProfileDisplayImage;
    private TextView mProfileName;
    private TextView mProfileLocation;
    private TextView mProfileProfession;
    private TextView mUserType;
    private TextView mProfileDescription;
    private TextView mProfileWebsite;
    private TextView mProfilePhoneNum;
    private TextView mProfileEmail;


    private RatingBar mRating;
    private Button mCollab;

    //Create storage reference in firebase
    private StorageReference mStorageRef;

    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        mProfileDisplayImage = (CircleImageView)findViewById(R.id.profileAvatar);
        mProfileName =  findViewById(R.id.profileFullName);
        mProfileProfession =  findViewById(R.id.profileProfession);
        mUserType = findViewById(R.id.profileUserType);
        mProfileDescription =  findViewById(R.id.profileAboutMe);
        mProfileLocation =  findViewById(R.id.profileLocation);
        mProfilePhoneNum =  findViewById(R.id.profilePhoneNumber);
        mProfileEmail =  findViewById(R.id.profileEmail);
        mProfileWebsite =  findViewById(R.id.profileWebsite);
        mRating = findViewById(R.id.profileRating);
        mCollab = findViewById(R.id.profileBtnCollaborate);


        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Image -> Reference to Firebase storage root
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //User data -> Reference to Firebase database root
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        //Get user_id
        String current_uid = mCurrentUser.getUid();

        //first set the value inside the text boxes to contain information that was set previously
        mDatabase.child("Users").child(current_uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //For image, we need to use picasso library
                String image = dataSnapshot.child("Image").getValue().toString();
                Picasso.get().load(image).into(mProfileDisplayImage);

                mProfileName.setText(dataSnapshot.child("FullName").getValue().toString());
                mProfileProfession.setText(dataSnapshot.child("Profession").getValue().toString());
                mUserType.setText(dataSnapshot.child("UserType").getValue().toString());
                mProfileDescription.setText(dataSnapshot.child("Description").getValue().toString());
                mProfileLocation.setText(dataSnapshot.child("Location").getValue().toString());
                mProfilePhoneNum.setText(dataSnapshot.child("PhoneNum").getValue().toString());
                mProfileEmail.setText(dataSnapshot.child("Email").getValue().toString());
                mProfileWebsite.setText(dataSnapshot.child("Website").getValue().toString());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


/*
        //When user click on chat -> Trigger chat activity
        mCollab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //by right should lead to Chat.java. But currently Chat.java not done, so lead to Main
                Intent intent = new Intent(ViewProfile.this, MainActivity.class);
                startActivity(intent);
            }
        });
*/
/*

        //When user click on website -> Allow user to go to website
        mProfileWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String website = "http://www.google.com";
                Uri webaddress = Uri.parse(website);

                Intent goTo = new Intent(Intent.ACTION_VIEW, webaddress);
                if (goTo.resolveActivity(getPackageManager()) != null) {
                    startActivity(goTo);
                }
            }
        });
*/
        //Need to add another button -> When user request for collab -> Lead to notification for
        // the other user to know -> Maybe send a message on chat? So lead to chat activity with a
        // preset message?


    }


}
