package com.cs.uaas.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.cs.uaas.base.BaseHibernateModel;

@Entity
public class LoginHis extends BaseHibernateModel {

	// 操作类型
	public enum LoginType {
		LOGIN("登入"), LOGOUT("登出");
		private final String text;

		private LoginType(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}
	}

	// 登录账号
	@Column(nullable = false, updatable = false)
	private String userName;

	// 操作类型（登入、登出）
	@Column(nullable = false, updatable = false)
	@Enumerated(EnumType.ORDINAL)
	private LoginType loginType;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public LoginType getLoginType() {
		return loginType;
	}

	public String getLoginTypeText() {
		return loginType != null ? loginType.getText() : "";
	}

	public void setLoginType(LoginType loginType) {
		this.loginType = loginType;
	}

}
