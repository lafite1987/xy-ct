function linkToRechargeList(){
        window.location.href = "rechargerecord.html";
    }
    function linkToList(){
        window.location.href = "inventlist.html";
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
    wx.onMenuShareAppMessage({  
        title : '分享好友标题', // 分享标题  
        desc : '分享好友描述', // 分享描述  
        link : 'http://api.mcwh123.com/wxp/recharge.htm', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致  
        imgUrl : 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505419265109&di=cc30743d364e5ae89172c62a662e1321&imgtype=0&src=http%3A%2F%2Fpic.qqtn.com%2Fup%2F2017-6%2F14973136731543515.jpg', // 分享图标  
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
        title : '分享朋友圈标题', // 分享标题  
        link : "http://api.mcwh123.com/wxp/recharge.html", // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致  
        imgUrl : 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505419265109&di=cc30743d364e5ae89172c62a662e1321&imgtype=0&src=http%3A%2F%2Fpic.qqtn.com%2Fup%2F2017-6%2F14973136731543515.jpg', // 分享图标  
        success : function() {  
            // 用户确认分享后执行的回调函数  
        },  
        cancel : function() {  
            // 用户取消分享后执行的回调函数  
        }  
    }); // end onMenuShareTimeline  
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