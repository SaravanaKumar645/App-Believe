package com.example.vendorapp.ui.menu;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.vendorapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class MenuFragment extends Fragment {
    String userPath,userId;
    Uri fileUri;
    FirebaseStorage firebaseStorage;
    FirebaseFirestore firestore;
    private MenuViewModel menuViewModel;
    EditText mCategory, mProdName, mPrice, mGST, mDeliveryCahrge, mOffer;
    String Category, ProdName, Price, GST, DeliveryCahrge, Offer;
    Button mUpload;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        menuViewModel =
                new ViewModelProvider(this).get(MenuViewModel.class);
        View root = inflater.inflate(R.layout.fragment_menu, container, false);
        mCategory=root.findViewById(R.id.p_category1);
        userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        userPath="user/"+userId+"/";
        firebaseStorage=FirebaseStorage.getInstance();
        firestore=FirebaseFirestore.getInstance();
        mProdName=root.findViewById(R.id.p_prodName);
        mPrice=root.findViewById(R.id.p_priceAmount);
        mGST=root.findViewById(R.id.p_GstAmount);
        mDeliveryCahrge=root.findViewById(R.id.p_deliveryCahrge);
        mOffer=root.findViewById(R.id.p_offer);
        mUpload=root.findViewById(R.id.p_Upload);

        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category=mCategory.getText().toString();
                ProdName=mProdName.getText().toString();
                Price=mPrice.getText().toString();
                GST=mGST.getText().toString();
                DeliveryCahrge=mDeliveryCahrge.getText().toString();
                Offer=mOffer.getText().toString();
                uploadDetails(Category,ProdName,Price,GST,DeliveryCahrge,Offer);

            }
        });

        return root;

    }

    private void uploadDetails(String category, String prodName, String price, String gst, String deliveryCahrge, String offer) {


        DocumentReference documentReference=firestore.collection("Product Details").document(userId);
        Map<String,Object> details=new HashMap<>();

        details.put("Category", category);
        details.put("Product Name", prodName);
        details.put("PriceAmount", price);
        details.put("GSTAmount",gst);
        details.put("DeliveryCharges",deliveryCahrge);
        details.put("Offer",offer);
        documentReference.set(details).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), "Product Details Added !", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error adding Product", Toast.LENGTH_SHORT).show();
            }
        });

    }


}