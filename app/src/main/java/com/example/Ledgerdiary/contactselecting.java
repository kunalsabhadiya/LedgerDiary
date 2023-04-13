package com.example.Ledgerdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Ledgerdiary.dashboardFragment.offlineuser;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class contactselecting extends AppCompatActivity {

    EditText addnum, addname;
    Button addcontect,addsavebtn;
    String finalnumber;

    static final int PICK_CONTACT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactselecting);
        addcontect = findViewById(R.id.contactnumbtn);
        addnum = findViewById(R.id.addcontactnum);
        addsavebtn = findViewById(R.id.addsavebtn);
        addname = findViewById(R.id.addcontactname);
        addcontect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);

            }
        });
        addsavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(addname.getText().toString().isEmpty()){
                    addname.setError("Name is required");
                    addname.requestFocus();
                }else if(addnum.getText().toString().isEmpty()){
                    addnum.setError("Phonenumber is required");
                    addnum.requestFocus();
                }else if(addnum.getText().toString().replaceAll("[\\D]", "").length()!=10 && addnum.getText().toString().replaceAll("[\\D]", "").length()!=12){
                   addnum.setError("Phonenumber is not valid");
                   addnum.requestFocus();
               }else if(addname.getText().toString().length()>16){
                   addname.setError("Name must be less then 16 latters");
                   addname.requestFocus();
               }else{
                    finalnumber =addnum.getText().toString().replaceAll("[\\D]", "");
                    if(finalnumber.length()==10){
                        finalnumber="91"+finalnumber;
                    }

                  FirebaseDatabase.getInstance().getReference().child("users").orderByChild("phonenumber").equalTo(finalnumber)
                                  .addListenerForSingleValueEvent(new ValueEventListener() {
                                      @Override
                                      public void onDataChange(@NonNull DataSnapshot snapshot) {
                                          if(snapshot.exists()) {
                                              for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                                  int am=0;
                                                  if (!Objects.equals(snapshot1.getKey(), FirebaseAuth.getInstance().getUid())) {
                                                      FirebaseDatabase.getInstance().getReference().child("users")
                                                              .child(FirebaseAuth.getInstance().getUid()).child("customer")
                                                              .child(finalnumber)
                                                              .child("Cphonenumber")
                                                              .setValue(finalnumber);

                                                      FirebaseDatabase.getInstance().getReference().child("users")
                                                              .child(FirebaseAuth.getInstance().getUid()).child("customer")
                                                              .child(finalnumber)
                                                              .child("Cname").setValue(addname.getText().toString());
                                                      FirebaseDatabase.getInstance().getReference().child("users")
                                                              .child(FirebaseAuth.getInstance().getUid()).child("customer")
                                                              .child(finalnumber)
                                                              .child("Ctamount").setValue(am);
                                                      for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                                          String otherUserId = childSnapshot.getKey();
                                                          String otherimageuri = childSnapshot.child("imageUri").getValue().toString();
                                                          FirebaseDatabase.getInstance().getReference().child("users")
                                                                  .child(FirebaseAuth.getInstance().getUid()).child("customer")
                                                                  .child(finalnumber)
                                                                  .child("Cuid").setValue(otherUserId);
                                                          FirebaseDatabase.getInstance().getReference().child("users")
                                                                  .child(FirebaseAuth.getInstance().getUid()).child("customer")
                                                                  .child(finalnumber)
                                                                  .child("Cimageuri").setValue(otherimageuri);
                                                      }

                                                      Toast.makeText(contactselecting.this, "Contact added successfully", Toast.LENGTH_SHORT).show();
                                                      addnum.setText("");
                                                      addname.setText("");
                                                      startActivity(new Intent(contactselecting.this, Dashboard.class));
                                                  }
                                              }
                                          }
                                                 else{
                                              sqliteDbhelper myDB = new sqliteDbhelper(contactselecting.this);
                                              myDB.addCustomer(addname.getText().toString().trim(),
                                                      finalnumber);
                                              addnum.setText("");
                                              addname.setText("");
                                              startActivity(new Intent(contactselecting.this,Dashboard.class));
                                          }
                                      }

                                      @Override
                                      public void onCancelled(@NonNull DatabaseError error) {

                                      }
                                  });

                }



                }

        });
    }









    public void onActivityResult(int reqCode, int resultCode, Intent data)
    {
        super.onActivityResult(reqCode, resultCode, data);

        switch(reqCode){
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK)
                {
                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String id =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                        String hasPhone =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                    null, null);
                            phones.moveToFirst();
                            String phn_no = phones.getString(phones.getColumnIndexOrThrow("data1"));
                            String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredPostal.DISPLAY_NAME));
                            addnum.setText(phn_no);
                            addname.setText(name);

                        }
                    }
                }
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No internet connection", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
}