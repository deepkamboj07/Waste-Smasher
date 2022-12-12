package com.practice.wastemanagment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class PhoneVarification extends AppCompatActivity {
    EditText one,two,three,four,five,six;
    TextView ResendButton,timer,head,otp;;
    Button verify,resend;
    String veriId,phone;
    FirebaseAuth auth;
    PhoneAuthCredential phoneAuthCredential;
    PhoneAuthProvider phoneAuthProvider;
    PhoneAuthProvider.ForceResendingToken token;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_varification);

        timer=findViewById(R.id.timer);
        otp=findViewById(R.id.getotp);
        head=findViewById(R.id.heading);

        FirebaseApp.initializeApp(this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                SafetyNetAppCheckProviderFactory.getInstance());

        Intent data = getIntent();
        phone = data.getStringExtra("phone");
        auth = FirebaseAuth.getInstance();
        //auth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
        otpSend(phone);
        editTextInput();

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        Sprite doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);
        progressBar.setVisibility(View.GONE);

        ResendButton = findViewById(R.id.resendButton);
        verify = findViewById(R.id.buttonVerify);


        ResendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PhoneVarification.this, "OTP is Resend", Toast.LENGTH_SHORT).show();
                otpSend(phone);
            }
        });


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one=findViewById(R.id.varification1);
                two=findViewById(R.id.varification2);
                three=findViewById(R.id.varification3);
                four=findViewById(R.id.varification4);
                five=findViewById(R.id.varification5);
                six=findViewById(R.id.varification6);
                //showUI();

                if(one.getText().toString().isEmpty() || two.getText().toString().isEmpty()||three.getText().toString().isEmpty() || four.getText().toString().isEmpty() || five.getText().toString().isEmpty() || six.getText().toString().isEmpty())
                {
                    Toast.makeText(PhoneVarification.this,"Invalid OTP",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(veriId!=null)
                    {

                        progressBar.setVisibility(View.VISIBLE);
                        String otp=one.getText().toString()+two.getText().toString()+three.getText().toString()+four.getText().toString()+five.getText().toString()+six.getText().toString();
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(veriId, otp);
                        FirebaseAuth
                                .getInstance()
                                .signInWithCredential(credential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            ResendButton.setVisibility(View.INVISIBLE);
                                            Toast.makeText(PhoneVarification.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                            Intent in=new Intent(PhoneVarification.this,    loginActivity.class);
                                            startActivity(in);
                                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            Animatoo.animateSlideUp(PhoneVarification.this);
                                            finish();
                                        } else {
                                            progressBar.setVisibility(View.GONE);
                                            ResendButton.setVisibility(View.VISIBLE);
                                            showUI();
                                            Toast.makeText(PhoneVarification.this, "OTP is not Valid!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }

                }
            }
        });

    }



//---------------------TO SEND OTP TO THE NUMBER----------------------------

    private void otpSend(String phonenum) {
        //auth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
        //ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        ResendButton=findViewById(R.id.resendButton);

        //progressBar.setVisibility(View.VISIBLE);
        ResendButton.setVisibility(View.INVISIBLE);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                //progressBar.setVisibility(View.GONE);
                ResendButton.setVisibility(View.VISIBLE);
                showUI();
                Toast.makeText(PhoneVarification.this, "OTP verification Failed" +"\n"+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // progressBar.setVisibility(View.GONE);
                ResendButton.setVisibility(View.VISIBLE);
                showUI();
                Toast.makeText(PhoneVarification.this, "OTP is successfully send.", Toast.LENGTH_SHORT).show();
                veriId=verificationId;
            }
        };

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phonenum)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    //-------------------------OTP INPUT-------------------------
    private void editTextInput() {
        one=findViewById(R.id.varification1);
        two=findViewById(R.id.varification2);
        three=findViewById(R.id.varification3);
        four=findViewById(R.id.varification4);
        five=findViewById(R.id.varification5);
        six=findViewById(R.id.varification6);
        verify = findViewById(R.id.buttonVerify);

        one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(one.getText().toString().length()>=1)     //size as per your requirement
                {
                    two.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        two.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(two.getText().toString().length()>=1)
                    three.requestFocus();
                if(two.getText().toString().length()==0)
                    one.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        three.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(three.getText().toString().length()>=1)
                    four.requestFocus();
                if(three.getText().toString().length()==0)
                    two.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        four.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(four.getText().toString().length()>=1)
                    five.requestFocus();
                if(four.getText().toString().length()==0)
                    three.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        five.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(five.getText().toString().length()>=1)
                    six.requestFocus();
                if(five.getText().toString().length()==0)
                    four.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        six.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(six.getText().toString().length()>=1) {
                   verify.callOnClick();
                   hideUI();
                }
                if(six.getText().toString().length()==0)
                    five.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void hideUI() {
        float alpha = 0.2f;
        one.setAlpha(alpha);
        two.setAlpha(alpha);
        three.setAlpha(alpha);
        four.setAlpha(alpha);
        five.setAlpha(alpha);
        six.setAlpha(alpha);
        one.setEnabled(false);
        two.setEnabled(false);
        three.setEnabled(false);
        four.setEnabled(false);
        five.setEnabled(false);
        six.setEnabled(false);

        resend.setAlpha(alpha);
        verify.setAlpha(alpha);
        head.setAlpha(alpha);
        otp.setAlpha(alpha);
        timer.setAlpha(alpha);
    }
    private void showUI() {
        float alpha = 1.0f;
        one.setAlpha(alpha);
        two.setAlpha(alpha);
        three.setAlpha(alpha);
        four.setAlpha(alpha);
        five.setAlpha(alpha);
        six.setAlpha(alpha);
        one.setEnabled(true);
        two.setEnabled(true);
        three.setEnabled(true);
        four.setEnabled(true);
        five.setEnabled(true);
        six.setEnabled(true);

        resend.setAlpha(alpha);
        verify.setAlpha(alpha);
        head.setAlpha(alpha);
        otp.setAlpha(alpha);
        timer.setAlpha(alpha);
    }



}