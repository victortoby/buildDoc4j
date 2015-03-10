package com.sbrinfo.tools.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.sbrinfo.tools.Builder;
import com.sbrinfo.utils.SM4Util;

import freemarker.template.Configuration;
import freemarker.template.Template;
/**
 * 
* Filename: BuilderAbstract.java  
* Description: 工具处理类的抽象类用于处理XML生成DOC的转换，实现工具处理类子类公用的方法
* Copyright:Copyright (c)2015
* Company:  SBRINFO
* @author:  Zhang.Kai
* @version: 1.0  
* @Create:  2015年3月5日上午10:43:46  
* Modification History:  
* Date								Author			Version
* ------------------------------------------------------------------  
* 2015年3月5日 上午10:43:46				Zhang.Kai  	1.0
 */
public abstract class BuilderAbstract implements Builder{
	
	Logger log = Logger.getLogger(BuilderAbstract.class);
	
	private static Configuration cfg = null;
	private static Map<String,Map<String,String>> fileSuggestMap = new HashMap<String,Map<String,String>>();
	/* 
	 * 解析扫描结果
	 */
	@Override
	public Document parserXML(String srcXMLPath) {
		/*SM4解密后的扫描结果文件存放路径路径*/
		//String decryptPath = srcXMLPath.substring(0, srcXMLPath.length() - 4) + "_decrypt.xml";//
		/*decryptFile为原始扫描结果文件*/
		/*if(decryptSM4File(srcXMLPath, decryptPath)) {
			log.info("SM4解密成功，解密后的文件路径为:" + decryptPath);
		} else {
			log.error("SM4解密失败!");
			return null;
		}*/
		//return parserOriginalXML(decryptPath);//解析原始结果文件
		Document document = null;
		try {
			SAXReader saxReader = new SAXReader();
			document = saxReader.read(new File(srcXMLPath));  //读取XML文件,获得document对象
		} catch(Exception e) {
			log.error("解析XML出错，查正后请重试!");
		}
		return document;
	}
	
	/**
	 * 
	 * @Description:SM4解密扫描结果文件
	 * @author Zhang.Kai
	 * @Created 2015年3月6日下午4:55:03
	 * @param srcPath
	 * @param tragetPath
	 * @return
	 */
	private Boolean decryptSM4File(String srcPath, String tragetPath) {
		//1，解密
		if(!checkFilePath(srcPath)) {
			log.error("原始结果文件错误，请查证!");
			return null;
		}
		return SM4Util.decryptFile("&$L(,nfT/%IU@#g",srcPath,tragetPath);//解密动作
	}
	
	/**
	 * 
	 * @Description:解析扫描结果文件
	 * @author Zhang.Kai
	 * @Created 2015年3月6日下午5:20:36
	 * @param originalXMLPath
	 * @return
	 */
	public Document parserOriginalXML(String originalXMLPath) {
		Document document = DocumentHelper.createDocument();
		if(!checkFilePath(originalXMLPath)) {
			log.error("SM4解密后的原始结果文件错误，查正后请重试!");
			return null;
		}
		try {
			SAXReader saxReader = new SAXReader();
			document = saxReader.read(new File(originalXMLPath));  //读取XML文件,获得document对象
		} catch(Exception e) {
			log.error("解析XML出错，查正后请重试!");
		}
		return document;
	}
	
	/**
	 * 
	 * @Description: 判断文件
	 * @author Zhang.Kai
	 * @Created 2015年3月6日下午5:09:15
	 * @param filePaht
	 * @return
	 */
	private Boolean checkFilePath(String filePath) {
		if(StringUtils.isEmpty(filePath)) {
			return false;
		}
		File encryptFile = new File(filePath);
		if(!encryptFile.isFile() && !encryptFile.exists()) {
			return false;
		}
		return true;
	}
	

	/**
	 * 生成word
	 */
	@Override
	public void createDoc(Document builded, String targetXMLPath) throws Exception {
		File targetFile = new File(targetXMLPath);
		if(null != targetFile.getParent()) {
			mkdirs(targetFile.getParent());
		}
		XMLWriter writer = new XMLWriter(new FileWriter(targetFile));
		writer.write(builded);
		writer.close();
		log.info("文件生成成功！");
	}
	
	/**
	 * 
	 * @Description:创建文件目录
	 * @author Zhang.Kai
	 * @Created 2015年3月9日上午9:41:33
	 * @param path
	 * @return
	 */
	private  boolean mkdirs(String path) {
		boolean result = false;
		File file = new File(path);
		// 如果文件存在直接返回
		if (file.exists()) {
			result = true;
		}
		// 否则创建路径
		else {
			result = file.mkdirs();
		}
		return result;
	}
	
	/**
	 * 关闭文件
	 */
	@Override
	public void closeFile() {
		 
	}
	
	/**
	 * 获取需要生成的模版
	 * @param Main.class
	 * @param 模版名称
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public  Template getTemplate(Class c,String name) {
		try {
			if(null == cfg){
				cfg = new Configuration();
			}
			cfg.setClassForTemplateLoading(c, "template");
			return cfg.getTemplate(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取建议
	 * @param fileName 文件名称(包含后缀名)
	 * @param itemCode 指标向对应的编号
	 * @return Suggest
	 */
	public String getSugget(String fileName,String itemCode){
		if(fileSuggestMap.size()>0 && fileSuggestMap.containsKey(fileName)){
			Map<String,String> suggetMap = fileSuggestMap.get(fileName);
			if(suggetMap.containsKey(itemCode)){
				return suggetMap.get(itemCode);
			}else{
				fileSuggestMap.remove(fileName);
			}
		}
		return conversionFile(fileName,itemCode);
	}
	
	
	/**
	 * 将文件的内容保存到map中
	 * @param fileName
	 * @param itemCode
	 * @return Suggest
	 */
	private static String conversionFile(String fileName,String itemCode){
		String suggest = "";
		try {
			File file = new File(System.getProperty("user.dir")+"/bin/com/sbrinfo/db/"+fileName);
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "GBK");
			BufferedReader buffer = new BufferedReader(isr); 
			Map<String,String> suggetMap = new HashMap<String,String>();
			String str = "";
             while((str = buffer.readLine())!=null){
                 String[] strs = str.split(",");
                 suggetMap.put(strs[0], strs[1]);
             }
             buffer.close();
             if(suggetMap.containsKey(itemCode)){
            	 suggest = suggetMap.get(itemCode);
             }
             fileSuggestMap.remove(fileName);
             fileSuggestMap.put(fileName, suggetMap);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return suggest;
	}
	
	
}
