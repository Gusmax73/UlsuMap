package com.isteam.ulsumap.ui.download;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.isteam.ulsumap.Decompress;
import com.isteam.ulsumap.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadFragment extends Fragment {

    private SharedPreferences mPrefs;
    private String VUZ_ID = "vuzId";
    private static final String APP_PREFERENCES = "setting";
    private OnDownloadFragmentInteractionListener mListener;
    private int REQUEST_CODE_PERMISSION;
    private String[] strVuzName;
    private String[] strVuzId;
    private String vuzChoice;
    private Spinner spinner_download;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        strVuzName = new String[]{getString(R.string.nameUlsu), "Пример 1", "Пример 2"};
        strVuzId = new String[]{"ulsu", "exam1", "exam2"};
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_download, container, false);
        Button button_start = root.findViewById(R.id.button_download_start);
        Button button_del = root.findViewById(R.id.button_download_delete);
        spinner_download = (Spinner) root.findViewById(R.id.spinner_download);

        final int permissionStatus_internet = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.INTERNET);

        ArrayAdapter<String> adapter_vuz = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, strVuzName);
        adapter_vuz.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_download.setAdapter(adapter_vuz);
        spinner_download.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vuzChoice = strVuzId[position];
                mListener.onFragmentVuzChoice(vuzChoice);

                File f = new File(getActivity().getFilesDir()  + "/" + vuzChoice + ".zip");
                if(f.exists()) {
                    SharedPreferences mPrefs = getActivity().getSharedPreferences(APP_PREFERENCES, getActivity().MODE_PRIVATE);
                    SharedPreferences.Editor ed = mPrefs.edit();
                    ed.putString(VUZ_ID, vuzChoice);
                    ed.commit();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((permissionStatus_internet == PackageManager.PERMISSION_GRANTED) ) {
                        new DownloadFileFromURL().execute(getString(R.string.host) + vuzChoice + ".zip");

                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.INTERNET},
                            REQUEST_CODE_PERMISSION);
                }
            }
        });

        button_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((permissionStatus_internet == PackageManager.PERMISSION_GRANTED)) {
                    File f = new File(getActivity().getFilesDir()  + "/" + vuzChoice + ".zip");
                    File fmap = new File(getActivity().getFilesDir()  + "/map/" + vuzChoice);
                    if (f.exists() || fmap.exists())
                    {
                        if(f.exists()) { deleteDir(f); }
                        if(fmap.exists()) { deleteDir(fmap); }
                        Toast toast = Toast.makeText(getActivity(), R.string.mapDelete, Toast.LENGTH_SHORT);
                        toast.show();
                    }

                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.INTERNET},
                            REQUEST_CODE_PERMISSION);
                }
            }
        });

        return root;
    }

    public interface OnDownloadFragmentInteractionListener {               //Интерфейс для взаимодействия с Activity
        void onFragmentVuzChoice(String choice);    //Передача выбранного вуза в Activity
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (DownloadFragment.OnDownloadFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    public class DownloadFileFromURL extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            File f = new File(getActivity().getFilesDir() + "/" + vuzChoice + ".zip");
            if (f.exists())
            {
                cancel(true);
                Toast toast = Toast.makeText(getActivity(), R.string.installAlready, Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream

                OutputStream output = new FileOutputStream(getActivity().getFilesDir() + "/" + vuzChoice + ".zip");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            File f = new File(getActivity().getFilesDir() + "/" + vuzChoice + ".zip");
            if (f.exists())
            {
                Toast toast = Toast.makeText(getActivity(), R.string.downloadSuccessfully, Toast.LENGTH_SHORT);
                toast.show();
                try {
                    new Decompress(getActivity().getFilesDir()  + "/" + vuzChoice + ".zip", getActivity().getFilesDir() + "/map/").execute();
                    toast = Toast.makeText(getActivity(), R.string.installSuccessfully, Toast.LENGTH_SHORT);
                    toast.show();

                    mListener.onFragmentVuzChoice(vuzChoice);
                    SharedPreferences mPrefs = getActivity().getSharedPreferences(APP_PREFERENCES, getActivity().MODE_PRIVATE);
                    SharedPreferences.Editor ed = mPrefs.edit();
                    ed.putString(VUZ_ID, vuzChoice);
                    ed.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                    toast = Toast.makeText(getActivity(), R.string.installFailed, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            else {
                Toast toast = Toast.makeText(getActivity(), R.string.downloadFailed, Toast.LENGTH_SHORT);
                toast.show();
            }

        }

    }

    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            assert children != null;
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

}
