using System;
using System.IO;
using System.Text;
using System.Security.Cryptography;

namespace AES4all
{
    class Program
    {
        public static RijndaelManaged getAESCBCCipher(byte[] keyBytes, byte[] IVBytes, PaddingMode padding) {
            RijndaelManaged cipher =  new RijndaelManaged();
            cipher.KeySize = 128;
            cipher.BlockSize = 128;
            cipher.Mode = CipherMode.CBC;
            cipher.Padding = padding;
            cipher.IV = IVBytes;
            cipher.Key = keyBytes;
            return cipher;
        }

        public static RijndaelManaged getAESECBCipher(byte[] keyBytes, PaddingMode padding)
        {
            RijndaelManaged cipher = new RijndaelManaged();
            cipher.KeySize = 128;
            cipher.BlockSize = 128;
            cipher.Mode = CipherMode.ECB;
            cipher.Padding = padding;
            cipher.Key = keyBytes;
            return cipher;
        }

        public static byte[] encrypt(RijndaelManaged cipher, byte[] toEncrypt)
        {
            //Get an encryptor.
            ICryptoTransform encryptor = cipher.CreateEncryptor();
            //Encrypt the data.
            MemoryStream msEncrypt = new MemoryStream();
            CryptoStream csEncrypt = new CryptoStream(msEncrypt, encryptor, CryptoStreamMode.Write);
            //Write all data to the crypto stream and flush it.
            csEncrypt.Write(toEncrypt, 0, toEncrypt.Length);
            csEncrypt.FlushFinalBlock();

            //Get encrypted array of bytes.
            return  msEncrypt.ToArray();
        }

        public static byte[] decrypt(RijndaelManaged cipher, byte[] encrypted){
            //Get an encryptor.
            ICryptoTransform decryptor = cipher.CreateDecryptor();
            //Now decrypt the previously encrypted message using the decryptor
            // obtained in the above step.
            MemoryStream msDecrypt = new MemoryStream(encrypted);
            CryptoStream csDecrypt = new CryptoStream(msDecrypt, decryptor, CryptoStreamMode.Read);
            byte[] fromEncrypt = new byte[encrypted.Length];
            //Read the data out of the crypto stream.
            //csDecrypt.Read(fromEncrypt, 0, fromEncrypt.Length);
            csDecrypt.Read(fromEncrypt, 0, fromEncrypt.Length);
            return fromEncrypt;
        }

        public static byte[] demo1encrypt(byte[] keyBytes, byte[] ivBytes, PaddingMode padding, byte[] messageBytes){
            RijndaelManaged cipher = getAESCBCCipher(keyBytes, ivBytes, padding); 
            return encrypt(cipher, messageBytes);
        }

        public static byte[] demo1decrypt(byte[] keyBytes, byte[] ivBytes, PaddingMode padding, byte[] encryptedMessageBytes){
            RijndaelManaged decipher = getAESCBCCipher(keyBytes, ivBytes, padding); 
            return decrypt(decipher, encryptedMessageBytes);
        }

        public static byte[] demo2encrypt(byte[] keyBytes, PaddingMode padding, byte[] messageBytes)
        {
            RijndaelManaged cipher = getAESECBCipher(keyBytes, padding);
            return encrypt(cipher, messageBytes);
        }

        public static byte[] demo2decrypt(byte[] keyBytes, PaddingMode padding, byte[] encryptedMessageBytes)
        {
            RijndaelManaged decipher = getAESECBCipher(keyBytes, padding);
            return decrypt(decipher, encryptedMessageBytes);
        }

        static void Main(string[] args)
        {
            ASCIIEncoding textConverter = new ASCIIEncoding();

            String sDemoMesage = "This is a demo message from C#!";
            byte[] demoMesageBytes = textConverter.GetBytes(sDemoMesage);
            //shared secret
            byte[] demoKeyBytes = new byte[] {  0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                    0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
            // Initialization Vector - usually a random data, stored along with the shared secret,
            // or transmitted along with a message.
            // Not all the ciphers require IV - we use IV in this particular sample
            byte[] demoIVBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                                            0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};

            PaddingMode padding = PaddingMode.ISO10126;
            /**/
            Console.WriteLine("Demo Key (base64): "+ System.Convert.ToBase64String(demoKeyBytes));
            Console.WriteLine("Demo IV  (base64): "+ System.Convert.ToBase64String(demoIVBytes));
            
            byte[] demo1EncryptedBytes = demo1encrypt(demoKeyBytes, demoIVBytes, padding, demoMesageBytes);
            Console.WriteLine("Demo1 encrypted (base64): "+ System.Convert.ToBase64String(demo1EncryptedBytes));
            byte[] demo1DecryptedBytes = demo1decrypt(demoKeyBytes, demoIVBytes, padding, demo1EncryptedBytes);
            Console.WriteLine("Demo1 decrypted message : "+ textConverter.GetString(demo1DecryptedBytes));

            byte[] demo2EncryptedBytes = demo2encrypt(demoKeyBytes, padding, demoMesageBytes);
            Console.WriteLine("Demo2 encrypted (base64): "+ System.Convert.ToBase64String(demo2EncryptedBytes));
            byte[] demo2DecryptedBytes = demo2decrypt(demoKeyBytes, padding, demo2EncryptedBytes);
            Console.WriteLine("Demo2 decrypted message : " + textConverter.GetString(demo2DecryptedBytes));
            Console.ReadLine();
        }
    }
}
