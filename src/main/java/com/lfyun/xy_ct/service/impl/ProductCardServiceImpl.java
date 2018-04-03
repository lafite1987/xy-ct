package com.lfyun.xy_ct.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lfyun.xy_ct.entity.ProductCardEntity;
import com.lfyun.xy_ct.mapper.ProductCardMapper;
import com.lfyun.xy_ct.service.ProductCardService;

@Service
public class ProductCardServiceImpl extends ServiceImpl<ProductCardMapper,ProductCardEntity> implements ProductCardService {

}
