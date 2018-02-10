package com.lfyun.xy_ct.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lfyun.xy_ct.entity.MovieEntity;
import com.lfyun.xy_ct.mapper.MovieMapper;
import com.lfyun.xy_ct.service.MovieService;

@Service
public class MovieServiceImpl extends ServiceImpl<MovieMapper,MovieEntity> implements MovieService {

}
