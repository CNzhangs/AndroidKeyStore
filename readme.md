# KeyStoreLibrary
该library主要是为了解决Android开发中加解密问题。
### 1.技术原理
采用JCE的Keystore技术，实现不明文存储秘钥，而通过 keystore动态生成秘钥的方式实现加解密数据，从而达到数据安全性保证。

### 2.使用方式
可参考App 部分SampleActivity中的demo代码
加密：
  ```Java
  keyStoreService.encrypt(key, data, new EncryptCallback() {
              @Override
              public void onSuccess(String result) {
                  tvResult.setText(result);
              }

              @Override
              public void onFail(ErrorMsg msg) {
              }
          });
```          
          
解密：

  ```Java
  keyStoreService.decrypt(key, new DecryptCallback() {
            @Override
            public void onSuccess(String data) {
                tvResult.setText(data);
            }

            @Override
            public void onFail(ErrorMsg msg) {
            }
        });
```

### 3.参考：
https://blog.csdn.net/csh86277516/article/details/68926931
