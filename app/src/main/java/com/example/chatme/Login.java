package com.example.chatme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
     Button button;
     EditText EmailAddress,LoginPassword;
     FirebaseAuth auth;
     String Emailpatten ="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
     TextView Signup;
     android.app.ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(getColor(R.color.black));


        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.Loginbutton);
        EmailAddress = findViewById(R.id.editTextLoginEmailAddress);
        LoginPassword = findViewById(R.id.editTexLoginPassword);
        Signup = findViewById(R.id.SingUpbutton1);
        Signup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(Login.this,SignUp.class);
               startActivity(intent);
               finish();
           }
       });
       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String password = LoginPassword.getText().toString();
               String Email= EmailAddress.getText().toString();

               if (TextUtils.isEmpty(Email)){
                   progressDialog.dismiss();
                   Toast.makeText(Login.this, "Enter the Email", Toast.LENGTH_SHORT).show();
               } else if (TextUtils.isEmpty(password)) {
                   progressDialog.dismiss();
                   Toast.makeText(Login.this, "Enter the Password", Toast.LENGTH_SHORT).show();
               } else if (!Email.matches(Emailpatten)) {
                   progressDialog.dismiss();
                   EmailAddress.setError("Give proper EMAIL Address");
               } else if (LoginPassword.length()<6) {
                   progressDialog.dismiss();
                   LoginPassword.setError("low Strenth");
               } else {
                   auth.signInWithEmailAndPassword(Email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()){
                               progressDialog.show();
                               try {
                                   Intent intent= new Intent(Login.this,MainActivity.class );
                               }
                               catch (Exception e){
                                   Toast.makeText(Login.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                               }
                           }
                           else {
                               Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                           }
                       }
                   });
               }
           }
       });

}
}