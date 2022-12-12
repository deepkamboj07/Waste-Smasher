package com.practice.wastemanagment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Phone extends AppCompatActivity {
    EditText phone;
    Button send;
    FirebaseAuth auth;
    String veriId;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        auth = FirebaseAuth.getInstance();
        send=findViewById(R.id.sendOTP);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone=findViewById(R.id.phone_no);
                if(phone.getText().toString().length()==10)
                {
                    otpSend("+91"+phone.getText().toString());
                }
            }
        });
    }

    private void otpSend(String phone) {
        //auth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
        //ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit);
       // ResendButton=findViewById(R.id.resendButton);

        //progressBar.setVisibility(View.VISIBLE);
       // ResendButton.setVisibility(View.INVISIBLE);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                //progressBar.setVisibility(View.GONE);
                //ResendButton.setVisibility(View.VISIBLE);
                Toast.makeText(Phone.this, "OTP verification Failed" +"\n"+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // progressBar.setVisibility(View.GONE);
                //ResendButton.setVisibility(View.VISIBLE);
                Toast.makeText(Phone.this, "OTP is successfully send.", Toast.LENGTH_SHORT).show();
                veriId=verificationId;
            }
        };

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}