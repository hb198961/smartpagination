package com.cs.uaas.base;

import org.apache.log4j.Logger;
import org.zkoss.zul.Datebox;

//@Composite(name = "MyDatebox", macroURI = "/WEB-INF/components/MyDatebox.zul")
@SuppressWarnings("serial")
public class MyDatebox extends Datebox {
	Logger logger = LoggerUtil.getLogger();

	@Override
	public void service(org.zkoss.zk.au.AuRequest request, boolean everError) {
		final String cmd = request.getCommand();
		logger.debug("cmd:" + cmd);
		logger.debug("getValue" + getValue());
		logger.debug("value:" + request.getData().get("value"));
		// setValue(new Date(System.currentTimeMillis()));
		super.service(request, everError);
	}

}
