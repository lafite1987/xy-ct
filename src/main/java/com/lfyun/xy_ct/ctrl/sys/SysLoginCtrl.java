package com.lfyun.xy_ct.ctrl.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lfyun.xy_ct.common.Result;
import com.lfyun.xy_ct.common.enums.ExceptionCodeEnums;
import com.lfyun.xy_ct.common.util.JwtToken;
import com.lfyun.xy_ct.common.util.MessageDigestUtil;
import com.lfyun.xy_ct.dto.AccountInfo;
import com.lfyun.xy_ct.entity.AccountEntity;
import com.lfyun.xy_ct.exception.AppException;
import com.lfyun.xy_ct.service.AccountService;

@Controller
public class SysLoginCtrl {

	@Autowired
	private AccountService accountService;
	
	@RequestMapping(value = "/login.json", method = RequestMethod.POST)
	@ResponseBody
	public Result<AccountInfo> list(@RequestBody AccountInfo account) {
		Result<AccountInfo> result = Result.success();
		AccountEntity entity = new AccountEntity();
		entity.setUsername(account.getUsername());
		EntityWrapper<AccountEntity> wrapper = new EntityWrapper<AccountEntity>(entity);
		AccountEntity accountEntity = accountService.selectOne(wrapper);
		String passwordSha = MessageDigestUtil.getSHA256(account.getPassword());
		if(accountEntity == null || !passwordSha.equals(accountEntity.getPassword())) {
			throw new AppException(ExceptionCodeEnums.USERNAME_OR_PASSWORD_ERROR);
		}
		AccountInfo accountInfo = new AccountInfo();
		accountInfo.setId(accountEntity.getId());
		accountInfo.setUsername(accountEntity.getUsername());
		String token = JwtToken.createToken(accountEntity.getId());
		accountInfo.setToken(token);
		result.setData(accountInfo);
		return result;
	}
}
