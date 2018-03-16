package com.lfyun.xy_ct.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lfyun.xy_ct.common.Result;
import com.lfyun.xy_ct.common.enums.ExceptionCodeEnums;
import com.lfyun.xy_ct.common.enums.PayStatusEnums;
import com.lfyun.xy_ct.dto.OrderDTO;
import com.lfyun.xy_ct.exception.AppException;
import com.lfyun.xy_ct.service.OrderService;
import com.lfyun.xy_ct.service.PayService;
import com.lly835.bestpay.model.PayResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * 微信支付
 * Created by Administrator on 2017/10/19 0019.
 */
@Controller
@Slf4j
@RequestMapping("/pay")
public class PayController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private PayService payService;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    @ResponseBody
    public Object create(@RequestParam("orderId") String orderId
            , @RequestParam( value = "returnUrl", required = false) String returnUrl , Map<String , Object> map) {
        //1查找订单
        OrderDTO orderDTO = orderService.getByOrderId(orderId);
        if(orderDTO == null) {
            log.error("【微信下单】订单不存在，orderId={}" , orderId);
            throw new AppException(ExceptionCodeEnums.ORDER_NOT_FOUND);
        }
        if(orderDTO.getPayStatus() == PayStatusEnums.FINISH.getCode()) {
        	throw new AppException(ExceptionCodeEnums.ORDER_PAY_NOT_FOUND);
        }
        //2发起支付
        PayResponse payResponse = payService.create(orderDTO);

        //3.存储预支付信息
        map.put("payResponse" , payResponse);
        map.put("returnUrl" , returnUrl);
        Result<PayResponse> result = Result.success();
        result.setData(payResponse);
        //3.唤起支付
//        return new ModelAndView("pay/create" , map);
        return result;
    }

    /**
     * 微信异步通知
     * @param notifyData
     */
    @PostMapping("/notify")
    public ModelAndView notify(@RequestBody String notifyData) {
        payService.notify(notifyData);
        //返回给微信处理结果
        return new ModelAndView("pay/success");
    }
}
