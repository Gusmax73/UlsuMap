package com.isteam.ulsumap.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.isteam.ulsumap.MainActivity;
import com.isteam.ulsumap.R;

import java.io.File;

public class MapFragment extends Fragment {

    private int REQUEST_CODE_PERMISSION;
    private int bufKorp;
    private int bufEtazh;
    private String vuz;
    private boolean check;          //false = 2D, true = 3D
    private WebView webView;
    private String url3D = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_map, container, false);
        MainActivity ma = (MainActivity) getActivity();
        vuz = ma.getVuzChoice();
        bufKorp = ma.getKorpus()+1;
        bufEtazh = ma.getEtazh()+1;


        webView = (WebView) root.findViewById(R.id.webView_map);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.setPadding(0, 0, 0, 0);
        webView.setScrollbarFadingEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);



        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.FAB);
        final TextView fab_textView = (TextView) root.findViewById(R.id.textView_fab);
        fab_textView.setText(R.string.fab_text2D);
        check = false;
        loadMap2D(webView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!check) {
                    check = true;
                    fab_textView.setText(R.string.fab_text3D);
                    loadMap3D(webView);
                }
                else {
                    check = false;
                    fab_textView.setText(R.string.fab_text2D);
                    loadMap2D(webView);
                }
            }
        });
        return root;
    }

    private void loadMap2D(WebView _webView) {
        File fDir = new File(getActivity().getFilesDir() + "/map/" + vuz);
        if (!fDir.isDirectory()) {
            _webView.loadData(getString(R.string.webView_noLoaded), "text/html; charset=utf-8", "utf-8");
        }
        else {
            File fMap = new File(getActivity().getFilesDir() + "/map/" + vuz + "/k" + bufKorp + "/e" + bufEtazh + ".svg");
            if(fMap.exists()) {
                _webView.loadUrl("file://" + getActivity().getFilesDir() + "/map/" + vuz + "/k" + bufKorp + "/e" + bufEtazh + ".svg");
            }
            else
            {
                _webView.loadData(getString(R.string.webView_hintEtazh), "text/html; charset=utf-8", "utf-8");
            }
        }
        Toolbar toolbar =  getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.nameKorpus) + " " + bufKorp + ", " + getString(R.string.nameEtazh) + " " + bufEtazh);
    }

    private void loadMap3D(WebView _webView) {
        int permissionStatus_state = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_NETWORK_STATE);
        if(!(permissionStatus_state == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_NETWORK_STATE},
                    REQUEST_CODE_PERMISSION);
        }
        else{
            if(isNetworkAvailable()){
                _webView.loadUrl(getString(R.string.url3D));
            }
            else {
                _webView.loadData(getString(R.string.webView_noConnection), "text/html; charset=utf-8", "utf-8");
            }
            Toolbar toolbar =  getActivity().findViewById(R.id.toolbar);
            toolbar.setTitle(getString(R.string.webView_name3D));
        }


    }

    @Override
    public void onResume() {
        MainActivity ma = (MainActivity) getActivity();
        vuz = ma.getVuzChoice();
        bufKorp = ma.getKorpus()+1;
        bufEtazh = ma.getEtazh()+1;
        Toolbar toolbar =  getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.nameKorpus) + " " + bufKorp + ", " + getString(R.string.nameEtazh) + " " + bufEtazh);
        super.onResume();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}