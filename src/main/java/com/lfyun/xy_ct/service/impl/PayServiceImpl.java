package com.lfyun.xy_ct.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lfyun.xy_ct.common.enums.ExceptionCodeEnums;
import com.lfyun.xy_ct.common.enums.PayStatusEnums;
import com.lfyun.xy_ct.common.util.JsonUtils;
import com.lfyun.xy_ct.common.util.MathUtils;
import com.lfyun.xy_ct.dto.OrderDTO;
import com.lfyun.xy_ct.exception.AppException;
import com.lfyun.xy_ct.service.OrderService;
import com.lfyun.xy_ct.service.PayService;
import com.lfyun.xy_ct.service.WeiXinPayService;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundRequest;
import com.lly835.bestpay.model.RefundResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import com.lly835.bestpay.utils.MoneyUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by Administrator on 2017/10/19 0019.
 */
@Service
@Slf4j
public class PayServiceImpl implements PayService {

    @Autowired
    private BestPayServiceImpl bestPayService;

    @Autowired
    private WeiXinPayService weiXinPayService;
    
    @Autowired
    private OrderService orderService;

    @Override
    public PayResponse create(OrderDTO orderDTO) {
        //1.设置payRequest
        PayRequest payRequest = new PayRequest();
        //2.设置订单信息
        payRequest.setOpenid(orderDTO.getBuyerOpenid());
        payRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        payRequest.setOrderId(orderDTO.getOrderId());
        payRequest.setOrderName(orderDTO.getOrderDesc());
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);

        log.info("【发起支付】payRequest={}" , JsonUtils.toJson(payRequest));
        //3.预支付
        PayResponse payResponse =  bestPayService.pay(payRequest);

        log.info("【发起支付】payResponse={}" , JsonUtils.toJson(payResponse));
        //4.进行支付
        return payResponse;
    }

    @Override
    public Map<String, String> notify(String notifyData) {
    	log.info("WeixinPay notifyData:{}", notifyData);
        //1.处理异步通知
//        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        Map<String, String> responseMap = null;
        try {
        	responseMap = weiXinPayService.asyncNotify(notifyData);
        } catch (Exception e) {
			log.error("【异步通知】解析参数并验证签名时异常：notifyData=" + notifyData, e);
		}
        log.info("【异步通知】payResponse={}" , responseMap);

        if(responseMap == null || responseMap.isEmpty()) {
        	throw new AppException(ExceptionCodeEnums.PARAM_ERROR);
        }
        String out_trade_no = responseMap.get("out_trade_no");
        //2.获取订单
        OrderDTO orderDTO = orderService.getByOrderId(out_trade_no);
        if(null == orderDTO) {
            log.error("【异步通知】订单不存在，orderId={}" , out_trade_no);
            throw new AppException(ExceptionCodeEnums.ORDER_NOT_FOUND);
        }
        //订单已完成
        if(orderDTO.getPayStatus() == PayStatusEnums.FINISH.getCode()) {
        	return responseMap;
        }
        //3.判断订单有效性
        //3.1判断签名 best-sdk 已经处理好
        //3.2判断支付状态 best-sdk 已经处理好
        //3.3判断支付金额
        double orderAmount = MoneyUtil.Fen2Yuan(Integer.parseInt(responseMap.get("total_fee")));//订单金额单位为分，需要转为系统中的元
        if(!MathUtils.isEqual(orderDTO.getOrderAmount().doubleValue() , orderAmount)) {
            //支付金额不一致
            log.error("【异步通知】支付金额与原订单金额不一致，微信请求金额={} , 订单原金额={}，orderId={}"
                    , orderAmount , orderDTO.getOrderAmount() , out_trade_no);
            throw new AppException(ExceptionCodeEnums.PAY_MONEY_NOT_EQUAL);
        }

        //3.4判断支付人（下单人==支付人）

        //3.更改订单支付状态
        orderDTO.setOutTradeNo(responseMap.get("transaction_id"));
        orderDTO.setPayFinishTime(System.currentTimeMillis()/1000);
        orderService.pay(orderDTO);

        return responseMap;
    }

    @Override
    public RefundResponse refund(OrderDTO orderDTO) {
        //1.配置退款参数
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        refundRequest.setOrderId(orderDTO.getOrderId());
        refundRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);

        log.info("【微信退款】refundRequest={}" , JsonUtils.toJson(refundRequest));
        //2.执行微信退款
        RefundResponse refundResponse = bestPayService.refund(refundRequest);
        log.info("【微信退款】refundResponse={}" , JsonUtils.toJson(refundResponse));

        return refundResponse;
    }
}
