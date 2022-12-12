package com.practice.wastemanagment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class RegisterActivity extends AppCompatActivity {
    EditText name,email,address,city,pincode,password,phoneCountryCode,phone;
    Button register;
    TextView singIn,bg,welcome;
    FirebaseAuth auth = null;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Sprite doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);
        progressBar.setVisibility(View.GONE);

        bg=findViewById(R.id.WelcomeTextView);
        welcome=findViewById(R.id.readItTogetherTextView);
        register=findViewById(R.id.regisButton);
        auth=FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRegistration();
            }
        });

        singIn=findViewById(R.id.sing_in);
        singIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(RegisterActivity.this,loginActivity.class);
                startActivity(in);
                finish();
            }
        });

    }

    //----------------Registration of User------------------------------
    private void userRegistration() {

        name=(EditText)findViewById(R.id.regisName);
        email=(EditText)findViewById(R.id.emailEditText);
        address=(EditText)findViewById(R.id.regisAddress);
        city=(EditText)findViewById(R.id.regisCity);
        pincode=(EditText)findViewById(R.id.regisPincode);
        password=(EditText)findViewById(R.id.passwordEditText);
        phoneCountryCode=(EditText)findViewById(R.id.countryCode);
        phone=(EditText)findViewById(R.id.phoneNo);


        String UserName=name.getText().toString();
        String UserEmail=email.getText().toString();
        String UserAddress=address.getText().toString();
        String UserCity=city.getText().toString().toUpperCase();
        String UserPin=pincode.getText().toString();
        String UserPass=password.getText().toString();
        String UserPhone=phone.getText().toString();

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Sprite doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);

        if(UserName.isEmpty())
        {
            name.setError("Full Name is Required");
            name.requestFocus();
            return;
        }
        if(UserEmail.isEmpty())
        {
            email.setError("Email is Required");
            email.requestFocus();
            return;
        }
        if(UserAddress.isEmpty())
        {
            address.setError("Address is Required");
            address.requestFocus();
            return;
        }
        if(UserCity.isEmpty())
        {
            city.setError("City is Required");
            city.requestFocus();
            return;
        }
        if(UserPin.isEmpty())
        {
            pincode.setError("Pincode is Required");
            pincode.requestFocus();
            return;
        }
        if(UserPass.isEmpty())
        {
            password.setError("Password is Required");
            password.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(UserEmail).matches())
        {
            email.setError("Please provide valid email address");
            email.requestFocus();
            return;
        }
        if(UserPass.length()<8)
        {
            password.setError("Password Should be at least 8 character");
            progressBar.setVisibility(View.GONE);
            return;
        }
        if(UserPhone.isEmpty())
        {
            phone.setError("Phone is Required");
            return;
        }
        if(UserPhone.length()!=10)
        {
            phone.setError("Please give valid phone number");
            return;
        }
        else if( UserEmail.isEmpty() ||UserAddress.isEmpty() || UserCity.isEmpty()||UserPin.isEmpty() || UserPass.isEmpty())
        {
            Toast.makeText(RegisterActivity.this,"All Fields is Required!",Toast.LENGTH_SHORT).show();
        }
        else
        {
            progressBar.setVisibility(View.VISIBLE);
            hideUI();
            firebaseRegistration(UserName,UserEmail,UserAddress,UserCity,UserPin,UserPass,UserPhone,phoneCountryCode.getText().toString());

        }

    }


    private void firebaseRegistration(String userName, String email, String userAddress, String userCity, String userPin, String pass,String userPhone,String code) {

           ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        // Write a message to the database
//        User user = new User("2322323",userName,email,userAddress,userCity,userPin);
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("message");
//
//        myRef.setValue(user);

            auth.createUserWithEmailAndPassword(email,pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        FirebaseUser firebaseUser=auth.getCurrentUser();
                        String id=firebaseUser.getUid();

                        reference= FirebaseDatabase.getInstance().getReference("Users").child(id);

                        User user = new User(id,userName,email,userAddress,userCity,userPin,code+userPhone);
                        reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    //Toast.makeText(RegisterActivity.this,"Registration Sucessfull!",Toast.LENGTH_SHORT).show();
                                    showeUI();
                                    progressBar.setVisibility(View.GONE);

                                    Intent phone = new Intent(RegisterActivity.this,PhoneVarification.class);
                                    phone.putExtra("phone",phoneCountryCode.getText().toString()+userPhone);
                                    phone.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(phone);
                                    Animatoo.animateSlideUp(RegisterActivity.this);
                                }
                            }
                        });
                    }
                    else
                    {
                        showeUI();
                        Toast.makeText(RegisterActivity.this,"You can not Register with this Email or password !",Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
    }

    private void hideUI() {
        float alpha = 0.2f;
        name.setAlpha(alpha);
        address.setAlpha(alpha);
        email.setAlpha(alpha);
        password.setAlpha(alpha);
        city.setAlpha(alpha);
        pincode.setAlpha(alpha);
        phone.setAlpha(alpha);
        phone.setEnabled(false);
        email.setEnabled(false);
        name.setEnabled(false);
        address.setEnabled(false);
        password.setEnabled(false);
        city.setEnabled(false);
        pincode.setEnabled(false);
        register.setAlpha(alpha);
        singIn.setAlpha(alpha);
        bg.setAlpha(alpha);
        welcome.setAlpha(alpha);
        phoneCountryCode.setAlpha(alpha);
    }
    private void showeUI() {
        float alpha = 1.0f;
        name.setAlpha(alpha);
        address.setAlpha(alpha);
        email.setAlpha(alpha);
        password.setAlpha(alpha);
        city.setAlpha(alpha);
        pincode.setAlpha(alpha);
        phone.setAlpha(alpha);
        phone.setEnabled(true);
        email.setEnabled(true);
        name.setEnabled(true);
        address.setEnabled(true);
        password.setEnabled(true);
        city.setEnabled(true);
        pincode.setEnabled(true);
        register.setAlpha(alpha);
        singIn.setAlpha(alpha);
        bg.setAlpha(alpha);
        welcome.setAlpha(alpha);
        phoneCountryCode.setAlpha(alpha);
    }


}