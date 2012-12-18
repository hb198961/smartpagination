package com.cs.uaas.base;

import org.apache.log4j.Logger;

public class LoggerUtil {

	/**
	 * 得到当前class名称，系统所有使用logger的地方直接调用此方法
	 * 
	 * @return
	 */
	public static Logger getLogger() {
		StackTraceElement[] stackEle = new RuntimeException().getStackTrace();
		return Logger.getLogger(stackEle[1].getClassName());
	}
}
