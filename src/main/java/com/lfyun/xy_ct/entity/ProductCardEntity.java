package com.lfyun.xy_ct.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;

import lombok.Data;

@Data
@TableName("t_product_card")
public class ProductCardEntity {

	@TableId(value="id")
	private Long id;
	
	private Long productId;
	/**
	 * 关联卡券
	 */
	private Long cardId;
	
	private Integer count;
	
	@TableField(fill = FieldFill.INSERT, value="createTime")
	private Long createTime;
	
	@TableField(fill = FieldFill.INSERT_UPDATE, value="updateTime")
	private Long updateTime;
	
	@TableLogic
    private Integer deleted;
}
