package com.example.asus.insulintimer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivityRegister extends AppCompatActivity {
    Button nextB,loginB;

    private EditText regFname;
    private EditText regLname;
    private EditText regUsername;
    private EditText regPassword;
    private EditText regConfirmPass;

    private String  regFnameB;
    private String regLnameB;
    private String regUsernameB;
    private String regPasswordB;
    private String regConfirmPassB;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbr = db.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nextB = (Button) findViewById(R.id.b_create);
        loginB = (Button) findViewById(R.id.b_loginreg);


        regFname = (EditText) findViewById(R.id.regFname);
        regLname = (EditText) findViewById (R.id.regLname);
        regUsername = (EditText) findViewById(R.id.regUsername);
        regPassword = (EditText) findViewById(R.id.regPassword);
        regConfirmPass = (EditText) findViewById(R.id.regConfirmPass);

        Intent backPage = getIntent();
        if (backPage != null){
            regFnameB = backPage.getStringExtra("regFnameB");
            regLnameB = backPage.getStringExtra("regLnameB");
            regUsernameB = backPage.getStringExtra("regUsernameB");
            //regPasswordB = backPage.getStringExtra("regPasswordB");
            //regConfirmPassB = backPage.getStringExtra("regConfirmPassB");

            regFname.setText(regFnameB);
            regLname.setText(regLnameB);
            regUsername.setText(regUsernameB);
            //regPassword.setText(regPasswordB);
            //regConfirmPass.setText(regConfirmPassB);
        }else{
            Log.d("Bundle","No Bundle");
        }


        nextB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String pass1 = regPassword.getText().toString();
                final String pass2 = regConfirmPass.getText().toString();
                if(!regFname.getText().toString().isEmpty()&&
                        !regLname.getText().toString().isEmpty()&&
                        !regPassword.toString().isEmpty()&&
                        !regConfirmPass.getText().toString().isEmpty()){
                    dbr.child("users").child(regUsername.getText().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Toast.makeText(ActivityRegister.this,
                                        "Username already exists", Toast.LENGTH_SHORT).show();
                                }
                            else if(pass1.length() >= 6 && pass2.length() >= 6) {
                                if(pass1.equals(pass2)) {
                                    finish();

                                    // To save the data until the next page
                                    Intent nextPage = new Intent(ActivityRegister.this, SecReg.class);
                                    nextPage.putExtra("regFname", regFname.getText().toString());
                                    nextPage.putExtra("regLname", regLname.getText().toString());
                                    nextPage.putExtra("regUsername", regUsername.getText().toString());
                                    nextPage.putExtra("regPassword", regPassword.getText().toString());
                                    nextPage.putExtra("regConfirmPass", regConfirmPass.getText().toString());

                                    startActivity(nextPage);


                                }else if(!pass1.equals(pass2)){
                                    Toast.makeText(ActivityRegister.this,
                                            "Passwords do not match", Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                Toast.makeText(ActivityRegister.this,
                                        "Password should contain more than 6 characters", Toast.LENGTH_SHORT).show();
                            }
                            }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                }else {
                    Toast.makeText(ActivityRegister.this,
                            "Fill up required fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent backPage = new Intent(ActivityRegister.this, ActivityLogin.class );
                startActivity(backPage);
            }
        });
    }
    @Override
    public void onBackPressed(){
        finish();
        Intent actlog = new Intent(ActivityRegister.this, ActivityLogin.class);
        startActivity(actlog);
    }
}
