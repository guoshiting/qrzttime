package com.scai.util;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {
	
	private static final String CODE_TYPE = "AES/ECB/PKCS5Padding";
	
    //注意: 这里的password(秘钥必须是16位的)
    private static final String CODE_KEY = "MdtsOW6wflaug2Y5";
    
    private static final String KeySpec = "AES";
    
    private static final String EN_CODE = "utf-8";
    
    // 盐巴
    private static final String CODE_SALT = "DCM-IRB-2017-04-07";
    
	/**
	 * 加密
	 * @param content
	 * @param password
	 * @return
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws UnsupportedEncodingException 
	 * @throws InvalidKeyException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 */
    private static byte[] encrypt(String content, String password) throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    	
        byte[] keyStr = getKey(password);
        SecretKeySpec key = new SecretKeySpec(keyStr, KeySpec);
        Cipher cipher = Cipher.getInstance(CODE_TYPE);	//CODE_TYPE
        byte[] byteContent = content.getBytes(EN_CODE);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] result = cipher.doFinal(byteContent);
        return result;
    }
    
    /**
     * 解密
     * @param content
     * @param password
     * @return
     * @throws NoSuchPaddingException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeyException 
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     */
    private static byte[] decrypt(byte[] content, String password) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        
	    byte[] keyStr = getKey(password);
	    SecretKeySpec key = new SecretKeySpec(keyStr, KeySpec);
	    Cipher cipher = Cipher.getInstance(CODE_TYPE);	//CODE_TYPE
	    cipher.init(Cipher.DECRYPT_MODE, key);
	    byte[] result = cipher.doFinal(content);
	    return result; //     

    }
    
    private static byte[] getKey(String password) {
        byte[] rByte = null;
        if (password != null) {
            rByte = password.getBytes();
        } else {
            rByte = new byte[24];
        }
        return rByte;
    }
    
    private static byte[] parseHexStr2Byte(String hexStr){
    	
    	if(hexStr == null || hexStr.length() < 1){
    		return null;
    	}
    	
    	byte[] result = new byte[hexStr.length() / 2];
    	for(int i = 0; i < hexStr.length() /2 ; i++){
    		
    		int high = Integer.parseInt(hexStr.substring(i*2,i*2+1), 16);
    		int low = Integer.parseInt(hexStr.substring(i*2 + 1,i*2+2), 16);
    		
    		result[i] = (byte)(high * 16 +low);
    		
    	}
    	
    	return result;
    }
    
	private static String parseBype2HexStr(byte[] encrypt) {
		
		StringBuilder sb = new StringBuilder();
		
		for(int i= 0; i < encrypt.length; i++){
			
			String hex = Integer.toHexString(encrypt[i] & 0xFF);
			
			if(hex.length() == 1){
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}
	/**
	 * 加密
	 * @param content
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws UnsupportedEncodingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
    public static String encode(String content) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException{
    	String salt_content = content + CODE_SALT;
    	
    	return parseBype2HexStr(encrypt(salt_content, CODE_KEY));
    }
    /**
     * 解密
     * @param content
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String decode(String content) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
    	
    	String result = "";
    	byte[] b = decrypt(parseHexStr2Byte(content), CODE_KEY);
    	result = new String(b);
    	result = result.replaceAll(CODE_SALT, "");
    	return result;
    }    
}
