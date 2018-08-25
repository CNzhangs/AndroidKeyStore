package com.zhangs.androidkeystore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_below)
    Button btnBelow;
    @BindView(R.id.btn_above)
    Button btnAbove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.btn_below)
    void onBelow(){
        SampleActivity.launch(SampleActivity.TYPE_BELOW_23,this);
    }

    @OnClick(R.id.btn_above)
    void onAbove(){
        SampleActivity.launch(SampleActivity.TYPE_ABOVE_23,this);
    }

}
