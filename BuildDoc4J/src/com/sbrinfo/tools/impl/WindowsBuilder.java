package com.sbrinfo.tools.impl;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.sbrinfo.Main;
import com.sbrinfo.bean.CpuInfo;
import com.sbrinfo.bean.MemoryInfo;
import com.sbrinfo.bean.SharePath;
import com.sbrinfo.util.Dom4jParseUtil;

import freemarker.template.Template;


public class WindowsBuilder extends BuilderAbstract {

	@Override
	public Document buliderXML(Document parsed, String docTemplate) {
		Map<String, Object> param = new HashMap<String, Object>();
		//1,获取模版
		Template template=getTemplate(Main.class,docTemplate);
		
		//2,生成后台数据
		//生成主机基本信息
		String[] hostElementName = {"hostName","userName","osName","osVersion","osType","uptime","mem","disk","systemPath"};
		String hostInfoPath = "result/report/host_info";
		Element hostInfos = Dom4jParseUtil.getSingleNodeByXpath(parsed, hostInfoPath);
		if(null != hostInfos) {
			//循环host_info下Element
			int index = 0;
			int arrayLength = hostElementName.length;
			for(Iterator<Element> elementIterator = hostInfos.elementIterator();elementIterator.hasNext();) {
				if(arrayLength==0) {
					break;
				}
				Element secElement = elementIterator.next();
				param.put(hostElementName[index], secElement.getData());
				index++;
				arrayLength --;
			}
		}
		
		//共享目录
		String shareListPath = "result/report/host_info/share_list";
		Element shareListElement = Dom4jParseUtil.getSingleNodeByXpath(parsed, shareListPath);
		if(null != shareListElement) {
			List<SharePath> shareList = new ArrayList<SharePath>();
			int shareIndex = 0;
			for(Iterator<Element> elementIterator = shareListElement.elementIterator();elementIterator.hasNext();) {
				Element secElement = elementIterator.next();
				SharePath sharePath = new SharePath();
				sharePath.setIndex(shareIndex);
				sharePath.setType(secElement.attribute("Type").getText());
				sharePath.setDescription(secElement.attribute("Description").getText());
				sharePath.setName(secElement.attribute("Name").getText());
				sharePath.setPath(secElement.attribute("Path").getText());
				shareList.add(sharePath);
				shareIndex ++;
			}
			param.put("shareList", shareList);
		}
		
		//CPU
		String cpuPath = "result/report/host_info/cpu";
		Element cpuListElement = Dom4jParseUtil.getSingleNodeByXpath(parsed, cpuPath);
		if(null != cpuListElement) {
			List<CpuInfo> cpuList = new ArrayList<CpuInfo>();
			int cpuIndex = 0;
			for(Iterator<Element> elementIterator = cpuListElement.elementIterator();elementIterator.hasNext();) {
				Element secElement = elementIterator.next();
				CpuInfo cpuInfo = new CpuInfo();
				cpuInfo.setIndex(cpuIndex);
				cpuInfo.setName(secElement.attribute("Name").getText());
				cpuInfo.setDataWidth(secElement.attribute("DataWidth").getText());
				cpuInfo.setCurrentVoltage(secElement.attribute("CurrentVoltage").getText());
				cpuInfo.setCurrentClockSpeed(secElement.attribute("CurrentClockSpeed").getText());
				cpuInfo.setMaxClockSpeed(secElement.attribute("MaxClockSpeed").getText());
				cpuInfo.setNumberOfCores(secElement.attribute("NumberOfCores").getText());
				cpuInfo.setVersion(secElement.attribute("Version").getText());
				cpuInfo.setProcessorId(secElement.attribute("ProcessorId").getText());
				cpuInfo.setManufacturer(secElement.attribute("Manufacturer").getText());
				cpuList.add(cpuInfo);
				cpuIndex ++;
			}
			param.put("cpuList", cpuList);
		}
		
		//主板
		String baseBoardPath = "result/report/host_info/baseboard/item";
		Element baseBoardElement = Dom4jParseUtil.getSingleNodeByXpath(parsed, baseBoardPath);
		if(null != baseBoardElement) {
			param.put("Manufacturer", baseBoardElement.attribute("Manufacturer").getText());
			param.put("BIOSVersion", baseBoardElement.attribute("BIOSVersion").getText());
			param.put("BIOSManufacturer", baseBoardElement.attribute("BIOSManufacturer").getText());
			param.put("ReleaseDate", baseBoardElement.attribute("ReleaseDate").getText());
			param.put("SerialNumber", baseBoardElement.attribute("SerialNumber").getText());
			param.put("Product", baseBoardElement.attribute("Product").getText());
		}
		
		//内存
		String memoryListPath = "result/report/host_info/memory_list";
		Element memoryListElement = Dom4jParseUtil.getSingleNodeByXpath(parsed, memoryListPath);
		if(null != memoryListElement) {
			List<MemoryInfo> memoryList = new ArrayList<MemoryInfo>();
			int memoryIndex = 0;
			for(Iterator<Element> elementIterator = memoryListElement.elementIterator();elementIterator.hasNext();) {
				Element secElement = elementIterator.next();
				MemoryInfo memoryInfo = new MemoryInfo();
				memoryInfo.setIndex(memoryIndex);
				memoryInfo.setName(secElement.attribute("Name").getText());
				memoryInfo.setDataWidth(secElement.attribute("DataWidth").getText());
				memoryInfo.setDeviceLocator(secElement.attribute("DeviceLocator").getText());
				memoryInfo.setCapacity(secElement.attribute("Capacity").getText());
				memoryList.add(memoryInfo);
				memoryIndex ++;
			}
		}
		
		
		StringWriter sw = new StringWriter();
		Document docXml = null;
		try {
			template.process(param, sw);
			docXml = DocumentHelper.parseText(sw.toString());
			super.createDoc(docXml, "d:/test.doc");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return docXml;
	}

}
