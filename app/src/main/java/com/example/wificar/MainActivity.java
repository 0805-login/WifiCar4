package com.example.wificar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static boolean wifi_switch;
    private Toast mToast;


    public static boolean isWifi_switch() {
        return wifi_switch;
    }


    public static WifiManager getWifi() {
        return mWifi;
    }
    private static WifiManager mWifi;
    static PrintWriter pw;

    static boolean isPlay = true;//设置音乐播放状态变量
    MediaPlayer mediaPlayer;//设置音乐播放器
    private Switch sw_music;//播放背景音乐开关

    private ProgressBar hrProgress;            //水平进度条
    private int mProgressStatus = 0;        //完成进度
    private Handler handler1;            //声明一个用于处理消息的Handler类的对象

    //发送广播
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int action = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            switch (action) {
                case WifiManager.WIFI_STATE_ENABLED:
                    if(wifi_switch == true){
                        showToast("Wifi已打开");
                    }
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    if(wifi_switch == false){
                        showToast("Wifi已关闭");
                    }
                    pw = ConnectThread.getPw();
                    pw = null;
                    break;
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sw_music = findViewById(R.id.sw_music);
        sw_music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i(TAG, "onCheckedChanged: ");
                if (!sw_music.isPressed()) {
                    return;
                }

                if (isChecked) {
                    showToast("on");
                    playMusic();//调用播放音乐的方法
                    isPlay = true;

                } else {
                    showToast("off");
                    mediaPlayer.pause();
                    isPlay = false;
                }
            }
        });

        hrProgress = (ProgressBar) findViewById(R.id.progressBar);    //获取水平进度条
        handler1 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x111) {
                    hrProgress.setProgress(mProgressStatus);    //更新进度
                } else {
                    hrProgress.setVisibility(View.GONE);    //设置进度条不显示，并且不占用空间
//                    startActivity(new Intent(MainActivity.this,connectWifi.class));
                }
            }
        };
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    mProgressStatus = doWork();    //获取耗时操作完成的百分比
                    Message m = new Message();
                    if (mProgressStatus < 100) {
                        m.what = 0x111;
                        handler1.sendMessage(m);    //发送信息
                    } else {
                        m.what = 0x110;
                        handler1.sendMessage(m);    //发送消息
                        break;
                    }
                }

            }

            //模拟一个耗时操作
            private int doWork() {
                mProgressStatus += Math.random() * 5;    //改变完成进度
                try {
                    Thread.sleep(200);        //线程休眠200毫秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return mProgressStatus;    //返回新的进度
            }
        }).start();    //开启一个线程

    }



    private void playMusic() {
            mediaPlayer = MediaPlayer.create(this,R.raw.car);
            mediaPlayer.setLooping(true);//设置背景音乐循环播放
            mediaPlayer.start();//启动播放音乐
        }



    //解析菜单资源文件
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();//实例化一个MenuInflater
        inflater.inflate(R.menu.menu,menu);//第一个参数是用于解析的菜单资源文件，第二个参数是一个menu对象  解析菜单资源文件
        return super.onCreateOptionsMenu(menu);
    }

    //当选中指定选项时的操作
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        mWifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        IntentFilter filter = new IntentFilter();
        switch (item.getItemId()){
            case R.id.openWifi:
                mWifi.setWifiEnabled(true);
                wifi_switch = true;
                break;
            case R.id.closeWifi:
                mWifi.setWifiEnabled(false);
                wifi_switch = false;
                break;
            case R.id.connectWifi:
                showToast("Wifi已连接");
                Intent intent = new Intent(MainActivity.this,connectWifi.class);
                startActivity(intent);
                break;
//            case R.id.haveConnectedWifi:
//                showToast("已连接的Wifi有： ");
//                Intent intent1 = new Intent(MainActivity.this, wifiList.class);
//                startActivity(intent1);
//                break;
            case R.id.Controller:
                showToast("进入控制页面");
                Intent intent2 = new Intent(MainActivity.this,ControllerActivity.class);
                startActivity(intent2);
                break;
            case R.id.arm_Ctrl:
                showToast("进入机械臂控制页面");
                Intent intent3 = new Intent(MainActivity.this,arm_controller.class);
                startActivity(intent3);
                break;
        }
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(receiver, filter);
        return super.onOptionsItemSelected(item);
    }
    //提示信息
    private void showToast(String text) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);

        mToast.show();
    }



    @Override
    protected void onStop() {
        super.onStop();
        if(mediaPlayer != null){
            mediaPlayer.stop();
        }
    }

    @Override
    protected void onDestroy() {//退出游戏主界面时，音乐自动停止
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();//清空音乐资源
            mediaPlayer = null;
        }
    }

//    @Override
//    protected void onRestart() {//返回游戏主界面时，音乐自动播放
//        super.onRestart();
//        if(isPlay == true){
//            playMusic();
//        }
//    }

}