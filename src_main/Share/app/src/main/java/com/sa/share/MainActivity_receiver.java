package com.sa.share;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.net.Socket;

public class MainActivity_receiver extends AppCompatActivity {

    Socket clientSocket;
    String IP;
    int SERVERPORT = 2935;

    EditText serverIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_receiver);

        serverIP = (EditText)findViewById(R.id.edit_serverIP);

    }

    void connectClient(View view){

        IP = serverIP.getText().toString();

        try {

            clientSocket = new Socket(IP, SERVERPORT);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
