package com.example.vendorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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
TextView reg_nav;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
EditText mEmail,mPassword;
Button mLogin;
FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
        fAuth=FirebaseAuth.getInstance();
        reg_nav=findViewById(R.id.reg_nav);
        mEmail=findViewById(R.id.vendor_email);
        mPassword=findViewById(R.id.vendor_pass);
        mLogin=findViewById(R.id.login_btn1);
        reg_nav.setText(Html.fromHtml("Don't have an account?\n<p  style= color:#02A4EC><u><b>Create Account</b></u></p>"));
        if (fAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),Home.class));
            finish();
        }
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=mEmail.getText().toString().trim();
                String passwd=mPassword.getText().toString().trim();

                if(!validateEmail() |  !validatePassword() )
                {

                    return ;
                }
                fAuth.signInWithEmailAndPassword(email,passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            startActivity(new Intent(getApplicationContext(), Home.class));
                            Toast.makeText(Login.this, "Logged in", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(Login.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            //relativeLayout.setVisibility(View.GONE);
                            //pgbar1.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });
    }
    private Boolean validateEmail() {
        String val = mEmail.getText().toString().trim();


        if (val.isEmpty()) {
            mEmail.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            mEmail.setError("Invalid email address");
            return false;
        } else {
            mEmail.setError(null);
            mEmail.setEnabled(true);
            return true;
        }
    }
    private Boolean validatePassword() {
        String val = mPassword.getText().toString().trim();

        if (val.isEmpty()) {
            mPassword.setError("Field cannot be empty");
            return false;
        } else if (val.length()<=5) {
            mPassword.setError("Password must be Atleast 6 characters");
            return false;
        } else {
            mPassword.setError(null);
            mPassword.setEnabled(true);
            return true;
        }
    }
}