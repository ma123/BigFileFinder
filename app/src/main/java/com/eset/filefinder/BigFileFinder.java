package com.eset.filefinder;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.RequiresPermission;

public class BigFileFinder {

    private int numberOfFiles = 0;
    private List<String> listOfPaths = new LinkedList<>();

    public BigFileFinder(int numberOfFiles, List<String> listOfPaths) {
        this.numberOfFiles = numberOfFiles;
        this.listOfPaths = listOfPaths;
    }

    public List<String> search() {
        List<String> listOfFiles = new LinkedList<>();


        for (String path:listOfPaths) {
            Log.d("Files", "File: "+ path);
            File directory = new File(path);
            File[] files = directory.listFiles();
            Log.d("Files", "File: "+ files);
        }

        return listOfFiles;
    }
}
