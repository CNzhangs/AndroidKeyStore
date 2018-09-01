# AndKeyStore
该library主要是为了解决Android开发中加解密数据的问题，提供**对称/非对称** 两种方式实现，并且提供Android**指纹验证**后的加解密功能。
### 技术原理
1. 采用JCE的Keystore技术，实现不明文存储秘钥，而通过 keystore动态生成秘钥的方式实现加解密数据，从而达到数据安全性保证；
2. 在JCE的基础上，可以配置参数 **authRequired** 开启指纹验证，实现通过指纹验证后才能对数据进行加解密的流程；
3. 由于加密的秘钥是存在Android的安全环境中，因此对于加密的数据结果是可以直接存储在本地任意地方，并不会造成安全问题；
4. 具体KeyStore原理可阅读文末参考资料。
### 使用方式
可参考App 部分SampleActivity中的demo代码  
参数说明:  
data-->需要加密的数据，可以是从服务器上获取到的key、token等需要加密的数据；  
key-->对于数据data对应的key值，可以认为key和data是1-1对应的。  
加密：
  ```java
  keyStoreService.encrypt(key, data, new EncryptCallback() {
              @Override
              public void onSuccess(String result) {
                  tvResult.setText(result); //对参数data加密成功后加密数据。
              }

              @Override
              public void onFail(ErrorMsg msg) {
              }
          });
```          
          
解密：

  ```java
  keyStoreService.decrypt(key, new DecryptCallback() {
            @Override
            public void onSuccess(String data) {
                tvResult.setText(data); //对加密后的数据的解密结果
            }

            @Override
            public void onFail(ErrorMsg msg) {
            }
        });
```

### 参考：
https://blog.csdn.net/csh86277516/article/details/68926931
