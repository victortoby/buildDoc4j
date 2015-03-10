package com.sbrinfo;

import org.dom4j.Document;

import com.sbrinfo.toolfactory.BuilderDocToolFactory;
import com.sbrinfo.tools.Builder;




public class Main {

	public static void main(String[] args) {
		String parserXmlPath = "D:\\test\\LsLinuxScan_2014-12-22-14-40-09_decrypt.xml";
		Builder c = BuilderDocToolFactory.getBuilder("3");
		try{
			Document parserDoc = c.parserXML(parserXmlPath);
			Document cd = c.buliderXML(parserDoc,"Linux主机配置检查工具检测报告模板.ftl");
			//TODO 生成doc
			c.createDoc(cd, "D:\\ceshi.doc");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
