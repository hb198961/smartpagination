package org.powerstone.smartpagination.tag;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.powerstone.smartpagination.common.BasePagingController;
import org.powerstone.smartpagination.common.PageModel;

public class PagingBar extends TagSupport {
	private static final long serialVersionUID = 6670041132688723682L;

	private final Logger log = Logger.getLogger(this.getClass());

	private String styleClass;

	private String url;

	private String margin = "5%";

	public String getStyleClass() {
		return styleClass;
	}

	public String getMargin() {
		return margin;
	}

	public void setMargin(String margin) {
		this.margin = margin;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public int doStartTag() throws JspException {
		return TagSupport.SKIP_BODY;
	}

	public static int computeRowNo(HttpServletRequest request) {
		PageModel pm = (PageModel) request
				.getAttribute(BasePagingController.DEFAULT_PAGE_MODEL_NAME);
		if (pm == null) {
			return 0;
		} else {
			return pm.computeRecordsBeginNo() + 1;
		}
	}

	@Override
	public int doEndTag() throws JspException {
		ResourceBundle rb = ResourceBundle.getBundle("paging_messages");
		// get the paging model from reuest context
		PageModel pm = (PageModel) pageContext.getRequest().getAttribute(
				BasePagingController.DEFAULT_PAGE_MODEL_NAME);

		if (pm == null) {
			log.warn("There is no PageModel in request:"
					+ ((HttpServletRequest) pageContext.getRequest())
							.getRequestURI());
			pm = new PageModel();
		}
		String fullUrl = generateBaseUrl(pm);
		fullUrl += "&" + BasePagingController.PAGE_SIZE_PARAM + "="
				+ pm.getPageSize();

		int newPageNo = pm.computeNewPageNoInTag();
		int totalPages = pm.computePageCount();
		int[] nearPages = new int[3];
		nearPages[0] = newPageNo - 1;
		nearPages[1] = newPageNo;
		nearPages[2] = newPageNo + 1;

		// html bar for paging bar
		StringBuffer htmlBuff = new StringBuffer();
		// format the start of the bar
		htmlBuff.append("<table width=\"100%\" border=\"0\" ");
		htmlBuff.append("cellpadding=\"0\" cellspacing=\"0\" ");
		if (styleClass == null || styleClass.length() == 0) {
			htmlBuff.append("style=\"background-color:#F4F9FF;\" ");
		} else {
			htmlBuff.append("class=\"");
			htmlBuff.append(styleClass);
			htmlBuff.append("\" ");
		}// margin
		htmlBuff.append(">\n<tr>\n\t");
		htmlBuff.append("<td width=\"");
		htmlBuff.append(margin);
		htmlBuff.append("\"></td>");
		htmlBuff.append("<td>\n\t\t");

		// link to first page
		if (totalPages == 0) {
			htmlBuff.append("<span><font face=webdings>9</font></span>");
		} else {
			htmlBuff.append("<span><a title=\"");
			htmlBuff.append(rb.getString("first"));
			htmlBuff.append("\" href=\"");
			htmlBuff.append(fullUrl);
			htmlBuff.append("&");
			htmlBuff.append(BasePagingController.TO_FIRST_PARAM);
			htmlBuff.append("=true\"> <font face=webdings>9</font></a></span>");
		}

		String currPage = "&" + BasePagingController.CURR_PAGE_PARAM + "="
				+ newPageNo;

		// link to previous page
		if (nearPages[0] > 0) {
			htmlBuff.append("<span><a title=\"");
			htmlBuff.append(rb.getString("last"));
			htmlBuff.append("\" href=\"");
			htmlBuff.append(fullUrl);
			htmlBuff.append(currPage);
			htmlBuff.append("&");
			htmlBuff.append(BasePagingController.TO_LAST_PARAM);
			htmlBuff.append("=true\" ><font face=webdings>7</font></a></span>");
		} else {
			htmlBuff.append("<span><font face=webdings>7</font></span>\n");
		}
		// link to near pages
		for (int i = 0; i < 3; i++) {
			if (nearPages[i] > 0 && nearPages[i] <= totalPages) {
				if (i == 1) {
					htmlBuff.append("<span><b><font color=#ff0000>");
					htmlBuff.append(nearPages[i]);
					htmlBuff.append("</font></b></span>");
				} else {
					htmlBuff.append("<span><b><a href=\"");
					htmlBuff.append(fullUrl);
					htmlBuff.append("&");
					htmlBuff.append(BasePagingController.TO_PAGE_NO_PARAM);
					htmlBuff.append("=");
					htmlBuff.append(nearPages[i]);
					htmlBuff.append("\">");
					htmlBuff.append(nearPages[i]);
					htmlBuff.append("</a></b></span>");
				}
			}
		}

		// link to next page
		if (nearPages[2] <= totalPages) {
			htmlBuff.append("<span><a title=\"");
			htmlBuff.append(rb.getString("next"));
			htmlBuff.append("\" href=\"");
			htmlBuff.append(fullUrl);
			htmlBuff.append(currPage);
			htmlBuff.append("&");
			htmlBuff.append(BasePagingController.TO_NEXT_PARAM);
			htmlBuff.append("=true\"><font face=webdings>8</font></a></span>");
		} else {
			htmlBuff.append("<span><font face=webdings>8</font></span>");
		}

		// link to end page
		if (totalPages == 0) {
			htmlBuff.append("<span><font face=webdings>:</font></span>");
		} else {
			htmlBuff.append("<span><a title=\"");
			htmlBuff.append(rb.getString("end"));
			htmlBuff.append("\" href=\"");
			htmlBuff.append(fullUrl);
			htmlBuff.append("&");
			htmlBuff.append(BasePagingController.TO_END_PARAM);
			htmlBuff.append("=true\"><font face=webdings>:</font></a></span>");
		}

		// common info on the bar
		htmlBuff.append("<span>");
		htmlBuff.append("&nbsp;&nbsp;");
		htmlBuff.append(rb.getString("total"));
		htmlBuff.append(":");
		htmlBuff.append(pm.getTotalRecordsNumber());

		htmlBuff.append("&nbsp;&nbsp;");

		htmlBuff.append(rb.getString("every"));
		htmlBuff.append(":");
		htmlBuff.append("<input type=text id=\"pageSize\"");
		htmlBuff.append(" maxlength=2 size=1 name=\"pageSize\" value=");
		htmlBuff.append(pm.getPageSize());
		htmlBuff.append(">\n");

		htmlBuff.append("&nbsp;&nbsp;");

		htmlBuff.append(rb.getString("totalpages"));
		htmlBuff.append(":");
		htmlBuff.append(totalPages);

		htmlBuff.append("</span>\n");

		htmlBuff.append("&nbsp;&nbsp;");
		// Go form
		htmlBuff.append("<span>");
		htmlBuff.append(rb.getString("goto"));
		htmlBuff.append("<input type=text id=\"targetPage\"");
		htmlBuff.append(" maxlength=5 size=1 name=Page value=");
		htmlBuff.append(newPageNo);
		htmlBuff.append(">\n");
		htmlBuff.append("<input type=submit value=\"Go\" ");
		htmlBuff.append("onclick=javascript:go2page(");
		htmlBuff.append("document.all.targetPage.value,");
		htmlBuff.append("document.all.pageSize.value,");
		htmlBuff.append(totalPages);
		htmlBuff.append(")></span>");
		// format the end of the bar
		htmlBuff.append("</td>");
		htmlBuff.append("<td width=\"");
		htmlBuff.append(margin);
		htmlBuff.append("\"></td>");
		htmlBuff.append("\n</tr>\n</table>\n");

		// Javascript code for paging bar
		StringBuffer jsBuff = new StringBuffer();
		// set the javascript code for paging bar
		jsBuff.append("<script langage=javascript>\n");
		jsBuff.append("function go2page(page, pageSize, totalpages){\n");

		jsBuff.append("if(isNaN(pageSize) | pageSize < 1 | pageSize > 100){\n");
		jsBuff.append("alert(\"");
		jsBuff.append(rb.getString("errorpagesize"));
		jsBuff.append("\")\n");
		jsBuff
				.append("}else if(isNaN(page) | page < 1 | page > totalpages){\n");
		jsBuff.append("alert(\"");
		jsBuff.append(rb.getString("errorpage"));
		jsBuff.append("\")\n");
		jsBuff.append("}else{\n");

		jsBuff.append("this.location = '");
		String formUrl = generateBaseUrl(pm);
		jsBuff.append(formUrl);
		jsBuff.append("&");
		jsBuff.append(BasePagingController.TO_PAGE_NO_PARAM);
		jsBuff.append("=' + page +");
		jsBuff.append("'&");
		jsBuff.append(BasePagingController.PAGE_SIZE_PARAM);
		jsBuff.append("=' + pageSize;\n");
		jsBuff.append("}\n");// end of else
		jsBuff.append("}\n");// end of function
		jsBuff.append("</script>\n");

		String barHtml = htmlBuff.toString();
		String barJs = jsBuff.toString();
		log.debug(barJs);
		log.debug(barHtml);
		// write out
		try {
			pageContext.getOut().println(barJs);
			pageContext.getOut().println(barHtml);
		} catch (IOException ioe) {
			throw new JspException(ioe);
		}

		return EVAL_PAGE;
	}

	// return /bms/url?orderBy=xx&orderDir=xxx
	private String generateBaseUrl(PageModel pm) {
		String formUrl = ((HttpServletRequest) pageContext.getRequest())
				.getContextPath()
				+ url;
		if (formUrl.indexOf("?") < 0) {
			formUrl += "?";
		}
		if (pm.getOrderBy() != null) {
			formUrl += "&" + BasePagingController.ORDER_BY_PARAM + "="
					+ pm.getOrderBy() + "&"
					+ BasePagingController.ORDER_DIR_PARAM + "="
					+ pm.getOrderDirection();
		}
		return formUrl;
	}
}
