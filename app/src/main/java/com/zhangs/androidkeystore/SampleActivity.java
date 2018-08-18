package com.zhangs.androidkeystore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SampleActivity extends Activity {
    public final static String TYPE= "type";
    public final static int TYPE_BELOW_23= 1;
    public final static int TYPE_ABOVE_23= 2;

    public static void launch(int type,Activity act ){
        Intent intent = new Intent(act,SampleActivity.class);
        intent.putExtra(TYPE,type);
        act.startActivity(intent);
    }

    @BindView(R.id.et_data)
    EditText etData;
    @BindView(R.id.btn_encrypt)
    Button btnEncrypt;
    @BindView(R.id.btn_decrypt)
    Button btnDecrypt;
    @BindView(R.id.tv_result)
    TextView tvResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_encrypt)
    void onEncrypt(){

    }

    @OnClick(R.id.btn_decrypt)
    void onDecrypt(){

    }

}
