package org.powerstone.smartpagination.tag;

import java.util.ResourceBundle;

import javax.servlet.jsp.PageContext;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class LongPagingBarTest extends TestCase {
	String jsResult = "<script langage=javascript>\n"
			+ "function go2page(page, pageSize, totalpages){\n"
			+ "if(isNaN(pageSize) | pageSize < 1 | pageSize > 100){\n"
			+ "alert(\"请输入有效每页记录数！\");\n"
			+ " document.all.p_pageSize.focus(); return false;\n"
			+ "}else if(isNaN(page) | page < 1 | page > totalpages){\n"
			+ "alert(\"请输入有效页码！\");\n"
			+ " document.all.p_toPageNo.focus(); return false;\n" + "}else{\n"
			+ "return true;}\n" + "}\n" + "</script>\n";

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testGenBarJs() {
		PageContext pageContext = Mockito.mock(PageContext.class);
		Mockito.stub(pageContext.getRequest()).toReturn(
				new MockHttpServletRequest());
		Mockito.stub(pageContext.getResponse()).toReturn(
				new MockHttpServletResponse());

		LongPagingBar.rb = ResourceBundle.getBundle("test_messages");
		LongPagingBar lpb = new LongPagingBar();
		lpb.setPageContext(pageContext);

		String barJs = lpb.genBarJs();
		Assert.assertEquals(jsResult, barJs);
	}
	
	public void testGenBarHtml() {
		
	}

}
