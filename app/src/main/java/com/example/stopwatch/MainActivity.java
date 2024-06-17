package com.example.stopwatch;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import java.text.MessageFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    MaterialButton stop,start,reset;
    int seconds, milliSeconds, minutes;
    long milliSecond, startTime, timeBuff, updateTime =0L;
    Handler handler;
    private final Runnable runnable=new Runnable() {
        @Override
        public void run() {
            milliSecond = SystemClock.uptimeMillis()-startTime;
            updateTime = timeBuff +milliSecond;
            seconds = (int) (updateTime/1000);
            minutes= seconds /60;
            seconds= seconds%60;
            milliSeconds= (int) (updateTime %1000);
            textView.setText(MessageFormat.format("{0}:{1}:{2}",minutes, String.format(Locale.getDefault(),"%02d",seconds),String.format(Locale.getDefault(),"%02d",milliSeconds)));
            handler.postDelayed(this,0);

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textView=findViewById(R.id.textView);
        start=findViewById(R.id.startbtn);
        stop=findViewById(R.id.stopbtn);
        reset=findViewById(R.id.resetbtn);

        handler=new Handler(Looper.getMainLooper());

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime= SystemClock.uptimeMillis();
                handler.postDelayed(runnable,0);
                reset.setEnabled(false);
                stop.setEnabled(true);
                start.setEnabled(false);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeBuff+= milliSecond;
                handler.removeCallbacks(runnable);
                reset.setEnabled(true);
                start.setEnabled(true);
                stop.setEnabled(false);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                milliSecond=0L;
                updateTime=0L;
                startTime=0L;
                timeBuff=0L;
                seconds=0;
                minutes=0;
                milliSeconds=0;
                reset.setEnabled(false);
                textView.setText("00:00:00");

            }
        });
        textView.setText("00:00:00");
    }

}