package com.sbrinfo.tools;

import org.dom4j.Document;

/**
 * 
* Filename: Builder.java  
* Description: 工具处理类的接口
* Copyright:Copyright (c)2015
* Company:  SBRINFO
* @author:  Zhang.Kai
* @version: 1.0  
* @Create:  2015年3月5日上午10:45:59  
* Modification History:  
* Date								Author			Version
* ------------------------------------------------------------------  
* 2015年3月5日 上午10:45:59				Zhang.Kai  	1.0
 */
public interface Builder {
	//解析XML
	Document parserXML(String srcXMLPath);
	//将解析后的XML对象放到DOC模版中
	Document buliderXML(Document parsed, String docTemplate);
	//生成DOC文件
	void createDoc(Document builded, String targetXMLPath) throws Exception;
	//关闭文件
	void closeFile();
	
}
