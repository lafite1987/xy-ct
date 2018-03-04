package com.lfyun.xy_ct.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lfyun.xy_ct.entity.AccountEntity;
import com.lfyun.xy_ct.mapper.AccountMapper;
import com.lfyun.xy_ct.service.AccountService;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper,AccountEntity> implements AccountService {

}
