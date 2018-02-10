package com.lfyun.xy_ct.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * Created by Administrator on 2017/10/15 0015.
 */
@Data
public class OrderDTO {
    /**订单id*/
    private String orderId;

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
