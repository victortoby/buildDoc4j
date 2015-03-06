package com.sbrinfo.tools.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;

import com.sbrinfo.bean.ResultInfo;
import com.sbrinfo.service.PasrseHandlers;
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
	/* 
	 * 解析扫描结果
	 */
	@Override
	public ResultInfo parserXML(String srcXMLPath) {
		/*SM4解密后的扫描结果文件存放路径路径*/
		String decryptPath = srcXMLPath.substring(0, srcXMLPath.length() - 4) + "_decrypt.xml";//
		/*decryptFile为原始扫描结果文件*/
		if(decryptSM4File(srcXMLPath, decryptPath)) {
			log.info("SM4解密成功，解密后的文件路径为:" + decryptPath);
		} else {
			log.error("SM4解密失败!");
			return null;
		}
		return parserOriginalXML(decryptPath);//解析原始结果文件
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
	public ResultInfo parserOriginalXML(String originalXMLPath) {
		if(!checkFilePath(originalXMLPath)) {
			log.error("SM4解密后的原始结果文件错误，查正后请重试!");
			return null;
		}
		File originalFile = new File(originalXMLPath);
		return PasrseHandlers.getResultInfo(originalFile);//解析动作
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
	
	/*
	 *  生成word文件
	 */
	@Override
	public void createDoc(Document builded, String targetXMLPath) {
		System.out.println("createDoc");
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
	
	
}
