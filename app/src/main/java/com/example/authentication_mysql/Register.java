package com.example.authentication_mysql;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
TextView login_nav;
String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
EditText mEmail,mName,mPhoneNumber,mPassword,mConfirmPassword;
Button mRegister;
FirebaseAuth fAuth;
FirebaseFirestore fStore;
String userid;
ImageView mshowpassreg, mshowconfirmpassreg;
RelativeLayout relativeLayout;
ProgressBar pgBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        login_nav=findViewById(R.id.login_nav);
        login_nav.setText(Html.fromHtml("Already have an account? <b>Login<b>"));
        mEmail=findViewById(R.id.reg_email);
        mName=findViewById(R.id.reg_userName);
        mPhoneNumber=findViewById(R.id.reg_phoneNumber);
        mPassword=findViewById(R.id.reg_password);
        mConfirmPassword=findViewById(R.id.reg_confirmPassword);
        relativeLayout=findViewById(R.id.relativeLayout_pgbar1);
        pgBar=findViewById(R.id.progressBar);
        mRegister=findViewById(R.id.register_btn);
        mEmail=findViewById(R.id.reg_email);
        mshowpassreg = findViewById(R.id.eye_icon_password1);
        mshowconfirmpassreg = findViewById(R.id.eye_icon_password_confrim);

        getSupportActionBar().hide();

        mshowpassreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.eye_icon_password1) {

                    if (mPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                        mshowpassreg.setImageResource(R.drawable.ic_eye_open);

                        //Show Password
                        mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        mPassword.setSelection(mPassword.getText().length());

                    } else {
                        ((ImageView) (view)).setImageResource(R.drawable.ic_baseline_visibility_off_24);

                        //Hide Password
                        mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        mPassword.setSelection(mPassword.getText().length());


                    }
                }
            }
        });
        mshowconfirmpassreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.eye_icon_password_confrim) {

                    if (mConfirmPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                        mshowconfirmpassreg.setImageResource(R.drawable.ic_eye_open);

                        //Show Password
                        mConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        mConfirmPassword.setSelection(mConfirmPassword.getText().length());

                    } else {
                        ((ImageView) (view)).setImageResource(R.drawable.ic_baseline_visibility_off_24);

                        //Hide Password
                        mConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        mConfirmPassword.setSelection(mConfirmPassword.getText().length());


                    }
                }
            }
        });




        login_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                final String fullname = mName.getText().toString().trim();
                final String phone = mPhoneNumber.getText().toString().trim();
                Bundle bundle = new Bundle();
                bundle.putString("Name", fullname);
                bundle.putString("Email", email);
                bundle.putString("Password", password);
                bundle.putString("PhoneNo", phone);
                if (!validateName() | !validatePassword() | !validatePhoneNo() | !validateEmail() | !validateConfirmPassword()) {

                    return;
                }
                relativeLayout.setVisibility(View.VISIBLE);
                pgBar.setVisibility(View.VISIBLE);

                //User Register
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        relativeLayout.setVisibility(View.VISIBLE);
                        pgBar.setVisibility(View.VISIBLE);
                        if (task.isSuccessful()) {
                            //send verification Link
                            fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){

                                        Intent intent = new Intent(getApplicationContext(), Home.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        relativeLayout.setVisibility(View.GONE);
                                        pgBar.setVisibility(View.GONE);
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(Register.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                            FirebaseUser fuser = fAuth.getCurrentUser();
                            Toast.makeText(Register.this, "User Created.", Toast.LENGTH_SHORT).show();


                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Register.this, "Verfication Email has been Sent ! Click to Verify.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("", "onFailure: Email not Sent " + e.getMessage());
                                }
                            });

                        }
                        userid = fAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = fStore.collection("UserDetails").document(userid);
                        Map<String, Object> user = new HashMap<>();
                        user.put("FullName", fullname);
                        user.put("E-mail", email);
                        user.put("Mobile-No", phone);
                        user.put("Password",password);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("", "onSuccess: User Profile is created for " + userid);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("", "onFailure: " + e.toString());
                            }
                        });
                    }
                });



            }
        });


    }
    private Boolean validateName() {

        String name = mName.getText().toString();
        if (name.isEmpty()) {
            mName.setError("Field cannot be empty");
            return false;
        } else if (name.length() >= 20) {
            mName.setError("Name Too Long");
            return false;
        } else {
            mName.setError(null);
            mName.setEnabled(true);
            return true;
        }

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
        } else if (val.length() <= 5) {
            mPassword.setError("Password must be Atleast 6 characters");
            return false;
        } else {
            mPassword.setError(null);
            mPassword.setEnabled(true);
            return true;
        }
    }

    private Boolean validatePhoneNo() {
        String val = mPhoneNumber.getText().toString().trim();

        if (val.isEmpty()) {
            mPhoneNumber.setError("Field cannot be empty");
            return false;
        }else if (val.length()>10){
            mPhoneNumber.setError("Invalid Number");
            return false;
        }
        else {
            mPhoneNumber.setError(null);
            mPhoneNumber.setEnabled(true);
            return true;
        }
    }

    private Boolean validateConfirmPassword() {
        String val = mConfirmPassword.getText().toString().trim();
        String val2 = mPassword.getText().toString().trim();
        if (val.isEmpty()) {
            mConfirmPassword.setError("Field cannot be empty");
            return false;
        } else if (!val2.equals(val)) {
            mConfirmPassword.setError("Password doesn't match");
            return false;
        } else {
            mConfirmPassword.setError(null);
            mConfirmPassword.setEnabled(true);
            return true;
        }
    }

}