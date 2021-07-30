package com.example.wificar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import java.io.PrintWriter;
import java.util.Timer;
import java.util.TimerTask;

public class connectWifi extends AppCompatActivity {
    private static final String TAG = "connectWifi";
    private Toast mToast;
    private EditText et_ip;
    private EditText et_port;
    private String ip;
    private String port;
    private Button bt_connect;
    static PrintWriter pw;//输出流的一种
    private ProgressDialog pd;//进度条
    private SharedPreferences pref;//数据存储
    private SharedPreferences.Editor edit;
    private CheckBox cb_remember;
    private ConnectThread connectThread;
    private ImageView img5;
    private SatelliteMenu satelliteMenu;

    static boolean isPlay = true;//设置音乐播放状态变量
    MediaPlayer mediaPlayer;//设置音乐播放器
    private Switch sw_music;//播放背景音乐开关


    final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    pw = connectThread.getPw();
                    pd.dismiss();
                    showToast("小车连接成功");
                    break;
                case 0:
                    pd.dismiss();
//                    showToast("未连接小车");
                    showToast("小车连接失败");
                    break;
            }
        }
    };
    private ImageView contrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_wifi);
        contrl = findViewById(R.id.contrl);
        satelliteMenu = findViewById(R.id.satelliteMenu);
//        View childAt = satelliteMenu.getChildAt(4);
//        if(contrl==childAt){
        satelliteMenu.setMyOnClick(new SatelliteMenu.OnSatelliteMenuItemClickListener(){
            @Override
            public void onClick(View view, int pos) {

                startActivity(new Intent(connectWifi.this,ControllerActivity.class));

//                View childView = satelliteMenu.getChildAt(pos);

//                childView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        startActivity(new Intent(connectWifi.this,ControllerActivity.class));
//                    }
//                });
                }


        });


//        contrl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i(TAG, "onClick: 进入控制");
//                startActivity(new Intent(connectWifi.this,ControllerActivity.class));
//            }
//        });
        //设置全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sw_music = findViewById(R.id.sw_music1);
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

        //判断父Activity是否为空，不为空设置导航图标显示
        String par = NavUtils.getParentActivityName(connectWifi.this);
        System.out.println("par是多少:"+par);
        ActionBar i = getSupportActionBar();
        System.out.println("getSupportActionBar是： "+i);
        if( par != null){
            i.setDisplayHomeAsUpEnabled(true);//显示向左的箭头图标    返回图标
        }

        et_ip = findViewById(R.id.et_ip);
        et_port = findViewById(R.id.et_port);
        bt_connect = findViewById(R.id.bt_connect);
        cb_remember = findViewById(R.id.isRemember);

        /*
           限制输入ip时的格式
         */
        et_ip.setInputType(InputType.TYPE_CLASS_NUMBER);
        String digits="0123456789.";
        et_ip.setKeyListener(DigitsKeyListener.getInstance(digits));

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = pref.getBoolean("isRemember", false);
        if (isRemember) {
            String remember_ip = pref.getString("ip", "");
            String remember_port = pref.getString("port", "");
            et_ip.setText(remember_ip);
            et_port.setText(remember_port);
            cb_remember.setChecked(true);
            showToast("保存啦！！");
        }

        bt_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiManager mWifi = MainActivity.getWifi();
                if (!mWifi.isWifiEnabled()) {
                    showToast("请先打开wifi");
                } else {
                    if (pw == null) {

                        ip = et_ip.getText().toString().trim();
                        port = et_port.getText().toString().trim();
                        if ("".equals(ip) || "".equals(port) || ip == null || port == null) {
                            showToast("请输入IP和端口号");
                        } else {
                            edit = pref.edit();
                            if (cb_remember.isChecked()) {

                                edit.putBoolean("isRemember", true);
                                edit.putString("ip", ip);
                                edit.putString("port", port);

                            }else{
                                edit.clear();
                            }
                            edit.apply();

                            initView();
                            connectThread = new ConnectThread(ip, Integer.parseInt(port), handler);
                            connectThread.start();
                        }
                    } else {
                        showToast("已连接");

                        //当前页面暂停1秒后实现跳转
                        Intent intent = new Intent(connectWifi.this,ControllerActivity.class);
                        Timer timer = new Timer();
                        TimerTask tast = new TimerTask() {
                            @Override
                            public void run() {
                                startActivity(intent);
                            }
                        };
                        timer.schedule(tast, 1000);
                    }
                }
            }
        });


    }

    private void playMusic() {
        mediaPlayer = MediaPlayer.create(this,R.raw.caring2);
        mediaPlayer.setLooping(true);//设置背景音乐循环播放
        mediaPlayer.start();//启动播放音乐
    }

    private void initView() {
        pd = new ProgressDialog(connectWifi.this);
        pd.setTitle("搬运小车");
        pd.setMessage("正在连接(＾Ｕ＾)ノ~ＹＯ...");
        pd.setCancelable(false);// 设置对话框能否用back键取消
        pd.show();
    }

    public static PrintWriter getPw(){
        return pw;
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

    /*@Override
    protected void onRestart() {//返回游戏主界面时，音乐自动播放
        super.onRestart();
        if(isPlay == true){
            playMusic();
        }
    }*/
}