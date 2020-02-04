package com.isteam.ulsumap;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.isteam.ulsumap.ui.change.ChangeFragment;
import com.isteam.ulsumap.ui.download.DownloadFragment;


public class MainActivity extends AppCompatActivity implements ChangeFragment.OnChangeFragmentInteractionListener, DownloadFragment.OnDownloadFragmentInteractionListener {

    private static final String APP_PREFERENCES = "setting";         //Имя файла в котором сохраняются настройки
    private int REQUEST_CODE_PERMISSION;
    private DrawerLayout drawer;
    private int choiceItemKorp = 0;
    private int choiceItemEtazh = 0;
    private String vuzChoice = "";
    private String VUZ_ID = "vuzId";
    private String NUM_ETAZH = "numEtazh";
    private String NUM_KORP = "numKorp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences mPrefs = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        vuzChoice = mPrefs.getString(VUZ_ID, "");
        choiceItemKorp = mPrefs.getInt(NUM_KORP, 0);
        choiceItemEtazh = mPrefs.getInt(NUM_ETAZH, 0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.nameKorpus) + " " + choiceItemKorp + ", " + getString(R.string.nameEtazh) + " " + choiceItemEtazh);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, drawer);
        NavigationUI.setupWithNavController(navigationView, navController);

        final int permissionStatus_internet = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        final int permissionStatus_state = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
        if(!(permissionStatus_internet == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.INTERNET}, REQUEST_CODE_PERMISSION);
        }

        if(!(permissionStatus_state == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_NETWORK_STATE},
                    REQUEST_CODE_PERMISSION);
        }

  //      Toast toast = Toast.makeText(getApplicationContext(), getFilesDir().toString(), Toast.LENGTH_SHORT);
   //     toast.show();

    }

    public int getKorpus(){
        return choiceItemKorp;
    }

    public int getEtazh(){
        return choiceItemEtazh;
    }

    public String getVuzChoice() {
        return vuzChoice;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, drawer)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onFragmentVuzChoice(String choice) {
        vuzChoice = choice;
    }

    @Override
    public void onFragmentKorpEtazh(int korp, int etazh) {
        choiceItemEtazh = etazh;
        choiceItemKorp = korp;
    }

    @Override
    protected void onPause() {
        SharedPreferences mPrefs = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putInt(NUM_KORP, choiceItemKorp);
        ed.putInt(NUM_ETAZH, choiceItemEtazh);
        ed.commit();
        super.onPause();
    }


}
