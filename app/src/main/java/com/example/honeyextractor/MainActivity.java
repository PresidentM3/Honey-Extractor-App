package com.example.honeyextractor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    Button buttonWifi;
    TextView textfreq;
    TextView textActualVel;
    TextView textDriverTemp;
    TextView textDCLinkV;
    TextView textOutpuV;
    TextView textStatusWord;

    Timer timer = new Timer();
    int millis = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("MainActivity","MainActivity: oncreate  !!!!!!!!!!!!!!!!");

        buttonWifi = findViewById(R.id.buttonWifi);
        SwitchCompat switchME = (SwitchCompat )findViewById(R.id.switchME);
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar);
        final TextView seekBarValue = (TextView)findViewById(R.id.textVel);


        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiverData
                , new IntentFilter("MonitorData"));


        try {
            TCPClient client = new TCPClient("192.168.45.113", 8081, MainActivity.this); //"192.168.45.149" .113
            Thread myThread = new Thread(client);
            myThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                Data.target_velocity = progress;
                seekBarValue.setText("Velocity: " + String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });

        switchME.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    Data.master_enable = true;
                }
                else{
                    Data.master_enable = false;
                }
            }
        });

        buttonWifi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WifiActivity.class);
                startActivity(intent);

            }
        });

    }

    private final BroadcastReceiver mReceiverData = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, android.content.Intent intent) {

            refresh();

        }
    };

    public void refresh(){

        TextView textfreq = (TextView)findViewById(R.id.textFreq);
        TextView textActualVel = (TextView)findViewById(R.id.textActualVel);
        TextView textDriverTemp = (TextView)findViewById(R.id.textDriverTemp);
        TextView textDCLinkV = (TextView)findViewById(R.id.textDCLinkV);
        TextView textOutpuV = (TextView)findViewById(R.id.textOutputV);
        TextView textStatusWord = (TextView)findViewById(R.id.textStatusWord);
        TextView textACurrent = (TextView)findViewById(R.id.textActualCurrent);
        TextView textATorque = (TextView)findViewById(R.id.textActualTorque);


        textfreq.setText(new StringBuilder().append(getString(R.string.frequency)).append(" ").append(Double.toString(Data.actual_frequency)).append(" Hz"));
//                textActualVel.setText(new StringBuilder().append(getString(R.string.frequency)).append(Data.actual_frequency).toString());
//                textDriverTemp.setText(new StringBuilder().append(getString(R.string.frequency)).append(Data.actual_frequency).toString());
        textDCLinkV.setText(new StringBuilder().append(getString(R.string.dc_link_v)).append(" ").append(Double.toString(Data.DC_link_voltage)).append(" V"));
//                textOutpuV.setText(getString(R.string.frequency) + Data.actual_frequency);
        textATorque.setText(new StringBuilder().append(getString(R.string.actual_torque)).append(" ").append(Double.toString(Data.actual_torque)));
        textACurrent.setText(new StringBuilder().append(getString(R.string.actual_current)).append(" ").append(Double.toString(Data.actual_current)));


//        String hexStatusWord = Data.status_word;
        textStatusWord.setText(new StringBuilder().append(getString(R.string.satus_word)).append(" 0x").append(Integer.toHexString(Data.status_word)));
//
    }
}