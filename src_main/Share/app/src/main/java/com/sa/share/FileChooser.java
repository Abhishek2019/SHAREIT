package com.sa.share;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;

public class FileChooser extends ListActivity {

    File currentDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_file_chooser);

        currentDir = new File("/storage/emulated/0");
        fill(currentDir);

    }

    void fill(File file){

    }


}
