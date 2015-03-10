package com.sbrinfo.toolfactory;

import org.dom4j.Document;
import org.junit.Test;

import com.sbrinfo.tools.Builder;

public class BuilderDocToolFactoryTest {

	@Test
	public void testGetBuilder() throws Exception {
		Builder builder = BuilderDocToolFactory.getBuilder("4");
		Document document = builder.parserXML("D:/LsWindowsScan_2014-12-15-11-53-53_decrypt.xml");
		builder.buliderXML(document, "Windows主机配置检查工具检查报告.ftl");
	}

}
