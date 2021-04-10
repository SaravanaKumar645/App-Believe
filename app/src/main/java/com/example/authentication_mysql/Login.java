package com.example.authentication_mysql;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
TextView reg_nav,mMob_login;
RelativeLayout relativeLayout;
ProgressBar pgbar1;
FirebaseAuth fAuth;
ImageView mShowhidePassword;
Button mLogin;
EditText mEmail,mPassword;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mMob_login=findViewById(R.id.login_mobnumber);
        fAuth=FirebaseAuth.getInstance();
        mShowhidePassword=findViewById(R.id.eye_icon_password);
        mLogin=findViewById(R.id.login_btn);
        pgbar1=findViewById(R.id.progressBar1);
        relativeLayout=findViewById(R.id.relativeLayout_pgbar);
        mEmail=findViewById(R.id.login_email);
        mPassword=findViewById(R.id.login_password);
        reg_nav=findViewById(R.id.reg_nav);
        reg_nav.setText(Html.fromHtml("Don't have an account? <b>Register<b>"));

        getSupportActionBar().hide();

        mShowhidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPasswordButton(view);
            }
        });
        if(fAuth.getCurrentUser() !=null) {
            startActivity(new Intent(getApplicationContext(), Home.class));
            finish();
        }

        reg_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
                finish();
            }
        });

        mMob_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=mEmail.getText().toString().trim();
                String passwd=mPassword.getText().toString().trim();

                if(!validateEmail() |  !validatePassword() )
                {

                    return ;
                }
                relativeLayout.setVisibility(View.VISIBLE);
                pgbar1.setVisibility(View.VISIBLE);

                //Authentication of user

                fAuth.signInWithEmailAndPassword(email,passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            startActivity(new Intent(getApplicationContext(), Home.class));
                            finish();
                        }else {
                            Toast.makeText(Login.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            relativeLayout.setVisibility(View.GONE);
                            pgbar1.setVisibility(View.GONE);
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
    private void showPasswordButton(View view) {
        if(view.getId()==R.id.eye_icon_password){

            if(mPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                mShowhidePassword.setImageResource(R.drawable.ic_eye_open);

                //Show Password
                mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                mPassword.setSelection(mPassword.getText().length());

            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_baseline_visibility_off_24);

                //Hide Password
                mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                mPassword.setSelection(mPassword.getText().length());


            }
        }
    }
}