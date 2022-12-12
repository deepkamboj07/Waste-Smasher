package com.practice.wastemanagment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialStyledDatePickerDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity {

    TextView singUp,forgetPass,bg,welcome,skip;
    Button login;
    FirebaseAuth auth;
    TextInputEditText email,pass;
    TextInputLayout em,ps;

    FirebaseUser firebaseUser;
    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null && firebaseUser.isEmailVerified())
        {
            Intent in= new Intent(loginActivity.this,MainActivity.class);
            startActivity(in);
            finish();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login=findViewById(R.id.loginButton);
        auth=FirebaseAuth.getInstance();
        email=findViewById(R.id.emaillogin);
        pass=findViewById(R.id.passwordlogin);
        bg=findViewById(R.id.readItTogetherTextView);
        welcome=findViewById(R.id.WelcomeTextView);
        skip=findViewById(R.id.skipTextView);
        em=findViewById(R.id.emaillogincont);
        ps=findViewById(R.id.passcont);

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Sprite doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);
        progressBar.setVisibility(View.GONE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String UserEmail=email.getText().toString();
                String UserPass=pass.getText().toString();

                if(UserEmail.isEmpty())
                {
                    email.setError("Email is Required");
                    email.requestFocus();
                    return;
                }
                if(UserPass.isEmpty())
                {
                    pass.setError("Password is Required");
                    pass.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(UserEmail).matches())
                {
                    email.setError("Enter Valid Email Address");
                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    hideUI();
                    loginUser(UserEmail,UserPass);
                }
            }
        });

        singUp=findViewById(R.id.sing_up);
                singUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in=new Intent(loginActivity.this,RegisterActivity.class);
                        startActivity(in);
                        finish();
                    }
                });

        //---------Forget Password---------------

        forgetPass=findViewById(R.id.forgetpass);
        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(loginActivity.this,forgetPassword.class));
                finish();
            }
        });
    }

    private void hideUI() {
        float alpha = 0.2f;
        singUp.setAlpha(alpha);
        forgetPass.setAlpha(alpha);
        email.setAlpha(alpha);
        pass.setAlpha(alpha);
        bg.setAlpha(alpha);
        welcome.setAlpha(alpha);
        email.setEnabled(false);
        pass.setEnabled(false);
        login.setAlpha(alpha);
        login.setEnabled(false);
        em.setAlpha(alpha);
        ps.setAlpha(alpha);
        skip.setAlpha(alpha);
    }

    private void loginUser(String userEmail, String userPass) {
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        auth.signInWithEmailAndPassword(userEmail,userPass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                            if(user.isEmailVerified()) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(loginActivity.this, "Login Successful !", Toast.LENGTH_SHORT).show();
                                Intent in = new Intent(loginActivity.this, MainActivity.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);
                                finish();
                            }
                            else
                            {
                                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        showUI();
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(loginActivity.this,"Please Verify your Email Address !", Toast.LENGTH_LONG).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        showUI();
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(loginActivity.this,e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        }else{
                            Toast.makeText(loginActivity.this, "Wrong Password or Email", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            showUI();
                        }
                    }
                });

    }

    private void showUI() {
        float alpha = 1.0f;
        singUp.setAlpha(alpha);
        forgetPass.setAlpha(alpha);
        email.setAlpha(alpha);
        pass.setAlpha(alpha);
        //bg.setAlpha(alpha);
        bg.setAlpha(alpha);
        welcome.setAlpha(alpha);
        email.setEnabled(true);
        pass.setEnabled(true);
        login.setAlpha(alpha);
        login.setEnabled(true);
        em.setAlpha(alpha);
        ps.setAlpha(alpha);
        skip.setAlpha(alpha);
    }
}