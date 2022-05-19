package com.syy.tessystem.test;

import com.syy.tessystem.util.RSAEncrypt;
import org.junit.Test;

/**
 * @Author Ke
 * @Date 2022/2/15 15:19
 * @Description
 * @Version 1.0
 */
public class codeTest {

    @Test
    public void test(){

        String encrypt = null;
        try {
            encrypt = RSAEncrypt.encrypt("123456");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(encrypt);

    }

    @Test
    public void test1(){

        try {
            String decrypt = RSAEncrypt.decrypt("Jtmg4VtGmEv9bB2GAn8EVnMksiRquf0ZKgTYpMeL5ng3vGxGUDZPjSfYuhPW/e8ltH1EF7dUOaFUGjMolvVcAy7U5RH5Y1YXolgQ2sp4roZLkZAr+xU2GqTNH5PR8PuO40tt1rkzp0UuHI31FKI/bQUR4HfR2KJiOoLgZNhV8yE=");
            System.out.println(decrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}