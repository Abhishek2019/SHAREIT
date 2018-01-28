package com.sa.share;

import android.os.Handler;
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
    Handler handler;
    TextView listenText;
    TextView serverStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sender);

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
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

}



