package com.practice.wastemanagment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

    LinearLayout logout,uploadImg,Ohistory;
    TextView name ,email,address,cancel;
    ImageView previous,addProfile;
    LinearLayout nameEdit;
    FirebaseAuth auth;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    String userId,Uname;
    Dialog popupPost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        auth=FirebaseAuth.getInstance();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        firebaseUser=auth.getCurrentUser();

        logout=findViewById(R.id.logoutButton);
        previous=findViewById(R.id.previous);
        name=findViewById(R.id.S_username);
        email=findViewById(R.id.S_email);
        address=findViewById(R.id.S_address);
        uploadImg=findViewById(R.id.uploadImg);
        Ohistory=findViewById(R.id.open_history);
        nameEdit=findViewById(R.id.name_edit);


        //--------add image to profile pic----------------
        addProfile=findViewById(R.id.add_profile_img);
        Uri url=firebaseUser.getPhotoUrl();

        if(url!=null)
            Glide.with(SettingActivity.this).load(firebaseUser.getPhotoUrl()).into(addProfile);



        userId=FirebaseAuth.getInstance().getCurrentUser().getUid();

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                if(user!=null)
                {
                    name.setText(user.getUserName());
                    email.setText(user.getUserEmail());
                    address.setText(user.getUserAddress());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
               Toast.makeText(SettingActivity.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
            }
        });


        previous.setOnClickListener(this);
        logout.setOnClickListener(this);
        uploadImg.setOnClickListener(this);
        Ohistory.setOnClickListener(this);
        nameEdit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.previous)
        {
            startActivity(new Intent(SettingActivity.this,MainActivity.class));
            finish();
            return;
        }
        else if(v.getId()==R.id.logoutButton)
        {
            new AlertDialog.Builder(SettingActivity.this).setTitle("Logout").setMessage("Are you sure you want to logout ?")
                    .setIcon(R.drawable.logout)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseAuth.getInstance().signOut();
                            Intent in=new Intent(SettingActivity.this,loginActivity.class);
                            startActivity(in);
                            finishAffinity();
                        }
                    })
                    .setNegativeButton("No",null)
                .show();

            return;
        }
        if(v.getId()==R.id.open_history)
        {
            startActivity(new Intent(SettingActivity.this,OrganicRequestHistory.class));
            return;
        }
        if(v.getId()==R.id.name_edit)
        {
            popupPost = new Dialog(this);
            popupPost.setContentView(R.layout.popup_name_update);
            popupPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
            popupPost.getWindow().getAttributes().gravity= Gravity.BOTTOM;
            popupPost.show();

            cancel=popupPost.findViewById(R.id.cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupPost.dismiss();
                }
            });

            return;
        }
        else if(v.getId()==R.id.uploadImg)
        {
            ImagePicker.Companion.with(SettingActivity.this)
                     .galleryOnly()
                     .cropSquare()
                     .compress(1024)			//Final image size will be less than 1 MB(Optional)
//                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start();

            return;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri= data.getData();
        //Toast.makeText(SettingActivity.this," "+uri,Toast.LENGTH_SHORT).show();
        addProfile=findViewById(R.id.add_profile_img);
        addProfile.setImageURI(uri);

        uploadImg(auth.getCurrentUser(),uri);

    }

    private void uploadImg(FirebaseUser currentUser, Uri uri) {

        if(uri==null)
        {
            startActivity(new Intent(SettingActivity.this,SettingActivity.class));
        }
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("users_profile");
        StorageReference imgFilePath= storageRef.child(uri.getLastPathSegment());
        imgFilePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imgFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileUpdate= new UserProfileChangeRequest.Builder()
                                .setDisplayName(Uname)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                        Toast.makeText(SettingActivity.this,"Upload Sucessfully",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });
            }
        });
    }
}



