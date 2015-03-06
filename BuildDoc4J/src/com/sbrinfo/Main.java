package com.sbrinfo;

import org.dom4j.Document;

import com.sbrinfo.toolfactory.BuilderDocToolFactory;
import com.sbrinfo.tools.Builder;

public class Main {

	public static void main(String[] args) {
		String parserXmlPath = "D:\\test\\tro_192.168.8.116.xml";
		Builder c = BuilderDocToolFactory.getBuilder("2");
		try{
//			Document parserDoc = c.parserXML(parserXmlPath);
//			c.buliderXML(parserDoc,"Linux主机配置检查工具检测报告模板.ftl");
			//TODO 生成doc
//			c.createDoc(null, "targetXMLPath");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
