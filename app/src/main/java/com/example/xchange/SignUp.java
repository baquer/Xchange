package com.example.xchange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {


    private EditText name,phone,email,password,location;
    private  Button b1;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_sign_up);
        //EditText name,phone,email,password,location;
        //Button b1;

        name=findViewById(R.id.name);
        phone=findViewById(R.id.phone);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        location=findViewById(R.id.location);
        b1=findViewById(R.id.button3);

        String name1=name.getText().toString();
        String phone1=phone.getText().toString();
        String email1=email.getText().toString();
        String password1=password.getText().toString();
        String location1=location.getText().toString();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SEnd the data to firebase and then, call main activity.
                registerUser();

            }
        });
    }

    private void registerUser() {

        String name1 = name.getText().toString().trim();
        String phone1 = phone.getText().toString().trim();
        String email1 = email.getText().toString().trim();
        String password1 = password.getText().toString().trim();
        String location1 = location.getText().toString().trim();

        if(name1.isEmpty()) {
            name.setError("Please Enter Your Name");
            name.requestFocus();
            return;
        }
        if(phone1.isEmpty()) {
            phone.setError("Please Enter Your Phone Number");
            phone.requestFocus();
            return;
        }
        if(location1.isEmpty()) {
            location.setError("Please Enter Your Location");
            location.requestFocus();
            return;
        }
        if(password1.isEmpty()) {
            password.setError("Please Enter a Password");
            password.requestFocus();
            return;
        }
        if(password1.length() < 6) {
            password.setError("Please Enter Password Of Length Greater Than 6");
            password.requestFocus();
            return;
        }
        if(email1.isEmpty()) {
            email.setError("Please Enter Your Email Address");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email1).matches()) {
            email.setError("Please Enter A Valid Password");
            email.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email1,password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    User user = new User(name1,location1,email1,phone1);
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(SignUp.this, "User has been registered successfully", Toast.LENGTH_SHORT).show();
                                // Redirect to Login view
                                Intent intent = new Intent(SignUp.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignUp.this, "Failed to register user", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignUp.this, "Failed to register user", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}