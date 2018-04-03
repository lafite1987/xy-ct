package com.lfyun.xy_ct.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lfyun.xy_ct.entity.ProductUserRelationEntity;
import com.lfyun.xy_ct.mapper.ProductUserRelationMapper;
import com.lfyun.xy_ct.service.ProductUserRelationService;

@Service
public class ProductUserRelationServiceImpl extends ServiceImpl<ProductUserRelationMapper,ProductUserRelationEntity> implements ProductUserRelationService {

}
