package com.sbrinfo.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.sbrinfo.jsm4sdk.SM4JSDK;

/**
 * 加解密工具类。 
 * 提供了两种加解密方式：
 * 加解密二进制数据encryptBinary、decryptBinary
 * 加解密文件encryptFile、decryptFile
 * 使用前需要加载ext库，请将 sdk/javasdk/1.6.x/ext 中对应操作系统的目录中的所有文件拷贝到
 * java.library.path 中的某个目录下
 * 
 * @author houhuiqiu 2014-05-26
 */
public class SM4Util {

	private static Logger log = Logger.getLogger(SM4Util.class);

	static{
		System.loadLibrary("scm_sm4javasdk_dll_mar");
	}
	
	/**
	 * 解密文件
	 * @param key 密钥
	 * @param inputFile 密文文件路径
	 * @param outputFile 输出解密后明文文件路径
	 * @return 成功true/失败false
	 */
	public static boolean decryptFile(String key, String inputFile, String outputFile){
		byte[] keyBuffer = key.getBytes();
        SM4JSDK sdk = new SM4JSDK();
        boolean retFlag = false;
        if (sdk.createContext(keyBuffer)){
        	retFlag = sdk.decryptFile(inputFile, outputFile);
        }
		return retFlag;
	}

	/**
	 * 加密文件
	 * @param key 密钥
	 * @param inputFile 明文文件路径
	 * @param outputFile 输出加密后文件路径
	 * @return 成功true/失败false
	 */
	public static boolean encryptFile(String key, String inputFile, String outputFile){
		byte[] keyBuffer = key.getBytes();
        SM4JSDK sdk = new SM4JSDK();
        boolean retFlag = false;
        if (sdk.createContext(keyBuffer)){
        	retFlag = sdk.encryptFile(inputFile, outputFile);
        }
		return retFlag;
	}
	
	/**
	 * 二进制加密方法
	 * @param key 密钥
	 * @param filePath 准备加密的文件
	 * @return 返回密文文件内容
	 */
	public static byte [] encryptBinary(String key, String filePath){
		byte[] keyBuffer = key.getBytes();
        SM4JSDK sdk = new SM4JSDK();
        byte [] outputBuffer = null;
        if (sdk.createContext(keyBuffer)){
        	byte [] contentBuffer = readXmlContent(filePath);
        	contentBuffer = SM4JSDK.addDataPadding(contentBuffer);
            outputBuffer = sdk.encryptBinary(contentBuffer);
            if (outputBuffer == null){
            	log.error(filePath + "加密失败");
            }
        }
		return outputBuffer;
	}

	/**
	 * 二进制解密方法
	 * @param key 密钥
	 * @param filePath 准备解密的文件
	 * @return 返回明文文件内容
	 */
	public static String decryptBinary(String key, String filePath) {
		byte[] keyBuffer = key.getBytes();
		String decrypt = "";
		SM4JSDK sdk = new SM4JSDK();
		if (sdk.createContext(keyBuffer)) {
			byte[] contentBuffer = readXmlContent(filePath);
			byte[] inputDecrypt = sdk.decryptBinary(contentBuffer);
			if (inputDecrypt != null) {
				inputDecrypt = SM4JSDK.removeDataPadding(inputDecrypt);
				decrypt = new String(inputDecrypt);
			} else {
				log.error(filePath + ",解密失败\n");
			}
		}
		return decrypt;
	}

	/**
	 * 二进制加密方法
	 * @param key 密钥
	 * @param contentBuffer 准备加密的byte数组
	 * @return 返回密文文件内容
	 */
	public static byte [] encryptBinaryByteArray(String key, byte[] contentBuffer){
		byte[] keyBuffer = key.getBytes();
        SM4JSDK sdk = new SM4JSDK();
        byte [] outputBuffer = null;
        if (sdk.createContext(keyBuffer)){
        	contentBuffer = SM4JSDK.addDataPadding(contentBuffer);
            outputBuffer = sdk.encryptBinary(contentBuffer);
            if (outputBuffer == null){
            	log.error("encryptBinaryContent加密失败!"); 
            }
        }
		return outputBuffer;
	}
	
