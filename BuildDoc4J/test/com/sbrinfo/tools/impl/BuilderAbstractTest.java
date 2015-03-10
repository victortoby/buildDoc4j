package com.sbrinfo.tools.impl;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import com.sbrinfo.toolfactory.BuilderDocToolFactory;

public class BuilderAbstractTest {
	
	BuilderAbstract windowsBuilder =(BuilderAbstract) BuilderDocToolFactory.getBuilder("4");

	@Test
	public void testParserXML() {
		windowsBuilder.parserXML("D:/LsWindowsScan_2015-03-05-17-26-36.xml");
	}
	
	@Test
	public void testParserOriginalXML() {
		windowsBuilder.parserOriginalXML("C:/Users/KAI/Desktop/解密后的工具检查结果文件/LsWindowsScan_2014-12-15-11-53-53_decrypt.xml");
	}
	
	@Test
	public void testCreateDoc() {
		SAXReader saxReader = new SAXReader();
		Document document;
		try {
			document = saxReader.read(new File("C:/Users/KAI/Desktop/解密后的工具检查结果文件/LsWindowsScan_2014-12-15-11-53-53_decrypt.xml"));
			File file = new File("d:/zhangkai.xml");
			if(null != file.getParent()) {
				mkdirs(file.getParent());
			}
			windowsBuilder.createDoc(document,"d:/zhangkai.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public  boolean mkdirs(String path) {
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

}
