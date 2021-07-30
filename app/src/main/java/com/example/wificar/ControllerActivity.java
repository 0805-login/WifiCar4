package com.example.wificar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.PrintWriter;

public class ControllerActivity extends AppCompatActivity {

    private Toast mToast;
    private PrintWriter pw;
    private TextView tv_go;
    private TextView tv_back;
    private TextView tv_left;
    private TextView tv_right;
    private TextView tv_car_stop;
    private TextView tv_transfer_left;
    private TextView tv_transfer_right;
    private TextView tv_rt_clockwise;
    private TextView tv_rt_Anticlockwise;
    private TextView arm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        tv_go = findViewById(R.id.go);
        tv_back = findViewById(R.id.back);
        tv_left = findViewById(R.id.left);
        tv_right = findViewById(R.id.right);
        tv_car_stop = findViewById(R.id.car_stop);
        tv_transfer_left = findViewById(R.id.transfer_left);
        tv_transfer_right = findViewById(R.id.transfer_right);
        tv_rt_clockwise = findViewById(R.id.rt_clockwise);
        tv_rt_Anticlockwise = findViewById(R.id.rt_Anticlockwise);
        arm = findViewById(R.id.arm);

        pw = connectWifi.getPw();

        arm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ControllerActivity.this,arm_controller.class));
            }
        });
        tv_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw != null) {
                    new SendThread('1').start();

                } else {
                    showToast("小车未连接");
                }
            }
        });

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw != null) {
                    new SendThread('0').start();
                } else {
                    showToast("小车未连接");
                }
            }
        });

        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw != null) {
                    new SendThread('3').start();
                } else {
                    showToast("小车未连接");
                }
            }
        });

        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw != null) {
                    new SendThread('4').start();
                } else {
                    showToast("小车未连接");
                }
            }
        });

        tv_car_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onClick", "stop: ");
                if (pw != null) {
                    new SendThread('2').start();
                } else {
                    showToast("小车未连接");
                }
            }
        });
        tv_transfer_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw != null) {
                    new SendThread('5').start();
                } else {
                    showToast("小车未连接");
                }
            }
        });
        tv_transfer_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw != null) {
                    new SendThread('6').start();
                } else {
                    showToast("小车未连接");
                }
            }
        });
        tv_rt_clockwise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw != null) {
                    new SendThread('7').start();
                } else {
                    showToast("小车未连接");
                }
            }
        });
        tv_rt_Anticlockwise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw != null) {
                    new SendThread('8').start();
                } else {
                    showToast("小车未连接");
                }
            }
        });
    }

    //提示信息
    private void showToast(String text) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);

        mToast.show();
    }
}