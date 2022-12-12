package com.practice.wastemanagment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.transition.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView logo,setting,home,news,add,postUserImg,postImg,postDisplayImg,deal;
    Button addPost;
    Dialog popupPost;
    EditText postTittle,postCity,postDescription;
    FirebaseAuth auth;
    String userName;
    DatabaseReference reference;
    Uri postImage;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Sprite doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);
        progressBar.setVisibility(View.VISIBLE);

        getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityContent, new HomeFragment()).commit();

        setting=findViewById(R.id.setting);
        home=findViewById(R.id.home);
        news=findViewById(R.id.news);
        add=findViewById(R.id.addPost);
        deal=findViewById(R.id.selling);

        home.setOnClickListener(this);
        setting.setOnClickListener(this);
        news.setOnClickListener(this);
        add.setOnClickListener(this);
        deal.setOnClickListener(this);

       // logout=findViewById(R.id.logoutButton);


    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.setting)
        {
            startActivity(new Intent(MainActivity.this,SettingActivity.class));
            return;
        }
        else if(v.getId()==R.id.home)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityContent, new HomeFragment()).commit();
            ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
            progressBar.setVisibility(View.GONE);

            return;
        }
        else if(v.getId()==R.id.news)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityContent, new NewsFragment()).commit();
            ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
            progressBar.setVisibility(View.GONE);
            return;
        }
        else if(v.getId()==R.id.selling)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityContent, new DealFragment()).commit();
            ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
            progressBar.setVisibility(View.GONE);
            return;
        }
        else if(v.getId()==R.id.addPost);
        {
            popupPost = new Dialog(this);
            popupPost.setContentView(R.layout.pop_up_add_post);
            popupPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
            popupPost.getWindow().getAttributes().gravity= Gravity.TOP;
            popupPost.show();

            popUPPostUpdate();

            return;
        }




    }
//-----------------popup Post taking input---------------------

    private void popUPPostUpdate() {
        postUserImg=popupPost.findViewById(R.id.postDP);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        Uri url=firebaseUser.getPhotoUrl();
        if(url!=null)
            Glide.with(MainActivity.this).load(firebaseUser.getPhotoUrl()).into(postUserImg);

        postImg=popupPost.findViewById(R.id.postImageUpload);
        addPost=popupPost.findViewById(R.id.addPostButton);
        postImg=popupPost.findViewById(R.id.postImageUpload);
        postTittle=popupPost.findViewById(R.id.postTittle);
        postCity=popupPost.findViewById(R.id.postCity);
        postDescription=popupPost.findViewById(R.id.discriptionPost);
        ProgressBar progressBar =popupPost.findViewById(R.id.postProgressbar);



        postImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(MainActivity.this)
                        .galleryOnly()
                        .crop()
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
//                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });


        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String tittle=postTittle.getText().toString();
               String city=postCity.getText().toString();
               String dis=postDescription.getText().toString();

               if(tittle.isEmpty())
               {
                   postTittle.setError("Tittle is Required");
                   postTittle.requestFocus();
                   return;
               }
               else if(city.isEmpty())
               {
                   postCity.setError("Post City is Required");
                   postCity.requestFocus();
                   return;
               }
               else if(dis.isEmpty())
               {
                   postDescription.setError("Post Description is Required");
                   postDescription.requestFocus();
                   return;
               }
               else if(postImage==null)
               {
                   Toast.makeText(MainActivity.this,"Please Upload Post Image",Toast.LENGTH_SHORT).show();
                   return;
               }
               else if(!city.isEmpty() && !dis.isEmpty() && !tittle.isEmpty()  && postImage!=null)
               {
                   progressBar.setVisibility(View.VISIBLE);
                   addPost.setVisibility(View.GONE);
                   postImg.setVisibility(View.GONE);
                   StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Post_images");
                   StorageReference imageFilePath= storageReference.child(postImage.getLastPathSegment());
                   imageFilePath.putFile(postImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                           imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                               @Override
                               public void onSuccess(Uri uri) {

                                   //---------Add Post Object---------------------
                                   DatabaseReference rf;
                                   rf= FirebaseDatabase.getInstance().getReference("Users");
                                   String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();

                                   rf.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                                           User user=snapshot.getValue(User.class);
                                           if(user!=null)
                                           {
                                               Post post= new Post(tittle,city,dis,uri.toString(),firebaseUser.getUid(),firebaseUser.getPhotoUrl().toString(),user.getUserName().toString());
                                               uploadPost(post);
                                           }

                                       }
                                       @Override
                                       public void onCancelled(@NonNull DatabaseError error) {
                                       }
                                   });

                               }
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   Toast.makeText(MainActivity.this,"Something went wrong on uploading Picture "+e.getMessage(),Toast.LENGTH_SHORT).show();
                                   progressBar.setVisibility(View.GONE);
                                   addPost.setVisibility(View.VISIBLE);
                                   postImg.setVisibility(View.VISIBLE);
                               }
                           });
                       }
                   });
               }

            }
        });
    }
//----------Adding post to database-------------------------
    private void uploadPost(Post post) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference =firebaseDatabase.getReference("Posts").push();

        String id=reference.getKey();
        post.setPostId(id);




        reference.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                postTittle=popupPost.findViewById(R.id.postTittle);
                postTittle.setText(post.getUserName());
                Toast.makeText(MainActivity.this,"Post Upload Successfully", Toast.LENGTH_SHORT).show();
                ProgressBar progressBar =popupPost.findViewById(R.id.postProgressbar);
                postImg=popupPost.findViewById(R.id.postImageUpload);
                addPost=popupPost.findViewById(R.id.addPostButton);
                progressBar.setVisibility(View.GONE);
                addPost.setVisibility(View.VISIBLE);
                postImg.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri= data.getData();
        postDisplayImg=popupPost.findViewById(R.id.imagePost);

        postImage=uri;
        postDisplayImg.setImageURI(uri);


    }
}