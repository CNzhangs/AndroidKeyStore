package com.zhangs.androidkeystore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangs.library.IKeyStoreService;
import com.zhangs.library.KeyStoreHelper;
import com.zhangs.library.LogUtils;
import com.zhangs.library.callback.DecryptCallback;
import com.zhangs.library.callback.EncryptCallback;
import com.zhangs.library.model.Config;
import com.zhangs.library.model.ErrorMsg;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

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
    private int type;
    private IKeyStoreService keyStoreService  = new KeyStoreHelper();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra(TYPE,0);
        if (type==0){
            Toast.makeText(this,"参数有误",Toast.LENGTH_SHORT).show();
            finish();
        }
        try {
            keyStoreService.setConfig(new Config(this.getApplicationContext()));
            boolean create =  keyStoreService.createKey("MyTest");
            LogUtils.e("create keystore :"+create);
        } catch (NoSuchProviderException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | KeyStoreException e) {
            e.printStackTrace();
        }
    }


    @OnClick(R.id.btn_encrypt)
    void onEncrypt(){
        String data = etData.getText().toString();
        keyStoreService.encrypt("Password", data, new EncryptCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(String result) {
                tvResult.setText(result);
            }

            @Override
            public void onFail(ErrorMsg msg) {
                tvResult.setText("Exception:"+msg.toString());
            }
        });
    }

    @OnClick(R.id.btn_decrypt)
    void onDecrypt(){
        keyStoreService.decrypt("Password", new DecryptCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(String data) {
                tvResult.setText(data);
            }

            @Override
            public void onFail(ErrorMsg msg) {
                tvResult.setText("Exception:"+msg.toString());
            }
        });
    }

}
