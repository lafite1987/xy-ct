package com.lfyun.xy_ct.ctrl.sys;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.lfyun.xy_ct.entity.ProductCardEntity;
import com.lfyun.xy_ct.entity.ProductEntity;
import com.lfyun.xy_ct.service.ProductCardService;
import com.lfyun.xy_ct.service.ProductService;

import jersey.repackaged.com.google.common.collect.MapDifference;
import jersey.repackaged.com.google.common.collect.MapDifference.ValueDifference;
import jersey.repackaged.com.google.common.collect.Maps;

@Controller
@RequestMapping("/sys/product")
public class SysProductCtrl {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductCardService productCardService;
	
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
		boolean ret = productService.insert(form);
		if(ret) {
			Map<Long, Integer> cardIdAndCounts = form.getCardIdAndCounts();
			if(cardIdAndCounts != null && !cardIdAndCounts.isEmpty()) {
				for(Entry<Long, Integer> entry : cardIdAndCounts.entrySet()) {
					ProductCardEntity productCardEntity = new ProductCardEntity();
					productCardEntity.setProductId(form.getId());
					productCardEntity.setCardId(entry.getKey());
					productCardEntity.setCount(entry.getValue());
					productCardService.insert(productCardEntity);
				}
			}
		}
		return result;
	}
	
	@RequestMapping(value = "/{id}/update.json", method = RequestMethod.POST)
	@ResponseBody
	public Result<Void> update(@RequestBody ProductEntity form) {
		Result<Void> result = Result.success();
		productService.updateById(form);
		ProductCardEntity productCardEntity = new ProductCardEntity();
		productCardEntity.setProductId(form.getId());
		EntityWrapper<ProductCardEntity> wrapper = new EntityWrapper<ProductCardEntity>(productCardEntity);
		List<ProductCardEntity> selectList = productCardService.selectList(wrapper);
		Map<Long, Integer> cardIdAndCounts = Maps.newHashMap();
		for(ProductCardEntity productCardEntity2 : selectList) {
			cardIdAndCounts.put(productCardEntity2.getCardId(), productCardEntity2.getCount());
		}
		MapDifference<Long, Integer> mapDifference = Maps.difference(form.getCardIdAndCounts(), cardIdAndCounts);
		
		//只在左边有（新增）
		Map<Long, Integer> entriesOnlyOnLeft = mapDifference.entriesOnlyOnLeft();
		//只在右边有（删除）
		Map<Long, Integer> entriesOnlyOnRight = mapDifference.entriesOnlyOnRight();
		//key相同，但是value不同（更新值）
		Map<Long, ValueDifference<Integer>> entriesDiffering = mapDifference.entriesDiffering();
		for(Entry<Long, Integer> entry : entriesOnlyOnLeft.entrySet()) {
			ProductCardEntity tmp = new ProductCardEntity();
			tmp.setProductId(form.getId());
			tmp.setCardId(entry.getKey());
			tmp.setCount(entry.getValue());
			productCardService.insert(tmp);
		}
		for(Entry<Long, Integer> entry : entriesOnlyOnRight.entrySet()) {
			ProductCardEntity delProductCardEntity = new ProductCardEntity();
			delProductCardEntity.setProductId(form.getId());
			delProductCardEntity.setCardId(entry.getKey());
			EntityWrapper<ProductCardEntity> wrapper2 = new EntityWrapper<ProductCardEntity>(delProductCardEntity);
			productCardService.delete(wrapper2);
		}
		for(Entry<Long, ValueDifference<Integer>> entry : entriesDiffering.entrySet()) {
			Integer count = form.getCardIdAndCounts().get(entry.getKey());
			ProductCardEntity updateProductCardEntity = new ProductCardEntity();
			updateProductCardEntity.setProductId(form.getId());
			updateProductCardEntity.setCardId(entry.getKey());
			
			ProductCardEntity newProductCardEntity = new ProductCardEntity();
			newProductCardEntity.setCount(count);
			EntityWrapper<ProductCardEntity> wrapper2 = new EntityWrapper<ProductCardEntity>(updateProductCardEntity);
			productCardService.update(newProductCardEntity, wrapper2);
		}
		return result;
	}
	
	@RequestMapping(value = "/{id}/detail.json", method = RequestMethod.GET)
	@ResponseBody
	public Result<ProductEntity> detail(@PathVariable Long id) {
		Result<ProductEntity> result = Result.success();
		ProductEntity entity = productService.selectById(id);
		if(entity != null) {
			ProductCardEntity productCardEntity = new ProductCardEntity();
			productCardEntity.setProductId(id);
			EntityWrapper<ProductCardEntity> wrapper = new EntityWrapper<ProductCardEntity>(productCardEntity);
			List<ProductCardEntity> selectList = productCardService.selectList(wrapper);
			Map<Long, Integer> cardIdAndCounts = Maps.newHashMap();
			for(ProductCardEntity productCardEntity2 : selectList) {
				cardIdAndCounts.put(productCardEntity2.getCardId(), productCardEntity2.getCount());
			}
			entity.setCardIdAndCounts(cardIdAndCounts);
		}
		result.setData(entity);
		return result;
	}
}
