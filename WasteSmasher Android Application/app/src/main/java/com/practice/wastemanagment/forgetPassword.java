package com.practice.wastemanagment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgetPassword extends AppCompatActivity {
    ImageView cancel;
    EditText email;
    Button reset;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Sprite doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);
        progressBar.setVisibility(View.GONE);

        auth=FirebaseAuth.getInstance();

        cancel=findViewById(R.id.cross);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(forgetPassword.this,loginActivity.class));
                finish();
            }
        });

        reset=findViewById(R.id.ResetButton);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=findViewById(R.id.emailReset);
                String UserEmail=email.getText().toString();
                if(UserEmail.isEmpty())
                {
                    email.setError("Email is required");
                    email.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(UserEmail).matches())
                {
                    email.setError("Please enter valid Email Address");
                    return;
                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    resetPass(UserEmail);
                }
            }
        });
    }

    private void resetPass(String userEmail) {
        auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
                if(task.isSuccessful())
                {
                    Toast.makeText(forgetPassword.this,"check your mail to reset your password !",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(forgetPassword.this,"Try again !",Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}