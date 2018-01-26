package com.sa.share;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
    }

    void onSender(View view){

        Intent i = new Intent(this, MainActivity_sender.class);
        startActivity(i);

    }

    void onReceiver(View view){
        Intent i = new Intent(this, MainActivity_receiver.class);
        startActivity(i);



    }

}
