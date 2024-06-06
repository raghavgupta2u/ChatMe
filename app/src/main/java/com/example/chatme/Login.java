package com.example.chatme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.Loginbutton);
        EmailAddress = findViewById(R.id.editTextLoginEmailAddress);
        LoginPassword = findViewById(R.id.editTexLoginPassword);

       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String password = LoginPassword.getText().toString();
               String Email= EmailAddress.getText().toString();

               if (TextUtils.isEmpty(Email)){
                   Toast.makeText(Login.this, "Enter the Email", Toast.LENGTH_SHORT).show();
               } else if (TextUtils.isEmpty(password)) {
                   Toast.makeText(Login.this, "Enter the Password", Toast.LENGTH_SHORT).show();
               } else if (!Email.matches(Emailpatten)) {
                   EmailAddress.setError("Give proper EMAIL Address");
               } else if (LoginPassword.length()<6) {
                   LoginPassword.setError("low Strenth");
               } else {
                   auth.signInWithEmailAndPassword(Email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()){
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