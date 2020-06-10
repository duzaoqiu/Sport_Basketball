package com.bench.android.core.util.encrypt;


import android.util.Base64;

import com.bench.android.core.util.LogUtils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

/**
 * Created by zhouyi on 2017/4/1.
 */
public class DesedeCryptor {


    private byte[] keyContent;

    /**
     * 二进制格式的key
     *
     * @param keyContent
     */
    public DesedeCryptor(String keyContent) {

        this.keyContent = Base64.decode(keyContent.getBytes(), Base64.DEFAULT);
    }

    /**
     * 二进制格式的key
     *
     * @param keyContent
     */
    public DesedeCryptor(byte[] keyContent) {
        this.keyContent = keyContent;
    }

    private Cipher getDecryptCipher() {
        try {
            DESedeKeySpec dks = new DESedeKeySpec(keyContent);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
            SecretKey key = keyFactory.generateSecret(dks);
            SecureRandom sr = new SecureRandom();
            Cipher decryptCipher = Cipher.getInstance("DESede");
            decryptCipher.init(Cipher.DECRYPT_MODE, key, sr);
            return decryptCipher;
        } catch (Exception e) {

        }
        return null;
    }

    public String decrypt(String content) {
        // TODO Auto-generated method stub
        try {
            if (content == null) {
                LogUtils.e("decrypt content为null");
            }
            return new String(decrypt(Base64.decode(content.getBytes(), Base64.DEFAULT)));
        } catch (Exception e) {
            return null;
        }
    }

    public byte[] decrypt(byte[] content) {
        try {
            return getDecryptCipher().doFinal(content);
        } catch (Exception e) {
            return null;
        }
    }


}
