package com.lfyun.xy_ct.common;

import java.io.Serializable;

import lombok.Data;

@Data
public class User implements Serializable {

	private static final long serialVersionUID = -8010376317388064730L;

	private Long id;
	
	private String nickname;
	
	private String avatar;
	
	private String openid;
	
	private String accessToken;

	@Override
	public String toString() {
		return "User [id=" + id + ", nickname=" + nickname + ", openid="
				+ openid + "]";
	}
}
