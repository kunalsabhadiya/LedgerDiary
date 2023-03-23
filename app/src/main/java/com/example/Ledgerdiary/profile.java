package com.example.Ledgerdiary;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import de.hdodenhof.circleimageview.CircleImageView;


public class profile extends AppCompatActivity {
private EditText pusername,pbuisness;
private TextView pnubmer;
private CircleImageView pimage;
private ImageView addimage;
private FirebaseDatabase database;
private FirebaseStorage storage;

private String username,buisness,img;
ActivityResultLauncher<String> launcher;
private Button psave;
private FirebaseAuth auth;
ProgressBar pg;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_profile);

            database=FirebaseDatabase.getInstance();
            auth=FirebaseAuth.getInstance();
            storage=FirebaseStorage.getInstance();

            pusername=findViewById(R.id.pusername);
            pbuisness=findViewById(R.id.pbusiness);
            psave=findViewById(R.id.psave);
            pimage=findViewById(R.id.profileimage);
            addimage=findViewById(R.id.addimage);
            pnubmer=findViewById(R.id.pnumber);
            pg=findViewById(R.id.pg1);

            pnubmer.setText(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());

            pg.setVisibility(View.VISIBLE);
            database.getReference().child("users").child(auth.getUid()).child("phonenumber").setValue(pnubmer.getText().toString().replaceAll("[\\D]", ""));
            database.getReference().child("users").child(auth.getUid()).child("buisness").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String  pb=snapshot.getValue(String.class);
                    pbuisness.setText(pb);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            database.getReference().child("users").child(auth.getUid()).child("username").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String pu=snapshot.getValue(String.class);
                    pusername.setText(pu);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            database.getReference().child("users").child(auth.getUid()).child("imageUri").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    img=snapshot.getValue(String.class);
                    Picasso.get()
                            .load(img)
                            .placeholder(R.drawable.profileimage)
                            .into(pimage);
                     pg.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });







            launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    pimage.setImageURI(result);
                    final StorageReference reference=storage.getReference().child("UserProfile");
                    reference.child(auth.getUid()).putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference.child(auth.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    database.getReference().child("users").child(auth.getUid()).child("imageUri").setValue(uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(profile.this, "Profile update Succesfully.", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(profile.this, "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                    });

                }
            });

            addimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launcher.launch("image/*");
                }
            });

            psave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    username=pusername.getText().toString();
                    buisness=pbuisness.getText().toString();
                    if(TextUtils.isEmpty(username)){
                            pusername.setError("Username is Required");
                            pusername.requestFocus();
                    }else{
                        database.getReference().child("users").child(auth.getUid()).child("username").setValue(username);
                        database.getReference().child("users").child(auth.getUid()).child("buisness").setValue(buisness);
                        Toast.makeText(profile.this, "Profile Update Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(profile.this,Dashboard.class));

                    }
                }
            });
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

            if (!isConnected) {
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No internet connection", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

        }
    }


