package com.isteam.ulsumap.ui.scanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.isteam.ulsumap.R;

public class ScannerFragment extends Fragment {

    private ScannerViewModel scannerViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        scannerViewModel =
                ViewModelProviders.of(this).get(ScannerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_scanner, container, false);
        /*
        final TextView textView = root.findViewById(R.id.text_share);
        scannerViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


         */
        return root;
    }
}