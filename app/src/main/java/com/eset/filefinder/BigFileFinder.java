package com.eset.filefinder;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.eset.filefinder.data.FileResult;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import androidx.documentfile.provider.DocumentFile;

public class BigFileFinder {

    private Context context;
    private List<Uri> listOfUri = new LinkedList<>();


    public BigFileFinder(Context context, List<Uri> listOfUri) {
        this.context = context;
        this.listOfUri = listOfUri;
    }

    public List<String> search(int numberOfFiles) {

        List<String> listOfBigFiles = new LinkedList<>();
        List<FileResult> listResultFiles = new LinkedList<>();
        Long largestFileSize = 0L;

        for (Uri u:listOfUri) {
            DocumentFile dfile = DocumentFile.fromTreeUri(context, u);
            DocumentFile[] fileList = dfile.listFiles();

            for (DocumentFile file : fileList) {
                if(!file.isDirectory()) {
                    if (largestFileSize < file.length()) {
                        largestFileSize = file.length();
                        listResultFiles.add(new FileResult(file.getUri().getPath(), file.length()));
                    }
                }
            }
        }

        int counter = 0;
        for (int i = listResultFiles.size() - 1; i >= 0; i--) {
            if(counter == numberOfFiles)
            {
                break;
            }
            else {
                Log.d("File", "File: " + listResultFiles.get(i).getPath() + " " + listResultFiles.get(i).getSize() + "  " + "\n");
                listOfBigFiles.add(String.format("%.2f", (double)listResultFiles.get(i).getSize() / (1024 * 1024)) + " MB\n"  + listResultFiles.get(i).getPath());
                counter++;
            }
        }

        return listOfBigFiles;
    }
}
