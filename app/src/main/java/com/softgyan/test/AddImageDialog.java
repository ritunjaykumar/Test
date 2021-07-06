package com.softgyan.test;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

public class AddImageDialog extends Dialog {
    private final Callback callback;
    private final String imageUri;

    public AddImageDialog(@NonNull Context context, Callback callback, String imageUri) {
        super(context);
        setContentView(R.layout.layout_add_image);
        setCancelable(false);
        this.callback = callback;
        this.imageUri = imageUri;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView imageView = findViewById(R.id.imageView);
        Glide.with(getContext()).load(imageUri).into(imageView);
        EditText editText = findViewById(R.id.etMessage);
        Button saveFile = findViewById(R.id.button3);
        saveFile.setOnClickListener(v -> {
            String text = editText.getText().toString();
            if (text.isEmpty()) {
                Toast.makeText(getContext(), "invalid text", Toast.LENGTH_SHORT).show();
                return;
            }
            callback.onDataSave(text, imageUri, this);
        });

        Button cancel = findViewById(R.id.button2);
        cancel.setOnClickListener(v -> {
            this.dismiss();
        });
    }

    public interface Callback {
        void onDataSave(String text, String imageUri, AddImageDialog imageDialog);
    }
}
