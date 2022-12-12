package com.practice.wastemanagment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.practice.wastemanagment.Adapter.CommentAdapter;

import org.w3c.dom.Comment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PostDetailedActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    String postId,uName;
    EditText commentText;
    TextView dTittle,dCity,dDate,dDescription,dPostName;
    ImageView postImage,userDp,postUserProfile,sendButton;
    CommentAdapter commentAdapter;
    List<Comments> listComm;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detailed);

        dTittle=findViewById(R.id.D_post_tittle);
        dCity=findViewById(R.id.Dpost_city);
        dDate=findViewById(R.id.postDetailedDate);
        dDescription=findViewById(R.id.D_post_dis);
        dPostName=findViewById(R.id.Dpost_user_name);

        commentText=findViewById(R.id.commentText);
        sendButton=findViewById(R.id.comtSendButton);

        postImage=findViewById(R.id.postDetailedImage);
        userDp=findViewById(R.id.userDp);
        postUserProfile=findViewById(R.id.d_user_profile);

        auth=FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();

        //-----------Intent Data-------------------
        String postImg=getIntent().getExtras().getString("postImage");
        String postUserImg=getIntent().getExtras().getString("postUserPic");
        String postUserName=getIntent().getExtras().getString("postUser");
        String tittle=getIntent().getExtras().getString("tittle");
        String desc=getIntent().getExtras().getString("desc");
        String city=getIntent().getExtras().getString("city");
        postId=getIntent().getExtras().getString("postKey");
        String date=timeStampToString(getIntent().getExtras().getLong("timeStamp"));

        dDescription.setText(desc);
        dCity.setText(city);
        dPostName.setText(postUserName);
        dTittle.setText(tittle);
        dDate.setText(date);
        Glide.with(PostDetailedActivity.this).load(postUserImg).into(postUserProfile);
        Glide.with(PostDetailedActivity.this).load(postImg).into(postImage);
        Glide.with(PostDetailedActivity.this).load(firebaseUser.getPhotoUrl()).into(userDp);

        retriveComments();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!commentText.getText().toString().isEmpty())
                {
                    sendButton.setVisibility(View.INVISIBLE);
                    DatabaseReference commentRef=firebaseDatabase.getReference("Comments").child(postId).push();
                    String com=commentText.getText().toString();
                    String uId=firebaseUser.getUid();
                    String uImg=firebaseUser.getPhotoUrl().toString();
                    //userName(uId);
                    String n=firebaseUser.getDisplayName().toString();
                    Comments comments =new Comments(uId,uImg,com,n);
                    commentRef.setValue(comments).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(PostDetailedActivity.this,"Comment Post..",Toast.LENGTH_SHORT).show();
                            commentText.setText("");
                            sendButton.setVisibility(View.VISIBLE);
                        }
                    });


                }
            }
        });
    }

    private void retriveComments() {
        recyclerView=findViewById(R.id.comment_Recyle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        auth=FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference("Comments").child(postId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listComm=new ArrayList<>();
                for (DataSnapshot sp:snapshot.getChildren()){
                    Comments com= sp.getValue(Comments.class);
                    listComm.add(com);

                }

                commentAdapter=new CommentAdapter(getApplicationContext(),listComm);
                recyclerView.setAdapter(commentAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void userName(String uId) {
        DatabaseReference rf;
        rf= FirebaseDatabase.getInstance().getReference("Users");
        String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        rf.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                if(user!=null)
                {
                    uName=user.getUserName();
                    Toast.makeText(PostDetailedActivity.this,uName,Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private String timeStampToString(long time)
    {

        Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = dateFormat.format(time);
        return strDate;
    }
}