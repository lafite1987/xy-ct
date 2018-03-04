package com.lfyun.xy_ct.common.enums;

import lombok.Getter;

@Getter
public enum UserType {
	USER(1, "用户"),
	STAFF(2, "员工"),
	;
	private int code;
	private String label;
	private UserType(int code, String label) {
		this.code = code;
		this.label = label;
	}
	
	public static UserType valueOf(int code) {
		for(UserType userType : values()) {
			if(userType.getCode() == code) {
				return userType;
			}
		}
		return UserType.USER;
	}
}
