package com.example.asus.insulintimer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomePage extends AppCompatActivity {

    private Button proceedB;
    private String user;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbr = db.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(WelcomePage.this);
        user = prefs.getString("username","");

        proceedB = (Button) findViewById(R.id.b_proceed);
        proceedB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbr.child("users").child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            Intent proceed = new Intent(WelcomePage.this, MainActivity.class);
                            startActivity(proceed);
                    }
                        else{
                            Log.d("CHECKER IF X", String.valueOf(user));
                        }

                }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                    });


            }
        });
    }
    @Override
    public void onBackPressed(){
        SharedPreferences dsp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = dsp.edit();
        editor.remove("username");
        editor.commit();
        finish();
        Intent login = new Intent(WelcomePage.this, ActivityLogin.class);
        startActivity(login);
    }
}
