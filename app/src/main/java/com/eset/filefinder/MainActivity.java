package com.eset.filefinder;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private List<String> listSelectedPaths = new LinkedList<>();
    private List<String> listSelectedFiles = new LinkedList<>();
    private int numberOfFiles = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnBrowse = this.findViewById(R.id.btn_browse);
        Button btnSearch = this.findViewById(R.id.btn_search);
        EditText etxNumberOfFiles = this.findViewById(R.id.etx_number_of_files);
        numberOfFiles = Integer.parseInt(etxNumberOfFiles.getText().toString());

        File imgFile = new  File("/storage/emulated/0/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Images/IMG-20220602-WA0000.jpg");

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);

            myImage.setImageBitmap(myBitmap);

        }

        RecyclerView recyclerViewPath = this.findViewById(R.id.recyclerview_paths);
        recyclerViewPath.setLayoutManager(new LinearLayoutManager(this));
        PathAdapter pathAdapter = new PathAdapter(listSelectedPaths);
        recyclerViewPath.setAdapter(pathAdapter);

        RecyclerView recyclerViewFiles = this.findViewById(R.id.recyclerview_files);
        recyclerViewFiles.setLayoutManager(new LinearLayoutManager(this));
        FileAdapter fileAdapter = new FileAdapter(listSelectedFiles);
        recyclerViewFiles.setAdapter(fileAdapter);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK)
                {
                    Intent intent = result.getData();
                    Uri uri = intent.getData();
                    String fileName = uri.getLastPathSegment();

                    String FilePath = intent.getData().getPath();
                    String FileName = intent.getData().getLastPathSegment();
                    int lastPos = FilePath.length() - FileName.length();
                    String Folder = FilePath.substring(0, lastPos);

                    Log.d("Files", "Full Path: \n" + intent.getData().getPath() + "\n");
                    Log.d("Files", "Folder: \n" + intent.getData().getLastPathSegment() + "\n");
                    Log.d("Files", "File Name: \n" + intent.getData().getEncodedPath() + "\n");


                    if(listSelectedPaths.contains(FilePath))
                    {
                        Toast.makeText(getBaseContext(), R.string.chosen_path, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        listSelectedPaths.add(FilePath);
                        pathAdapter.notifyItemInserted(listSelectedPaths.size()-1);
                    }
                }
            }
        });

        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                activityResultLauncher.launch(intent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listSelectedPaths.size() != 0)
                {
                    Toast.makeText(getApplicationContext(), R.string.search_begun, Toast.LENGTH_SHORT).show();
                    // TODO nieco krutiace aby bolo vidno ze sa nieco deje


                    BigFileFinder bigFileFinder = new BigFileFinder(numberOfFiles, listSelectedPaths);
                    listSelectedFiles = bigFileFinder.search();

                    fileAdapter.notifyItemInserted(listSelectedFiles.size()-1);
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.select_paths, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
