$(function(){
	$(".follow-btn").click(follow);
});

function follow() {
    // 获取csrf令牌，在发送AJAX请求之前，将令牌设置到请求的消息头中
    var token = $("meta[name = '_csrf']").attr("content");
    var header = $("meta[name = '_csrf_header']").attr("content");

    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

	var btn = this;
    var entityId = $("#entityId").val();
	if($(btn).hasClass("btn-info")) {
		// 关注TA
        $.post(
            CONTEXT_PATH + "/follow",
            {"entityId":entityId,"entityType":3},
            function (data) {
                data = $.parseJSON(data);
                if (data.code === 0) {
                    $(btn).text("已关注").removeClass("btn-info").addClass("btn-secondary");
                    window.location.reload();
                } else {
                    alert(data.msg);
                }
            }
        )
	} else {
		// 取消关注
        $.post(
            CONTEXT_PATH + "/unfollow",
            {"entityId":entityId,"entityType":3},
            function (data) {
                data = $.parseJSON(data);
                if (data.code === 0) {
                    $(btn).text("关注TA").removeClass("btn-secondary").addClass("btn-info");
                    window.location.reload();
                } else {
                    alert(data.msg);
                }
            }
        )
	}
}