package com.cs.uaas.base;

@SuppressWarnings("serial")
public class TabCloseFailureException extends RuntimeException {
	public TabCloseFailureException(String errorMsg) {
		super(errorMsg);
	}
}