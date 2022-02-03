package de.aggromc.confighubhost.utils;

import javax.crypto.spec.*;
import java.nio.charset.*;
import javax.crypto.*;
import java.security.*;
import java.util.*;

public class Encryption
{
    private static final HashUtils hasch;
    private static SecretKeySpec secretKey;
    
    public static String hash(final String raw) {
        return Encryption.hasch.hash(raw);
    }
    
    public static boolean pwRight(final String hash, final String raw) {
        return Encryption.hasch.verifyHash(raw, hash);
    }
    
    public static void setKey(final String myKey) {
        try {
            byte[] key = myKey.getBytes(StandardCharsets.UTF_8);
            final MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            Encryption.secretKey = new SecretKeySpec(key, "AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    
    private static String encryptStr(final String strToEncrypt) {
        try {
            final Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(1, Encryption.secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        }
        catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
            return null;
        }
    }
    
    private static String decryptStr(final String strToDecrypt, final String secret) {
        try {
            setKey(secret);
            final Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(2, Encryption.secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        }
        catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
            return null;
        }
    }
    
    static {
        hasch = new HashUtils(8);
    }
}
