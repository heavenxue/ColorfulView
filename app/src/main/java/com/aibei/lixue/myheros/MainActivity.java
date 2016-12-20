package com.aibei.lixue.myheros;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aibei.lixue.myheros.customeview.SlidingButton;

public class MainActivity extends AppCompatActivity {
    private SlidingButton slidingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }
    private void initView(){
        slidingButton = (SlidingButton) findViewById(R.id.slidiingButton);
    }
    private void initData(){
//        slidingButton.setOnSwitchListener(new SlidingButtons.OnSwitchListener() {
//            @Override
//            public void onSwitch(boolean isPastHalf) {
//                if (isPastHalf) {
//                    Toast.makeText(getApplicationContext(), "open", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "close", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        slidingButton.setOnSlidingEndListener(new SlidingButton.OnSlidingEndListener() {
            @Override
            public void OnSlidingEnd() {
                Intent intent = new Intent(getApplicationContext(),SecondActivity.class);
                startActivity(intent);
            }
        });
    }
}
