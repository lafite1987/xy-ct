package com.lfyun.xy_ct.service;

import com.baomidou.mybatisplus.service.IService;
import com.lfyun.xy_ct.entity.UserEntity;

public interface UserService extends IService<UserEntity> {

	UserEntity getByOpenid(String openid);
}
