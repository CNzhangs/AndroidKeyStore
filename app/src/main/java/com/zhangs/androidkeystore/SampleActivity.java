package com.zhangs.androidkeystore;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhangs.library.IKeyStoreService;
import com.zhangs.library.KeyStoreHelper;
import com.zhangs.library.callback.DecryptCallback;
import com.zhangs.library.callback.EncryptCallback;
import com.zhangs.library.model.Config;
import com.zhangs.library.model.ErrorMsg;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class SampleActivity extends FragmentActivity {
    public final static String TYPE = "type";
    public final static int TYPE_BELOW_23 = 1;
    public final static int TYPE_ABOVE_23 = 2;

    public static void launch(int type, Activity act) {
        Intent intent = new Intent(act, SampleActivity.class);
        intent.putExtra(TYPE, type);
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
    private int type;
    private IKeyStoreService keyStoreService = new KeyStoreHelper();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra(TYPE, 0);
        if (type == 0) {
            Toast.makeText(this, "参数有误", Toast.LENGTH_SHORT).show();
            finish();
        }
        try {
            Config config = new Config(this.getApplicationContext());
            config.authRequired = true;
            keyStoreService.setConfig(config);
            keyStoreService.createKey("MyTest");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClick(R.id.btn_encrypt)
    void onEncrypt() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions.request(Manifest.permission.USE_FINGERPRINT)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            if (aBoolean) {
                                encrypt();
                            }
                        }
                    });
        } else {
            encrypt();
        }
    }

    private void encrypt() {
        String data = etData.getText().toString();
        keyStoreService.encrypt("Password", data, new EncryptCallback() {
            @Override
            public void onSuccess(String result) {
                tvResult.setText(result);
            }

            @Override
            public void onFail(ErrorMsg msg) {
                tvResult.setText("Exception:");
            }
        });
    }

    @OnClick(R.id.btn_decrypt)
    void onDecrypt() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions.request(Manifest.permission.USE_FINGERPRINT)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            if (aBoolean) {
                                decrypt();
                            }
                        }
                    });
        } else {

            decrypt();
        }
    }

    private void decrypt() {
        keyStoreService.decrypt("Password", new DecryptCallback() {
            @Override
            public void onSuccess(String data) {
                tvResult.setText(data);
            }

            @Override
            public void onFail(ErrorMsg msg) {
                tvResult.setText("Exception:" + msg.toString());
            }
        });
    }

}
