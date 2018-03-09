package com.lfyun.xy_ct.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lfyun.xy_ct.entity.ProductShareUserEntity;

@Repository
public interface ProductShareUserMapper extends BaseMapper<ProductShareUserEntity> {

	/**
	 * 加收益
	 * @param id
	 * @param earning
	 * @return
	 */
	int addEarning(@Param("id")Long id, @Param("earning")Double earning);
}
