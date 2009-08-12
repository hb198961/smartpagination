package org.powerstone.smartpagination.hdiv;

import junit.framework.Assert;

import org.hdiv.config.HDIVConfig;
import org.hdiv.dataComposer.DataComposerMemory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public class HdivUtilTest extends AbstractDependencyInjectionSpringContextTests {
	HDIVConfig config;

	DataComposerMemory dataComposerMemory;

	protected MockHttpServletRequest request;

	protected MockHttpServletResponse response;

	@Override
	protected String[] getConfigLocations() {
		return new String[] { "classpath:hdiv-*.xml" };
	}

	public void testUrlEncode() {
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		request.setMethod("GET");
		request.setRequestURI("/abcd.htm");
		request.addParameter("p1", "11");
		request.addParameter("p2", "22");
		request.setAttribute("dataComposer",dataComposerMemory);

		HdivUtil util = new HdivUtil();
		util.setConfig(config);
		String url = null;
		try {
			url = util.encodeUrl("/abcd.htm?p1=11&p2=22", request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug(url);
		Assert.assertTrue("", url.indexOf("abcd.htm") > 0);
	}

	public void setConfig(HDIVConfig config) {
		this.config = config;
	}

	public void setDataComposerMemory(DataComposerMemory dataComposerMemory) {
		this.dataComposerMemory = dataComposerMemory;
	}

}
