package com.lfyun.xy_ct.dto;

import lombok.Data;

@Data
public class UserBalanceDetailDTO {

    private Long id;
	
	private Long userId;
	
	private transient String nickname;
	/**
	 * 金额
	 */
	private Double amount;
	/**
	 * 业务类型：1-充值；2-消费；3-退款
	 */
	private Integer businessType;
	/**
	 * 1-成功；2-失败
	 */
	private Integer state;
	
	private Long createTime;
}
