package com.lfyun.xy_ct.ctrl.sys;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lfyun.xy_ct.common.DataWrapper;
import com.lfyun.xy_ct.common.QueryDTO;
import com.lfyun.xy_ct.common.Result;
import com.lfyun.xy_ct.common.util.MessageDigestUtil;
import com.lfyun.xy_ct.entity.AccountEntity;
import com.lfyun.xy_ct.service.AccountService;

@Controller
@RequestMapping("/sys/account")
public class SysAccountCtrl {

	@Autowired
	private AccountService accountService;
	
	@RequestMapping(value = "/list.json", method = RequestMethod.POST)
	@ResponseBody
	public Result<DataWrapper<AccountEntity>> list(@RequestBody QueryDTO<AccountEntity> query) {
		AccountEntity entity = query.getQuery();
		Page<AccountEntity> page2 = query.toPage();
		page2.setOrderByField("createTime");
		page2.setAsc(false);
		EntityWrapper<AccountEntity> wrapper = new EntityWrapper<AccountEntity>(entity);
		Page<AccountEntity> page = accountService.selectPage(page2, wrapper);
		DataWrapper<AccountEntity> dataWrapper = new DataWrapper<>();
		dataWrapper.setList(page.getRecords());
		dataWrapper.setPage(new DataWrapper.Page(page.getCurrent(), page.getSize(), page.getTotal()));
		Result<DataWrapper<AccountEntity>> result = Result.success();
		result.setData(dataWrapper);
		return result;
	}
	
	@RequestMapping(value = "/add.json", method = RequestMethod.POST)
	@ResponseBody
	public Result<Void> add(@RequestBody AccountEntity form) {
		Result<Void> result = Result.success();
		if(StringUtils.isNotBlank(form.getPassword())) {
			String password = MessageDigestUtil.getSHA256(form.getPassword());
			form.setPassword(password);
		}
		accountService.insert(form);
		return result;
	}
	
	@RequestMapping(value = "/{id}/update.json", method = RequestMethod.POST)
	@ResponseBody
	public Result<Void> update(@RequestBody AccountEntity form) {
		Result<Void> result = Result.success();
		if(StringUtils.isNotBlank(form.getPassword())) {
			String password = MessageDigestUtil.getSHA256(form.getPassword());
			form.setPassword(password);
		}
		accountService.updateById(form);
		return result;
	}
	
	@RequestMapping(value = "/{id}/detail.json", method = RequestMethod.GET)
	@ResponseBody
	public Result<AccountEntity> detail(@PathVariable Long id) {
		Result<AccountEntity> result = Result.success();
		AccountEntity entity = accountService.selectById(id);
		entity.setPassword(null);
		result.setData(entity);
		return result;
	}
}
