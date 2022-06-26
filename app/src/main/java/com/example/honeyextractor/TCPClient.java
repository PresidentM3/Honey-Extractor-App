package com.example.honeyextractor;

import android.util.Log;

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

    Socket socket;

    public TCPClient(String IP, int port) throws IOException {
        this.IPaddress = IP;
        this.port = port;

    }

    public void init(){

        try {
            InetAddress inetAddress = InetAddress.getByName(this.IPaddress);
            socket = new Socket(inetAddress, this.port);
            outputWriter = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            this.send();
            
            Runnable task1 = this::receive;
            Thread t1 = new Thread(task1);
            t1.start();

            Runnable task2 = this::send;
            Thread t2 = new Thread(task2);
            t2.start();
            
        } catch (IOException e ) {
            e.printStackTrace();
        }
    }

    private void send() {
            outputWriter.println("Hello from client!");


            int i = 0;
            while(true){

                outputWriter.println("Hello from client!" + i);
                Log.d("Send","From CLIEND:" + "Hello" + i);
                i++;

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    }

    private void receive() {

        try {

            while(true){
                String inStr = input.readLine();
                Log.d("Read","From SERVER:" + inStr);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void close(){

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        this.init();
    }
}
