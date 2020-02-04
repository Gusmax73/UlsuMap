package com.isteam.ulsumap.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.isteam.ulsumap.R;

public class SettingsFragment extends Fragment {

    private Switch switch1;
    private Switch switch2;
    private Switch switch3;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        switch1 = root.findViewById(R.id.switch1);
        switch2 = root.findViewById(R.id.switch2);
        switch3 = root.findViewById(R.id.switch3);

        return root;
    }


}