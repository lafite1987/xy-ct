package com.lfyun.xy_ct.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lfyun.xy_ct.entity.ProductEntity;
import com.lfyun.xy_ct.mapper.ProductMapper;
import com.lfyun.xy_ct.service.ProductService;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper,ProductEntity> implements ProductService {

}
