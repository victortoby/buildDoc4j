package com.sbrinfo.tools.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;

import com.sbrinfo.Main;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 病毒处理
 * @author HULANHUI
 */
public class CockhorseBuilder extends BuilderAbstract {

	@Override
	public Document buliderXML(Document parsed, String docTemplate) {
		Map<String,Object> param = new HashMap<String, Object>();
		//1，获取到模版
		Template template=getTemplate(Main.class,docTemplate);
		//2，系统信息和扫描信息
		Element root = parsed.getRootElement();
	    Element taskInfo=root.element("task_info"); 
	    param.put("osName",taskInfo.attribute("os").getText());
	    param.put("ipAddress",taskInfo.attribute("ip").getText());
	    param.put("osVersion",taskInfo.attribute("os").getText());
	    param.put("beginDate",taskInfo.attribute("stamp").getText());
	    param.put("endDate",taskInfo.attribute("elapsed").getText());
		//检查结果汇总信息
	    Element result=root.element("result"); 
	    int findCockhorseNum = 0;
	    int secureFileNum = 0;
	    for(Iterator it=result.elementIterator();it.hasNext();){
	    	Element element = (Element) it.next();
	    	if(taskInfo.attribute("field").getText()=="木马数量"){//木马数量
	    		String fn = taskInfo.attribute("value").getText();
	    		param.put("findCockhorseNum",fn);
	    		if(null!=fn && !"".equals(fn)){
	    			findCockhorseNum = Integer.parseInt(fn);
	    		}
	    	}
	    	if(taskInfo.attribute("field").getText()=="扫描文件"){//扫描文件
	    		String sn = taskInfo.attribute("value").getText();
	    		if(null!=sn && !"".equals(sn)){
	    			secureFileNum = Integer.parseInt(sn);
	    		}
	    		//安全文件=扫描文件-木马数量
	    		param.put("secureFileNum",(secureFileNum-findCockhorseNum));
	    	}
	    	if(element.attribute("field").getText()=="扫描目录"){//扫描目录
	    		param.put("scanDirectoryNum",taskInfo.attribute("value").getText());
	    	}
	    }
	    //3，生成图表图片
//	    param.put("image", getImageStr());
		//4，木马列表
		
		//5，将数据保存到map中，并将map填充到模版中
		
		StringWriter sw = new StringWriter();
		try {
			template.process(param , sw);
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("sw:"+sw.toString());
		return null;
	}
}
