package org.powerstone.smartpagination.hdiv;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hdiv.config.HDIVConfig;
import org.hdiv.util.HDIVErrorCodes;
import org.hdiv.util.HDIVUtil;
import org.springframework.web.filter.OncePerRequestFilter;

public class HdivSimpleValidateFilter extends OncePerRequestFilter {
	public static final String HDIV_PARAMETER = "HDIVParameter";
	
	public static final String DATA_TYPE_TEXT = "text";

	protected static Logger log = Logger.getLogger(HdivSimpleValidateFilter.class);

	private HDIVConfig hdivConfig;

	public void setHdivConfig(HDIVConfig hdivConfig) {
		this.hdivConfig = hdivConfig;
	}

	public HdivSimpleValidateFilter() {
	}

	protected void initFilterBean() throws ServletException {
		HDIVUtil.initFactory();
		this.hdivConfig = (HDIVConfig) HDIVUtil.getApplication().getBean("config");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		try {
			Hashtable unauthorizedEditableParameters = new Hashtable();
			Enumeration parameters = request.getParameterNames();
			while (parameters.hasMoreElements()) {
				String parameter = (String) parameters.nextElement();
				if (!this.hdivConfig.needValidation(parameter, (String) request.getSession()
						.getAttribute(HDIV_PARAMETER))) {
					log.debug("parameter " + parameter + " doesn't need validation");
					continue;
				}

				String[] values = request.getParameterValues(parameter);
				boolean isValid = hdivConfig.areEditableParameterValuesValid(request
						.getRequestURI(), parameter, values, DATA_TYPE_TEXT);
				if (!isValid) {
					StringBuffer unauthorizedValues = new StringBuffer(values[0]);
					for (int i = 1; i < values.length; i++) {
						unauthorizedValues.append("," + values[i]);
					}
					unauthorizedEditableParameters.put(parameter, values);
					log.error("[HDIV]" + HDIVErrorCodes.EDITABLE_VALIDATION_ERROR + "[url="
							+ request.getRequestURI() + "|parameter=" + parameter + "|values="
							+ unauthorizedValues.toString() + "]");
				}
			}

			if (unauthorizedEditableParameters.size() > 0) {
				request.setAttribute(HDIVErrorCodes.EDITABLE_PARAMETER_ERROR,
						unauthorizedEditableParameters);
				response.sendRedirect(response.encodeRedirectURL(request.getContextPath()
						+ hdivConfig.getErrorPage()));
			} else {
				filterChain.doFilter(request, response);
			}
		} finally {
			HDIVUtil.resetLocalData();
		}
	}

}
