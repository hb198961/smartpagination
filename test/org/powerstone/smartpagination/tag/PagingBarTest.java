/*
 * File: PagingBarTest.java
 * ProjectName: 
 * Description: 
 * 
 * Copyright 2004-2008 99Bill Corporation. All rights reserved.
 * -----------------------------------------------------------
 * Date            Author          Changes
 * 2009-8-12       vincent.li      created
 */
package org.powerstone.smartpagination.tag;

import junit.framework.Assert;
import junit.framework.TestCase;

/** 
 * <p>Title</p> 
 * 
 * Description .
 * 
 * @version $ Revision: 1.2 $  $ Date: 2009-8-12 下午04:40:20 $
 */

public class PagingBarTest extends TestCase {

	public void testHdivSubString() {
		PagingBar pb = new PagingBar();
		String result = pb.hdivSubString(
				"/formtags-2.5/form.htm?id=1&_HDIV_STATE_=17-1-D8282283A62D",
				PagingBar._HDIV_STATE_ + "=");
		Assert.assertEquals("_HDIV_STATE_", "17-1-D8282283A62D", result);

		result = pb.hdivSubString("_HDIV_STATE_=17-1-D8282283A62D", PagingBar._HDIV_STATE_ + "=");
		Assert.assertEquals("_HDIV_STATE_", "17-1-D8282283A62D", result);

		Assert.assertNull("bad token", pb.hdivSubString(
				"/formtags-2.5/form.htm?id=1&_HDIV_STATE_=17-1-D8282283A62D", "ccxcxcx"));
	}

}
