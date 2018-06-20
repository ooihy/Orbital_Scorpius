package com.example.shanna.orbital2;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class users_clients extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mClientsList;
    private DatabaseReference mUsersDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_clients);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_clients);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Clients");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mClientsList = (RecyclerView)findViewById(R.id.clients_list);
        mClientsList.setHasFixedSize(true);
        mClientsList.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    protected void onStart() {
        super.onStart();
        startListening();

    }
    public void startListening(){
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users");

        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(query, Users.class)
                        .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Users, UserViewHolder>(options) {
            @Override
            public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.users_single_layout, parent, false);

                return new UserViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(UserViewHolder holder, int position, Users model) {
                // Bind the Chat object to the ChatHolder
                holder.setName(model.getFullName());
                holder.setDescription(model.getDescription());
                holder.setUserImage(model.getThumb_image());

                final String user_id = getRef(position).getKey();

                Toast.makeText(users_clients.this,"For debugging: User id is " + user_id + " position is "+position, Toast.LENGTH_LONG).show();

                //holder.mView.setOnClickListener(new View.OnClickListener(){
                holder.mView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent profileIntent = new Intent(users_clients.this, ViewProfile.class);
                        profileIntent.putExtra("user_id", user_id);
                        startActivity(profileIntent);
                    }
                });

            }

        };
        mClientsList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setName(String name){
            TextView userNameView = (TextView) mView.findViewById(R.id.single_name);
            userNameView.setText(name);
        }
        public void setDescription(String description){
            TextView userDescription = (TextView) mView.findViewById(R.id.single_description);
            userDescription.setText(description);
        }
        //  public void setUserImage(String thumb_image) {
        public void setUserImage(String thumb_image) {
            ImageView userImageView = mView.findViewById(R.id.single_image);

            //The commented code below works to upload an image...
             //Picasso.get().load("https://i.imgur.com/tGbaZCY.jpg").placeholder(R.drawable.spaceman_1x).into(userImageView);

            //below:doesn't work
            Picasso.get().load(thumb_image).placeholder(R.drawable.spaceman_1x).into(userImageView);
        }
    }



}
