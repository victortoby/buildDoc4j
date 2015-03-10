package com.sbrinfo.tools.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.sbrinfo.Main;
import com.sbrinfo.bean.Linux;
import com.sbrinfo.util.Dom4jParseUtil;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Linux主机配置检查工具检查报告实现类
 * @author HULANHUI
 *
 */
public class LinuxBuilder extends BuilderAbstract {

	@Override
	public Document buliderXML(Document parsed, String docTemplate) {
		Map<String,Object> param = new HashMap<String, Object>();
		//1，获取到模版
		Template template=getTemplate(Main.class,docTemplate);
		//2，系统信息和扫描信息
	    Element operationInfo=(Element)parsed.selectSingleNode("result/scheme/operation_info");
	    param.put("osName",operationInfo.attribute("SrcOs").getText());
	    param.put("ipAddress",operationInfo.attribute("DestIp").getText());
	    param.put("osVersion",operationInfo.attribute("SrcOs").getText());
	    param.put("beginDate",operationInfo.attribute("Stamp").getText());
	    param.put("memory","无");
	    param.put("protocolVersion","无");
	    param.put("diskCapacity","无");
	    param.put("endDate","无");
	    param.put("checkType","无");
	  //帐号信息
	  	String itemPath = "result/report/account_info/item";
	  	List<Element> itemList = Dom4jParseUtil.getNodesByXpath(parsed,itemPath);
	  	List<Linux> linuxs = new ArrayList<Linux>();
	  	if(itemList!=null && itemList.size()>0){
	  		Linux linux = null;
	  		for(Element item : itemList){
	  			String suggest = "";
	  			String checkResult = "正常";
	  			//都不存在<weak_password>项目或者存在<weak_password>项目且数值 =3，为符合,只要有一个<item>项存在<weak_password>项目并且数值 = 1 或 2，为不符合。
	  			Element elePwd = item.element("weak_password");
	  			if(null!= elePwd && !elePwd.getText().equals("3")){
	  				checkResult = elePwd.getText().equals("1")?"高风险":elePwd.getText().equals("2")?"低风险":"正常";
	  				suggest = getSugget("linux_kl.csv", "t203s4301102115");
	  			}
	  			String accountLevel = item.elementText("account_level").equals("3")?"超级用户":item.elementText("account_level").equals("2")?"管理员":"普通用户";
	  			linux = new Linux(item.elementText("fullname"),accountLevel,checkResult,suggest);
	  			linuxs.add(linux);
	  		}
		}
	  	param.put("accountInfo", linuxs);
	  	//活动端口及服务信息
//		param.put("serverInfo", "");
		//5，将数据保存到map中，并将map填充到模版中
		StringWriter sw = new StringWriter();
		Document docXml = null;
		try {
			template.process(param , sw);
			docXml = DocumentHelper.parseText(sw.toString());
			
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return docXml;
	}

}
