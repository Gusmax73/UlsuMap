package com.isteam.ulsumap;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Decompress extends AsyncTask<Void, Void, Void> {
    private String zipFile;
    private String location;
   // private int per = 0;

    public Decompress(String zipFile, String location) {
        this.zipFile = zipFile;
        this.location = location;
        dirChecker("");
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try  {
            FileInputStream fin = new FileInputStream(zipFile);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                Log.v("Decompress", "Unzipping " + ze.getName());

                if(ze.isDirectory()) {
                    dirChecker(ze.getName());
                } else {
                    FileOutputStream fout = new FileOutputStream(location + ze.getName());
                    for (int c = zin.read(); c != -1; c = zin.read()) {
                        fout.write(c);
                    }

                    zin.closeEntry();
                    fout.close();
                }

            }
            zin.close();
        } catch(Exception e) {
            Log.e("Decompress", "unzip", e);
        }

        return null;
    }

    private void dirChecker(String dir) {
        File f = new File(location + dir);

        if(!f.isDirectory()) {
            f.mkdirs();
        }
    }

}