package org.powerstone.smartpagination.sample;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.powerstone.smartpagination.common.BasePagingController;
import org.powerstone.smartpagination.common.PageModel;
import org.powerstone.smartpagination.sample.SamplePagingController;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class SamplePagingControllerTest extends TestCase {
	protected MockHttpServletRequest request;

	protected MockHttpServletResponse response;

	public void testTest() throws Exception {
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		SamplePagingController spc = new SamplePagingController();

		request.setMethod("GET");

		request.addParameter("pOrder", "code1");

		spc.list(request, response);

		PageModel pm = (PageModel) request
				.getAttribute(BasePagingController.DEFAULT_PAGE_MODEL_NAME);
		List data = (List) request
				.getAttribute(BasePagingController.DEFAULT_PAGE_DATA_NAME);

		Assert.assertEquals(10, data.size());
		Assert.assertEquals(11, pm.computePageCount());
	}

}
