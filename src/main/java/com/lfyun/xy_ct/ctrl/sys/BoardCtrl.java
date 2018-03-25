package com.lfyun.xy_ct.ctrl.sys;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.lfyun.xy_ct.common.Result;
import com.lfyun.xy_ct.service.BoardService;
import com.lfyun.xy_ct.service.BoardService.EarningInfo;
import com.lfyun.xy_ct.service.BoardService.RechargeInfo;

import jersey.repackaged.com.google.common.collect.Maps;

@Controller
@RequestMapping("/board")
public class BoardCtrl {

	@Autowired
	private BoardService boardService;
	
	@RequestMapping(value = "/data.json", method = RequestMethod.GET)
	@ResponseBody
	public Result<Map<String, Object>> board() {
		Result<Map<String, Object>> result = Result.success();
		RechargeInfo rechargeInfo = boardService.getRechargeInfo();
		EarningInfo earningInfo = boardService.getEarningInfo();
		Map<String, Object> data = Maps.newHashMap();
		data.put("rechargeList", Lists.newArrayList(rechargeInfo));
		data.put("earningList", Lists.newArrayList(earningInfo));
		result.setData(data);
		return result;
	}
}
