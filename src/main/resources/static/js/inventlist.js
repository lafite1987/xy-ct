function linkToRecord() {
	window.location.href = "withdrawrecord.htm";
}

var indexToPosition = [ "first", "second", "third" ];
var eleWidth = 0;
var maxnum = 0;
function doList(data, position, item) {
	var ele = document.getElementById(position + 'List');
	var isNeedJiantou = false;
	if (item instanceof Array && item.length != 0) {
		if (item.length > 4) {
			ele.className += ' img-more'
		} else {
			ele.className += ' img-little'
		}
		if (item.length > maxnum) {
			item = item.slice(0, maxnum - 1);
			isNeedJiantou = true
		}
		appendImgs(item, ele);
		if (isNeedJiantou) {
			addMoreBtn(data, position, ele);

		}
	}
}
function addMoreBtn(data, position, ele) {
	var moreEle = document.createElement("div");
	moreEle.className = 'more';
	moreEle.setAttribute('id', position + 'More');
	moreEle.onclick = function() {
		clickMore(data, position, ele)
	}

	ele.appendChild(moreEle);

}
function addCloseMore(data, position, ele) {
	var moreEle = document.createElement("div");
	moreEle.className = 'more-close';
	moreEle.setAttribute('id', position + 'CloseMore');
	moreEle.onclick = function() {
		closeMore(data, position, ele)
	}
	ele.appendChild(moreEle);
}
function appendImgs(imgs, ele) {
	imgs.forEach(function(img, index, array) {
		var imgEle = document.createElement("img");
		imgEle.src = img.avatar;
		ele.appendChild(imgEle);
	});
}
function getIndex(value) {
	var ind = -1;
	indexToPosition.forEach(function(item, index, array) {
		if (item == value) {
			ind = index;
		}
	});
	return ind;
}
function clickMore(data, position, ele) {
	while (ele.hasChildNodes()) {
		ele.removeChild(ele.firstChild);
	}
	var index = getIndex(position);
	var thisImgsData = data[index];
	appendImgs(thisImgsData, ele);
	addCloseMore(data, position, ele);

}
function closeMore(data, position, ele) {
	while (ele.hasChildNodes()) {
		ele.removeChild(ele.firstChild);
	}
	var index = getIndex(position);
	listData = data[index].slice(0, maxnum - 1);
	appendImgs(listData, ele);
	addMoreBtn(data, position, ele);
}
$(function() {
	$.ajax({
        url : "/wxp/user/inviteList",
        type : "get",
        async: true,
        dataType : 'json',
        data :{token : "3f5553dc-9f97-4f84-9586-9a2717535b2d"},
        success : function(data) {
        	$("#level1Count").html(data.data.level1.length);
        	$("#level2Count").html(data.data.level2.length);
        	$("#level3Count").html(data.data.level3.length);
        	
        	$("#level1Earning").html(data.data.level1Earning);
        	$("#level2Earning").html(data.data.level2Earning);
        	$("#level3Earning").html(data.data.level3Earning);
        	
        	data = [data.data.level1, data.data.level2, data.data.level3];
        	eleWidth = document.getElementById('firstList').offsetWidth;
        	chuWidth = document.getElementById('getPx').offsetWidth;
        	maxnum = Math.floor(eleWidth / chuWidth);
        	data.forEach(function(item, index, array) {
        		doList(data, indexToPosition[index], item);
        	})
        }
	});
});