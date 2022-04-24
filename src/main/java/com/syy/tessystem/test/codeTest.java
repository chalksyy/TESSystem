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
            String decrypt = RSAEncrypt.decrypt("Wdwow1CDe40beeGHWeCddbr4BykJn5UeXCmMnUGt51o+GvMtBwVkOFHcbReUmWvSfuWcVOjf01fTp+Mb/lVnw1D9GLFjJjLoZqWD/uBP1z/uYdhTjp1IqXgwRl6F9IsYEl1X7t7vWJLAYcHUCUBil+kwmzhROMTZgXPGkYP3aRQ=");
            System.out.println(decrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}