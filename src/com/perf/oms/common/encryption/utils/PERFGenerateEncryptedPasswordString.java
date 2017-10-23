package com.perf.oms.common.encryption.utils;


import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.perf.oms.common.utils.encryption.PERFTextEncryption;

//import java.io.IOException;

public class PERFGenerateEncryptedPasswordString {  

	public static  PERFTextEncryption perfEncr = new PERFTextEncryption();
	public static  PERFTextEncryption perfDecr = new PERFTextEncryption();
	/**
	 * @param args
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws IOException 
	 * @throws InvalidKeyException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 */
	private static Scanner scanner;
	
	/****  ***/
	public static void main(String[] args) 
			throws Exception {
		
		//String key = "DB99A2A8EB6904F492E9DF0595ED683C";
        //String password = "Admin";

        scanner = new Scanner(System.in);
        
        System.out.println("Please Enter Plain Text Password:");
        String password = scanner.next();
        
        System.out.println("Salt Input - Please Enter Option number:");
        System.out.println("Enter 1 to provide secret key");
        System.out.println("Enter 2 to take input from key file");
        
        String option = scanner.next();
        
        if(option.trim().equalsIgnoreCase("1") || option.trim().equalsIgnoreCase("")) {
        System.out.println("<+++++++++You selected option 1 ++++++++1>");
        
        System.out.println("Please enter secret Key:");
        final String salt = scanner.next();
        
       // final String salt = "12345";
        
        String sEncryptedString = perfEncr.encrypt(password, salt);
        
		System.out.println("***sEncryptedString-->\n"+sEncryptedString) ;
		
		System.out.println("***Start decryption***" );
		
		String sDecryptedString = perfDecr.decrypt(sEncryptedString,salt);
		
		System.out.println("***sDecryptedString-->\n"+sDecryptedString) ;
        
        }else if(option.trim().equalsIgnoreCase("2")) {
        	
        	String sEncryptedString = perfEncr.encrypt(password.trim());
        	
        	System.out.println("***Generating encrypted string for value -->"+password.trim()) ;
            
    		System.out.println("***sEncryptedString to generate encrypted string-->"+sEncryptedString) ;
    		
    		System.out.println("***Start decryption***" );
    		
    		String sDecryptedString = perfDecr.decrypt(sEncryptedString.trim());
    		
    		System.out.println("***sDecryptedString:Original Password-->"+sDecryptedString) ;
        	
        	
        } else {
        	
        	System.out.println("***Quitting - Rerun to Select Option***" );
        	System.out.println("***To use Option 2, ensure KeyFile.txt with key exists under resource folder.***" );
        	
        }
		
       
	} 
	
	
	 
}
