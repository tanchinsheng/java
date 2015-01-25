package com.st.comments;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;

import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.security.InvalidAlgorithmParameterException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import sun.misc.BASE64Encoder;
//import javax.xml.bind.DatatypeConverter;

/**
 * @author xxx This program provides the following cryptographic functionalities
 * 1. Encryption using AES 2. Decryption using AES
 *
 * High Level Algorithm : 1. Generate a AES key (specify the Key size during
 * this phase) 2. Create the Cipher 3. To Encrypt : Initialize the Cipher for
 * Encryption 4. To Decrypt : Initialize the Cipher for Decryption
 *
 *
 */
public class App {

    public static void main(String[] args) {

        String strDataToEncrypt = new String();
        String strCipherText = new String();
        String strDecryptedText = new String();

        File file = new File("passwordfile.txt");
        StringBuffer contents = new StringBuffer();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));
            String text = null;

            // repeat until all lines is read
            while ((text = reader.readLine()) != null) {
                contents.append(text)
                        .append(System.getProperty(
                        "line.separator"));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // show file contents here
        System.out.println("Password from file is " + contents.toString());

        if (contents.length() > 0) {
            try {
                /**
                 * Step 1. Generate an AES key using KeyGenerator Initialize the
                 * keysize to 128
                 *
                 */
                KeyGenerator keyGen = KeyGenerator.getInstance("AES");
                keyGen.init(128);
                SecretKey secretKey = keyGen.generateKey();

                /**
                 * Step2. Create a Cipher by specifying the following parameters
                 * a. Algorithm name - here it is AES
                 */
                Cipher aesCipher = Cipher.getInstance("AES");

                /**
                 * Step 3. Initialize the Cipher for Encryption
                 */
                aesCipher.init(Cipher.ENCRYPT_MODE, secretKey);

                /**
                 * Step 4. Encrypt the Data 1. Declare / Initialize the Data.
                 * Here the data is of type String 2. Convert the Input Text to
                 * Bytes 3. Encrypt the bytes using doFinal method
                 */
                strDataToEncrypt = contents.toString();
                byte[] byteDataToEncrypt = strDataToEncrypt.getBytes();
                byte[] byteCipherText = aesCipher.doFinal(byteDataToEncrypt);
                String s = new String(byteCipherText);
                strCipherText = new BASE64Encoder().encode(byteCipherText);
                System.out.println("Cipher Text generated using AES is " + strCipherText);

                /**
                 * Step 5. Decrypt the Data 1. Initialize the Cipher for
                 * Decryption 2. Decrypt the cipher bytes using doFinal method
                 */
                aesCipher.init(Cipher.DECRYPT_MODE, secretKey, aesCipher.getParameters());
                byte[] byteDecryptedText = aesCipher.doFinal(byteCipherText);
                strDecryptedText = new String(byteDecryptedText);
                System.out.println(" Decrypted Text message is " + strDecryptedText);
            } catch (NoSuchAlgorithmException noSuchAlgo) {
                System.out.println(" No Such Algorithm exists " + noSuchAlgo);
            } catch (NoSuchPaddingException noSuchPad) {
                System.out.println(" No Such Padding exists " + noSuchPad);
            } catch (InvalidKeyException invalidKey) {
                System.out.println(" Invalid Key " + invalidKey);
            } catch (BadPaddingException badPadding) {
                System.out.println(" Bad Padding " + badPadding);
            } catch (IllegalBlockSizeException illegalBlockSize) {
                System.out.println(" Illegal Block Size " + illegalBlockSize);
            } catch (InvalidAlgorithmParameterException invalidParam) {
                System.out.println(" Invalid Parameter " + invalidParam);
            }
        }
    }
}
