package com.lfyun.xy_ct.ctrl.sys;

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
import com.lfyun.xy_ct.common.enums.PayStatusEnums;
import com.lfyun.xy_ct.entity.OrderEntity;
import com.lfyun.xy_ct.service.OrderService;

@Controller
@RequestMapping("/sys/order")
public class SysOrderCtrl {

	@Autowired
	private OrderService orderService;
	
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
	
	@RequestMapping(value = "/list.json", method = RequestMethod.POST)
	@ResponseBody
	public Result<DataWrapper<OrderEntity>> list(@RequestBody QueryDTO<OrderEntity> query) {
		OrderEntity entity = query.getQuery();
		Page<OrderEntity> page2 = query.toPage();
		page2.setOrderByField("createTime");
		page2.setAsc(false);
		EntityWrapper<OrderEntity> wrapper = new EntityWrapper<OrderEntity>(entity);
		if(entity.getStartTime() != null) {
			wrapper.ge("createTime", entity.getStartTime());
		}
		if(entity.getEndTime() != null) {
			wrapper.le("createTime", entity.getEndTime() + 86399999);
		}
		Page<OrderEntity> page = orderService.selectPage(page2, wrapper);
		DataWrapper<OrderEntity> dataWrapper = new DataWrapper<>();
		dataWrapper.setList(page.getRecords());
		dataWrapper.setPage(new DataWrapper.Page(page.getCurrent(), page.getSize(), page.getTotal()));
		Result<DataWrapper<OrderEntity>> result = Result.success();
		result.setData(dataWrapper);
		return result;
	}
	
}
