package com.example.vendorapp.ui.payIn;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.vendorapp.R;

public class PayInFragment extends Fragment {

    private PayInViewModel payInViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        payInViewModel =
                new ViewModelProvider(this).get(PayInViewModel.class);
        View root = inflater.inflate(R.layout.fragment_payin, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        payInViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}