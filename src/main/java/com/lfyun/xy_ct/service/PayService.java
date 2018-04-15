package com.lfyun.xy_ct.service;

import java.util.Map;

import com.lfyun.xy_ct.dto.OrderDTO;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundResponse;

/**
 * Created by Administrator on 2017/10/19 0019.
 */
public interface PayService {
    /**
     * 下单
     * @param orderDTO
     * @return
     */
    PayResponse create(OrderDTO orderDTO);

    /**
     * 处理异步通知
     * @param notifyData
     */
    Map<String, String> notify(String notifyData);

    RefundResponse refund(OrderDTO orderDTO);
}
