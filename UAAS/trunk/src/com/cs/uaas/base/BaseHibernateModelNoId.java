package com.cs.uaas.base;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

@MappedSuperclass
public class BaseHibernateModelNoId {

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false, updatable = false)
	private Date crtDttm = new Date();

	@Column(nullable = false, updatable = false)
	private String crtUser;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date lastUpdateDttm = new Date();

	@Column(nullable = false)
	private String lastUpdateUser;

	@Column(nullable = false)
	private Boolean enableFlg = Boolean.TRUE;

	@Version
	@Column
	private long version;

	// 用于显示名称不与数据库做映射
	@Transient
	private String crtUserName;

	@Transient
	private String lastUpdateUserName;

	public Boolean getEnableFlg() {
		return enableFlg;
	}

	public void setEnableFlg(Boolean enableFlg) {
		this.enableFlg = enableFlg;
	}

	public Date getCrtDttm() {
		return crtDttm;
	}

	/**
	 * @return yyyy-MM-dd HH:mm:ss ,such as 2012-12-25 20:20:20
	 */
	public String getCrtDttmString() {
		SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_PATTERN_yyyy_MM_dd_HH_MM_ss);
		return crtDttm != null ? sdf.format(crtDttm) : "";
	}

	public void setCrtDttm(Date crtDttm) {
		this.crtDttm = crtDttm;
	}

	public String getCrtUser() {
		return crtUser;
	}

	public void setCrtUser(String crtUserId) {
		this.crtUser = crtUserId;
	}

	public Date getLastUpdateDttm() {
		return lastUpdateDttm;
	}

	/**
	 * @return yyyy-MM-dd HH:mm:ss ,such as 2012-12-25 20:20:20
	 */
	public String getLastUpdateDttmString() {
		SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_PATTERN_yyyy_MM_dd_HH_MM_ss);
		return lastUpdateDttm != null ? sdf.format(lastUpdateDttm) : "";
	}

	public void setLastUpdateDttm(Date lastUpdateDttm) {
		this.lastUpdateDttm = lastUpdateDttm;
	}

	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(String lastUpdateUserId) {
		this.lastUpdateUser = lastUpdateUserId;
	}

	public String getCrtUserName() {
		return crtUserName;
	}

	public void setCrtUserName(String crtUserName) {
		this.crtUserName = crtUserName;
	}

	public String getLastUpdateUserName() {
		return lastUpdateUserName;
	}

	public void setLastUpdateUserName(String lastUpdateUserName) {
		this.lastUpdateUserName = lastUpdateUserName;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}
}