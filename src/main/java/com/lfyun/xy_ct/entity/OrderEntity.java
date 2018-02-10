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
@TableName("t_order")
public class OrderEntity {

	@TableId(value="id",type=IdType.AUTO)
    private Long id;
	
	
	@TableField(fill = FieldFill.INSERT, value="createTime", strategy = FieldStrategy.IGNORED)
	private Long createTime;
	
	@TableField(fill = FieldFill.INSERT_UPDATE, value="updateTime", strategy = FieldStrategy.IGNORED)
	private Long updateTime;
	@TableLogic
    private Integer deleted;
}
