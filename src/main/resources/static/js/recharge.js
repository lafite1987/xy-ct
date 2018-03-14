function linkToRechargeList(){
        window.location.href = "user/rechargerecord.htm";
    }
    function linkToList(){
        window.location.href = "user/myInviteList.htm";
    }
    function linkToreCharge() {
    	var productId = $("#productId").val();
    	alert(productId);
        window.location.href = "order/create?productId=" + productId;
    }
    function showActiveLayer() {
        var visible = document.getElementById("activeLayer").style.display = 'block';
        console.log(visible);
    }
    function showMyQRCode() {
    	window.location.href = "user/myQRCode.htm";
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
function appendConsole (val) {
    $('#myConsole').append(val).append('<br />')
}
wx.ready(function(){

    // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，
    // config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。
    // 对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
    appendConsole("wx is ready")
    //获取 open id
    share();
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

function share() {
	var productId = $("#productId").val();
	$.ajax({
	      type: 'get',
	      url: '/wxp/share?productId=' + productId,
	      dataType: 'json',
	      success: function (data) {
	    	  wx.onMenuShareAppMessage({  
	    	        title : data.data.title, // 分享标题  
	    	        desc : data.data.description, // 分享描述  
	    	        link : data.data.link, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致  
	    	        imgUrl : data.data.imageUrl, // 分享图标  
	    	        type : '', // 分享类型,music、video或link，不填默认为link  
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
	    	    	title : data.data.title, // 分享标题  
	    	        link : data.data.link, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致  
	    	        imgUrl : data.data.imageUrl, // 分享图标  
	    	        success : function() {  
	    	            // 用户确认分享后执行的回调函数  
	    	        },  
	    	        cancel : function() {  
	    	            // 用户取消分享后执行的回调函数  
	    	        }  
	    	    }); // end onMenuShareTimeline  
	      },
	      complete: function () {
	          appendConsole("share is complete.")
	      }
	  })
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
              debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
              appId: param.appId, // 必填，公众号的唯一标识
              timestamp: param.timestamp, // 必填，生成签名的时间戳
              nonceStr: param.nonceStr, // 必填，生成签名的随机串
              signature: param.signature,// 必填，签名，见附录1
              jsApiList: ['onMenuShareAppMessage', 'onMenuShareTimeline'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
          });
      },
      complete: function () {
          appendConsole("getJsApiParam is complete.")
      }
  })
}

$(function() {
    // 1. get accesss token
    var url = location.href.split('#')[0]
    appendConsole(url);
    getJsApiParam(url);
})