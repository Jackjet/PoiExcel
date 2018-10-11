package com.excel.testcode;

/**
 * 联系人
 */
public class Contacts {
	
	public Contacts() {
		super();
	}
	
	public Contacts(String userId, String name, String userType, String mobile, String email, String address,
			String city, String postalCode, String country, String profession, String remark) {
		super();
		this.userId = userId;
		this.name = name;
		this.userType = userType;
		this.mobile = mobile;
		this.email = email;
		this.address = address;
		this.city = city;
		this.postalCode = postalCode;
		this.country = country;
		this.profession = profession;
		this.remark = remark;
	}

	/**
	 * 联系人ID
	 */
	private String userId;
	/**
	 * 联系人
	 */
	private String name;
	/**
	 * 联系人类型
	 */
	private String userType;
	/**
	 * 联系电话
	 */
	private String mobile;
	/**
	 * 电子邮件
	 */
	private String email;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 城市
	 */
	private String city;
	/**
	 * 邮政编码
	 */
	private String postalCode;
	/**
	 * 国家/地区
	 */
	private String country;
	/**
	 * 联系人职务
	 */
	private String profession;
	/**
	 * 备注
	 */
	private String remark;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Override
	public String toString() {
		return "{\"userId\":\"" + userId + "\", \"name\":\"" + name + "\", \"userType\":\"" + userType + "\", \"mobile\":\"" + mobile
				+ "\", \"email\":\"" + email + "\", \"address\":\"" + address + "\", \"city\":\"" + city + "\", \"postalCode\":\"" + postalCode
				+ "\", \"country\":\"" + country + "\", \"profession\":\"" + profession + "\", \"remark\":\"" + remark + "\"}";
	}
}
