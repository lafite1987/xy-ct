package com.lfyun.xy_ct.service;

import com.baomidou.mybatisplus.service.IService;
import com.lfyun.xy_ct.entity.ProductShareUserEntity;

public interface ProductShareUserService extends IService<ProductShareUserEntity> {

	void createRelation(Long productId, Long parentUserId, Long userId);
}
