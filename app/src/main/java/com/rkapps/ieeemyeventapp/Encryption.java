package com.rkapps.ieeemyeventapp;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by robin on 6/9/2016.
 *
 */
public class Encryption {

/*

    Encryption and Hashing functions

 */


    public static String word(String toEnc)
       throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-256");
        byte[] crazyCrypt = new byte[40];
        md.update(toEnc.getBytes("iso-8859-1"), 0, toEnc.length());
        crazyCrypt = md.digest();
        return myString(crazyCrypt);
    }


    private static String myString(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        }
        return buf.toString();
    }

}
