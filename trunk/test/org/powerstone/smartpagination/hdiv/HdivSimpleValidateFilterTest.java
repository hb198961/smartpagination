package org.powerstone.smartpagination.hdiv;

import java.util.Hashtable;

import javax.servlet.FilterChain;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.hdiv.config.HDIVConfig;
import org.hdiv.util.HDIVErrorCodes;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public class HdivSimpleValidateFilterTest extends AbstractDependencyInjectionSpringContextTests {
	Logger log = Logger.getLogger(getClass());
	private HDIVConfig hdivConfig;

	protected MockHttpServletRequest request;

	protected MockHttpServletResponse response;

	public void setHdivConfig(HDIVConfig hdivConfig) {
		this.hdivConfig = hdivConfig;
	}

	@Override
	protected String[] getConfigLocations() {
		return new String[] { "classpath:hdiv-*.xml" };
	}

	@SuppressWarnings("unchecked")
	public void testDoFilterInternal() throws Exception {
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		request.setRequestURI("/abc.do");
		request.addParameter("sqlInject", "''");
		request.addParameter("xss", "<script>");
		//ignore userStartParameters eg:password
		request.addParameter("password", "''");

		HdivSimpleValidateFilter filter = new HdivSimpleValidateFilter();
		filter.setHdivConfig(hdivConfig);
		filter.doFilter(request, response, Mockito.mock(FilterChain.class));
		Hashtable errors = (Hashtable) request
				.getAttribute(HDIVErrorCodes.EDITABLE_PARAMETER_ERROR);
		
		log.debug(errors);
		Assert.assertTrue("ERRORS", errors != null);
		Assert.assertEquals("ERRORS size", 2, errors.size());

	}

}
