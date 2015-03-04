package com.sbrinfo.toolfactory;

import java.util.HashMap;
import java.util.Map;

import com.sbrinfo.tools.Builder;
import com.sbrinfo.tools.impl.WindowsBuilder;
/**
 * 
* Filename: BuilderDocToolFactory.java  
* Description: Builder工厂类，生成Builder的实现类
* Copyright:Copyright (c)2015
* Company:  SBRINFO
* @author:  Zhang.Kai
* @version: 1.0  
* @Create:  2015年3月5日上午10:42:44  
* Modification History:  
* Date								Author			Version
* ------------------------------------------------------------------  
* 2015年3月5日 上午10:42:44				Zhang.Kai  	1.0
 */
public class BuilderDocToolFactory {
	
	private static Map<String, Builder> builderMap = null;
	/**
	 * 默认构造方法，以单例模式创建工厂中的builderMap
	 */
	static {
		if(null == builderMap) {
			builderMap = initFactory();
		}
	}
	
	private static Map<String, Builder> initFactory() {
		builderMap = new HashMap<String, Builder>();
		builderMap.put("1", new WindowsBuilder());
		return builderMap;
	}
	
	/**
	 * 
	 * @Description:传入toolCode，返回toolCode对应的Builder对象
	 * @author Zhang.Kai
	 * @Created 2015年3月4日下午6:02:17
	 * @param toolCode
	 * @return
	 */
	public static Builder getBuilder(String toolCode) {
		return builderMap.get(toolCode);
	}

}
