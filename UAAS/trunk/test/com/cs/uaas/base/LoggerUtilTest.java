package com.cs.uaas.base;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;

public class LoggerUtilTest {

	@Test
	public void testGetLogger() {
		Logger logger = LoggerUtil.getLogger();
		String loggerName = logger.getName();
		logger.debug("本logger名称为：" + loggerName);
		Assert.assertEquals("com.cs.uaas.base.LoggerUtilTest", loggerName);
	}

}
