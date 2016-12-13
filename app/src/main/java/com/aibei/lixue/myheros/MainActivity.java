package com.aibei.lixue.myheros;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
        slidingButton.setOnSlidingEndListener(new SlidingButton.OnSlidingEndListener() {
            @Override
            public void OnSlidingEnd() {
                Intent intent = new Intent(getApplicationContext(),SecondActivity.class);
                startActivity(intent);
            }
        });
    }
}
