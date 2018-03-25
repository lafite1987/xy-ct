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
import com.lfyun.xy_ct.entity.ProductEntity;
import com.lfyun.xy_ct.service.ProductService;

@Controller
@RequestMapping("/sys/product")
public class SysProductCtrl {

	@Autowired
	private ProductService productService;
	
	@RequestMapping(value = "/list.json", method = RequestMethod.POST)
	@ResponseBody
	public Result<DataWrapper<ProductEntity>> list(@RequestBody QueryDTO<ProductEntity> query) {
		ProductEntity entity = query.getQuery();
		Page<ProductEntity> page2 = query.toPage();
		page2.setOrderByField("createTime");
		page2.setAsc(false);
		EntityWrapper<ProductEntity> wrapper = new EntityWrapper<ProductEntity>(entity);
		Page<ProductEntity> page = productService.selectPage(page2, wrapper);
		DataWrapper<ProductEntity> dataWrapper = new DataWrapper<>();
		dataWrapper.setList(page.getRecords());
		dataWrapper.setPage(new DataWrapper.Page(page.getCurrent(), page.getSize(), page.getTotal()));
		Result<DataWrapper<ProductEntity>> result = Result.success();
		result.setData(dataWrapper);
		return result;
	}
	
	@RequestMapping(value = "/add.json", method = RequestMethod.POST)
	@ResponseBody
	public Result<Void> add(@RequestBody ProductEntity form) {
		Result<Void> result = Result.success();
		productService.insert(form);
		return result;
	}
	
	@RequestMapping(value = "/{id}/update.json", method = RequestMethod.POST)
	@ResponseBody
	public Result<Void> update(@RequestBody ProductEntity form) {
		Result<Void> result = Result.success();
		productService.updateById(form);
		return result;
	}
	
	@RequestMapping(value = "/{id}/detail.json", method = RequestMethod.GET)
	@ResponseBody
	public Result<ProductEntity> detail(@PathVariable Long id) {
		Result<ProductEntity> result = Result.success();
		ProductEntity entity = productService.selectById(id);
		result.setData(entity);
		return result;
	}
}
