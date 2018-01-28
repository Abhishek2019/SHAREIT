package com.sa.share;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.Socket;

public class MainActivity_receiver extends AppCompatActivity {

    Socket clientSocket;
    String IP;
    int SERVERPORT = 2935;
    boolean connected = false;
    Handler handler;

    TextView clientStatus;
    EditText serverIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_receiver);
        handler = new Handler();
        serverIP = (EditText)findViewById(R.id.edit_serverIP);
        clientStatus = (TextView)findViewById(R.id.text_clientStatus);

    }

    void connectServer(View view){

        //IP = serverIP.getText().toString();

            //clientSocket = new Socket(IP, SERVERPORT);
        Thread clientThread = new Thread(new ClientThread());
        clientThread.start();
    }

    class ClientThread implements Runnable{

        public void run(){

            IP = serverIP.getText().toString();
            try {
                clientSocket = new Socket(IP, SERVERPORT);
                connected = true;

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(connected) {
                            clientStatus.setText("connected");
                        }
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}
