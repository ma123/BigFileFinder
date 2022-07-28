package com.eset.filefinder;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private List<String> listSelectedPaths = new LinkedList<>();
    private List<Uri> listSelectedUri = new LinkedList<>();
    private List<String> listSelectedFiles = new LinkedList<>();
    private ProgressBar simpleProgressBar;
    private Button btnBrowse;
    private Button btnSearch;
    private EditText etxNumberOfFiles;
    private TextView txtInfo;
    private FileAdapter fileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBrowse = this.findViewById(R.id.btn_browse);
        btnSearch = this.findViewById(R.id.btn_search);
        etxNumberOfFiles = this.findViewById(R.id.etx_number_of_files);
        simpleProgressBar = findViewById(R.id.search_bar);
        txtInfo = findViewById(R.id.txt_info);

        RecyclerView recyclerViewPath = this.findViewById(R.id.recyclerview_paths);
        recyclerViewPath.setLayoutManager(new LinearLayoutManager(this));
        PathAdapter pathAdapter = new PathAdapter(listSelectedPaths);
        recyclerViewPath.setAdapter(pathAdapter);

        RecyclerView recyclerViewFiles = this.findViewById(R.id.recyclerview_files);
        recyclerViewFiles.setLayoutManager(new LinearLayoutManager(this));
        fileAdapter = new FileAdapter(listSelectedFiles);
        recyclerViewFiles.setAdapter(fileAdapter);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK)
                {
                    Intent intent = result.getData();
                    Uri uri = intent.getData();

                    if(listSelectedPaths.contains(uri.getPath()))
                    {
                        Toast.makeText(getBaseContext(), R.string.chosen_path, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        listSelectedPaths.add(uri.getPath());
                        listSelectedUri.add(uri);
                        pathAdapter.notifyDataSetChanged();
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
                txtInfo.setText("");
                AsyncTaskExample asyncTask=new AsyncTaskExample();
                asyncTask.execute();
            }
        });
    }

    private class AsyncTaskExample extends AsyncTask<Void, Void, List<String>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            btnBrowse.setEnabled(false);
            btnSearch.setEnabled(false);
            simpleProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<String> doInBackground(Void... voids) {
            if(listSelectedPaths.size() != 0)
            {
                int numberOfFiles = Integer.parseInt(etxNumberOfFiles.getText().toString());
                if(numberOfFiles > 0)
                {
                    txtInfo.setText(R.string.search_begun);
                    BigFileFinder bigFileFinder = new BigFileFinder(getApplicationContext(), listSelectedUri);

                    listSelectedFiles = bigFileFinder.search(numberOfFiles);
                }
                else {
                    txtInfo.setText(R.string.select_number_of_files);
                }
            }
            else {
                txtInfo.setText(R.string.select_paths);
            }

            return listSelectedFiles;
        }

        @Override
        protected void onPostExecute(List<String> list) {
            simpleProgressBar.setVisibility(View.INVISIBLE);
            btnBrowse.setEnabled(true);
            btnSearch.setEnabled(true);
            fileAdapter.updateData(listSelectedFiles);
        }
    }
}
