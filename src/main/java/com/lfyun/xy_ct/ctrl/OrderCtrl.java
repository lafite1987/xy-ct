package com.lfyun.xy_ct.ctrl;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lfyun.xy_ct.common.DataWrapper;
import com.lfyun.xy_ct.common.QueryDTO;
import com.lfyun.xy_ct.common.Result;
import com.lfyun.xy_ct.common.User;
import com.lfyun.xy_ct.common.enums.PayStatusEnums;
import com.lfyun.xy_ct.configure.wx.ProjectUrlConfig;
import com.lfyun.xy_ct.entity.OrderEntity;
import com.lfyun.xy_ct.entity.UserEntity;
import com.lfyun.xy_ct.service.OrderService;
import com.lfyun.xy_ct.service.ProductShareUserService;
import com.lfyun.xy_ct.service.SessionManager;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpService;

@Controller
public class OrderCtrl {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderCtrl.class);
	
	@Autowired
	private SessionManager sessionManager;
	
	@Autowired
	private ProjectUrlConfig projectUrlConfig;
	
	@Autowired
    private WxMpService wxMpService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ProductShareUserService productShareUserService;
	
	@RequestMapping(value = "/order/create", method = RequestMethod.GET)
	public String create(Long productId, HttpServletRequest request, Model model) {
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
		orderEntity.setUserId(user.getId());
		orderEntity.setPayStatus(PayStatusEnums.NEW.getCode());
		orderService.insert(orderEntity);
		
		model.addAttribute("orderId", orderEntity.getId());
		return "redirect:/order-detail.htm?orderId=" + orderEntity.getId();
	}
	
	@RequestMapping(value = "recharge.htm", method = RequestMethod.GET)
	public String recharge(@RequestParam(name = "from", defaultValue = "", required = false)String from, 
			@RequestParam(name = "productId", defaultValue = "", required = false)Long productId, Model model, HttpServletRequest request) {
		User user = sessionManager.getUser(request);
		if(user == null) {
			//1.配置微信公众号信息
			String returnUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/wxp/recharge.htm?from=" + from + "&productId=" + productId;
	        String url = projectUrlConfig.getMpAuthorizeUrl()+"/wxp/wechat/userInfo";
	        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAUTH2_SCOPE_USER_INFO, URLEncoder.encode(returnUrl));
			return "redirect:" + redirectUrl;
		}
		if(StringUtils.isNotBlank(from)) {
			try {
				Long parentUserId = Long.parseLong(from);
				productShareUserService.createRelation(productId, parentUserId, user.getId());
			} catch (Exception e) {
				LOGGER.error("更新用户推荐人错误:userId:{} from:{}", user.getId(), from, e);
			}
		}
		model.addAttribute("productId", productId);
		return "recharge";
	}
	
	@RequestMapping(value = "order-detail.htm", method = RequestMethod.GET)
	public String detail(Long orderId, Model model) {
		OrderEntity orderEntity = orderService.selectById(orderId);
		model.addAttribute("orderId", orderEntity.getId());
		return "order-detail";
	}
	
	@RequestMapping(value = "/order/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Result<OrderEntity> getDetail(@PathVariable Long id) {
		OrderEntity orderEntity = orderService.selectById(id);
		Result<OrderEntity> result = Result.<OrderEntity>success().setData(orderEntity);
		return result;
	}
	
	@RequestMapping(value = "/order/update/{id}", method = RequestMethod.GET)
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
	
	@RequestMapping(value = "/order/list.json", method = RequestMethod.POST)
	@ResponseBody
	public Result<DataWrapper<OrderEntity>> list(@RequestBody QueryDTO<OrderEntity> query) {
		OrderEntity entity = query.getQeury();
		EntityWrapper<OrderEntity> wrapper = new EntityWrapper<OrderEntity>(entity);
		Page<OrderEntity> page = orderService.selectPage(query.toPage(), wrapper);
		DataWrapper<OrderEntity> dataWrapper = new DataWrapper<>();
		dataWrapper.setList(page.getRecords());
		dataWrapper.setPage(new DataWrapper.Page(page.getCurrent(), page.getSize(), page.getTotal()));
		Result<DataWrapper<OrderEntity>> result = Result.success();
		result.setData(dataWrapper);
		return result;
	}
}
