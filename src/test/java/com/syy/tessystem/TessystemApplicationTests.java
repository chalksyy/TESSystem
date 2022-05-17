package com.syy.tessystem;

import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static com.syy.tessystem.util.RSAEncrypt.toByteArray;

@SpringBootTest
class TessystemApplicationTests {

    @Test
    void contextLoads() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        //从本地读取私钥
        Resource resource = new ClassPathResource("configuration/private.key");
        InputStream inputStream = resource.getInputStream();
        byte[] privateKeyBytes = toByteArray(inputStream);
        String str = "Jtmg4VtGmEv9bB2GAn8EVnMksiRquf0ZKgTYpMeL5ng3vGxGUDZPjSfYuhPW/e8ltH1EF7dUOaFUGjMolvVcAy7U5RH5Y1YXolgQ2sp4roZLkZAr+xU2GqTNH5PR8PuO40tt1rkzp0UuHI31FKI/bQUR4HfR2KJiOoLgZNhV8yE=";
        //+hcmwanRNwk0GkdgINQre7XT6THTeDZRWrH3o5cN5TdtYnDGh8kfx4H8fBlLs5sdc39yMNK6UcmukRHjHLXrEfoHheR0RN0hV9K4DPNcNSj15RJpX2MICBvmVlSXI=
        //+hcmwanRNwk0GkdgINQre7XT6THTeDZRWrH3o5cN5TdtYnDGh8kfx4H8fBlLs5sdc39yMNK6UcmukRHjHLXrEfoHheR0RN0hV9K4DPNcNSj15RJpX2MICBvmVlSXI=
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKeyBytes);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        System.out.println("outStr = " + outStr);
    }

    @Test
    void encrypt() throws Exception{

        Resource resource = new ClassPathResource("configuration/public.key");
        InputStream inputStream = resource.getInputStream();
        byte[] publicKeyBytes = toByteArray(inputStream);

        String str = "123456";
        //从本地读取公钥
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKeyBytes);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        System.out.println("outStr = " + outStr);
    }

}
