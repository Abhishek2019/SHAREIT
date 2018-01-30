package com.sa.share;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity_sender extends AppCompatActivity {

    ServerSocket serverSocket;
    Socket sSocket;
    int SERVERPORT = 2935;
    Handler handler;
    TextView listenText;
    TextView serverStatus;

    String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sender);

        Intent fileIntent = getIntent();
        filePath = fileIntent.getStringExtra("path");

        Toast.makeText(this,filePath,Toast.LENGTH_SHORT).show();

        listenText = (TextView)findViewById(R.id.text_listen);
        listenText.setText("Not Listening");
        serverStatus = (TextView)findViewById(R.id.text_serverStatus);
        serverStatus.setText("Disconnected");



        handler = new Handler();
    }

    void startServer(View view){


            Thread serverThread = new Thread(new ServerThread());
            serverThread.start();

    }

    class ServerThread implements Runnable{


        public void run(){
            try {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listenText.setText("Listening on port : "+SERVERPORT);
                    }
                });

                serverSocket = new ServerSocket(SERVERPORT);

                while (true) {
                    // LISTEN FOR INCOMING CLIENTS
                    Socket client = serverSocket.accept();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            serverStatus.setText("Connected...");
                        }
                    });

                    File myFile = new File (filePath);
                    byte [] mybytearray  = new byte [(int)myFile.length()];
                    FileInputStream fis = new FileInputStream(myFile);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    bis.read(mybytearray,0,mybytearray.length);
                    OutputStream os = client.getOutputStream();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(MainActivity_sender.this,"Sending....",Toast.LENGTH_LONG).show();
                            serverStatus.setText("sending File ...");
                        }
                    });

                    os.write(mybytearray,0,mybytearray.length);
                    os.flush();
                    client.close();

                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

}



