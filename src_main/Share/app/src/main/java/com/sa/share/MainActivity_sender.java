package com.sa.share;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity_sender extends AppCompatActivity {

    ServerSocket serverSocket;
    Socket sSocket;
    int SERVERPORT = 2935;

    TextView listenText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sender);
        listenText = (TextView)findViewById(R.id.text_listen);
    }

    void startServer(View view){

        try {

            serverSocket = new ServerSocket();

            serverSocket.bind(new InetSocketAddress(SERVERPORT));

            sSocket = serverSocket.accept();
            listenText.setText("Listening....");




        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
