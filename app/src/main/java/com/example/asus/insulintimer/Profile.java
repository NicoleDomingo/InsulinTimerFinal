package com.example.asus.insulintimer;

/**
 * Created by Mharjorie Sandel on 04/03/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.User;

import static com.example.asus.insulintimer.ActivityLogin.MyPREFERENCES;

public class Profile extends Fragment {
    private Button edit, logout;
    private String username;
    private TextView tv_fullname, tv_height, tv_weight, tv_min, tv_max;
    private ImageView profpic;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private DatabaseReference userDataReference;
    SharedPreferences sharedpreferences;

    public static Profile newInstance() {
        Profile fragment = new Profile();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile_layout, container, false);
        getActivity().setTitle("Profile");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        username = prefs.getString("username","");


        tv_fullname = view.findViewById(R.id.textView21);
        tv_height = view.findViewById(R.id.tbl_txt6);
        tv_weight = view.findViewById(R.id.tbl_txt8);
        tv_max = view.findViewById(R.id.tbl_txt2);
        tv_min = view.findViewById(R.id.tbl_txt4);
        profpic = view.findViewById(R.id.imageView12);

        logout = (Button) view.findViewById((R.id.b_logout));
        edit = (Button) view.findViewById(R.id.b_editprof);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fM = getActivity().getSupportFragmentManager();
                FragmentTransaction fT = fM.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("username",username);
                EditProfile h= new EditProfile();
                h.setArguments(bundle);
                fT.replace(R.id.frame_layout,h).addToBackStack(null);
                fT.commit();

                //Toast.makeText(getContext(), "Edit!", Toast.LENGTH_SHORT).show();
            }
        });

        userDataReference = databaseReference.child("users");
        userDataReference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User usertemp = dataSnapshot.getValue(User.class);
                tv_fullname.setText(usertemp.getFname() + " " + usertemp.getLname());
                tv_height.setText(usertemp.getHeight());
                tv_weight.setText(usertemp.getWeight());
                tv_max.setText(usertemp.getMaxblood());
                tv_min.setText(usertemp.getMinblood());
                if(dataSnapshot.hasChild("photourl")){
                    Picasso.get().load(dataSnapshot.child("photourl").getValue().toString()).into(profpic);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences dsp = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = dsp.edit();
                editor.remove("username");
                editor.commit();
                Intent loginIntent = new Intent(getContext(),ActivityLogin.class);
                startActivity(loginIntent);

            }
        });
        return view;
    }

}