package com.example.asus.insulintimer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.User;

public class SecReg extends AppCompatActivity {

    Button nextB, backB;
    private String regFname, regLname, regUsername, regPassword, regConfirmPass;
    private EditText regHeight;
    private EditText regWeight;
    private EditText regMin;
    private EditText regMax;
    SharedPreferences sharedpreferences;
    SharedPreferences dsp;
    public static final String MyPREFERENCES = "MyPrefs" ;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbr = db.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_reg);

        //To get data from prev. page
        // Intent is like a session
        Intent intent = getIntent();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        regFname = intent.getStringExtra("regFname");
        regLname = intent.getStringExtra("regLname");
        regUsername = intent.getStringExtra("regUsername");
        regPassword = intent.getStringExtra("regPassword");
        regConfirmPass = intent.getStringExtra("regConfirmPass");

        regHeight = (EditText)findViewById(R.id.regHeight);
        regWeight = (EditText)findViewById(R.id.regWeight);
        regMin =(EditText)findViewById(R.id.regMin);
        regMax = (EditText)findViewById(R.id.regMax);

        nextB = (Button) findViewById(R.id.b_create);
        backB = (Button) findViewById(R.id.b_back);

        nextB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!regHeight.getText().toString().isEmpty() &&
                        !regWeight.getText().toString().isEmpty() &&
                        !regMax.getText().toString().isEmpty() &&
                        !regMin.getText().toString().isEmpty() &&
                        !regFname.isEmpty()) {

                    int min = Integer.parseInt(regMin.getText().toString());
                    int max = Integer.parseInt(regMax.getText().toString());

                    if (!(min < 20) && !(max > 600)) {
                        if (min < max) {

                            User user1 = new User(regUsername, regFname, regLname, regPassword, regHeight.getText().toString(), regWeight.getText().toString(), regMax.getText().toString(), regMin.getText().toString(), "0", "0");
                            dbr.child("users").child(regUsername).setValue(user1);
                            finish();
                            Intent welcomePage = new Intent(SecReg.this, WelcomePage.class);

                            Toast.makeText(SecReg.this,
                                    "Successfully registered!", Toast.LENGTH_LONG).show();

                            dsp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                            SharedPreferences.Editor editor = dsp.edit();
                            editor.putString("username", regUsername);
                            editor.putBoolean("isChecked",false);
                            Log.d("usernameProceed", regUsername + "");
                            editor.commit();

                            startActivity(welcomePage);
                        } else
                            Toast.makeText(SecReg.this, "Min Blood should be less than Max Blood", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SecReg.this, "Input Min above 20 and Max below 600.", Toast.LENGTH_SHORT).show();
                    }}
                else{
                        Toast.makeText(SecReg.this, "Please fill in the blanks!", Toast.LENGTH_SHORT).show();
                    }
                }

        });
        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent backPage = new Intent(SecReg.this, ActivityRegister.class);
                backPage.putExtra("regFnameB", regFname);
                backPage.putExtra("regLnameB", regLname);
                backPage.putExtra("regUsernameB", regUsername);
                //backPage.putExtra("regPasswordB", regPassword);
                //backPage.putExtra("regConfirmPassB", regConfirmPass);

                startActivity(backPage);
            }
        });
    }
    @Override
    public void onBackPressed(){
        finish();
        Intent backPage = new Intent(SecReg.this, ActivityRegister.class);
        backPage.putExtra("regFnameB", regFname);
        backPage.putExtra("regLnameB", regLname);
        backPage.putExtra("regUsernameB", regUsername);


        startActivity(backPage);
    }
}
