package com.practice.wastemanagment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class BiodegredableForm extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    String[] types = { "Paper", "Cardboard", "Organic", "Plants", "Other"};
    Button submit;
    EditText address, pinCode,city;
    LinearLayout uploadImg,location,typcont;
    TextView wel;
    Uri postImage;
    ImageView waste;
    FirebaseAuth auth;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    String type="Paper";
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biodegredable_form);

        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,types);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Sprite doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);
        progressBar.setVisibility(View.GONE);


        address=findViewById(R.id.BioAddress);
        city=findViewById(R.id.BioCity);
        pinCode=findViewById(R.id.BioPincode);
        submit=findViewById(R.id.OrganicSubmit);
        uploadImg=findViewById(R.id.uploadBWasteImg);
        waste=findViewById(R.id.wasteImage);
        location=findViewById(R.id.location);
        wel=findViewById(R.id.WelcomeTextView);
        typcont=findViewById(R.id.typecont);

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(BiodegredableForm.this)
                        .galleryOnly()
                        .cropSquare()
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
//                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(BiodegredableForm.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location=task.getResult();
                            if(location!=null){
                                try {
                                    Geocoder geocoder=new Geocoder(BiodegredableForm.this, Locale.getDefault());
                                    List<Address> loc=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                    address.setText(Html.fromHtml("<font color:'##4a4a4a'> Latitude : "+loc.get(0).getLatitude()+" Longitude : "+loc.get(0).getLongitude()+"</font>"));
                                    city.setText(Html.fromHtml("<font color:'##4a4a4a'>"+loc.get(0).getLocality()+"</font>"));
                                    pinCode.setText(Html.fromHtml("<font color:'##4a4a4a'>"+loc.get(0).getPostalCode()+"</font>"));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }else{
                    ActivityCompat.requestPermissions(BiodegredableForm.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (address.getText().toString().isEmpty())
                    address.setError("Address is Required");
                else if (city.getText().toString().isEmpty())
                    city.setError("City is Required");
                else if (pinCode.getText().toString().isEmpty())
                    pinCode.setError("Pincode is Required");
                else if (postImage == null)
                    Toast.makeText(BiodegredableForm.this, "Please Upload Waste Image", Toast.LENGTH_SHORT).show();
                else {
                    hideUI();
                    progressBar.setVisibility(View.VISIBLE);
                   // submit.setVisibility(View.GONE);
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("OrganicWaste");
                    StorageReference imgFilePath= storageRef.child(postImage.getLastPathSegment());
                    imgFilePath.putFile(postImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imgFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    //---------Add Post Object---------------------
                                    DatabaseReference rf;
                                    rf= FirebaseDatabase.getInstance().getReference("Users");
                                    String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();

                                    rf.child(userId).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            User user=snapshot.getValue(User.class);
                                            if(user!=null)
                                            {
                                                OrganicWaste complaint = new OrganicWaste(user.getUserName(), address.getText().toString(), pinCode.getText().toString()
                                                        , city.getText().toString(), type, firebaseUser.getPhotoUrl().toString(), firebaseUser.getUid(), uri.toString(),user.getUserPhone());
                                                SendRequest(complaint);
                                                showUI();
                                                startActivity(new Intent(BiodegredableForm.this,OrganicRequestHistory.class));
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                                showUI();
                                                progressBar.setVisibility(View.INVISIBLE);
                                        }
                                    });

                                }
                            });
                        }
                    });


                }
            }
        });
    }

    private void SendRequest(OrganicWaste request)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference =firebaseDatabase.getReference("OrganicRequest").push();

        String id=reference.getKey();
        request.setBioRequestId(id);
        request.setStatus("Request Send");

        reference.setValue(request).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(BiodegredableForm.this,"Request Send Successfully", Toast.LENGTH_SHORT).show();
                ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
                progressBar.setVisibility(View.GONE);
                submit.setVisibility(View.VISIBLE);
                address.setText("");
                pinCode.setText("");
                city.setText("");
                Resources res = getResources();
                waste.setImageDrawable(res.getDrawable(R.drawable.testing));

            }
        });



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(getApplicationContext(),types[position] , Toast.LENGTH_LONG).show();
        type=types[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri= data.getData();
        waste=findViewById(R.id.wasteImage);
        postImage=uri;
        waste.setImageURI(uri);
    }

    private void showUI() {
        float alpha = 1.0f;
        address.setAlpha(alpha);
        pinCode.setAlpha(alpha);
        city.setAlpha(alpha);
        waste.setAlpha(alpha);
        uploadImg.setAlpha(alpha);
        location.setAlpha(alpha);
        submit.setAlpha(alpha);
        address.setEnabled(true);
        city.setEnabled(true);
        pinCode.setEnabled(true);
        wel.setAlpha(alpha);
        typcont.setAlpha(alpha);
    }
    private void hideUI() {
        float alpha = 0.2f;
        address.setAlpha(alpha);
        pinCode.setAlpha(alpha);
        city.setAlpha(alpha);
        waste.setAlpha(alpha);
        uploadImg.setAlpha(alpha);
        location.setAlpha(alpha);
        submit.setAlpha(alpha);
        address.setEnabled(false);
        city.setEnabled(false);
        pinCode.setEnabled(false);
        wel.setAlpha(alpha);
        typcont.setAlpha(alpha);
    }
}