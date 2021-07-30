package com.example.wificar;

public class SendThread extends Thread{

    private char c;

    public SendThread(char c) {
        this.c = c;
    }

    @Override
    public void run() {

        MainActivity.pw.print(c);
        MainActivity.pw.print('n');
        MainActivity.pw.flush();
    }
}
