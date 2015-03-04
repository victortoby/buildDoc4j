package com.sbrinfo.toolfactory;

import org.junit.Test;

import com.sbrinfo.tools.Builder;

public class BuilderDocToolFactoryTest {

	@Test
	public void testGetBuilder() {
		Builder builder = BuilderDocToolFactory.getBuilder("1");
		builder.createDoc(null, null);
	}

}
