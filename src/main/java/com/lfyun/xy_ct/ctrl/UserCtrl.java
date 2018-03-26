package com.lfyun.xy_ct.ctrl;

import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.lfyun.xy_ct.common.Result;
import com.lfyun.xy_ct.common.User;
import com.lfyun.xy_ct.common.util.DateUtil;
import com.lfyun.xy_ct.common.util.JwtToken;
import com.lfyun.xy_ct.common.util.QRCodes;
import com.lfyun.xy_ct.configure.wx.ProjectUrlConfig;
import com.lfyun.xy_ct.dto.UserCardDTO;
import com.lfyun.xy_ct.dto.UserShareDTO;
import com.lfyun.xy_ct.entity.CardEntity;
import com.lfyun.xy_ct.entity.UserCardEntity;
import com.lfyun.xy_ct.entity.UserEntity;
import com.lfyun.xy_ct.service.CardService;
import com.lfyun.xy_ct.service.SessionManager;
import com.lfyun.xy_ct.service.UserCardService;
import com.lfyun.xy_ct.service.UserService;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpService;

@Controller
@RequestMapping("/user")
public class UserCtrl {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserCtrl.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SessionManager sessionManager;
	
	@Autowired
    private WxMpService wxMpService;
	
	@Autowired
	private ProjectUrlConfig projectUrlConfig;
	
	@Autowired
	private UserCardService userCardService;
	
	@Autowired
	private CardService cardService;
	
	@RequestMapping(value = "/withdraw", method = RequestMethod.POST)
	@ResponseBody
	public Result<Void> withdraw(HttpServletRequest request) {
		User user = sessionManager.getUser(request);
		Result<Void> result = Result.success();
		if(user != null) {
			userService.withdraw(user.getId());
		}
		return result;
	}
	
	@RequestMapping(value = "/myInviteList.htm", method = RequestMethod.GET)
	public String inventlist(String productId, Model model, HttpServletRequest request) {
		User user = sessionManager.getUser(request);
		if(user == null) {
			//1.配置微信公众号信息
			String returnUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/wxp/user/myInviteList.htm?productId=" + productId;
	        String url = projectUrlConfig.getMpAuthorizeUrl()+"/wxp/wechat/userInfo";
	        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAUTH2_SCOPE_USER_INFO, URLEncoder.encode(returnUrl));
			return "redirect:" + redirectUrl;
		}
		UserEntity userEntity = userService.selectById(user.getId());
		model.addAttribute("productId", productId);
		model.addAttribute("earning", userEntity.getEarning());
		model.addAttribute("totalEarning", userEntity.getTotalEarning());
		return "inventlist";
	}
	
	@RequestMapping(value = "/myPayQrCode", method = RequestMethod.GET)
	@ResponseBody
	public Result<String> myPayQrCode(Model model, HttpServletRequest request) {
		Result<String> result = Result.success();
		User user = sessionManager.getUser(request);
		String createUserQCodeData = JwtToken.createUserQCodeData(user.getId());
		String qrCode = QRCodes.createQRCode(createUserQCodeData, 200, "1");
		result.setData(qrCode);
		return result;
	}
	
	@RequestMapping(value = "/myShareQrCode", method = RequestMethod.GET)
	@ResponseBody
	public Result<String> myShareQrCode(String productId, HttpServletRequest request) {
		Result<String> result = Result.success();
		User user = sessionManager.getUser(request);
		String shareUrl = projectUrlConfig.getXyct() + "/wxp/recharge.htm?fromUserId=" + user.getId() + "&productId=" + productId;
		String qrCode = QRCodes.createQRCode(shareUrl, 200, "1");
		result.setData(qrCode);
		return result;
	}
	
	@RequestMapping(value = "/share", method = RequestMethod.GET)
	@ResponseBody
	public Result<UserShareDTO> share(Long productId, HttpServletRequest request) {
		User user = sessionManager.getUser(request);
		LOGGER.info("user={} share productId={}", user, productId);
		UserShareDTO userShareDTO = new UserShareDTO();
		userShareDTO.setTitle("充值活动");
		userShareDTO.setDescription("充值即送优惠券，邀请好友返现！");
		userShareDTO.setUserId(user.getId());
		userShareDTO.setImageUrl("");
		String link = projectUrlConfig.getXyct() + "/wxp/recharge.htm?fromUserId=" + user.getId() + "&productId=" + productId;
		userShareDTO.setLink(link);
		Result<UserShareDTO> result = Result.success();
		result.setData(userShareDTO);
		return result;
	}
	
	@RequestMapping(value = "/myCard.htm", method = RequestMethod.GET)
	public String myCard(Model model, HttpServletRequest request) {
		User user = sessionManager.getUser(request);
		if(user == null) {
			//1.配置微信公众号信息
			String returnUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/wxp/user/myCard.htm";
	        String url = projectUrlConfig.getMpAuthorizeUrl()+"/wxp/wechat/userInfo";
	        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAUTH2_SCOPE_USER_INFO, URLEncoder.encode(returnUrl));
			return "redirect:" + redirectUrl;
		}
		UserCardEntity entity = new UserCardEntity();
		entity.setUserId(user.getId());
		EntityWrapper<UserCardEntity> wrapper = new EntityWrapper<UserCardEntity>(entity);
		wrapper.orderBy("state", true);
		wrapper.orderBy("createTime", false);
		List<UserCardEntity> list = userCardService.selectList(wrapper);
		Set<Long> userCardIdSet = Sets.newHashSet();
		for(UserCardEntity userCardEntity : list) {
			userCardIdSet.add(userCardEntity.getCardId());
		}
		List<CardEntity> selectBatchIds = Lists.newArrayList();
		if(!userCardIdSet.isEmpty()) {
			selectBatchIds = cardService.selectBatchIds(Lists.newArrayList(userCardIdSet));
		}
		Map<Long, CardEntity> map = Maps.newHashMap();
		for(CardEntity cardEntity : selectBatchIds) {
			map.put(cardEntity.getId(), cardEntity);
		}
		List<UserCardDTO> userCardList = Lists.newArrayList();
		for(UserCardEntity userCardEntity : list) {
			CardEntity cardEntity = map.get(userCardEntity.getCardId());
			if(cardEntity != null) {
				String classStyle = "list-item";
				if(userCardEntity.getState() == 2) {
					classStyle = "list-item userd";
				}
				if(cardEntity.getEffectiveEndTime() < System.currentTimeMillis()) {
					classStyle = "list-item userd";
				}
				String state = userCardEntity.getState() == 1 ? "未使用" : "已使用";
				UserCardDTO userCardDTO = new UserCardDTO(userCardEntity.getId(), cardEntity.getCardType(), cardEntity.getAstrict(), 
						cardEntity.getLeastCost(), cardEntity.getReduceCost(), cardEntity.getDiscount(), cardEntity.getTitle(), 
						cardEntity.getDescription(), cardEntity.getInstruction(), DateUtil.formatDate(new Date(cardEntity.getEffectiveBeginTime())), DateUtil.formatDate(new Date(cardEntity.getEffectiveEndTime())), state, classStyle);
				userCardList.add(userCardDTO);
			}
		}
		model.addAttribute("userCardList", userCardList);
		return "myCard";
	}
}
