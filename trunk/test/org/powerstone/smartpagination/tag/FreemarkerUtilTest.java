package org.powerstone.smartpagination.tag;

import java.util.HashMap;

import junit.framework.Assert;
import junit.framework.TestCase;

public class FreemarkerUtilTest extends TestCase {

	public void testRentPageHead() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("paging_fullUrl", "TTTT");
		String html = FreemarkerUtil.rendPageHead(map);
		System.out.println(html);
		Assert.assertTrue(html.indexOf("TTTT") > 0);
	}

	public void testRentPageBar() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("paging_totalPages", "100");
		String html = FreemarkerUtil.rendPageBar(map);
		System.out.println(html);
		Assert.assertTrue(html.indexOf("100") > 0);
	}
}