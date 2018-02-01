package com.sa.share;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class MainActivity_receiver extends AppCompatActivity {

    Socket clientSocket;
    String IP;
    int SERVERPORT = 2935;
    boolean connected = false;
    boolean sending = false;
    Handler handler;

    TextView clientStatus;
    EditText serverIP;

    int filesize = 100000; // filesize temporary hardcoded

    long start = System.currentTimeMillis();
    int bytesRead;
    int current = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_receiver);
        handler = new Handler();
        serverIP = (EditText)findViewById(R.id.edit_serverIP);
        clientStatus = (TextView)findViewById(R.id.text_clientStatus);

    }


    void scanIP(View view){



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



                byte [] mybytearray  = new byte [filesize];
                InputStream is = clientSocket.getInputStream();
                FileOutputStream fos = new FileOutputStream("/storage/emulated/0/abhi.docx");
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                bytesRead = is.read(mybytearray,0,mybytearray.length);
                current = bytesRead;

                do {
                    bytesRead =is.read(mybytearray, current, (mybytearray.length-current));
                    if(bytesRead > 0)
                    {
                        current += bytesRead;
                    }
                } while(bytesRead > 0);

                bos.write(mybytearray, 0 , current);
                bos.flush();
                long end = System.currentTimeMillis();
                System.out.println(end-start);
                bos.close();
                clientSocket.close();
                sending = true;



                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if(sending){
                            clientStatus.setText("received");
                        }
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}
