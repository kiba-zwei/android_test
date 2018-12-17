package com.example.xingbin.handlertest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HandlerTest extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "HandlerTest";
    private static final int PROGRESS = 1;
    private static final int OVER = 0;
    private TextView text;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "handleMessage: msg.what = " + msg.what + " msg.arg = " + msg.arg1);
            switch (msg.what) {
                case OVER:
                    text.setText("下载完成");
                    break;
                case PROGRESS:
                    text.setText("已完成" + msg.arg1 + "%");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_handler);
        text = (TextView) findViewById(R.id.text);
        Button download = (Button) findViewById(R.id.download);
        download.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        new DownloadThread(handler).start();
    }

    class DownloadThread extends Thread {
        Handler handler;
        int progress = 0;

        public DownloadThread(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            while (progress < 100) {
                Message response = Message.obtain(handler);
                response.what = PROGRESS;
                progress += 10;
                if (progress == 100) {
                    response.what = OVER;
                }
                response.arg1 = progress;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                response.sendToTarget();
            }
        }
    }
}

