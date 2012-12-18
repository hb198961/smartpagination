package com.cs.uaas.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import com.cs.uaas.validator.DistinctEmail;

/**
 * 联系方式信息
 */
@Embeddable
public class UserContactInfo {

	@NotEmpty
	@Pattern(regexp = ".+@.+\\.[a-z]+", message = "请输入合法的Email！")
	@Column(unique = true, nullable = false)
	private String email;

	private String mobile;

	private String telephone;

	@Transient
	private User userModel;

	public UserContactInfo() {
	}

	public UserContactInfo(User userModel) {
		this.userModel = userModel;
	}

	/**
	 * 定制的Validation constraint，
	 * 因为判断email是否重复时，需要根据id是否为空做不同判断，因此不能仅加在email字段上
	 */
	@DistinctEmail
	public UserContactInfo getSelf() {
		return this;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telphone) {
		this.telephone = telphone;
	}

	@AssertTrue(message = "手机和座机不能全为空！")
	public Boolean getMobileOrTelephone() {
		return StringUtils.isNotEmpty(mobile) || StringUtils.isNotEmpty(telephone);
	}

	public User getUserModel() {
		return userModel;
	}

	public void setUserModel(User userModel) {
		this.userModel = userModel;
	}
}