package com.hah.excel.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

public class MainController {
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile dataFile = multipartRequest.getFile("data");
		dataFile.getInputStream();
		return null;
	}

}
