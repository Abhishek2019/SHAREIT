package com.sa.share;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.necistudio.libarary.FilePickerActivity;

import java.io.File;

public class FileChooser extends AppCompatActivity {

    TextView filePath;
    int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_chooser);
        filePath = (TextView) findViewById(R.id.text_filePath);
        permissionFile();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                Toast.makeText(this,"permission already granted",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String path = data.getStringExtra("path");
                filePath.setText(path);
                Log.e("data",path);
            }else{
                Log.e("data","cance");
            }
        }

    }

    public void permissionFile() {

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            Toast.makeText(this,"permission already granted",Toast.LENGTH_SHORT).show();
        }
    }

    public void chooseFile(View view){
        Intent intent = new Intent(getApplicationContext(),FilePickerActivity.class);
        startActivityForResult(intent, 1);
    }

    public void moveNext(View view){
        System.out.println(new File(filePath.getText().toString()).exists());
        if(new File(filePath.getText().toString()).exists()){
            Intent intent = new Intent(this, MainActivity_sender.class);
            intent.putExtra("path",filePath.getText().toString());
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please Select a File", Toast.LENGTH_SHORT).show();
        }
    }
}
