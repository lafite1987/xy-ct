package com.lfyun.xy_ct.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderDTO {
    /**订单id*/
    private String orderId;
    /**订单描述*/
    private String orderDesc;
    /**买家姓名*/
    private String buyerName;

    /**买家手机号码*/
    private String buyerPhone;

    /**买家送货地址*/
    private String buyerAddress;

    /**买家微信openid*/
    private String buyerOpenid;

    /**订单总额*/
    private BigDecimal orderAmount;

    /**订单状态*/
    private Integer orderStatus;

    /**支付状态*/
    private Integer payStatus;

    /**创建时间*/
    private Long createTime;

    /**更新时间*/
    private Long updateTime;

}
