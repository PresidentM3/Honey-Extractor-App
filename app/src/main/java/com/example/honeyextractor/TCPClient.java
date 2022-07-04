package com.example.honeyextractor;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class TCPClient implements Runnable{

    private final String IPaddress;
    private final int port;
    private PrintWriter outputWriter;
    private  BufferedReader input;
    public Context mContext;

    Socket socket;

    public TCPClient(String IP, int port, Context context) throws IOException {
        this.IPaddress = IP;
        this.port = port;
        this.mContext = context;

    }

    public void init(){

        try {
            InetAddress inetAddress = InetAddress.getByName(this.IPaddress);
            socket = new Socket(inetAddress, this.port);
            outputWriter = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            outputWriter.println("Hello from client!");
            outputWriter.flush();


            Runnable task2 = this::send;
            Thread t2 = new Thread(task2);
            t2.start();

            Runnable task1 = this::receive;
            Thread t1 = new Thread(task1);
            t1.start();



        } catch (IOException e ) {
            e.printStackTrace();

        }
    }

    private void send() {

        JSONObject Initdata = makeJasonInit();
        outputWriter.println(Initdata);

            while(true){

                JSONObject data = makeJason();
                outputWriter.println(data);
//                Log.d("Read","From CLIENT:" + data);

                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    }
    private JSONObject makeJason(){
        JSONObject sendData = new JSONObject();
        try {

            sendData.put("master_enable", Data.master_enable);
            sendData.put("target_velocity", Data.target_velocity);
            sendData.put("target_frequency", Data.target_frequency);
            sendData.put("operation_mode", Data.operation_mode);

            return sendData;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    private JSONObject makeJasonInit(){

        JSONObject sendData = new JSONObject();
        try {
            sendData.put("init", true);
            sendData.put("source", Data.source);
            sendData.put("motor_id", Data.motor_id);
            sendData.put("number_of_pole_pairs", Data.number_of_pole_pairs);
            sendData.put("max_velocity", Data.max_velocity);
            sendData.put("can_baud_rate", Data.can_baud_rate);

            return sendData;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void receive() {

        try {

            while(true){
                String inStr = input.readLine();
                Log.d("Read","From SERVER:" + inStr);

                JSONObject reader = new JSONObject(inStr);
                String status_word = reader.getString("status_word");
//                String actual_velocity = reader.getString("actual_velocity");
                String actual_frequency = reader.getString("actual_frequency");
                String DC_link_voltage = reader.getString("DC_link_voltage");
                String actual_current = reader.getString("actual_current");
                String actual_torque = reader.getString("actual_torque");


//
                Data.actual_frequency = Double.parseDouble(actual_frequency);
                Data.DC_link_voltage = Double.parseDouble(DC_link_voltage);
                Data.status_word  = Integer.decode(status_word);
                Data.actual_torque = Integer.parseInt(actual_torque);
                Data.actual_current = Integer.parseInt(actual_current);



                String filter = "MonitorData";
                Intent intent = new Intent(filter);

                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);


            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }

    public void close(){

        try {
            socket.close();
            outputWriter.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        this.init();
    }
}
