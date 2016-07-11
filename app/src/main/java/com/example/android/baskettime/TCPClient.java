package com.example.android.baskettime;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Externalizable;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Fabio on 07/07/2016.
 */
public class TCPClient {

    private String serverMessage;
    private OnMessageReceived mMessageListener = null;
    public static final String ServerIp = "192.168.1.9";
    public static final int ServerPort = 8080;
    private boolean mRun = false;

    private Boolean mServerMessage;

    PrintWriter out;
    BufferedReader in;

    public void stopClient() {

        mRun = false;
        if (out != null) {
            out.flush();
            out.close();
        }

        mMessageListener = null;
        in = null;
        out = null;
        mServerMessage = null;
    }

    public TCPClient(OnMessageReceived listener) {

        mMessageListener = listener;
    }

    public void run() {

        mRun = true;

        try {
            InetAddress serverAddr = InetAddress.getByName(ServerIp);
            Log.e("TCP Client", "C: Connecting..");

            Socket socket = new Socket(serverAddr, ServerPort);

            try {

                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                Log.d("TCP Client", "C: Sent");
                Log.d("TCP Client", "C: Done");

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while (mRun) {

                    Log.d("TCP Client", "C: Response");
                    mServerMessage = in.read()!=0;

                }
                Log.e("Response from server", "Login ok");

            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {

        if (out != null && !out.checkError()) {
            out.println(message);
            out.flush();
        }
    }

    public interface OnMessageReceived {
        public void messageReceived(Boolean message);
    }
}
