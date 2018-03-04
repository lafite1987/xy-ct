package com.lfyun.xy_ct.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;

@Data
@TableName("t_product_share_user")
public class ProductShareUserEntity {

	/**
	 * Id
	 */
	@TableId(value="id",type=IdType.AUTO)
	private Long id;
	/**
	 * 商品Id
	 */
	private Long productId;
	/**
	 * 父用户Id
	 */
	private Long parentUserId;
	/**
	 * 用户Id
	 */
	private Long userId;
	/**
	 * 1-一级；2-二级；3-三级；
	 */
	private Integer level;
	
	@TableField(fill = FieldFill.INSERT, value="createTime", strategy = FieldStrategy.IGNORED)
	private Long createTime;
	
	@TableField(fill = FieldFill.INSERT_UPDATE, value="updateTime", strategy = FieldStrategy.IGNORED)
	private Long updateTime;
	/**
	 * 1-已经删除；0-未删除
	 */
	@TableLogic
    private Integer deleted;
}
