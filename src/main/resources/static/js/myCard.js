function useTicket(userCardId) {
	if (confirm("确定使用该优惠券吗？")) {
		$.ajax({
	          url : "/wxp/user/useCard",
	          type : "GET",
	          data : {userCardId : userCardId},
	          dataType : 'json',
	          success : function(data) {
	              if (data.code == 200) {
	            	  alert("卡券使用成功");
	              }
	          }
		});
	} else {
		
	}
}