	/**
	 *  二进制解密Byte数组内容
	 * @param key 密钥
	 * @param contentBuffer Byte数组内容
	 * @return 明文byte数组
	 */
	public static byte[] decryptBinaryByteArray(String key, byte [] contentBuffer) {
		byte[] keyBuffer = key.getBytes();
		SM4JSDK sdk = new SM4JSDK();
		if (sdk.createContext(keyBuffer)) {
			byte[] inputDecrypt = sdk.decryptBinary(contentBuffer);
			if (inputDecrypt != null) {
				inputDecrypt = SM4JSDK.removeDataPadding(inputDecrypt);
				return inputDecrypt;
			} else {
				log.error("解密失败\n");
			}
		}
		return null;
	}
	
	/**
	 *  二进制解密Byte数组内容
	 * @param key 密钥
	 * @param contentBuffer Byte数组内容
	 * @return 明文字符串
	 */
	public static String decryptBinaryContent(String key, byte [] contentBuffer) {
		byte[] keyBuffer = key.getBytes();
		String decrypt = "";
		SM4JSDK sdk = new SM4JSDK();
		if (sdk.createContext(keyBuffer)) {
			byte[] inputDecrypt = sdk.decryptBinary(contentBuffer);
			if (inputDecrypt != null) {
				inputDecrypt = SM4JSDK.removeDataPadding(inputDecrypt);
				decrypt = new String(inputDecrypt);
			} else {
				log.error("解密失败\n");
			}
		}
		return decrypt;
	}
	
	/**
	 * 读取文件内容。
	 * 
	 * @param filePath
	 *            文件
	 * @return 文件内容的byte数组
	 */
	private static byte[] readXmlContent(String filePath) {
		File file = new File(filePath);
		FileInputStream in = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = null;
		try {
			in = new FileInputStream(file);
			buffer = new byte[in.available()];
			in.read(buffer);
			out.write(buffer);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
				log.error(e.getMessage(), e);
			}
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
				log.error(e.getMessage(), e);
			}
		}
		return buffer;
	}

	public static void main(String[] args) {
//		//ok
//		SM4Util.decryptFile("&$L(,nfT/%IU@#g", "D:/ceshi.xml","d:/zhangkaijiemi.xml");
//		//ok
//		SM4Util.decryptFile("123456", "d:/999.xml","d:/777.xml");
//		//ok
//		byte [] encryptByteArr = SM4Util.encryptBinary("654321", "D:/sbr_work_space/testsm4.xml");
//		System.out.println("encryptByteArr = " + encryptByteArr.length);
//		//ok
//		String decryptStr = SM4Util.decryptBinary("123456", "d:/999.xml"); 
//		System.out.println("decryptStr = " + decryptStr);
//		//ok
//		String decryptStr = SM4Util.decryptBinaryContent("654321", encryptByteArr);
//		System.out.println("decryptStr = " + decryptStr);
		
		/**
		 * 2014-08-26  验证问题
		 */
		String str = "abcdedddddddddddddddddddddddddddddddddddf";
		byte [] encryptByte = SM4Util.encryptBinaryByteArray("111111", str.getBytes());
		System.out.println("encryptByte = " + new String(encryptByte)); 
		byte [] data = SM4Util.decryptBinaryByteArray("111111", encryptByte);
		System.out.println("decryptData = " + new String(data));
		//执行这一行重复解密时，JVM就直接死掉.
		data = SM4Util.decryptBinaryByteArray("111111", data);
		System.out.println("data = " + new String(data)); 
		
//		boolean encryptFlag = SM4Util.encryptFile("&$L(,nfT/%IU@#g", "D:/sm4/work.xml","d:/sm4/work_sm4.xml");
//		System.out.println("encryptFlag = " + encryptFlag);
//		boolean decryptFlag = SM4Util.decryptFile("&$L(,nfT/%IU@#g", "d:/sm4/sm1test.zip","d:/sm4/sm1test_decrypt.zip");
//		System.out.println("@@@@@@@"); 
//		decryptFlag = SM4Util.decryptFile("&$L(,nfT/%IU@#g", "d:/sm4/sm1test.zip","d:/sm4/sm1test_decrypt.zip");
//		System.out.println("decryptFlag = " + decryptFlag);
		

	}
	
}
