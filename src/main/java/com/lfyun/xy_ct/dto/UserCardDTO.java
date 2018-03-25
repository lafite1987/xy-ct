package com.lfyun.xy_ct.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCardDTO {

	private Long id;
	/**
	 * GIFT-礼券；CASH-代金券；DISCOUNT-折扣券；
	 */
	private String cardType;
	/**
	 * 1-限制；2-不限制
	 */
	private Integer astrict;
	/**
	 * 满xx使用
	 */
	private Double leastCost;
	/**
	 * 金额
	 */
	private Integer reduceCost;
	/**
	 * 折扣
	 */
	private Double discount;
	
	private String title;
	
	private String description;
	/**
	 * 使用说明
	 */
	private String instruction;
	/**
	 * 生效时间
	 */
	private String effectiveBeginTime;
	/**
	 * 失效时间
	 */
	private String effectiveEndTime;
	
	private String state;
	/**
	 * list-item userd-过期或已使用；list-item-未使用
	 */
	private String classStyle;
}
