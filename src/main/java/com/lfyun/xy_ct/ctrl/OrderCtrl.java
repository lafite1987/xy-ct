package com.lfyun.xy_ct.ctrl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lfyun.xy_ct.common.Result;
import com.lfyun.xy_ct.dto.OrderDTO;

@Controller
@RequestMapping("/order")
public class OrderCtrl {

	public Result<OrderDTO> create(Long productId, String sessionid) {
		return null;
	}
}
