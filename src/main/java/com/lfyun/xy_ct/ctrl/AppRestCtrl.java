package com.lfyun.xy_ct.ctrl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lfyun.xy_ct.common.DataWrapper;
import com.lfyun.xy_ct.common.Result;
import com.lfyun.xy_ct.common.enums.ResultCodeEnums;
import com.lfyun.xy_ct.common.util.JwtToken;
import com.lfyun.xy_ct.dto.UserBalanceDetailDTO;
import com.lfyun.xy_ct.dto.UserToken;
import com.lfyun.xy_ct.entity.UserBalanceDetailEntity;
import com.lfyun.xy_ct.entity.UserEntity;
import com.lfyun.xy_ct.service.AccountService;
import com.lfyun.xy_ct.service.UserBalanceDetailService;
import com.lfyun.xy_ct.service.UserService;

import jersey.repackaged.com.google.common.collect.Lists;

@Controller
@RequestMapping("/app")
public class AppRestCtrl {

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserBalanceDetailService userBalanceDetailService;
	
	public Long getAccountId(HttpServletRequest request) {
		String token = request.getParameter("token");
		if(StringUtils.isBlank(token)) {
			token = request.getHeader("Authorization");
		}
		if(StringUtils.isBlank(token)) {
			Cookie[] cookies = request.getCookies();
			if(cookies != null) {
				for(Cookie cookie : cookies) {
					if("Authorization".equals(cookie.getName())) {
						token = cookie.getValue();
						break;
					}
				}
			}
		}
		if(StringUtils.isBlank(token)) {
			return 0L;
		}
		Long userId = JwtToken.verify(token);
		return userId;
	}
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public Result<UserToken> login(String username, String password) {
		UserToken userToken = accountService.login(username, password);
		if(userToken == null) {
			Result<UserToken> result = Result.fail(ResultCodeEnums.USER_OR_PASSWORD_ERROR.getCode());
			return result;
		}
		Result<UserToken> result = Result.success();
		result.setData(userToken);
		return result;
	}
	
	@RequestMapping(value = "/consumeList", method = RequestMethod.POST)
	@ResponseBody
	public Result<DataWrapper<UserBalanceDetailDTO>> consumeList(@RequestParam(name = "currentPage", required = false, defaultValue = "1")Integer currentPage, 
			@RequestParam(name = "pageSize", required = false, defaultValue = "1")Integer pageSize, HttpServletRequest request) {
		Long accountId = getAccountId(request);
		if(accountId == 0) {
			Result<DataWrapper<UserBalanceDetailDTO>> result = Result.fail(ResultCodeEnums.NO_LOGIN.getCode());
			return result;
		}
		Result<DataWrapper<UserBalanceDetailDTO>> result = Result.success();
		Page<UserBalanceDetailEntity> page = new Page<>(currentPage, pageSize);
		UserBalanceDetailEntity entity = new UserBalanceDetailEntity();
		EntityWrapper<UserBalanceDetailEntity> wrapper = new EntityWrapper<UserBalanceDetailEntity>(entity);
		Page<UserBalanceDetailEntity> pageTemp = userBalanceDetailService.selectPage(page, wrapper);
		List<Long> userIdList = Lists.newArrayList();
		for(UserBalanceDetailEntity ubde : pageTemp.getRecords()) {
			userIdList.add(ubde.getUserId());
		}
		Map<Long, UserEntity> map = userService.getByIds(userIdList);
		List<UserBalanceDetailDTO> list = Lists.newArrayList();
		for(UserBalanceDetailEntity ubde : pageTemp.getRecords()) {
			UserBalanceDetailDTO userBalanceDetailDTO = new UserBalanceDetailDTO();
			BeanUtils.copyProperties(ubde, userBalanceDetailDTO);
			UserEntity userEntity = map.get(ubde.getUserId());
			if(userEntity != null) {
				userBalanceDetailDTO.setNickname(userEntity.getNickname());
			}
			list.add(userBalanceDetailDTO);
		}
		DataWrapper.Page p = new DataWrapper.Page(pageTemp.getCurrent(), pageTemp.getSize(), pageTemp.getTotal());
		DataWrapper<UserBalanceDetailDTO> dataWrapper = new DataWrapper<>();
		dataWrapper.setList(list);
		dataWrapper.setPage(p);
		result.setData(dataWrapper);
		return result;
	}
	
	/**
	 * 核销接口
	 * @return
	 */
	@RequestMapping(value = "/chargeOff")
	@ResponseBody
	public Result<Object> chargeOff(@RequestParam(name = "accountId", required = false, defaultValue = "0")String accountId,
			@RequestParam(name = "amount", required = false, defaultValue = "0")Double amount,
			@RequestParam(name = "data", required = false, defaultValue = "")String data, HttpServletRequest request) {
		Long currentAccountId = getAccountId(request);
		if(currentAccountId == 0) {
			return Result.fail(ResultCodeEnums.NO_LOGIN.getCode());
		}
		userBalanceDetailService.chargeOff(currentAccountId, amount, data);
		Result<Object> result = Result.success();
		return result;
	}
	
	/**
	 * 核销接口
	 * @return
	 */
	@RequestMapping(value = "/refund")
	@ResponseBody
	public Result<Object> refund(@RequestParam(name = "accountId", required = false, defaultValue = "0")String accountId,
			@RequestParam(name = "amount", required = false, defaultValue = "0")Double amount,
			@RequestParam(name = "data", required = false, defaultValue = "")String data, HttpServletRequest request) {
		Long currentAccountId = getAccountId(request);
		if(currentAccountId == 0) {
			return Result.fail(ResultCodeEnums.NO_LOGIN.getCode());
		}
		userBalanceDetailService.refund(currentAccountId, amount, data);
		Result<Object> result = Result.success();
		return result;
	}
	
	
}
