package com.softgyan.test;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.softgyan.test.adapter.TempAdapter;
import com.softgyan.test.database.TempQuery;
import com.softgyan.test.models.TempModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText searchText;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;
    private TempAdapter tempAdapter;
    private List<TempModel> tempModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchText = findViewById(R.id.etSearch);
        ImageView ivClear = findViewById(R.id.iv_clear);
        ivClear.setOnClickListener(v -> searchText.setText(null));

        fabAdd = findViewById(R.id.floatingActionButton);
        fabAdd.setOnClickListener(v -> {
            openGallery();
        });

        recyclerView = findViewById(R.id.recyclerView);

        Loader loader = new Loader();
        loader.execute();
    }

    private void setRecyclerView(List<TempModel> tempModelList) {
        tempAdapter = new TempAdapter(this, tempModelList);
        recyclerView.setAdapter(tempAdapter);
        tempAdapter.notifyDataSetChanged();

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tempAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
//                do nothing
            }
        });
    }


    private final class Loader extends AsyncTask<Void, Void, List<TempModel>> {
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("loading data from database..");
            progressDialog.show();
        }

        @Override
        protected List<TempModel> doInBackground(Void... voids) {

            return TempQuery.getAllData(MainActivity.this);
        }

        @Override
        protected void onPostExecute(List<TempModel> tempModels) {
            MainActivity.this.tempModels.addAll(tempModels);
            super.onPostExecute(tempModels);
            setRecyclerView(tempModels);
            progressDialog.dismiss();
        }
    }

    private void openGallery() {
        Log.d("MainActivity", "openGallery: cliking");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE )== PackageManager.PERMISSION_GRANTED) {
//            Intent intent = new Intent();
//            intent.setAction(Intent.ACTION_GET_CONTENT);
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            ActivityCompat.startActivityForResult(this, Intent.createChooser(intent, "select image"),
                    100, null);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            final Uri data1 = data.getData();
            if (data1 != null) {
                Log.d("my_tag", "onActivityResult: uri : " + data1);
                AddImageDialog addImageDialog = new AddImageDialog(MainActivity.this, callback, data1.toString());
                addImageDialog.show();
                Log.d("my_tag", "onActivityResult: show");
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private final AddImageDialog.Callback callback = (text, imageUri, imageDialog) -> {
        final int i = TempQuery.insertData(MainActivity.this, text, imageUri);
        Log.d("my_tag", "image uri : " + imageUri);
        if (i != -1) {
            try {
                tempModels.add(0, new TempModel(i, text, imageUri));
                tempAdapter.notifyDataSetChanged();
                setRecyclerView(tempModels);
            }catch (Exception e){
                Log.d("my_tag", "error: "+e.getMessage());
            }
        }
        Log.d("my_tag", "i: " + i + "size :"+tempModels.size());
        imageDialog.dismiss();
    };


}