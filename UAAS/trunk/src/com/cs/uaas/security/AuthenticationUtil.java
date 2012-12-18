package com.cs.uaas.security;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.cs.uaas.base.LoggerUtil;

/**
 * 此工具类可以方便获取当前用户信息、所属机构、权限等
 */
public class AuthenticationUtil {
	private static final ThreadLocal<MyUserDetails> MyUserDetailsHolder = new ThreadLocal<MyUserDetails>();

	Logger logger = LoggerUtil.getLogger();

	public static void setCurrentUser(String userName) {
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userName, ""));
	}

	/**
	 * 清空ThreadLocal（单元测试用）
	 */
	public static void clearThreadLocal() {
		MyUserDetailsHolder.remove();
	}

	/**
	 * 取得当前用户标识（登录名称）
	 */
	public static String getCurrentUser() {
		Authentication currentUser = getCurrentUserAuthentication();
		if (currentUser != null) {
			return currentUser.getName();
		}

		return null;
	}

	/**
	 * 获取当前登录用户
	 * @return
	 */
	public static Authentication getCurrentUserAuthentication() {
		Authentication currentUser = null;

		SecurityContext context = SecurityContextHolder.getContext();

		if (null != context) {
			currentUser = context.getAuthentication();
		}
		return currentUser;
	}
}