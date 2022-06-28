package com.example.honeyextractor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
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

        buttonWifi = findViewById(R.id.buttonWifi);
        SwitchCompat switchME = (SwitchCompat )findViewById(R.id.switchME);
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar);
        final TextView seekBarValue = (TextView)findViewById(R.id.textVel);

        TextView textfreq = (TextView)findViewById(R.id.textFreq);
        TextView textActualVel = (TextView)findViewById(R.id.textActualVel);
        TextView textDriverTemp = (TextView)findViewById(R.id.textDriverTemp);
        TextView textDCLinkV = (TextView)findViewById(R.id.textDCLinkV);
        TextView textOutpuV = (TextView)findViewById(R.id.textOutpuV);
        TextView textStatusWord = (TextView)findViewById(R.id.textStatusWord);

        String string = getString(R.string.frequency);

        try {
            TCPClient client = new TCPClient("192.168.45.113", 8081); //"192.168.45.149" .113
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

    public void refresh(){

                textfreq.setText(new StringBuilder().append(getString(R.string.frequency)).append(Data.actual_frequency).toString());
//                textActualVel.setText(new StringBuilder().append(getString(R.string.frequency)).append(Data.actual_frequency).toString());
//                textDriverTemp.setText(new StringBuilder().append(getString(R.string.frequency)).append(Data.actual_frequency).toString());
                textDCLinkV.setText(new StringBuilder().append(getString(R.string.dc_link_v)).append(Data.DC_link_voltage).toString());
//                textOutpuV.setText(getString(R.string.frequency) + Data.actual_frequency);
//                textStatusWord.setText(getString(R.string.frequency) + Data.actual_frequency);
//
    }
}