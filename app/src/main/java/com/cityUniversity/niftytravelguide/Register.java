package com.cityUniversity.niftytravelguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText name,email,password,phone;
    CheckBox driver,tourist,admin;
    TextView login;
    boolean valid = true;
    Button create;

    FirebaseAuth fAuth;

    FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        name = findViewById(R.id.registerNameId);
        email = findViewById(R.id.registerEmailId);
        password = findViewById(R.id.registerPasswordId);
        phone = findViewById(R.id.registerPhoneNumberId);
        login = findViewById(R.id.gotoLoginId);
        create = findViewById(R.id.registerBtnId);
        admin = findViewById ( R.id.isAdminId );
        driver = findViewById ( R.id.isDriverId );
        tourist = findViewById ( R.id.isTouristId);

        //check boxes logics

        admin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){

                    driver.setChecked(false);
                    tourist.setChecked(false);

                }
            }
        });

        tourist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){

                    driver.setChecked(false);
                    admin.setChecked(false);

                }
            }
        });

        driver.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){

                    tourist.setChecked(false);
                    admin.setChecked(false);

                }
            }
        });


        create.setOnClickListener(view -> {
            checkField(name);
            checkField(email);
            checkField(password);
            checkField(phone);
            admin = findViewById ( R.id.isAdminId );
            tourist = findViewById ( R.id.isTouristId );
            driver = findViewById ( R.id.isDriverId );

            //checkbox validation





            if(valid)
            {
                fAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(authResult -> {
                    FirebaseUser user = fAuth.getCurrentUser();
                    String uid=user.getUid();

                    Toast.makeText(Register.this, "Successfully Account Created", Toast.LENGTH_SHORT).show();

                    assert user != null;
                    DocumentReference df =fStore.collection("Users").document(user.getUid());

                    Map<String,Object> userInfo = new HashMap<>();
                    userInfo.put("FullName",name.getText().toString());
                    userInfo.put("UserEmail",email.getText().toString());
                    userInfo.put("PhoneNumber",phone.getText().toString());

                    //specify if user is admin

                    if(admin.isChecked ()) {

                        userInfo.put ( "isAdminId", "1");

                        Map<String,Object> admin = new HashMap<>();
                        admin.put("FullName",name.getText().toString());
                        admin.put("UserEmail",email.getText().toString());
                        admin.put("PhoneNumber",phone.getText().toString());
                        admin.put("uid",uid);
                        admin.put ( "type", "admin");
                        FirebaseDatabase database=FirebaseDatabase.getInstance();
                        DatabaseReference reference=database.getReference("admin");



                        reference.child(uid).setValue(admin);
                    }

                    if(tourist.isChecked ()){

                        userInfo.put ( "isTouristId","1" );

                        Map<String,Object> tourist = new HashMap<>();
                        tourist.put("FullName",name.getText().toString());
                        tourist.put("UserEmail",email.getText().toString());
                        tourist.put("PhoneNumber",phone.getText().toString());
                        tourist.put("uid",uid);
                        tourist.put ( "type", "tourist");
                        FirebaseDatabase database=FirebaseDatabase.getInstance();
                        DatabaseReference reference=database.getReference("tourist");

                        reference.child(uid).setValue(tourist);
                    }

                    if(driver.isChecked ()){

                        userInfo.put ( "isDriverId","1" );

                        Map<String,Object> driver = new HashMap<>();
                        driver.put("FullName",name.getText().toString());
                        driver.put("UserEmail",email.getText().toString());
                        driver.put("PhoneNumber",phone.getText().toString());
                        driver.put("uid",uid);
                        driver.put ( "type", "driver");
                        FirebaseDatabase database=FirebaseDatabase.getInstance();
                        DatabaseReference reference=database.getReference("driver");

                        reference.child(uid).setValue(driver);
                    }


                    df.set(userInfo);
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();
                }) .addOnFailureListener(e -> Toast.makeText(Register.this, "Failed to Created Account", Toast.LENGTH_SHORT).show());



            }



        });


        login.setOnClickListener (view -> startActivity ( new Intent ( getApplicationContext ( ) , Login.class ) ));


    }
    public void checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else {
            valid = true;
        }

    }

}