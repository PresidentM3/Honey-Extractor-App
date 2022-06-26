package com.example.honeyextractor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button buttonWifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonWifi = findViewById(R.id.buttonWifi);

        try {
            TCPClient client = new TCPClient("192.168.45.113", 8081);
            Thread myThread = new Thread(client);
            myThread.start();
//            client.init();
        } catch (IOException e) {
            e.printStackTrace();
        }


        buttonWifi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WifiActivity.class);
                startActivity(intent);

            }
        });


    }
}