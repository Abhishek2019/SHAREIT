package com.sa.share;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class MainActivity_receiver extends AppCompatActivity {

    Socket clientSocket;
    String IP = "";
    int SERVERPORT = 2935;
    boolean connected = false;
    boolean sending = false;
    Handler handler;
    IntentIntegrator qrScan;

    int PERMISSION_REQUEST_CODE = 1;

    TextView clientStatus;
    EditText serverIP;

    int filesize = 1024; // filesize temporary hardcoded

    long start = System.currentTimeMillis();
    int bytesRead;
    int current = 0;

    String segments[];
    String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_receiver);
        handler = new Handler();

        qrScan = new IntentIntegrator(this);
        serverIP = (EditText)findViewById(R.id.edit_serverIP);
        clientStatus = (TextView)findViewById(R.id.text_clientStatus);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                Toast.makeText(this,"permission already granted",Toast.LENGTH_LONG).show();
            }
        }
    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {

                    Toast.makeText(this,result.getContents(),Toast.LENGTH_LONG).show();
                    segments = result.getContents().split("/");
                    IP = segments[0];
                    fileName = segments[1];
                    filesize = Byte.parseByte(segments[2]);
                    serverIP.setText(IP);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



    public void scanIP(View view){

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
        } else {
            Toast.makeText(this,"permission already granted",Toast.LENGTH_LONG).show();
            qrScan.initiateScan();

        }

    }

    public void connectServer(View view){
        //IP = serverIP.getText().toString();
        //clientSocket = new Socket(IP, SERVERPORT);
        System.out.println("This is IP : "+IP);
        if(IP.equals("0.0.0.0") || IP.equals("")){
            Toast.makeText(this, "Invalid Sender", Toast.LENGTH_SHORT).show();
            return;
        }
        Thread clientThread = new Thread(new ClientThread());
        clientThread.start();
    }

    class ClientThread implements Runnable{
        public void run(){
            //IP = serverIP.getText().toString();
            try {
                clientSocket = new Socket(MainActivity_receiver.this.IP, MainActivity_receiver.this.SERVERPORT);
                connected = true;

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(connected) {
                            clientStatus.setText("connected");
                        }
                    }
                });

                File ShareFile = new File(Environment.getExternalStorageDirectory()+File.separator+"ShareFile");

                System.out.println(ShareFile.toString());

                if(!ShareFile.exists() && !ShareFile.isDirectory())
                {
                    // create empty directory
                    ShareFile.mkdirs();
                }

                System.out.println("Actual Location : "+new File(ShareFile,MainActivity_receiver.this.fileName).toString());

                byte [] mybytearray  = new byte [MainActivity_receiver.this.filesize];
                InputStream is = clientSocket.getInputStream();
                FileOutputStream fos = new FileOutputStream(new File(ShareFile,MainActivity_receiver.this.fileName));
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                bytesRead = is.read(mybytearray,0,mybytearray.length);
                current = bytesRead;

                do {
                    bytesRead =is.read(mybytearray, current, (mybytearray.length-current));
                    if(bytesRead > 0) {
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