package com.example.authentication_mysql;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Home extends AppCompatActivity {
TextView mName,mDesc,mPrice,mcutoutprice,moffer,mDelivery;
String name,desc,price,offer,gst,dc;
Button mCart,mBuynow;
FirebaseFirestore fstore;
String userId;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about_app_menuBtn:

                //add the function to perform here
                return (true);
            case R.id.exit_app_menuBtn:
                finish();
                System.exit(0);
                //add the function to perform here
                return (true);
            case R.id.logout_menuBtn:
                //add the function to perform here
                return (true);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        fstore=FirebaseFirestore.getInstance();
        mDelivery=findViewById(R.id.delivery_cahrge);
        mcutoutprice=findViewById(R.id.cut_out_price);
        moffer=findViewById(R.id.offer_text);
        mName=findViewById(R.id.prod_name);
        mPrice=findViewById(R.id.prod_price);
        mDesc=findViewById(R.id.prod_details);
        userId=FirebaseAuth.getInstance().getCurrentUser().getUid();


        DocumentReference documentReference = fstore.collection("Product Details").document("qmIrQiTj23UFF0OKZGWrL3hC5IQ2");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    desc=documentSnapshot.getString("Product Name");
                    name=documentSnapshot.getString("Category");
                    price=documentSnapshot.getString("PriceAmount");
                    offer=documentSnapshot.getString("Offer");
                    gst=documentSnapshot.getString("GSTAmount");
                    dc=documentSnapshot.getString("DeliveryCharges");


                    mName.setText(name);
                    mDesc.setText(desc);
                    mcutoutprice.setText("Rs."+gst+" GST");
                    mPrice.setText("Rs."+price);
                    moffer.setText(offer+"%off");
                    mDelivery.setText(Html.fromHtml("<b>Delivery Charges :</b> "+"Rs."+dc));

                }
            }
        });










    }
    public void logout(MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        finish();//to log out
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    public void aboutApplication(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setTitle("About App");
        builder.setCancelable(true);

        builder.setMessage("It is a free e- commerce Application.You can use it to purchase any products.\n\n\nThis application is still under development.So you may not experience full features. ");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        builder.show();
    }
}