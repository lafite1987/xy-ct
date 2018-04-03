package com.lfyun.xy_ct.ctrl.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.lfyun.xy_ct.common.DataWrapper;
import com.lfyun.xy_ct.common.QueryDTO;
import com.lfyun.xy_ct.common.Result;
import com.lfyun.xy_ct.dto.CardDTO;
import com.lfyun.xy_ct.entity.CardEntity;
import com.lfyun.xy_ct.service.CardService;

@Controller
@RequestMapping("/sys/card")
public class SysCardCtrl {

	@Autowired
	private CardService cardService;
	
	@RequestMapping(value = "/list.json", method = RequestMethod.POST)
	@ResponseBody
	public Result<DataWrapper<CardEntity>> list(@RequestBody QueryDTO<CardEntity> query) {
		CardEntity entity = query.getQuery();
		Page<CardEntity> page2 = query.toPage();
		page2.setOrderByField("createTime");
		page2.setAsc(false);
		EntityWrapper<CardEntity> wrapper = new EntityWrapper<CardEntity>(entity);
		Page<CardEntity> page = cardService.selectPage(page2, wrapper);
		DataWrapper<CardEntity> dataWrapper = new DataWrapper<>();
		dataWrapper.setList(page.getRecords());
		dataWrapper.setPage(new DataWrapper.Page(page.getCurrent(), page.getSize(), page.getTotal()));
		Result<DataWrapper<CardEntity>> result = Result.success();
		result.setData(dataWrapper);
		return result;
	}
	
	@RequestMapping(value = "/add.json", method = RequestMethod.POST)
	@ResponseBody
	public Result<Void> add(@RequestBody CardEntity form) {
		Result<Void> result = Result.success();
		cardService.insert(form);
		return result;
	}
	
	@RequestMapping(value = "/{id}/update.json", method = RequestMethod.POST)
	@ResponseBody
	public Result<Void> update(@RequestBody CardEntity form) {
		Result<Void> result = Result.success();
		cardService.updateById(form);
		return result;
	}
	
	@RequestMapping(value = "/{id}/detail.json", method = RequestMethod.GET)
	@ResponseBody
	public Result<CardEntity> detail(@PathVariable Long id) {
		Result<CardEntity> result = Result.success();
		CardEntity entity = cardService.selectById(id);
		result.setData(entity);
		return result;
	}
	
	@RequestMapping(value = "/{id}/del.json", method = RequestMethod.POST)
	@ResponseBody
	public Result<Void> del(@PathVariable Long id) {
		Result<Void> result = Result.success();
		cardService.deleteById(id);
		return result;
	}
	
	@RequestMapping(value = "/all.json", method = RequestMethod.GET)
	@ResponseBody
	public Result<List<CardDTO>> all() {
		CardEntity entity = new CardEntity();
		entity.setState(1);
		EntityWrapper<CardEntity> wrapper = new EntityWrapper<CardEntity>(entity);
		wrapper.orderBy("createTime", false);
		List<CardEntity> list = cardService.selectList(wrapper);
		List<CardDTO> arr = Lists.newArrayList();
		for(CardEntity card : list) {
			CardDTO cardDTO = new CardDTO();
			cardDTO.setId(String.valueOf(card.getId()));
			cardDTO.setTitle(card.getTitle());
			arr.add(cardDTO);
		}
		Result<List<CardDTO>> result = Result.success();
		result.setData(arr);
		return result;
	}
}
