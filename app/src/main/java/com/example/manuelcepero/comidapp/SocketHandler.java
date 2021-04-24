package com.example.manuelcepero.comidapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketHandler {
    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;

    public static synchronized Socket getSocket(){
        return socket;
    }

    public static synchronized void setSocket(Socket socket){
        SocketHandler.socket = socket;
    }

    public static synchronized PrintWriter getOut() {
        return out;
    }

    public static synchronized void setOut() {
        try {
            out = new PrintWriter(SocketHandler.socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized BufferedReader getIn() {
        return in;
    }

    public static synchronized void setIn() {
        try {
            in = new BufferedReader(new InputStreamReader(SocketHandler.socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}