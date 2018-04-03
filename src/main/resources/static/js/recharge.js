var title, desc, link, imgUrl;
window.onload = function () {
    document.getElementById('pop-con1').onclick = function (ev) {
        var oEvent = ev || event;
        oEvent.cancelBubble = true;
        oEvent.stopPropagation();
    }
    document.getElementById('pop-con2').onclick = function (ev) {
        var oEvent = ev || event;
        oEvent.cancelBubble = true;
        oEvent.stopPropagation();
    }
    document.getElementById('pop-con3').onclick = function (ev) {
        var oEvent = ev || event;
        oEvent.cancelBubble = true;
        oEvent.stopPropagation();
    }
}
function showLayer(id) {
    var visible = document.getElementById(id).style.display = 'block';
    console.log(visible);
}
function closeLayer(id) {
    var visible = document.getElementById(id).style.display = 'none';
    console.log(visible);
}
function linkToRechargeList() {
	window.location.href = "user/rechargerecord.htm";
}
function linkToList() {
	var productId = $("#productId").val();
	window.location.href = "user/myInviteList.htm?productId=" + productId;
}
function linkToreCharge() {
	var productId = $("#productId").val();
	window.location.href = "order/create?productId=" + productId;
}
function showActiveLayer() {
	var visible = document.getElementById("activeLayer").style.display = 'block';
	console.log(visible);
}
function myCard() {
	window.location.href = "user/myCard.htm";
}
function myPayQrCode(id) {
	var visible = document.getElementById(id).style.display = 'block';
	$.ajax({
		type : 'get',
		url : '/wxp/user/myPayQrCode',
		dataType : 'json',
		success : function(data) {
			$("#myPayQrCodeImg").attr("src", data.data);
		}
	});
}
function myShareQrCode(id) {
	var productId = $("#productId").val();
	var visible = document.getElementById(id).style.display = 'block';
	$.ajax({
		type : 'get',
		url : '/wxp/user/myShareQrCode',
		dataType : 'json',
		data: {productId: productId},
		success : function(data) {
			$("#myShareQrCodeImg").attr("src", data.data);
		}
	});
}

function closeActiveLayer() {
	var visible = document.getElementById("activeLayer").style.display = 'none';
	console.log(visible);
}
function showShareLayer() {
	var visible = document.getElementById("shareLayer").style.display = 'block';
	console.log(visible);
}
function closeShareLayer() {
	var visible = document.getElementById("shareLayer").style.display = 'none';
	console.log(visible);
}
function appendConsole(val) {
	$('#myConsole').append(val).append('<br />')
}

wx.error(function(res) {
	// config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
	appendConsole("wx.error: " + res.errMsg);
});
function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}

function getJsApiParam(url) {
}

$(function() {
	// 1. get accesss token
	var url = location.href.split('#')[0];
	appendConsole(url);
	var productId = $("#productId").val();
	$.ajax({
		type : 'get',
		url : '/wxp/user/share?productId=' + productId,
		dataType : 'json',
		async : false,
		success : function(data) {
			var title = data.data.title; // 分享标题
			var desc = data.data.description; // 分享描述
			var timelineDescription = data.data.timelineDescription;
			var link = data.data.link; // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
			var imgUrl = data.data.imageUrl;// 分享图标

			$.ajax({
				type : 'post',
				url : '/wxp/jssdk_config',
				dataType : 'json',
				data : {
					'url' : url
				},
				success : function(param) {
					wx.config({
						debug : false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
						appId : param.appId, // 必填，公众号的唯一标识
						timestamp : param.timestamp, // 必填，生成签名的时间戳
						nonceStr : param.nonceStr, // 必填，生成签名的随机串
						signature : param.signature,// 必填，签名，见附录1
						jsApiList : [ 'onMenuShareAppMessage',
								'onMenuShareTimeline', 'onMenuShareQQ' ]
					// 必填，需要使用的JS接口列表，所有JS接口列表见附录2
					});
					wx.ready(function() {

						// config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，
						// config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。
						// 对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
						appendConsole("wx is ready")
						wx.onMenuShareAppMessage({
							title : title, // 分享标题
							desc : desc, // 分享描述
							link : link, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
							imgUrl : imgUrl, // 分享图标
							type : 'link', // 分享类型,music、video或link，不填默认为link
							dataUrl : '', // 如果type是music或video，则要提供数据链接，默认为空
							success : function() {
								// 用户确认分享后执行的回调函数
								// alert('share successful');

							},
							cancel : function() {
								// 用户取消分享后执行的回调函数
								// alert('share cancel');
							}
						}); // end onMenuShareAppMessage

						wx.onMenuShareTimeline({
							title : title, // 分享标题
							desc: timelineDescription,
							link : link, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
							imgUrl : imgUrl, // 分享图标
							success : function() {
								// 用户确认分享后执行的回调函数
							},
							cancel : function() {
								// 用户取消分享后执行的回调函数
							}
						}); // end onMenuShareTimeline
						wx.onMenuShareQQ({
							title: title, // 分享标题
							desc: desc, // 分享描述
							link: link, // 分享链接
							imgUrl: '', // 分享图标
							success: function () {
							// 用户确认分享后执行的回调函数
							},
							cancel: function () {
							// 用户取消分享后执行的回调函数
							}
							});
					});
				},
				complete : function() {
					appendConsole("getJsApiParam is complete.")
				}
			})

		},
		complete : function() {
			appendConsole("share is complete.")
		}
	});

})