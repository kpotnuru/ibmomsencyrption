package com.perf.oms.common.encryption.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class PERFGenerateSaltKey {
	
	private static String CRYPTO_ALGO_MODE ="AES";
	
	public static void main(String args[]) throws NoSuchAlgorithmException {
		
		KeyGenerator keyGen = KeyGenerator.getInstance(PERFGenerateSaltKey.CRYPTO_ALGO_MODE);
        keyGen.init(128);
        SecretKey sk = keyGen.generateKey();
        String key = byteArrayToHexString(sk.getEncoded());
        System.out.println("key: \n" + key);
        
    }
	
	 private static String byteArrayToHexString(byte[] b) {
	        StringBuffer sb = new StringBuffer(b.length * 2);
	        for (int i = 0; i < b.length; i++) {
	            int v = b[i] & 0xff;
	            if (v < 16) {
	                sb.append('0');
	            }
	            sb.append(Integer.toHexString(v));
	        }
	        return sb.toString().toUpperCase();
	    }


}
