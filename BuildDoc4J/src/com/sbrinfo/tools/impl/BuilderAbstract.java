package com.sbrinfo.tools.impl;

import org.dom4j.Document;

import com.sbrinfo.tools.Builder;
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

	@Override
	public Document parserXML(String srcXMLPath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document buliderXML(Document parsed, String docTemplate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void closeFile() {
		// TODO Auto-generated method stub
		
	}
	
}
