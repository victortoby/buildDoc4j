package com.sbrinfo.tools.impl;

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

}
