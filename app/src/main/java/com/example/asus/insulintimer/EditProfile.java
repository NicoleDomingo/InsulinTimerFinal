
package com.example.asus.insulintimer;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import model.User;

import static android.app.Activity.RESULT_OK;


public class EditProfile extends Fragment {

    Button btnSave, btnCancel, btnUpload;
    private TextView fullname;
    private EditText editFname;
    private EditText editLname;
    private EditText editHeight;
    private EditText editWeight;
    private EditText editMin;
    private EditText editMax;
    private ImageView editProfpic;
    private ScrollView scrollView;
    private Uri filePath;
    private String username;

    private final int PICK_IMAGE_REQUEST = 71;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private DatabaseReference userDataReference;

    FirebaseStorage storage;
    StorageReference storageReference;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        getActivity().setTitle("Edit Profile");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.edit_profile, container, false);
        getActivity().setTitle("Edit Profile");
        fullname = view.findViewById(R.id.edit_fullname);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        username = prefs.getString("username","");
        Log.d("username", String.valueOf(username));


        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.edit_profile);
        btnSave = view.findViewById(R.id.b_save);
        btnCancel = view.findViewById(R.id.b_cancel);
        btnUpload = view.findViewById(R.id.b_upload);
        scrollView = view.findViewById(R.id.sv);

        editFname = view.findViewById(R.id.editFname);
        editLname = view.findViewById(R.id.editLname);
        editHeight = view.findViewById(R.id.editHeight);
        editWeight = view.findViewById(R.id.editWeight);
        editMin = view.findViewById(R.id.editMin);
        editMax = view.findViewById(R.id.editMax);

        editProfpic = view.findViewById(R.id.imageView12);

        storage = FirebaseStorage.getInstance();



        userDataReference = databaseReference.child("users");
        userDataReference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User usertemp = dataSnapshot.getValue(User.class);
                fullname.setText(usertemp.getFname() + " " + usertemp.getLname());
                editFname.setText(usertemp.getFname());
                editLname.setText(usertemp.getLname());
                editHeight.setText(usertemp.getHeight());
                editWeight.setText(usertemp.getWeight());
                editMax.setText(usertemp.getMaxblood());
                editMin.setText(usertemp.getMinblood());
                if(dataSnapshot.hasChild("photourl")){
                    Picasso.get().load(dataSnapshot.child("photourl").getValue().toString()).into(editProfpic);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       /* btnSave.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view1) {
                if (!editFname.getText().toString().isEmpty() &&
                        !editLname.getText().toString().isEmpty() &&
                        !editHeight.getText().toString().isEmpty() &&
                        !editWeight.getText().toString().isEmpty() &&
                        !editMax.toString().isEmpty()) {


                    Intent intent1 = getActivity().getIntent();
                    user = intent1.getStringExtra("username");


                    FragmentManager fM = getActivity().getSupportFragmentManager();
                    FragmentTransaction fT = fM.beginTransaction();
                    EditProfile h = new EditProfile();
                    fT.replace(R.id.frame_layout, h);
                    fT.commit();

                }
            }
        });*/

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fM = getActivity().getSupportFragmentManager();
                FragmentTransaction fT = fM.beginTransaction();
                Profile h = new Profile();
                fT.replace(R.id.frame_layout, h);
                fT.commit();

            }
        });

        btnSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                userDataReference = databaseReference.child("users");

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/fname/", editFname.getText().toString());
                childUpdates.put("/lname/", editLname.getText().toString());
                childUpdates.put("/height/", editHeight.getText().toString());
                childUpdates.put("/weight/", editWeight.getText().toString());
                childUpdates.put("/minblood/", editMin.getText().toString());
                childUpdates.put("/maxblood/", editMax.getText().toString());

                userDataReference.child(username).updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity().getApplicationContext(), "New information saved", Toast.LENGTH_SHORT).show();
                        uploadImage();
                    }
                });

                FragmentManager fM = getActivity().getSupportFragmentManager();
                FragmentTransaction fT = fM.beginTransaction();
                Profile h = new Profile();
                fT.replace(R.id.frame_layout, h);
                fT.commit();


            }
        });

        /*btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });*/
        scrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return true;
            }
        });
        return view;
    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                editProfpic.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            storageReference = FirebaseStorage.getInstance().getReference();

            StorageReference ref = storageReference.child("images/"+ username + ".jpg");
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put("/photourl/", taskSnapshot.getDownloadUrl().toString());
                            userDataReference.child(username).updateChildren(childUpdates);

                            Toast.makeText(getActivity().getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                            FragmentManager fM = getActivity().getSupportFragmentManager();
                            FragmentTransaction fT = fM.beginTransaction();
                            Profile h = new Profile();
                            fT.replace(R.id.frame_layout, h);
                            fT.commit();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

}

