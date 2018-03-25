package com.lfyun.xy_ct.service;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.lfyun.xy_ct.mapper.OrderMapper;
import com.lfyun.xy_ct.mapper.UserEarningMapper;

import lombok.Data;

@Service
public class BoardService {

	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private UserEarningMapper userEarningMapper;
	
	@Data
	public static class RechargeInfo {
		private double todayRecharge;
		private double monthRecharge;
		private double yearRecharge;
		private double totalRecharge;
	}
	private LoadingCache<String, RechargeInfo> rechargeCache = 
			CacheBuilder.newBuilder().initialCapacity(1)
			.expireAfterWrite(60, TimeUnit.SECONDS)
			.build(new CacheLoader<String, RechargeInfo>() {

				@Override
				public RechargeInfo load(String key) throws Exception {
					return loadRechargeInfo(key);
				}
	});
	
	private RechargeInfo loadRechargeInfo(String key) {
		RechargeInfo rechargeInfo = new RechargeInfo();
		Calendar startTime = Calendar.getInstance();
		startTime.set(Calendar.HOUR, 0);
		startTime.set(Calendar.MINUTE, 0);
		startTime.set(Calendar.SECOND, 0);
		startTime.set(Calendar.MILLISECOND, 0);
		
		Calendar endTime = Calendar.getInstance();
		endTime.set(Calendar.HOUR, 23);
		endTime.set(Calendar.MINUTE, 59);
		endTime.set(Calendar.SECOND, 59);
		endTime.set(Calendar.MILLISECOND, 999);
		
		Double todayRecharge = orderMapper.getAmountByTime(startTime.getTimeInMillis(), endTime.getTimeInMillis());
		rechargeInfo.setTodayRecharge(todayRecharge == null ? 0D : todayRecharge);
		
		//月总充值
		startTime.set(Calendar.DAY_OF_MONTH, 1);
		endTime.set(Calendar.DAY_OF_MONTH, startTime.getActualMaximum(Calendar.DAY_OF_MONTH));
		Double monthRecharge = orderMapper.getAmountByTime(startTime.getTimeInMillis(), endTime.getTimeInMillis());
		rechargeInfo.setMonthRecharge(monthRecharge == null ? 0D : monthRecharge);
		
		//今年总充值
		startTime.set(Calendar.MONTH, Calendar.JANUARY);
		endTime.set(Calendar.MONTH, Calendar.DECEMBER);
		endTime.set(Calendar.DAY_OF_MONTH, startTime.getActualMaximum(Calendar.DAY_OF_MONTH));
		Double yearRecharge = orderMapper.getAmountByTime(startTime.getTimeInMillis(), endTime.getTimeInMillis());
		rechargeInfo.setYearRecharge(yearRecharge == null ? 0D : yearRecharge);
		
		//总充值
		Double totalRecharge = orderMapper.getTotalAmount();
		rechargeInfo.setTotalRecharge(totalRecharge == null ? 0D : totalRecharge);
		return rechargeInfo;
	}
	public RechargeInfo getRechargeInfo() {
		try {
			return rechargeCache.get("rechargeInfo");
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Data
	public static class EarningInfo {
		private double todayEarning;
		private double monthEarning;
		private double yearEarning;
		private double totalEarning;
	}
	private LoadingCache<String, EarningInfo> earningCache = 
			CacheBuilder.newBuilder().initialCapacity(1)
			.expireAfterWrite(60, TimeUnit.SECONDS)
			.build(new CacheLoader<String, EarningInfo>() {

				@Override
				public EarningInfo load(String key) throws Exception {
					return loadEarningInfo(key);
				}
	});
	
	private EarningInfo loadEarningInfo(String key) {
		EarningInfo earningInfo = new EarningInfo();
		Calendar startTime = Calendar.getInstance();
		startTime.set(Calendar.HOUR, 0);
		startTime.set(Calendar.MINUTE, 0);
		startTime.set(Calendar.SECOND, 0);
		startTime.set(Calendar.MILLISECOND, 0);
		
		Calendar endTime = Calendar.getInstance();
		endTime.set(Calendar.HOUR, 23);
		endTime.set(Calendar.MINUTE, 59);
		endTime.set(Calendar.SECOND, 59);
		endTime.set(Calendar.MILLISECOND, 999);
		
		//今日总充值
		Double todayEarning = userEarningMapper.getAmountByTime(startTime.getTimeInMillis(), endTime.getTimeInMillis());
		earningInfo.setTodayEarning(todayEarning == null ? 0D : todayEarning);
		
		//月总充值
		startTime.set(Calendar.DAY_OF_MONTH, 1);
		endTime.set(Calendar.DAY_OF_MONTH, startTime.getActualMaximum(Calendar.DAY_OF_MONTH));
		Double monthEarning = userEarningMapper.getAmountByTime(startTime.getTimeInMillis(), endTime.getTimeInMillis());
		earningInfo.setMonthEarning(monthEarning == null ? 0D : monthEarning);
		
		//今年总充值
		startTime.set(Calendar.MONTH, Calendar.JANUARY);
		endTime.set(Calendar.MONTH, Calendar.DECEMBER);
		endTime.set(Calendar.DAY_OF_MONTH, startTime.getActualMaximum(Calendar.DAY_OF_MONTH));
		Double yearEarning = userEarningMapper.getAmountByTime(startTime.getTimeInMillis(), endTime.getTimeInMillis());
		earningInfo.setYearEarning(yearEarning == null ? 0D : yearEarning);
		
		//总充值
		Double totalEarning = userEarningMapper.getTotalAmount();
		earningInfo.setTotalEarning(totalEarning == null ? 0D : totalEarning);
		return earningInfo;
	}
	public EarningInfo getEarningInfo() {
		try {
			return earningCache.get("eaningInfo");
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}
}
