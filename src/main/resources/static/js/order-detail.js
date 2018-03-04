function appendConsole (val) {
      $('#myConsole').append(val).append('<br />')
  }
  wx.ready(function(){

      // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，
      // config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。
      // 对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
      appendConsole("wx is ready")
      //获取 open id
      onBridgeReady();
  });

  wx.error(function (res) {
      // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
      appendConsole("wx.error: " + res.errMsg);
  });
  function getQueryString(name) {
	  var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	  var r = window.location.search.substr(1).match(reg);
	  if(r!=null)return  unescape(r[2]); return null;
  }
  
   function onBridgeReady() { 
	   var orderId = $("#orderId").val();
	   alert(orderId);
	   $.ajax({
	          url : "/wxp/pay/create",
	          type : "get",
	          async: true,
	          data : {orderId : orderId},
	          dataType : 'json',
	          success : function(data) {
	        	  alert("data:" + data);
	              if (data.code == 200) {
	            	  var appId = data.data.appId
	            	  var timeStamp = data.data.timeStamp;
	            	  var nonceStr = data.data.nonceStr;
	            	  var pg = data.data['package'];
	            	  var signType = data.data.signType;
	            	  var paySign = data.data.paySign;
	            	  alert(appId + ":" + pg);
	            	  wx.chooseWXPay({
	                      timestamp: timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
	                      nonceStr: nonceStr, // 支付签名随机串，不长于 32 位
	                      package: pg, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
	                      signType: signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
	                      paySign: paySign, // 支付签名
	                      success: function (res) {
	                          // 支付成功后的回调函数
	                          alert("response:" + res)
	                          alert("OK")
	                      },
	                      fail: function(res) {
	                          alert("fail:" + res);
	                      }
	                  }); 
	              } else {
	            	  alert(data.message);
	              }
	          },
	          error : function(data) {
	        	  alert("error:" + data);
	          }
	   });
    } 
     
function getJsApiParam(url) {
    //获取 open id
    $.ajax({
        type: 'post',
        url: '/wxp/jssdk_config',
        dataType: 'json',
        data: { 'url': url },
        success: function (param) {
            wx.config({
                debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                appId: param.appId, // 必填，公众号的唯一标识
                timestamp: param.timestamp, // 必填，生成签名的时间戳
                nonceStr: param.nonceStr, // 必填，生成签名的随机串
                signature: param.signature,// 必填，签名，见附录1
                jsApiList: ['chooseWXPay'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
            });
        },
        complete: function () {
            appendConsole("getJsApiParam is complete.")
        }
    })
}

function pay(){ 
	var orderId = $("#orderId").val();
	alert("order:" + orderId);
	getJsApiParam("http://api.mcwh123.com/wxp/order-detail.htm"); 
} 