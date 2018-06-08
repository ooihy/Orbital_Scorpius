package com.example.shanna.orbital2;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class ViewProfile extends AppCompatActivity {

    private ImageView mProfileImage;
    private TextView mInfo;
    private TextView mDescription;
    private RatingBar mRating;
    private Button mChat;
    private TextView mWebsite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        mProfileImage = findViewById(R.id.imageView);
        mInfo = findViewById(R.id.textView);
        mDescription = findViewById(R.id.textView2);
        mRating = findViewById(R.id.ratingBar2);
        mChat = findViewById(R.id.chatButton);

        mChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewProfile.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mWebsite.setOnClickListener(new View.OnClickListener() {
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

        int aveRating = 0; //get from database to calculate
        mRating.setNumStars(aveRating);
    }


}
