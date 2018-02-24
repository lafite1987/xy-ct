package com.lfyun.xy_ct.ctrl;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lfyun.xy_ct.common.Result;
import com.lfyun.xy_ct.common.User;
import com.lfyun.xy_ct.common.enums.PayStatusEnums;
import com.lfyun.xy_ct.configure.wx.ProjectUrlConfig;
import com.lfyun.xy_ct.dto.OrderDTO;
import com.lfyun.xy_ct.entity.OrderEntity;
import com.lfyun.xy_ct.service.OrderService;
import com.lfyun.xy_ct.service.SessionManager;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpService;

@Controller
@RequestMapping("/order")
public class OrderCtrl {

	@Autowired
	private SessionManager sessionManager;
	
	@Autowired
	private ProjectUrlConfig projectUrlConfig;
	
	@Autowired
    private WxMpService wxMpService;
	
	@Autowired
	private OrderService orderService;
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(Long productId, HttpServletRequest request) {
		User user = sessionManager.getUser(request);
		if(user == null) {
			//1.配置微信公众号信息
			String returnUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/wxp/order/create?productId=" + productId;
	        String url = projectUrlConfig.getMpAuthorizeUrl()+"/wxp/wechat/userInfo";
	        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAUTH2_SCOPE_USER_INFO, URLEncoder.encode(returnUrl));
			return "redirect:" + redirectUrl;
		}
		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setProductId(productId);
		orderEntity.setProductName("充值398元");
		orderEntity.setAmount(0.01D);
		orderEntity.setOpenid(user.getOpenid());
		orderEntity.setPayStatus(PayStatusEnums.NEW.getCode());
		orderService.insert(orderEntity);
		
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setOrderId(String.valueOf(orderEntity.getId()));
		orderDTO.setBuyerOpenid(orderEntity.getOpenid());
		return "redirect:/order-detail.html?orderId=" + orderEntity.getId();
	}
	
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public String detail() {
		return "order-detail";
	}
	
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Result<OrderEntity> getDetail(@PathVariable Long id) {
		OrderEntity orderEntity = orderService.selectById(id);
		Result<OrderEntity> result = Result.<OrderEntity>success().setData(orderEntity);
		return result;
	}
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Result<OrderEntity> update(@PathVariable Long id) {
		Result<OrderEntity> result = Result.success();
		OrderEntity entity = new OrderEntity();
		entity.setId(id);
		entity.setPayStatus(PayStatusEnums.FINISH.getCode());
		orderService.updateById(entity);
		OrderEntity orderEntity = orderService.selectById(id);
		result.setData(orderEntity);
		return result;
	}
}
