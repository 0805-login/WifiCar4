package com.example.wificar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.PrintWriter;

public class arm_controller extends AppCompatActivity {
    private Toast mToast;
    private PrintWriter pw;
    private TextView tv_Arm_head;
    private TextView tv_Arm1;
    private TextView tv_Arm2;
    private TextView tv_Arm3;
    private TextView tv_0;
    private TextView tv_Spin30;
    private TextView tv_Spin45;
    private TextView tv_Spin60;
    private TextView tv_Spin90;
    private TextView tv_Spin180;
    private TextView tv_Spin360;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arm_controller);

        tv_Arm_head = findViewById(R.id.roboticArm_head);
        tv_Arm1 = findViewById(R.id.roboticArm1);
        tv_Arm2 = findViewById(R.id.roboticArm2);
        tv_Arm3 = findViewById(R.id.roboticArm3);
        tv_0 = findViewById(R.id.init);
        tv_Spin30 = findViewById(R.id.Spin30);
        tv_Spin45 = findViewById(R.id.Spin45);
        tv_Spin60 = findViewById(R.id.Spin60);
        tv_Spin90 = findViewById(R.id.Spin90);
        tv_Spin180 = findViewById(R.id.Spin180);
        tv_Spin360 = findViewById(R.id.Spin360);


        pw = connectWifi.getPw();

        tv_Arm_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw != null) {
                    new SendThread('a').start();
                } else {
                    showToast("小车未连接");
                }
            }
        });
        tv_Arm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw != null) {
                    new SendThread('b').start();
                } else {
                    showToast("小车未连接");
                }
            }
        });
        tv_Arm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw != null) {
                    new SendThread('c').start();
                } else {
                    showToast("小车未连接");
                }
            }
        });
        tv_Arm3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw != null) {
                    new SendThread('d').start();
                } else {
                    showToast("小车未连接");
                }
            }
        });
        tv_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw != null) {
                    new SendThread('e').start();
                } else {
                    showToast("小车未连接");
                }
            }
        });
        tv_Spin30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw != null) {
                    new SendThread('f').start();
                } else {
                    showToast("小车未连接");
                }
            }
        });
        tv_Spin45.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw != null) {
                    new SendThread('g').start();
                } else {
                    showToast("小车未连接");
                }
            }
        });
        tv_Spin60.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw != null) {
                    new SendThread('h').start();
                } else {
                    showToast("小车未连接");
                }
            }
        });
        tv_Spin90.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw != null) {
                    new SendThread('i').start();
                } else {
                    showToast("小车未连接");
                }
            }
        });
        tv_Spin180.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw != null) {
                    new SendThread('j').start();
                } else {
                    showToast("小车未连接");
                }
            }
        });
        tv_Spin360.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw != null) {
                    new SendThread('k').start();
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
