package com.aes4all.samples;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;

public class AES4all {

    public static Cipher getAESCBCEncryptor(byte[] keyBytes, byte[] IVBytes, String padding) throws Exception{
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(IVBytes);
        Cipher cipher = Cipher.getInstance("AES/CBC/"+padding);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        return cipher;
    }
    
    public static Cipher getAESCBCDecryptor(byte[] keyBytes, byte[] IVBytes, String padding) throws Exception{
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(IVBytes);
        Cipher cipher = Cipher.getInstance("AES/CBC/"+padding);
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        return cipher;
    } 

    public static Cipher getAESECBEncryptor(byte[] keyBytes, String padding) throws Exception{
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/"+padding);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher;
    }
    
    public static Cipher getAESECBDecryptor(byte[] keyBytes, String padding) throws Exception{
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/"+padding);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher;
    }
    
    public static byte[] encrypt(Cipher cipher, byte[] dataBytes) throws Exception{
        ByteArrayInputStream bIn = new ByteArrayInputStream(dataBytes);
        CipherInputStream cIn = new CipherInputStream(bIn, cipher);
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        int ch;
        while ((ch = cIn.read()) >= 0) {
          bOut.write(ch);
        }
        return bOut.toByteArray();
    } 

    public static byte[] decrypt(Cipher cipher, byte[] dataBytes) throws Exception{
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        CipherOutputStream cOut = new CipherOutputStream(bOut, cipher);
        cOut.write(dataBytes);
        cOut.close();
        return bOut.toByteArray();    
    } 
    /**
     * @param args
     */
    
    public static byte[] demo1encrypt(byte[] keyBytes, byte[] ivBytes, String sPadding, byte[] messageBytes) throws Exception {
        Cipher cipher = getAESCBCEncryptor(keyBytes, ivBytes, sPadding); 
        return encrypt(cipher, messageBytes);
    }

    public static byte[] demo1decrypt(byte[] keyBytes, byte[] ivBytes, String sPadding, byte[] encryptedMessageBytes) throws Exception {
        Cipher decipher = getAESCBCDecryptor(keyBytes, ivBytes, sPadding);
        return decrypt(decipher, encryptedMessageBytes);
    }
    
    public static byte[] demo2encrypt(byte[] keyBytes, String sPadding, byte[] messageBytes) throws Exception {
        Cipher cipher = getAESECBEncryptor(keyBytes, sPadding); 
        return encrypt(cipher, messageBytes);
    }

    public static byte[] demo2decrypt(byte[] keyBytes, String sPadding, byte[] encryptedMessageBytes) throws Exception {
        Cipher decipher = getAESECBDecryptor(keyBytes, sPadding);
        return decrypt(decipher, encryptedMessageBytes);
    }
    
    public static void main(String[] args) throws Exception {
        String sDemoMesage = "This is a demo message from Java!";
        byte[] demoMesageBytes = sDemoMesage.getBytes();
        //shared secret
        byte[] demoKeyBytes = new byte[] {  0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
        // Initialization Vector - usually a random data, stored along with the shared secret,
        // or transmitted along with a message.
        // Not all the ciphers require IV - we use IV in this particular sample
        byte[] demoIVBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                                        0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
        String sPadding = "ISO10126Padding"; //"ISO10126Padding", "PKCS5Padding"

        System.out.println("Demo Key (base64): "+ new String(SimpleBase64Encoder.encode(demoKeyBytes)));
        System.out.println("Demo IV  (base64): "+ new String(SimpleBase64Encoder.encode(demoIVBytes)));
        
        byte[] demo1EncryptedBytes = demo1encrypt(demoKeyBytes, demoIVBytes, sPadding, demoMesageBytes);
        System.out.println("Demo1 encrypted (base64): "+ new String(SimpleBase64Encoder.encode(demo1EncryptedBytes)));
        byte[] demo1DecryptedBytes = demo1decrypt(demoKeyBytes, demoIVBytes, sPadding, demo1EncryptedBytes);
        System.out.println("Demo1 decrypted message : "+new String(demo1DecryptedBytes));

        byte[] demo2EncryptedBytes = demo2encrypt(demoKeyBytes, sPadding, demoMesageBytes);
        System.out.println("Demo2 encrypted (base64): "+ new String(SimpleBase64Encoder.encode(demo2EncryptedBytes)));
        byte[] demo2DecryptedBytes = demo2decrypt(demoKeyBytes, sPadding, demo2EncryptedBytes);
        System.out.println("Demo2 decrypted message : "+new String(demo2DecryptedBytes));
        
    }

}
