package com.example.asus.insulintimer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.preference.PreferenceManager;


public class ActivityLogin extends AppCompatActivity  {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private DatabaseReference userDataReference;

    public static final String MyPREFERENCES = "MyPrefs" ;

    private EditText username;
    private EditText password;
    SharedPreferences sharedpreferences;
    SharedPreferences dsp;
    private ImageView iv;


    Button loginB, signupB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        loginB = (Button) findViewById(R.id.b_proceed);
        signupB = (Button) findViewById(R.id.b_register);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        iv = (ImageView) findViewById(R.id.image);
        iv.clearAnimation();


        String user = SaveSharedPreference.getUserName(ActivityLogin.this);
        if(SaveSharedPreference.getUserName(ActivityLogin.this).length() == 0) {
            loginB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RotateAnimation rotateAnimation = new RotateAnimation(0, 360f,
                            Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f);

                    rotateAnimation.setInterpolator(new LinearInterpolator());
                    rotateAnimation.setDuration(500);
                    rotateAnimation.setRepeatCount(Animation.INFINITE);

                    iv.startAnimation(rotateAnimation);
                    if (!username.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                        userDataReference = databaseReference.child("users");
                        userDataReference.orderByChild("username").equalTo(username.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    userDataReference = databaseReference.child("users"); //orderByChild("password").equalTo(password.getText().toString()).
                                    userDataReference.orderByChild("password").equalTo(password.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                // ArrayList<String> stringtmep = aosjbfjhafs
                                                /*for (DataSnapshot childRow : dataSnapshot.getChildren()) {
                                                }*/
                                                dsp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                                                SharedPreferences.Editor editor = dsp.edit();
                                                editor.putString("username", username.getText().toString());
                                                editor.putBoolean("isChecked", false); //NEWLYADDED BY NICOLE
                                                editor.commit();
                                                finish();

                                                Intent loginIntent = new Intent(ActivityLogin.this, MainActivity.class);

                                                startActivity(loginIntent);
                                            } else {
                                                Toast.makeText(ActivityLogin.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                } else {
                                    Toast.makeText(ActivityLogin.this, "The username does not exist", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    } else {
                        Toast.makeText(ActivityLogin.this,
                                "Please fill in the blanks", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            signupB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    Intent signupIntent = new Intent (ActivityLogin.this, ActivityRegister.class);
                    startActivity(signupIntent);
                }
            });
        }

        else{
            Intent i = new Intent(getBaseContext(), MainActivity.class);
            startActivity(i);
            finish();

        }

    }
    @Override
    public void onBackPressed(){
     finish();
    }
}
