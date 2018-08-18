package com.zhangs.androidkeystore;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SampleActivity extends Activity {
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
}
