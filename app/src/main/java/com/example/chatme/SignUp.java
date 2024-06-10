package com.example.chatme;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity {
    TextView SU_loginButton;
    EditText SU_emailaddress, SU_username, SU_confirmpassword, SU_password;
    Button SU_singupbutton;
    String Emailpatten = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri imageURI;
    String imageuri;
    ProgressDialog progressDialog;
    CircleImageView SU_profileimg;
    ActivityResultLauncher<Intent> imagePickLanture;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Singup your account");
        progressDialog.setCancelable(false);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(getColor(R.color.black));

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        SU_emailaddress = findViewById(R.id.editEmailAddress);
        SU_username = findViewById(R.id.editusername);
        SU_confirmpassword = findViewById(R.id.editconfirmPassword);
        SU_password = findViewById(R.id.editPassword);
        SU_loginButton = findViewById(R.id.textloginButton);
        SU_singupbutton = findViewById(R.id.Singupbutton);
        SU_profileimg = findViewById(R.id.profilerg);


        SU_loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
                finish();
            }
        });



        //Adding Profile Picture
        SU_profileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),10);

            }
        });




        SU_singupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namee = SU_username.getText().toString();
                String email = SU_emailaddress.getText().toString();
                String password = SU_password.getText().toString();
                String cpassword = SU_confirmpassword.getText().toString();
                String status = "Hey I'm Using This Application";

                if (TextUtils.isEmpty(namee) || TextUtils.isEmpty(email) ||
                        TextUtils.isEmpty(password) || TextUtils.isEmpty(cpassword)) {
                    progressDialog.dismiss();
                    Toast.makeText(SignUp.this, "Please Enter Valid Information", Toast.LENGTH_SHORT).show();
                } else if (!email.matches(Emailpatten)) {
                     progressDialog.dismiss();
                    SU_emailaddress.setError("Type A Valid Email Here");
                } else if (password.length() < 6) {
                     progressDialog.dismiss();
                    SU_password.setError("Password Must Be 6 Characters Or More");
                } else if (!password.equals(cpassword)) {
                     progressDialog.dismiss();
                    SU_password.setError("The Password Doesn't Match");
                } else {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String id = task.getResult().getUser().getUid();
                                DatabaseReference reference = database.getReference().child("user").child(id);
                                StorageReference storageReference = storage.getReference().child("Upload").child(id);

                                if (imageURI != null) {
                                    storageReference.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageuri = uri.toString();
                                                        Users users = new Users(id, namee, email, password, imageuri, status);
                                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    progressDialog.show();
                                                                    Intent intent = new Intent(SignUp.this, MainActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                } else {
                                                                    Toast.makeText(SignUp.this, "Error in creating the user", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                } else {

                                    imageuri = "https://firebasestorage.googleapis.com/v0/b/chateapp-my.appspot.com/o/circle-user_48.png?alt=media&token=18e78bcc-0b77-45cd-abca-dfb2ec2b21da";
                                    Users users = new Users(id, namee, email, password, imageuri, status);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.show();
                                                Intent intent = new Intent(SignUp.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(SignUp.this, "Error in creating the user", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }

}
