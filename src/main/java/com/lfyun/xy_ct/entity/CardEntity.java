package com.lfyun.xy_ct.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;

@Data
@TableName("t_card")
public class CardEntity {

	@TableId(value="id",type=IdType.AUTO)
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
	 * 库存
	 */
	private Integer quantity;
	/**
	 * 已发放数量
	 */
	private Integer issuedCount;
	/**
	 * 限制数量：每个用户限制领取数量，默认1
	 */
	private Integer perUserLimitCount;
	/**
	 * 开始发放时间
	 */
	private Long beginIssuedTime;
	/**
	 * 结束发放时间
	 */
	private Long endIssuedTime;
	/**
	 * 生效时间
	 */
	private Long effectiveBeginTime;
	/**
	 * 失效时间
	 */
	private Long effectiveEndTime;
	/**
	 * 1-正常；2-暂停；3-停止
	 */
	private Integer state;
	
	@TableField(fill = FieldFill.INSERT, value="createTime")
	private Long createTime;
	
	@TableField(fill = FieldFill.INSERT_UPDATE, value="updateTime")
	private Long updateTime;
	
	@TableLogic
    private Integer deleted;
	
}
