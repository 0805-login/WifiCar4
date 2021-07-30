package com.example.wificar;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class ConnectThread extends Thread{

    private static PrintWriter pw;
    private  String ip;
    private int port;
    /* PrintWriter（）的作用是为了定义流输出的位置，并且此流可以正常的存储中文，减少乱码输出。
       PrintWriter 可以在写入同时对写入的数据进行格式化。
       PrintStream主要操作byte流，而PrintWriter用来操作字符流*/
    private Handler handler;

    //带参构造方法
    public ConnectThread(String ip, int port, Handler handler) {
        this.ip = ip;
        this.port = port;
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(ip, port);
            socket.connect(socketAddress);
            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
//            pw.println(1);测试是否连接上

            Message msg = handler.obtainMessage();
            msg.what=1;
            handler.sendMessage(msg);

        } catch (IOException e) {
            e.printStackTrace();
            Message msg = handler.obtainMessage();
            msg.what=0;
            handler.sendMessage(msg);
        }
    }

    public static PrintWriter getPw() {
        return pw;
    }
}
